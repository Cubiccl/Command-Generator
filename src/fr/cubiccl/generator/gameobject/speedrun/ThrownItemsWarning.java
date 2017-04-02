package fr.cubiccl.generator.gameobject.speedrun;

import java.util.List;

public class ThrownItemsWarning extends MissingItemsError
{

	public ThrownItemsWarning(Checkpoint checkpoint, ItemStackS... items)
	{
		super(checkpoint, items);
	}

	public ThrownItemsWarning(Checkpoint checkpoint, List<ItemStackS> items)
	{
		this(checkpoint, items.toArray(new ItemStackS[items.size()]));
	}

}
