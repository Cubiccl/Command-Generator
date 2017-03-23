package fr.cubiccl.generator.gameobject.speedrun;

import java.util.ArrayList;

import fr.cubiccl.generator.gameobject.speedrun.Checkpoint.CheckpointResult;

public class Inventory
{
	public static final int ENDERCHEST_SIZE = 27;

	public final ItemStackS[] armor;
	public final ItemStackS[] inventory, enderchest;
	private ArrayList<ItemStackS> transferring;

	public Inventory(int inventorySize)
	{
		this.inventory = new ItemStackS[inventorySize];
		this.enderchest = new ItemStackS[ENDERCHEST_SIZE];
		this.armor = new ItemStackS[4];
		this.transferring = new ArrayList<ItemStackS>();
	}

	public CheckpointResult apply(Checkpoint checkpoint)
	{
		MissingItemsError missing = this.applyDeletions(checkpoint);

		ArrayList<ItemStackS> thrown = new ArrayList<ItemStackS>(), blocked = new ArrayList<ItemStackS>();

		int missingSpace = this.applyTransfers(checkpoint);
		if (missingSpace == -1) thrown.addAll(this.transferring);
		else if (missingSpace > 0) blocked.addAll(this.transferring);
		this.transferring.clear();

		missingSpace = this.applyAdditions(checkpoint);
		if (missingSpace == -1) thrown.addAll(this.transferring);
		else if (missingSpace > 0) blocked.addAll(this.transferring);
		return new CheckpointResult(missing, new MissingSpaceError(blocked), new ThrownItemsWarning(thrown));
	}

	private int applyAdditions(Checkpoint checkpoint)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	private MissingItemsError applyDeletions(Checkpoint checkpoint)
	{
		// TODO Auto-generated method stub
		return null;
	}

	private int applyTransfers(Checkpoint checkpoint)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected Inventory clone() throws CloneNotSupportedException
	{
		Inventory i = new Inventory(this.inventory.length);
		for (int j = 0; j < this.inventory.length; j++)
			i.inventory[j] = this.inventory[j].clone();
		for (int j = 0; j < this.enderchest.length; j++)
			i.enderchest[j] = this.enderchest[j].clone();
		for (int j = 0; j < this.armor.length; j++)
			i.armor[j] = this.armor[j].clone();
		return i;
	}

}
