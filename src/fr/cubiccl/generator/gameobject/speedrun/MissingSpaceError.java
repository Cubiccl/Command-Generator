package fr.cubiccl.generator.gameobject.speedrun;

import java.util.List;

public class MissingSpaceError extends MissingItemsError
{

	public final int missingSlots;

	public MissingSpaceError(ItemStackS... blockedItems)
	{
		super(blockedItems);
		this.missingSlots = blockedItems.length;
	}

	public MissingSpaceError(List<ItemStackS> blocked)
	{
		this(blocked.toArray(new ItemStackS[blocked.size()]));
	}

}
