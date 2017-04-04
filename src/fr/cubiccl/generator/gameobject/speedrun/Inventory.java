package fr.cubiccl.generator.gameobject.speedrun;

import java.util.ArrayList;
import java.util.List;

import fr.cubiccl.generator.gameobject.speedrun.Checkpoint.CheckpointResult;

public class Inventory
{
	public static final int ENDERCHEST_SIZE = 27;

	public final ItemStackS[] armor;
	private ArrayList<ItemStackS> failed;
	public final ItemStackS[] inventory, enderchest;

	public Inventory(int inventorySize)
	{
		this.inventory = new ItemStackS[inventorySize];
		this.enderchest = new ItemStackS[ENDERCHEST_SIZE];
		this.armor = new ItemStackS[4];
		this.failed = new ArrayList<ItemStackS>();
	}

	private ItemStackS add(ItemMove move, byte to)
	{
		try
		{
			ItemStackS missing = move.getItem().clone();
			switch (to)
			{
				case ItemMove.INVENTORY:
					missing.amount = this.add(this.inventory, move.getItem());
					break;

				case ItemMove.ENDERCHEST:
					missing.amount = this.add(this.enderchest, move.getItem());
					break;

				default:
					int index = to - ItemMove.HEAD;
					missing.amount = this.add(this.armor, index, move.getItem(), move.getItem().amount);
					break;
			}
			return missing;
		} catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	private int add(ItemStackS[] inventory, int index, ItemStackS item, int quantity)
	{
		if (inventory[index] != null && !inventory[index].matches(item)) return quantity;

		try
		{
			if (inventory[index] == null)
			{
				inventory[index] = item.clone();
				inventory[index].amount = 0;
			}
			int previous = inventory[index].amount;
			inventory[index].amount += quantity;
			if (inventory[index].amount > inventory[index].getItem().maxStackSize) inventory[index].amount = inventory[index].getItem().maxStackSize;
			quantity -= inventory[index].amount - previous;
			// System.out.println("Added " + (-previous + inventory[index].amount) + " " + item.getItem().name());
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		return quantity < 0 ? 0 : quantity;
	}

	private int add(ItemStackS[] inventory, ItemStackS item)
	{
		int[] matching = this.findMatching(inventory, item, true);

		int missing = item.amount, index = 0;
		while (missing > 0 && index < matching.length)
		{
			missing = this.add(inventory, matching[index], item, missing);
			++index;
		}

		return missing < 0 ? 0 : missing;
	}

	public CheckpointResult apply(Checkpoint checkpoint)
	{
		MissingItemsError missing = this.applyDeletions(checkpoint, checkpoint.deletions());

		ArrayList<ItemStackS> thrown = new ArrayList<ItemStackS>(), blocked = new ArrayList<ItemStackS>();

		int missingSpace = this.applyTransfers(checkpoint, checkpoint.transfers());
		if (missingSpace == -1) thrown.addAll(this.failed);
		else if (missingSpace > 0) blocked.addAll(this.failed);
		this.failed.clear();

		missingSpace = this.applyAdditions(checkpoint.additions());
		if (missingSpace == -1) thrown.addAll(this.failed);
		else if (missingSpace > 0) blocked.addAll(this.failed);
		this.failed.clear();
		return new CheckpointResult(missing, new MissingSpaceError(checkpoint, blocked), new ThrownItemsWarning(checkpoint, thrown));
	}

	private int applyAdditions(List<ItemMove> additions)
	{
		for (ItemMove move : additions)
		{
			ItemStackS m = this.add(move, move.to);
			if (m.amount > 0) this.failed.add(m);
		}

		// TODO Thrown not supported yet

		return this.failed.size();
	}

	private MissingItemsError applyDeletions(Checkpoint checkpoint, List<ItemMove> deletions)
	{
		ArrayList<ItemStackS> missing = new ArrayList<ItemStackS>();

		for (ItemMove move : deletions)
		{
			ItemStackS m = this.remove(move, move.from);
			if (m.amount > 0) missing.add(m);
		}

		return new MissingItemsError(checkpoint, missing);
	}

	private int applyTransfers(Checkpoint checkpoint, List<ItemMove> transfers)
	{
		this.applyDeletions(checkpoint, transfers);
		this.applyAdditions(transfers);
		return this.failed.size();
	}

	@Override
	protected Inventory clone() throws CloneNotSupportedException
	{
		Inventory i = new Inventory(this.inventory.length);
		for (int j = 0; j < this.inventory.length; j++)
			if (this.inventory[j] == null) i.inventory[j] = null;
			else i.inventory[j] = this.inventory[j].clone();
		for (int j = 0; j < this.enderchest.length; j++)
			if (this.enderchest[j] == null) i.enderchest[j] = null;
			else i.enderchest[j] = this.enderchest[j].clone();
		for (int j = 0; j < this.armor.length; j++)
			if (this.armor[j] == null) i.armor[j] = null;
			else i.armor[j] = this.armor[j].clone();
		return i;
	}

	private int[] findMatching(ItemStackS[] inventory, ItemStackS item, boolean allowNull)
	{
		ArrayList<Integer> matching = new ArrayList<Integer>();
		for (int i = 0; i < inventory.length; ++i)
			if (allowNull)
			{
				if (inventory[i] == null || inventory[i].matches(item)) matching.add(i);
			} else if (inventory[i] != null && inventory[i].matches(item)) matching.add(i);

		int[] m = new int[matching.size()];
		for (int i = 0; i < m.length; ++i)
			m[i] = matching.get(i);
		return m;
	}

	private ItemStackS remove(ItemMove move, byte from)
	{
		try
		{
			ItemStackS missing = move.getItem().clone();
			switch (from)
			{
				case ItemMove.INVENTORY:
					missing.amount = this.remove(this.inventory, move.getItem());
					break;

				case ItemMove.ENDERCHEST:
					missing.amount = this.remove(this.enderchest, move.getItem());
					break;

				default:
					int index = from - ItemMove.HEAD;
					missing.amount = this.remove(this.armor, index, move.getItem(), move.getItem().amount);
					break;
			}
			return missing;
		} catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	private int remove(ItemStackS[] inventory, int index, ItemStackS item, int quantity)
	{
		if (inventory[index] != null && !inventory[index].matches(item)) return quantity;

		int previous = inventory[index].amount;
		inventory[index].amount -= quantity;
		if (inventory[index].amount < 0) inventory[index].amount = 0;
		quantity -= previous - inventory[index].amount;
		// System.out.println("Removed " + (previous - inventory[index].amount) + " " + item.getItem().name());
		if (inventory[index].amount == 0) inventory[index] = null;

		return quantity < 0 ? 0 : quantity;
	}

	private int remove(ItemStackS[] inventory, ItemStackS item)
	{
		int[] matching = this.findMatching(inventory, item, false);

		int missing = item.amount, index = 0;
		while (missing > 0 && index < matching.length)
		{
			missing = this.remove(inventory, matching[index], item, missing);
			++index;
		}

		return missing < 0 ? 0 : missing;
	}

	public UnusedItemsWarning unusedItems()
	{
		ArrayList<ItemStackS> items = new ArrayList<ItemStackS>();
		for (int i = 0; i < this.armor.length; ++i)
			if (this.armor[i] != null && !this.armor[i].isForced()) items.add(this.armor[i]);
		for (int i = 0; i < this.inventory.length; ++i)
			if (this.inventory[i] != null && !this.inventory[i].isForced()) items.add(this.inventory[i]);
		for (int i = 0; i < this.enderchest.length; ++i)
			if (this.enderchest[i] != null && !this.enderchest[i].isForced()) items.add(this.enderchest[i]);
		return new UnusedItemsWarning(null, items);
	}
}
