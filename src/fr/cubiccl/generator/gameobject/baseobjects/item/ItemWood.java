package fr.cubiccl.generator.gameobject.baseobjects.item;

import fr.cubiccl.generator.gameobject.baseobjects.Item;
import fr.cubiccl.generator.gameobject.baseobjects.block.BlockWood;
import fr.cubiccl.generator.utils.Text;

/** This Item has 6 data values, determining which type of wood it is made of. */
public class ItemWood extends Item
{

	public ItemWood()
	{
		super();
		this.setMaxDamage(5);
	}

	@Override
	public Text name(int damage)
	{
		return BlockWood.getName(this.id(), damage);
	}

}
