package fr.cubiccl.generator.gameobject.baseobjects.block;

import fr.cubiccl.generator.gameobject.baseobjects.Block;
import fr.cubiccl.generator.utils.Replacement;
import fr.cubiccl.generator.utils.Text;

public class BlockFurniture extends Block
{

	public static Text getName(String id, int damage)
	{
		return new Text("block." + id + ".x", new Replacement("<facing>", new Text("utils.facing." + damage)));
	}

	public BlockFurniture(int idInt, String idString)
	{
		super(idInt, idString, new int[]
		{ 2, 3, 4, 5 });
		this.textureType = -1;
	}

	@Override
	public Text name(int damage)
	{
		return getName(this.id(), damage);
	}

}
