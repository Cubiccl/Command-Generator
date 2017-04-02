package fr.cubiccl.generator.gameobject.speedrun;

import java.util.List;

public class MissingItemsError
{

	public final Checkpoint checkpoint;
	public final ItemStackS[] items;

	public MissingItemsError(Checkpoint checkpoint, ItemStackS... items)
	{
		this.checkpoint = checkpoint;
		this.items = items;
	}

	public MissingItemsError(Checkpoint checkpoint, List<ItemStackS> missing)
	{
		this(checkpoint, missing.toArray(new ItemStackS[missing.size()]));
	}

	public boolean isEmpty()
	{
		return this.items.length == 0;
	}

}
