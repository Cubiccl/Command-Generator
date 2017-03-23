package fr.cubiccl.generator.gameobject.speedrun;

import java.util.ArrayList;

public class Checkpoint
{
	public static class CheckpointResult
	{

		public final MissingItemsError missingItems;
		public final MissingSpaceError missingSpace;
		public final ThrownItemsWarning thrownItems;

		public CheckpointResult(MissingItemsError missingItems, MissingSpaceError missingSpace, ThrownItemsWarning thrownItems)
		{
			super();
			this.missingItems = missingItems;
			this.missingSpace = missingSpace;
			this.thrownItems = thrownItems;
		}

	}

	public Inventory currentInventory;
	private ArrayList<ItemMove> moves;
	public CheckpointResult result;
	public final Speedrun speedrun;

	public Checkpoint(Speedrun speedrun)
	{
		this.speedrun = speedrun;
		this.moves = new ArrayList<ItemMove>();
	}

	public void addMove(ItemMove move)
	{
		this.moves.add(move);
		this.onChange();
	}

	public void removeMove(ItemMove move)
	{
		this.moves.remove(move);
		this.onChange();
	}

	void onChange()
	{
		this.speedrun.verify();
	}

}
