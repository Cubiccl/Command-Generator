package fr.cubiccl.generator.gameobject;

import fr.cubiccl.generator.gameobject.baseobjects.Item;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.tags.TagNumber;
import fr.cubiccl.generator.gameobject.tags.TagString;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;

public class ItemStack
{

	public static ItemStack createFrom(TagCompound tag)
	{
		Item item = ObjectRegistry.items.find(((TagString) tag.getTagFromId(Tags.ITEM_ID.id())).value());
		int amount = ((TagNumber) tag.getTagFromId(Tags.ITEM_COUNT.id())).value();
		int data = ((TagNumber) tag.getTagFromId(Tags.ITEM_DAMAGE.id())).value();
		int slot = tag.hasTag(Tags.ITEM_SLOT.id()) ? ((TagNumber) tag.getTagFromId(Tags.ITEM_SLOT.id())).value() : -1;
		ItemStack is = new ItemStack(item, data, amount, (TagCompound) tag.getTagFromId(Tags.ITEM_NBT.id()));
		is.slot = slot;
		return is;
	}

	public final int amount, damage;
	public final Item item;
	public final TagCompound nbt;
	public int slot;

	public ItemStack(Item item, int data, int amount)
	{
		this(item, data, amount, new TagCompound(Tags.ITEM_NBT));
	}

	public ItemStack(Item item, int data, int amount, TagCompound nbt)
	{
		super();
		this.item = item;
		this.damage = data;
		this.amount = amount;
		this.slot = 0;
		this.nbt = nbt;
	}

	public String toCommand()
	{
		return this.item.id() + " " + this.amount + " " + this.damage + " " + this.nbt.toCommand();
	}

	public TagCompound toTag(TemplateCompound container)
	{
		if (this.slot == -1) return new TagCompound(container, new TagString(Tags.ITEM_ID, this.item.id()), new TagNumber(Tags.ITEM_DAMAGE, this.damage),
				new TagNumber(Tags.ITEM_COUNT, this.amount), this.nbt);
		return new TagCompound(container, new TagString(Tags.ITEM_ID, this.item.id()), new TagNumber(Tags.ITEM_DAMAGE, this.damage), new TagNumber(
				Tags.ITEM_COUNT, this.amount), new TagNumber(Tags.ITEM_SLOT, this.slot), this.nbt);
	}

}
