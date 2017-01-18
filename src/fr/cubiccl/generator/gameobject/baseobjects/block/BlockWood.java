package fr.cubiccl.generator.gameobject.baseobjects.block;

import fr.cubiccl.generator.gameobject.baseobjects.Block;
import fr.cubiccl.generator.utils.Replacement;
import fr.cubiccl.generator.utils.Text;

/** This Block has 6 data values, determining which type of wood it is made of. */
public class BlockWood extends Block
{

	public static Text getName(String id, int damage)
	{
		return new Text("block." + id + ".x", new Replacement("<wood>", new Text("utils.wood." + damage)));
	}

	public BlockWood(int idInt, String idString)
	{
		super(idInt, idString, 5);
	}

	@Override
	public Text name(int damage)
	{
		return getName(this.idString, damage);
	}

}
