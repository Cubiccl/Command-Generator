package fr.cubiccl.generator.gameobject;

import java.awt.Component;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import fr.cubiccl.generator.gameobject.baseobjects.Attribute;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gameobject.tags.*;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gui.component.interfaces.IObjectList;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelAttributeModifier;
import fr.cubiccl.generator.gui.component.panel.utils.ListProperties;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

public class AttributeModifier extends GameObject implements IObjectList<AttributeModifier>
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

		AttributeModifier m = new AttributeModifier(a, n, s, o, am, um, ul);
		m.findName(tag);
		return m;
	}

	public final double amount;
	public final Attribute attribute;
	public final String name, slot;
	public final byte operation;
	public final long UUIDMost, UUIDLeast;

	public AttributeModifier()
	{
		this(ObjectRegistry.attributes.find("generic.armor"), "", "mainhand", (byte) 0, 0, ThreadLocalRandom.current().nextLong(), ThreadLocalRandom.current()
				.nextLong());
	}

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

	@Override
	public CGPanel createPanel(ListProperties properties)
	{
		PanelAttributeModifier p = new PanelAttributeModifier(properties.contains("isApplied") && (boolean) properties.get("isApplied"),
				properties.hasCustomObjects());
		p.setupFrom(this);
		return p;
	}

	@Override
	public Component getDisplayComponent()
	{
		return new CGLabel(new Text(this.name, false));
	}

	@Override
	public String getName(int index)
	{
		return this.customName() != null && !this.customName().equals("") ? this.customName() : this.name;
	}

	@Override
	public AttributeModifier setupFrom(CGPanel panel) throws CommandGenerationException
	{
		return ((PanelAttributeModifier) panel).generate();
	}

	@Override
	public String toCommand()
	{
		return this.toString();
	}

	@Override
	public String toString()
	{
		return this.name + ", affects " + this.attribute.name().toString();
	}

	@Override
	@Deprecated
	public TagCompound toTag(TemplateCompound container, boolean includeName)
	{
		return this.toTag(container, true, includeName);
	}

	/** @param isApplied - True if is applied to an entity. Thus attribute and slot won't be included. */
	public TagCompound toTag(TemplateCompound container, boolean isApplied, boolean includeName)
	{
		ArrayList<Tag> tags = new ArrayList<Tag>();
		tags.add(new TagString(Tags.ATTRIBUTE_MODIFIER_NAME, this.name));
		tags.add(new TagNumber(Tags.ATTRIBUTE_OPERATION, this.operation));
		tags.add(new TagBigNumber(Tags.ATTRIBUTE_AMOUNT, this.amount));
		tags.add(new TagBigNumber(Tags.ATTRIBUTE_UUIDMOST, this.UUIDMost));
		tags.add(new TagBigNumber(Tags.ATTRIBUTE_UUIDLEAST, this.UUIDLeast));
		if (!isApplied)
		{
			tags.add(new TagString(Tags.ATTRIBUTE_ATTRIBUTE_NAME, this.attribute.id));
			tags.add(new TagString(Tags.ATTRIBUTE_SLOT, this.slot));
		}
		if (includeName) tags.add(this.nameTag());
		return new TagCompound(container, tags.toArray(new Tag[tags.size()]));
	}
}
