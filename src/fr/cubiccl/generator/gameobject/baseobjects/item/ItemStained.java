package fr.cubiccl.generator.gameobject.baseobjects.item;

import fr.cubiccl.generator.gameobject.baseobjects.Item;
import fr.cubiccl.generator.gameobject.baseobjects.block.BlockStained;
import fr.cubiccl.generator.utils.Text;

/** This Item has 16 data values, determining which color it has. */
public class ItemStained extends Item
{

	public ItemStained()
	{
		super();
		this.setMaxDamage(15);
	}

	@Override
	public Text name(int damage)
	{
		return BlockStained.getName(this.id(), damage);
	}

}
