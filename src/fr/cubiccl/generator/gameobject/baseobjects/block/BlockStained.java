package fr.cubiccl.generator.gameobject.baseobjects.block;

import fr.cubiccl.generator.gameobject.baseobjects.Block;
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
		super(idInt, idString, 15);
	}

	@Override
	public Text name(int damage)
	{
		return getName(this.idString, damage);
	}

}
