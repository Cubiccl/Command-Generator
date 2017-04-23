package fr.cubiccl.generator.gameobject.baseobjects.block;

import fr.cubiccl.generator.gameobject.baseobjects.Block;
import fr.cubiccl.generator.gameobject.baseobjects.BlockState;
import fr.cubiccl.generator.utils.Replacement;
import fr.cubiccl.generator.utils.Text;

public class BlockStairs extends Block
{

	public static Text getName(String id, int damage)
	{
		return new Text("block." + id + ".x", new Replacement("<orientation>", new Text("utils.stairs." + damage)));
	}

	public BlockStairs(int idInt, String idString)
	{
		super(idInt, idString);
		this.addState(new BlockState("facing", BlockState.STRING, 1, "east", "west", "south", "north"));
		this.addState(new BlockState("half", BlockState.STRING, 4, "bottom", "top"));
		this.addState(new BlockState("shape", BlockState.STRING, -1, "straight", "inner_left", "inner_right", "outer_left", "outer_right"));
		this.textureType = -4;
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
