package fr.cubiccl.generator.gameobject.speedrun;

public class MissingSpaceError extends MissingItemsError
{

	public final int missingSlots;

	public MissingSpaceError(int missingSlots, ItemStackS... blockedItems)
	{
		super(blockedItems);
		this.missingSlots = missingSlots;
	}

}
