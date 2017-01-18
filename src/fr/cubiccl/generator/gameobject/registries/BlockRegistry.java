package fr.cubiccl.generator.gameobject.registries;

import fr.cubiccl.generator.gameobject.baseobjects.Block;

public class BlockRegistry extends ObjectRegistry<Block>
{

	public BlockRegistry()
	{
		super(true, true, true, Block.class);
	}

	@Override
	public void checkNames()
	{
		for (Block b : this.registry.values())
		{
			b.mainName().toString();
			for (int d : b.damage)
			{
				b.name(d).toString();
			}
		}
	}

	@Override
	public void loadTextures()
	{
		for (Block b : this.registry.values())
			for (int d : b.damage)
				b.texture(d);
	}

}
