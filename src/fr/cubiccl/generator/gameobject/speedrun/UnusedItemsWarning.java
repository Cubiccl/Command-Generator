package fr.cubiccl.generator.gameobject.speedrun;

import java.util.List;

import fr.cubiccl.generator.utils.Text;

public class UnusedItemsWarning extends MissingItemsError
{

	public UnusedItemsWarning(Checkpoint checkpoint, List<ItemStackS> unused)
	{
		super(checkpoint, unused);
	}

	@Override
	public String description()
	{
		String d = new Text("speedrun.problems.unused.desc").addReplacement("<items>", Integer.toString(this.items.length)).toString() + "\n";
		for (ItemStackS item : this.items)
			d += item.displayName() + "\n";
		return d;
	}

	@Override
	public Text name()
	{
		return new Text("speedrun.problems.unused").addReplacement("<items>", Integer.toString(this.items.length));
	}

}
