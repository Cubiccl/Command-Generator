package fr.cubiccl.generator.gameobject.speedrun;

import java.util.List;

public class MissingSpaceError extends MissingItemsError
{

	public final int missingSlots;

	public MissingSpaceError(Checkpoint checkpoint, ItemStackS... blockedItems)
	{
		super(checkpoint, blockedItems);
		this.missingSlots = blockedItems.length;
	}

	public MissingSpaceError(Checkpoint checkpoint, List<ItemStackS> blocked)
	{
		this(checkpoint, blocked.toArray(new ItemStackS[blocked.size()]));
	}

}
