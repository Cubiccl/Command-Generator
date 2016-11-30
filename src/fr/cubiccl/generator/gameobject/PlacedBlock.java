package fr.cubiccl.generator.gameobject;

import fr.cubiccl.generator.gameobject.baseobjects.Block;
import fr.cubiccl.generator.gameobject.tags.TagCompound;

public class PlacedBlock
{

	public final Block block;
	public final int data;
	public final TagCompound nbt;

	public PlacedBlock(Block block, int data, TagCompound nbt)
	{
		this.block = block;
		this.data = data;
		this.nbt = nbt;
	}

	public String toCommand()
	{
		return this.block.idString + " " + this.data + " " + this.nbt.value();
	}

}
