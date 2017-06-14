package fr.cubiccl.generator.gameobject.registries;

import fr.cubiccl.generator.gameobject.baseobjects.Item;

/** Specific Registry for Items. */
public class ItemRegistry extends ObjectRegistry<Item>
{

	public ItemRegistry()
	{
		super(true, true, Item.class);
	}

	@Override
	public void checkNames()
	{
		for (Item i : this.registry.values())
		{
			i.mainName().toString();
			if (!i.hasDurability) for (int d : i.getDamageValues())
				i.name(d).toString();
		}
	}

	@Override
	public void loadTextures()
	{
		for (Item i : this.registry.values())
			if (i.hasDurability) i.texture(0);
			else for (int d : i.getDamageValues())
				i.texture(d);
	}

}
