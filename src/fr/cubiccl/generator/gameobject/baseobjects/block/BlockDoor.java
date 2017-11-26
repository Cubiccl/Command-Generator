package fr.cubiccl.generator.gameobject.baseobjects.block;

import fr.cubiccl.generator.gameobject.baseobjects.Block;
import fr.cubiccl.generator.gameobject.baseobjects.BlockState;
import fr.cubiccl.generator.gameobject.registries.Blocks112;
import fr.cubiccl.generator.utils.Replacement;
import fr.cubiccl.generator.utils.Text;

/** Door Blocks, with 11 data values and various states describing its status. */
public class BlockDoor extends Block
{

	public static Text getName(String id, int damage)
	{
		return new Text("block." + id + ".x", new Replacement("<door_status>", new Text("utils.door_status." + damage)));
	}

	public BlockDoor(int idInt, String idString)
	{
		super(idInt, idString);
		Blocks112.variant(this, "facing", -1, "north", "south", "west", "east");
		Blocks112.variant(this, "half", -1, "lower", "upper");
		Blocks112.variant(this, "hinge", -1, "left", "right");
		Blocks112.bool(this, "open", -1);
		Blocks112.bool(this, "powered", -1);
		this.setMaxDamage(11);
		this.setTextureType(-8);
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
