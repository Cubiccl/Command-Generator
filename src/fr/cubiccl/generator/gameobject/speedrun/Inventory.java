package fr.cubiccl.generator.gameobject.speedrun;

import fr.cubiccl.generator.gameobject.speedrun.Checkpoint.CheckpointResult;

public class Inventory
{
	public static final int ENDERCHEST_SIZE = 27;

	public final ItemStackS[] armor;
	public final ItemStackS[] inventory, enderchest;

	public Inventory(int inventorySize)
	{
		this.inventory = new ItemStackS[inventorySize];
		this.enderchest = new ItemStackS[ENDERCHEST_SIZE];
		this.armor = new ItemStackS[4];
	}

	public CheckpointResult apply(Checkpoint checkpoint)
	{
		// TODO apply(checkpoint)
		return new CheckpointResult(null, null, null);
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
