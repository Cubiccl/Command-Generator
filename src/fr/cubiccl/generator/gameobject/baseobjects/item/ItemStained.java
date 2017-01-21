package fr.cubiccl.generator.gameobject.baseobjects.item;

import fr.cubiccl.generator.gameobject.baseobjects.Item;
import fr.cubiccl.generator.gameobject.baseobjects.block.BlockStained;
import fr.cubiccl.generator.utils.Text;

public class ItemStained extends Item
{

	public ItemStained(int idInt, String idString)
	{
		super(idInt, idString, 15);
	}

	@Override
	public Text name(int damage)
	{
		return BlockStained.getName(this.id(), damage);
	}

}
