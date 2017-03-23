package fr.cubiccl.generator.gameobject.speedrun;

import fr.cubiccl.generator.gameobject.speedrun.Checkpoint.CheckpointResult;

public class Inventory
{
	public static final int ENDERCHEST_SIZE = 27;

	public final ItemStackS[] inventory, enderchest;
	public final ItemStackS[] armor;

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

}
