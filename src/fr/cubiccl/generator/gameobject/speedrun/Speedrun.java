package fr.cubiccl.generator.gameobject.speedrun;

import java.util.ArrayList;

public class Speedrun
{

	private ArrayList<Checkpoint> checkpoints;
	private boolean isValid, isPost19;
	private ArrayList<MissingItemsError> missingItems;

	public Speedrun()
	{
		this.checkpoints = new ArrayList<Checkpoint>();
		this.missingItems = new ArrayList<MissingItemsError>();
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

		// TODO verify

		this.isValid = this.missingItems.isEmpty();
	}
}
