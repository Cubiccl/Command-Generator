package fr.cubiccl.generator.gameobject;

import fr.cubiccl.generator.gameobject.baseobjects.Item;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.tags.TagNumber;
import fr.cubiccl.generator.gameobject.tags.TagString;
import fr.cubiccl.generator.gameobject.templatetags.Tags;

public class ItemStack
{

	public static ItemStack createFrom(TagCompound tag)
	{
		Item item = ObjectRegistry.getItemFromID(((TagString) tag.getTagFromId(Tags.ITEM_ID.id)).value());
		int amount = ((TagNumber) tag.getTagFromId(Tags.ITEM_COUNT.id)).value();
		int data = ((TagNumber) tag.getTagFromId(Tags.ITEM_DAMAGE.id)).value();
		int slot = ((TagNumber) tag.getTagFromId(Tags.ITEM_SLOT.id)).value();
		ItemStack is = new ItemStack(item, data, amount, (TagCompound) tag.getTagFromId(Tags.ITEM_NBT.id));
		is.slot = slot;
		return is;
	}

	public final int amount, data;
	public final Item item;
	public final TagCompound nbt;
	public int slot;

	public ItemStack(Item item, int data, int amount, TagCompound nbt)
	{
		super();
		this.item = item;
		this.data = data;
		this.amount = amount;
		this.slot = 0;
		this.nbt = nbt;
	}

	public String toCommand()
	{
		return this.item.idString + " " + this.amount + " " + this.data + " " + this.nbt.toCommand();
	}

	public TagCompound toTag()
	{
		return new TagCompound(Tags.ITEM, new TagString(Tags.ITEM_ID, this.item.idString), new TagNumber(Tags.ITEM_DAMAGE, this.data), new TagNumber(
				Tags.ITEM_COUNT, this.amount), new TagNumber(Tags.ITEM_SLOT, this.slot));
	}

}
