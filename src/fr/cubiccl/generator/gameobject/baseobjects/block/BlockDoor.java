package fr.cubiccl.generator.gameobject.baseobjects.block;

import fr.cubiccl.generator.gameobject.baseobjects.Block;
import fr.cubiccl.generator.utils.Replacement;
import fr.cubiccl.generator.utils.Text;

public class BlockDoor extends Block
{

	public static Text getName(String id, int damage)
	{
		return new Text("block." + id + ".x", new Replacement("<door_status>", new Text("utils.door_status." + damage)));
	}

	public BlockDoor(int idInt, String idString)
	{
		super(idInt, idString, 11);
		this.textureType = -8;
	}

	@Override
	public Text name(int damage)
	{
		return getName(this.id(), damage);
	}

}
