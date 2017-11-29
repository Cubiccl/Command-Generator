package fr.cubiccl.generator.gameobject.baseobjects.block;

import fr.cubiccl.generator.gameobject.baseobjects.Block;
import fr.cubiccl.generator.gameobject.baseobjects.BlockState;
import fr.cubiccl.generator.gameobject.registries.v112.Blocks112;
import fr.cubiccl.generator.utils.Replacement;
import fr.cubiccl.generator.utils.Text;

/** Terracotta Blocks, with 4 data values determining which direction it's facing. */
public class BlockTerracotta extends Block
{

	public static Text getName(String id, int damage)
	{
		return new Text("block." + id + ".x", new Replacement("<terra>", new Text("utils.terra." + damage)));
	}

	public BlockTerracotta(int idInt, String idString)
	{
		super(idInt, idString);
		Blocks112.variant(this, "facing", 1, "south", "west", "north", "east");
		this.setTextureType(-1);
	}

	@Override
	public Text name(int damage)
	{
		return getName(this.id(), damage);
	}

	@Override
	protected boolean shouldSaveState(BlockState state)
	{
		return !state.id.equals("facing");
	}

}
