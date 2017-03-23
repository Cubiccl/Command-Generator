package fr.cubiccl.generator.gameobject.speedrun;

import java.util.ArrayList;
import java.util.List;

import fr.cubiccl.generator.gameobject.ItemStack;
import fr.cubiccl.generator.gameobject.baseobjects.Item;
import fr.cubiccl.generator.gameobject.tags.TagCompound;

public class ItemStackS extends ItemStack
{
	public static final boolean FORCED = true, OPTIONNAL = false;

	public boolean importance = OPTIONNAL;

	public ItemStackS()
	{
		super();
	}

	public ItemStackS(Item item, int data, int amount)
	{
		super(item, data, amount);
	}

	public ItemStackS(Item item, int data, int amount, TagCompound nbt)
	{
		super(item, data, amount, nbt);
	}

	@Override
	public ItemStackS clone() throws CloneNotSupportedException
	{
		ItemStackS i = new ItemStackS(item, damage, amount, nbt);
		i.slot = this.slot;
		i.importance = this.importance;
		return i;
	}

	public boolean isForced()
	{
		return this.importance == FORCED;
	}

	public boolean isOptionnal()
	{
		return this.importance == OPTIONNAL;
	}

	public boolean matches(ItemStackS item)
	{
		return item.item == item.item && item.damage == item.damage;
	}

	public List<ItemStackS> splitQuantity()
	{
		// TODO maxStackSize
		ArrayList<ItemStackS> split = new ArrayList<ItemStackS>();
		int quantity = this.amount, remove = 0;
		while (quantity > 0)
		{
			remove = quantity >= Inventory.STACKSIZE ? Inventory.STACKSIZE : quantity;
			quantity -= remove;
			try
			{
				ItemStackS i = this.clone();
				i.amount = remove;
				split.add(i);
			} catch (CloneNotSupportedException e)
			{
				e.printStackTrace();
			}
		}
		return split;
	}

}
