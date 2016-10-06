package fr.cubiccl.generator.gameobject;

import fr.cubiccl.generator.gameobject.baseobjects.Block;

public class PlacedBlock
{

	public final Block block;
	public final int data;

	public PlacedBlock(Block block, int data)
	{
		this.block = block;
		this.data = data;
	}

	public String toCommand()
	{
		return this.block.idString + " " + this.data;
	}

}
