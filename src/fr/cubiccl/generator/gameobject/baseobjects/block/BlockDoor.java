package fr.cubiccl.generator.gameobject.baseobjects.block;

import fr.cubiccl.generator.gameobject.baseobjects.Block;
import fr.cubiccl.generator.gameobject.baseobjects.BlockState;
import fr.cubiccl.generator.utils.Replacement;
import fr.cubiccl.generator.utils.Text;

/** Door Blocks, with 11 data values and various states describing its status. */
public class BlockDoor extends Block
{

	public static Text getName(String id, int damage)
	{
		return new Text("block." + id + ".x", new Replacement("<door_status>", new Text("utils.door_status." + damage)));
	}

	public BlockDoor()
	{
		super();
		this.addState(new BlockState("facing", BlockState.STRING, -1, "north", "south", "west", "east"));
		this.addState(new BlockState("half", BlockState.STRING, -1, "lower", "upper"));
		this.addState(new BlockState("hinge", BlockState.STRING, -1, "left", "right"));
		this.addState(new BlockState("open", BlockState.STRING, -1, "false", "true"));
		this.addState(new BlockState("powered", BlockState.STRING, -1, "false", "true"));
		this.setMaxDamage(11);
		this.textureType = -8;
	}

	@Override
	public Text name(int damage)
	{
		return getName(this.id(), damage);
	}

	@Override
	protected boolean shouldSaveState(BlockState state)
	{
		return !state.id.equals("facing") && !state.id.equals("half") && !state.id.equals("hinge") && !state.id.equals("open") && !state.id.equals("powered");
	}

}
