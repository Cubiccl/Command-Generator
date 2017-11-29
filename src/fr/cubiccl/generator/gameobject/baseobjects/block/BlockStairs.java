package fr.cubiccl.generator.gameobject.baseobjects.block;

import fr.cubiccl.generator.gameobject.baseobjects.Block;
import fr.cubiccl.generator.gameobject.baseobjects.BlockState;
import fr.cubiccl.generator.gameobject.registries.v112.Blocks112;
import fr.cubiccl.generator.utils.Replacement;
import fr.cubiccl.generator.utils.Text;

/** Stairs Blocks, with 8 data values determining which direction it's facing, and a state describing its shape. */
public class BlockStairs extends Block
{

	public static Text getName(String id, int damage)
	{
		return new Text("block." + id + ".x", new Replacement("<orientation>", new Text("utils.stairs." + damage)));
	}

	public BlockStairs(int idInt, String idString)
	{
		super(idInt, idString);
		Blocks112.variant(this, "facing", 1, "east", "west", "south", "north");
		Blocks112.variant(this, "half", 4, "bottom", "top");
		Blocks112.variant(this, "shape", -1, "straight", "inner_left", "inner_right", "outer_left", "outer_right");
		this.setTextureType(-4);
	}

	@Override
	public Text name(int damage)
	{
		return getName(this.id(), damage);
	}

	@Override
	protected boolean shouldSaveState(BlockState state)
	{
		return !state.id.equals("facing") && !state.id.equals("half") && !state.id.equals("shape");
	}

}
