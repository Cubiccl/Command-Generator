package fr.cubiccl.generator.gameobject.baseobjects.block;

import fr.cubiccl.generator.gameobject.baseobjects.Block;
import fr.cubiccl.generator.gameobject.baseobjects.BlockState;
import fr.cubiccl.generator.utils.Replacement;
import fr.cubiccl.generator.utils.Text;

/** Redstone Rail Blocks, with 16 data values determining which shape it has and whether it's powered. */
public class BlockRail extends Block
{

	public static Text getName(String id, int damage)
	{
		return new Text("block." + id + ".x", new Replacement("<shape>", new Text("utils.rail." + damage)));
	}

	public BlockRail(int idInt, String idString)
	{
		super(idInt, idString);
		this.addState(new BlockState("shape", BlockState.STRING, 1, "north_south", "east_west", "ascending_east", "ascending_west", "ascending_north",
				"ascending_south"));
		this.addState(new BlockState("powered", BlockState.BOOLEAN, 8, "true", "false"));
	}

	@Override
	public Text name(int damage)
	{
		return getName(this.id(), damage);
	}

	@Override
	protected boolean shouldSaveState(BlockState state)
	{
		return !state.id.equals("shape") && !state.id.equals("powered");
	}

}
