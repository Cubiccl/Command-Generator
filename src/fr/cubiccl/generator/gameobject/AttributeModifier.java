package fr.cubiccl.generator.gameobject;

import fr.cubiccl.generator.gameobject.baseobjects.Attribute;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagBigNumber;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.tags.TagNumber;
import fr.cubiccl.generator.gameobject.tags.TagString;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;

public class AttributeModifier
{
	public static final byte OP_ADD = 0, OP_MULTIPLY = 1, OP_MULTIPLY_ALL = 2;
	
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
