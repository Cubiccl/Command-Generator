package fr.cubiccl.generator.gameobject.baseobjects.block;

import fr.cubiccl.generator.gameobject.baseobjects.Block;
import fr.cubiccl.generator.utils.Replacement;
import fr.cubiccl.generator.utils.Text;

public class BlockRail extends Block
{

	public static Text getName(String id, int damage)
	{
		return new Text("block." + id + ".x", new Replacement("<shape>", new Text("utils.rail." + damage)));
	}

	public BlockRail(int idInt, String idString)
	{
		super(idInt, idString, new int[]
		{ 0, 1, 2, 3, 4, 5, 8, 9, 10, 11, 12, 13 });
	}

	@Override
	public Text name(int damage)
	{
		return getName(this.id(), damage);
	}

}
