package fr.cubiccl.generator.gameobject.baseobjects.block;

import fr.cubiccl.generator.gameobject.baseobjects.Block;
import fr.cubiccl.generator.utils.Replacement;
import fr.cubiccl.generator.utils.Text;

public class BlockFacing extends Block
{

	public static Text getName(String id, int damage)
	{
		return new Text("block." + id + ".x", new Replacement("<facing>", new Text("utils.facing." + damage)));
	}

	public BlockFacing(int idInt, String idString)
	{
		super(idInt, idString, 5);
		this.textureType = -1;
	}

	@Override
	public Text name(int damage)
	{
		return getName(this.idString, damage);
	}

}
