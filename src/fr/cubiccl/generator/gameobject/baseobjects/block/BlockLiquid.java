package fr.cubiccl.generator.gameobject.baseobjects.block;

import fr.cubiccl.generator.gameobject.baseobjects.Block;
import fr.cubiccl.generator.gameobject.registries.Blocks112;
import fr.cubiccl.generator.utils.Replacement;
import fr.cubiccl.generator.utils.Text;

/** Liquid Blocks, with 16 data values determining how much liquid it has. */
public class BlockLiquid extends Block
{

	public static Text getName(String id, int damage)
	{
		return new Text("block." + id + ".x", new Replacement("<liquid>", new Text("utils.liquid." + damage)));
	}

	public BlockLiquid(int idInt, String idString)
	{
		super(idInt, idString);
		Blocks112.numbered(this, "level", 1, 15);
		this.setTextureType(-1);
	}

	@Override
	public Text name(int damage)
	{
		return getName(this.id(), damage);
	}

}
