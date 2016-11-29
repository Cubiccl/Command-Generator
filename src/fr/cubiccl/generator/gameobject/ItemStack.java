package fr.cubiccl.generator.gameobject;

import fr.cubiccl.generator.gameobject.baseobjects.Item;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.tags.TagNumber;
import fr.cubiccl.generator.gameobject.tags.TagString;
import fr.cubiccl.generator.gameobject.templatetags.Tags;

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
		return new TagCompound(Tags.ITEM, new TagString(Tags.ITEM_ID, this.item.idString), new TagNumber(Tags.ITEM_DAMAGE, Integer.toString(this.data)),
				new TagNumber(Tags.ITEM_COUNT, Integer.toString(this.amount)));
	}

}
