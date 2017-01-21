package fr.cubiccl.generator.gameobject.baseobjects.block;

import fr.cubiccl.generator.gameobject.baseobjects.Block;
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
		super(idInt, idString, 7);
		this.textureType = -4;
	}

	@Override
	public Text name(int damage)
	{
		return getName(this.id(), damage);
	}

}
