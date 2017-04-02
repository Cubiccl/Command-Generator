package fr.cubiccl.generator.gameobject.speedrun;

import java.util.ArrayList;
import java.util.List;

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
	public int position = 0;
	public CheckpointResult result;
	public final Speedrun speedrun;

	public Checkpoint(Speedrun speedrun)
	{
		this.speedrun = speedrun;
		this.moves = new ArrayList<ItemMove>();
	}

	public List<ItemMove> additions()
	{
		ArrayList<ItemMove> additions = new ArrayList<ItemMove>();
		for (ItemMove move : this.moves)
			if (move.isAdding()) additions.add(move);
		return additions;
	}

	public void addMove(ItemMove move)
	{
		this.moves.add(move);
		this.onChange();
	}

	public List<ItemMove> deletions()
	{
		ArrayList<ItemMove> deletions = new ArrayList<ItemMove>();
		for (ItemMove move : this.moves)
			if (move.isDeleting()) deletions.add(move);
		return deletions;
	}

	public List<ItemMove> moves()
	{
		return this.moves;
	}

	void onChange()
	{
		this.speedrun.verify();
	}

	public void removeMove(ItemMove move)
	{
		this.moves.remove(move);
		this.onChange();
	}

	public List<ItemMove> transfers()
	{
		ArrayList<ItemMove> transfers = new ArrayList<ItemMove>();
		for (ItemMove move : this.moves)
			if (move.isTransferring()) transfers.add(move);
		return transfers;
	}

}
