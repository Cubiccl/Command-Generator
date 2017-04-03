package fr.cubiccl.generator.gameobject.speedrun;

import java.util.List;

import fr.cubiccl.generator.utils.Text;

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

	@Override
	public String description()
	{
		String d = new Text("speedrun.problems.missing_space.desc").addReplacement("<checkpoint>", this.checkpoint.getName())
				.addReplacement("<slots>", Integer.toString(this.missingSlots)).toString()
				+ "\n";
		for (ItemStackS item : this.items)
			d += item.displayName() + "\n";
		return d;
	}

	@Override
	public Text name()
	{
		return new Text("speedrun.problems.missing_space").addReplacement("<checkpoint>", this.checkpoint.getName());
	}

}
