package fr.cubiccl.generator.gameobject.speedrun;

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

	public Speedrun speedrun;

}
