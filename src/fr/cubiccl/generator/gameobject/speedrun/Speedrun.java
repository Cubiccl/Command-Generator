package fr.cubiccl.generator.gameobject.speedrun;

import java.util.ArrayList;

import fr.cubiccl.generator.gameobject.speedrun.Checkpoint.CheckpointResult;

public class Speedrun
{

	private ArrayList<Checkpoint> checkpoints;
	private boolean isValid, isPost19;
	private ArrayList<MissingItemsError> missingItems;
	private ArrayList<MissingSpaceError> missingSpace;
	private ArrayList<ThrownItemsWarning> thrownItems;

	public Speedrun()
	{
		this.checkpoints = new ArrayList<Checkpoint>();
		this.missingItems = new ArrayList<MissingItemsError>();
		this.missingSpace = new ArrayList<MissingSpaceError>();
		this.thrownItems = new ArrayList<ThrownItemsWarning>();
		this.isValid = true;
		this.isPost19 = true;
	}

	public void addCheckpoint(Checkpoint checkpoint)
	{
		this.checkpoints.add(checkpoint);
		checkpoint.speedrun = this;
	}

	public boolean isValid()
	{
		return this.isValid;
	}

	public void moveCheckpointLeft(Checkpoint checkpoint)
	{
		int i = this.checkpoints.indexOf(checkpoint);
		if (i <= 0) return;

		Checkpoint previous = this.checkpoints.get(i - 1);
		this.checkpoints.set(i - 1, checkpoint);
		this.checkpoints.set(i, previous);
	}

	public void moveCheckpointRight(Checkpoint checkpoint)
	{
		int i = this.checkpoints.indexOf(checkpoint);
		if (i >= this.checkpoints.size() - 1) return;

		Checkpoint previous = this.checkpoints.get(i + 1);
		this.checkpoints.set(i + 1, checkpoint);
		this.checkpoints.set(i, previous);
	}

	public void removeCheckpoint(Checkpoint checkpoint)
	{
		this.checkpoints.remove(checkpoint);
		checkpoint.speedrun = null;
	}

	public void verify()
	{
		this.missingItems.clear();

		Inventory inventory = new Inventory(this.isPost19 ? 37 : 36);
		for (Checkpoint checkpoint : this.checkpoints)
		{
			CheckpointResult result = inventory.apply(checkpoint); // TODO change to isEmpty()
			if (result.missingItems != null) this.missingItems.add(result.missingItems);
			if (result.missingSpace != null) this.missingSpace.add(result.missingSpace);
			if (result.thrownItems != null) this.thrownItems.add(result.thrownItems);
		}

		this.isValid = this.missingItems.isEmpty() && this.missingSpace.isEmpty();
	}
}
