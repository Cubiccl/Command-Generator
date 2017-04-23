package fr.cubiccl.generator.gameobject.baseobjects.block;

import fr.cubiccl.generator.gameobject.baseobjects.Block;
import fr.cubiccl.generator.gameobject.baseobjects.BlockState;
import fr.cubiccl.generator.utils.Replacement;
import fr.cubiccl.generator.utils.Text;

public class BlockStained extends Block
{

	public static Text getName(String id, int damage)
	{
		return new Text("block." + id + ".x", new Replacement("<color>", new Text("utils.color." + damage)));
	}

	public BlockStained(int idInt, String idString)
	{
		super(idInt, idString);
		this.addState(new BlockState("color", BlockState.STRING, 1, "white", "orange", "magenta", "light_blue", "yellow", "lime", "pink", "gray", "silver",
				"cyan", "purple", "blue", "brown", "green", "red", "black"));
	}
	
	@Override
	public Text name(int damage)
	{
		return getName(this.id(), damage);
	}

	@Override
	protected boolean shouldSaveState(BlockState state)
	{
		return !state.id.equals("color");
	}

}
