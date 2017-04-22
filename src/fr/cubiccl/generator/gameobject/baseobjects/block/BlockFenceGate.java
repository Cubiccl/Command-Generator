package fr.cubiccl.generator.gameobject.baseobjects.block;

import fr.cubiccl.generator.gameobject.baseobjects.Block;
import fr.cubiccl.generator.gameobject.baseobjects.BlockState;
import fr.cubiccl.generator.utils.Replacement;
import fr.cubiccl.generator.utils.Text;

public class BlockFenceGate extends Block
{

	public static Text getName(String id, int damage)
	{
		return new Text("block." + id + ".x", new Replacement("<gate_status>", new Text("utils.gate_status." + damage)));
	}

	public BlockFenceGate(int idInt, String idString)
	{
		super(idInt, idString);
		this.addState(new BlockState("facing", BlockState.STRING, 1, "south", "west", "north", "east"));
		this.addState(new BlockState("open", BlockState.BOOLEAN, 4, "false", " true"));
		this.addState(new BlockState("powered", BlockState.BOOLEAN, -1, "false", " true"));
		this.addState(new BlockState("in_wall", BlockState.BOOLEAN, -1, "false", " true"));
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
		return !state.id.equals("facing") && !state.id.equals("open") && !state.id.equals("powered") && !state.id.equals("in_wall");
	}

}
