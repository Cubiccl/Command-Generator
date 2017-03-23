package fr.cubiccl.generator.gameobject.speedrun;

import fr.cubiccl.generator.gameobject.ItemStack;
import fr.cubiccl.generator.gameobject.baseobjects.Item;
import fr.cubiccl.generator.gameobject.tags.TagCompound;

public class ItemStackS extends ItemStack
{
	public static final byte KEY = 0, RESOURCE = 1, OPTIONNAL = 2;

	public byte importance = RESOURCE;

	public ItemStackS()
	{
		super();
	}

	public ItemStackS(Item item, int data, int amount)
	{
		super(item, data, amount);
	}

	public ItemStackS(Item item, int data, int amount, TagCompound nbt)
	{
		super(item, data, amount, nbt);
	}

	@Override
	public ItemStackS clone() throws CloneNotSupportedException
	{
		ItemStackS i = new ItemStackS(item, damage, amount, nbt);
		i.slot = this.slot;
		i.importance = this.importance;
		return i;
	}

	public boolean isKey()
	{
		return this.importance == KEY;
	}

	public boolean isOptionnal()
	{
		return this.importance == OPTIONNAL;
	}

	public boolean isResource()
	{
		return this.importance == RESOURCE;
	}

}
