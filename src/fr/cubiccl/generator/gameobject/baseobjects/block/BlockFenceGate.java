package fr.cubiccl.generator.gameobject.baseobjects.block;

import fr.cubiccl.generator.gameobject.baseobjects.Block;
import fr.cubiccl.generator.utils.Replacement;
import fr.cubiccl.generator.utils.Text;

public class BlockFenceGate extends Block
{

	public static Text getName(String id, int damage)
	{
		return new Text("block." + id + ".x", new Replacement("<gate_status>", new Text("utils.gate_status." + damage)));
	}

	public BlockFenceGate(int idInt, String idString)
	{
		super(idInt, idString, 7);
		this.textureType = -4;
	}

	@Override
	public Text name(int damage)
	{
		return getName(this.idString, damage);
	}

}
