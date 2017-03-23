package fr.cubiccl.generator.gameobject.speedrun;

import java.util.List;

public class MissingItemsError
{

	public final ItemStackS[] items;

	public MissingItemsError(ItemStackS[] items)
	{
		this.items = items;
	}

	public MissingItemsError(List<ItemStackS> missing)
	{
		this(missing.toArray(new ItemStackS[missing.size()]));
	}

	public boolean isEmpty()
	{
		return this.items.length == 0;
	}

}
