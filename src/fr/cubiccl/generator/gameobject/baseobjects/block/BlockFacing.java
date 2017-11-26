package fr.cubiccl.generator.gameobject.baseobjects.block;

import fr.cubiccl.generator.gameobject.baseobjects.Block;
import fr.cubiccl.generator.gameobject.baseobjects.BlockState;
import fr.cubiccl.generator.utils.Replacement;
import fr.cubiccl.generator.utils.Text;

/** Directional Blocks, with 6 data values determining which direction it's facing (including up and down). */
public class BlockFacing extends Block
{

	public static Text getName(String id, int damage)
	{
		if (damage >= 8) return new Text("block." + id + ".8.x", new Replacement("<facing>", new Text("utils.facing." + damage % 8)));
		return new Text("block." + id + ".x", new Replacement("<facing>", new Text("utils.facing." + damage)));
	}

	public BlockFacing(int idInt, String idString)
	{
		super(idInt, idString);
		this.addState(new BlockState("facing", BlockState.STRING, 1, "down", "up", "north", "south", "west", "east"));
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
		return !state.id.equals("facing");
	}

	@Override
	protected boolean shouldSaveTextureType()
	{
		return this.textureType() != -8;
	}

}
