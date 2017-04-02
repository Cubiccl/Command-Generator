package fr.cubiccl.generator.gameobject.speedrun;

import java.util.ArrayList;
import java.util.List;

import org.jdom2.Element;

public class Checkpoint implements Comparable<Checkpoint>
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

	public static Checkpoint createFrom(Speedrun speedrun, Element checkpoint)
	{
		Checkpoint c = new Checkpoint(speedrun);
		c.position = Integer.parseInt(checkpoint.getAttributeValue("position"));
		for (Element move : checkpoint.getChildren("move"))
			c.addMove(ItemMove.createFrom(c, move));
		return c;
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

	@Override
	public int compareTo(Checkpoint o)
	{
		return this.position - o.position;
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
		this.speedrun.onChange();
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
