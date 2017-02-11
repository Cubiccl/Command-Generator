package fr.cubiccl.generator.gameobject.baseobjects.block;

import fr.cubiccl.generator.gameobject.baseobjects.Block;
import fr.cubiccl.generator.utils.Replacement;
import fr.cubiccl.generator.utils.Text;

public class BlockTerracotta extends Block
{

	public static Text getName(String id, int damage)
	{
		return new Text("block." + id + ".x", new Replacement("<terra>", new Text("utils.terra." + damage)));
	}

	public BlockTerracotta(int idInt, String idString)
	{
		super(idInt, idString, 3);
		this.textureType = -1;
	}

	@Override
	public Text name(int damage)
	{
		return getName(this.id(), damage);
	}

}