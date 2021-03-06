package fr.cubiccl.generator.gameobject.baseobjects.block;

import fr.cubiccl.generator.gameobject.baseobjects.Block;
import fr.cubiccl.generator.gameobject.baseobjects.BlockState;
import fr.cubiccl.generator.utils.Replacement;
import fr.cubiccl.generator.utils.Text;

/** This Block has several data values, the number displays in the name. */
public class BlockNumbered extends Block
{

	public static Text getName(String id, int damage)
	{
		int actual = damage + 1;
		if (id.contains("weighted_") || id.contains("_wire")) --actual;
		Text t = new Text("block." + id + "." + damage, new Replacement("<count>", Integer.toString(actual)));
		if (t.isTranslated()) return t;
		return new Text("block." + id + ".x", new Replacement("<count>", Integer.toString(actual)));
	}

	public BlockNumbered()
	{
		super();
		this.textureType = -1;
	}

	@Override
	public Text name(int damage)
	{
		return getName(this.id(), damage);
	}

	@Override
	protected boolean shouldSaveState(BlockState state)
	{
		return true;
	}

	@Override
	protected boolean shouldSaveTextureType()
	{
		return this.textureType != -1;
	}

}
