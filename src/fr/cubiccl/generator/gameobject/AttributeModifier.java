package fr.cubiccl.generator.gameobject;

import fr.cubiccl.generator.gameobject.baseobjects.Attribute;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gameobject.tags.*;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;

public class AttributeModifier
{
	public static final byte OP_ADD = 0, OP_MULTIPLY = 1, OP_MULTIPLY_ALL = 2;

	public static final String[] SLOTS =
	{ "mainhand", "offhand", "feet", "legs", "chest", "head" };

	public static AttributeModifier createFrom(TagCompound tag)
	{
		Attribute a = ObjectRegistry.attributes.first();
		String n = "", s = SLOTS[0];
		byte o = OP_ADD;
		double am = 0;
		long um = 0, ul = 0;

		for (Tag t : tag.value())
		{
			if (t.id().equals(Tags.ATTRIBUTE_ATTRIBUTE_NAME.id())) a = ObjectRegistry.attributes.find(((TagString) t).value());
			if (t.id().equals(Tags.ATTRIBUTE_MODIFIER_NAME.id())) n = ((TagString) t).value();
			if (t.id().equals(Tags.ATTRIBUTE_SLOT.id())) s = ((TagString) t).value();
			if (t.id().equals(Tags.ATTRIBUTE_OPERATION.id())) o = (byte) (int) ((TagNumber) t).value();
			if (t.id().equals(Tags.ATTRIBUTE_AMOUNT.id())) am = ((TagBigNumber) t).value();
			if (t.id().equals(Tags.ATTRIBUTE_UUIDMOST.id())) um = (long) (double) ((TagBigNumber) t).value();
			if (t.id().equals(Tags.ATTRIBUTE_UUIDLEAST.id())) ul = (long) (double) ((TagBigNumber) t).value();
		}

		return new AttributeModifier(a, n, s, o, am, um, ul);
	}

	public final double amount;
	public final Attribute attribute;
	public final String name, slot;
	public final byte operation;
	public final long UUIDMost, UUIDLeast;

	public AttributeModifier(Attribute attribute, String name, String slot, byte operation, double amount, long UUIDMost, long UUIDLeast)
	{
		super();
		this.attribute = attribute;
		this.name = name;
		this.slot = slot;
		this.operation = operation;
		this.amount = amount;
		this.UUIDMost = UUIDMost;
		this.UUIDLeast = UUIDLeast;
	}

	/** @param isApplied - True if is applied to an entity. Thus attribute and slot won't be included. */
	public TagCompound toTag(TemplateCompound container, boolean isApplied)
	{
		Tag[] tags = new Tag[isApplied ? 5 : 7];
		tags[0] = new TagString(Tags.ATTRIBUTE_MODIFIER_NAME, this.name);
		tags[1] = new TagNumber(Tags.ATTRIBUTE_OPERATION, this.operation);
		tags[2] = new TagBigNumber(Tags.ATTRIBUTE_AMOUNT, this.amount);
		tags[3] = new TagBigNumber(Tags.ATTRIBUTE_UUIDMOST, this.UUIDMost);
		tags[4] = new TagBigNumber(Tags.ATTRIBUTE_UUIDLEAST, this.UUIDLeast);
		if (!isApplied)
		{
			tags[5] = new TagString(Tags.ATTRIBUTE_ATTRIBUTE_NAME, this.attribute.id);
			tags[6] = new TagString(Tags.ATTRIBUTE_SLOT, this.slot);
		}
		return new TagCompound(container, tags);
	}

}
