package fr.cubiccl.generator.gameobject.speedrun;

public class ItemMove
{
	public static final byte OUT = -1, INVENTORY = 0, ENDERCHEST = 1, HEAD = 2, CHEST = 3, LEGS = 4, BOOTS = 5;

	public final Checkpoint checkpoint;
	public byte from, to;
	private ItemStackS stack;

	public ItemMove(Checkpoint checkpoint, ItemStackS item)
	{
		this.checkpoint = checkpoint;
		this.stack = item;
		this.from = OUT;
		this.to = INVENTORY;
	}

	public ItemStackS getItem()
	{
		return this.stack;
	}

	public boolean isAdding()
	{
		return this.from == OUT;
	}

	public boolean isDeleting()
	{
		return this.to == OUT;
	}

	public boolean isTransfering()
	{
		return !this.isAdding() && !this.isDeleting();
	}

	public void setItem(ItemStackS item)
	{
		this.stack = item;
		this.checkpoint.onChange();
	}

}
