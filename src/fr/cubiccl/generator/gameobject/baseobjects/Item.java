package fr.cubiccl.generator.gameobject.baseobjects;

public class Item extends BlockItem
{
	public boolean hasDurability = false;

	public Item(int idInt, String idString)
	{
		super(ITEM, idInt, idString);
	}

	public Item(int idInt, String idString, int maxDamage)
	{
		super(ITEM, idInt, idString, maxDamage);
	}

	public Item(int idInt, String idString, int... damage)
	{
		super(ITEM, idInt, idString, damage);
	}

}
