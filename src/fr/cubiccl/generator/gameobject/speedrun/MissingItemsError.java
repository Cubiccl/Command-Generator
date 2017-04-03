package fr.cubiccl.generator.gameobject.speedrun;

import java.util.List;

import fr.cubiccl.generator.utils.Text;

public class MissingItemsError
{

	public final Checkpoint checkpoint;
	public final ItemStackS[] items;

	public MissingItemsError(Checkpoint checkpoint, ItemStackS... items)
	{
		this.checkpoint = checkpoint;
		this.items = items;
	}

	public MissingItemsError(Checkpoint checkpoint, List<ItemStackS> missing)
	{
		this(checkpoint, missing.toArray(new ItemStackS[missing.size()]));
	}

	public String description()
	{
		String d = new Text("speedrun.problems.missing_items.desc").addReplacement("<checkpoint>", this.checkpoint.getName()).toString() + "\n";
		for (ItemStackS item : this.items)
			d += item.displayName() + "\n";
		return d;
	}

	public boolean isEmpty()
	{
		return this.items.length == 0;
	}

	public Text name()
	{
		return new Text("speedrun.problems.missing_items").addReplacement("<checkpoint>", this.checkpoint.getName());
	}

}
