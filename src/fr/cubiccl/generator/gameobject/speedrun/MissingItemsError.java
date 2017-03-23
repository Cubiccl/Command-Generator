package fr.cubiccl.generator.gameobject.speedrun;

public class MissingItemsError
{

	public final ItemStackS[] items;

	public MissingItemsError(ItemStackS[] items)
	{
		this.items = items;
	}

	public boolean isEmpty()
	{
		return this.items.length == 0;
	}

}
