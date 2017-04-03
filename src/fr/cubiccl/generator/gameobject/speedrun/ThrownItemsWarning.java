package fr.cubiccl.generator.gameobject.speedrun;

import java.util.List;

import fr.cubiccl.generator.utils.Text;

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

	@Override
	public String description()
	{
		String d = new Text("speedrun.problems.thrown.desc").addReplacement("<checkpoint>", this.checkpoint.getName()).toString() + "\n";
		for (ItemStackS item : this.items)
			d += item.displayName() + "\n";
		return d;
	}

	@Override
	public Text name()
	{
		return new Text("speedrun.problems.thrown").addReplacement("<checkpoint>", this.checkpoint.getName());
	}

}
