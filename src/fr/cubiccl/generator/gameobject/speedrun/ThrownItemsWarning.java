package fr.cubiccl.generator.gameobject.speedrun;

import java.util.List;

public class ThrownItemsWarning extends MissingItemsError
{

	public ThrownItemsWarning(ItemStackS[] items)
	{
		super(items);
	}

	public ThrownItemsWarning(List<ItemStackS> items)
	{
		this(items.toArray(new ItemStackS[items.size()]));
	}

}
