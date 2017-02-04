package fr.cubiccl.generator.gameobject;

import java.util.ArrayList;

import fr.cubiccl.generator.gameobject.baseobjects.Item;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.tags.TagNumber;
import fr.cubiccl.generator.gameobject.tags.TagString;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;

public class ItemStack extends GameObject
{

	public static ItemStack createFrom(TagCompound tag)
	{
		Item i = ObjectRegistry.items.first();
		int a = 1, d = 0, s = -1;
		TagCompound nbt = new TagCompound(Tags.ITEM_NBT);

		for (Tag t : tag.value())
		{
			if (t.id().equals(Tags.ITEM_ID)) i = ObjectRegistry.items.find(((TagString) t).value);
			if (t.id().equals(Tags.ITEM_COUNT)) a = ((TagNumber) t).value;
			if (t.id().equals(Tags.ITEM_DAMAGE)) d = ((TagNumber) t).value;
			if (t.id().equals(Tags.ITEM_SLOT)) s = ((TagNumber) t).value;
			if (t.id().equals(Tags.ITEM_NBT.id())) nbt = (TagCompound) t;
		}

		ItemStack is = new ItemStack(i, d, a, nbt);
		is.slot = s;
		is.findName(tag);
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

	@Override
	public String toCommand()
	{
		return this.item.id() + " " + this.amount + " " + this.damage + " " + this.nbt.valueForCommand();
	}

	@Override
	public String toString()
	{
		return this.amount + " " + this.item.name(this.damage);
	}

	@Override
	public TagCompound toTag(TemplateCompound container, boolean includeName)
	{
		ArrayList<Tag> tags = new ArrayList<Tag>();
		tags.add(new TagString(Tags.ITEM_ID, this.item.id()));
		tags.add(new TagNumber(Tags.ITEM_DAMAGE, this.damage));
		tags.add(new TagNumber(Tags.ITEM_COUNT, this.amount));
		if (this.slot != -1) tags.add(new TagNumber(Tags.ITEM_SLOT, this.slot));
		tags.add(this.nbt);
		if (includeName) tags.add(this.nameTag());
		return new TagCompound(container, tags.toArray(new Tag[tags.size()]));
	}

}
