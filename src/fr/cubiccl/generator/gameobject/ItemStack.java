package fr.cubiccl.generator.gameobject;

import fr.cubiccl.generator.gameobject.baseobjects.Item;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.tags.TagNumber;
import fr.cubiccl.generator.gameobject.tags.TagString;

public class ItemStack
{

	public final int amount, data;
	public final Item item;

	public ItemStack(Item item, int data, int amount)
	{
		super();
		this.item = item;
		this.data = data;
		this.amount = amount;
	}

	public String toCommand()
	{
		return this.item.idString + " " + this.amount + " " + this.data;
	}

	public Tag toTag()
	{
		return new TagCompound("item", new TagString("id", this.item.idString), new TagNumber("Damage", Integer.toString(this.data), TagNumber.SHORT),
				new TagNumber("Count", Integer.toString(this.amount), TagNumber.BYTE));
	}

}
