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
		String n = "";
		ArrayList<String> s = new ArrayList<String>();
		byte o = OP_ADD;
		double am = 0, amm = -1;
		long um = 0, ul = 0;

		if (tag.hasTag(Tags.ATTRIBUTE_ATTRIBUTE_NAME)) a = ObjectRegistry.attributes.find(((TagString) tag.getTag(Tags.ATTRIBUTE_ATTRIBUTE_NAME)).value());
		if (tag.hasTag(Tags.ATTRIBUTE_attribute_name)) a = ObjectRegistry.attributes.find(((TagString) tag.getTag(Tags.ATTRIBUTE_attribute_name)).value());
		if (tag.hasTag(Tags.ATTRIBUTE_MODIFIER_NAME)) n = ((TagString) tag.getTag(Tags.ATTRIBUTE_MODIFIER_NAME)).value();
		if (tag.hasTag(Tags.ATTRIBUTE_modifier_name)) n = ((TagString) tag.getTag(Tags.ATTRIBUTE_modifier_name)).value();
		if (tag.hasTag(Tags.ATTRIBUTE_SLOT)) s.add(((TagString) tag.getTag(Tags.ATTRIBUTE_SLOT)).value());
		if (tag.hasTag(Tags.ATTRIBUTE_slots))
		{
			for (Tag slot : ((TagList) tag.getTag(Tags.ATTRIBUTE_slots)).value())
				s.add(((TagString) slot).value());
		}
		if (tag.hasTag(Tags.ATTRIBUTE_OPERATION)) o = (byte) (int) ((TagNumber) tag.getTag(Tags.ATTRIBUTE_OPERATION)).value();
		if (tag.hasTag(Tags.ATTRIBUTE_operation)) o = (byte) (int) ((TagNumber) tag.getTag(Tags.ATTRIBUTE_operation)).value();
		if (tag.hasTag(Tags.ATTRIBUTE_AMOUNT)) am = ((TagBigNumber) tag.getTag(Tags.ATTRIBUTE_AMOUNT)).value();
		if (tag.hasTag(Tags.ATTRIBUTE_amount)) am = ((TagBigNumber) tag.getTag(Tags.ATTRIBUTE_amount)).value();
		if (tag.hasTag(Tags.ATTRIBUTE_amount_range))
		{
			TagCompound container = (TagCompound) tag.getTag(Tags.ATTRIBUTE_amount_range);
			am = ((TagBigNumber) container.getTag(Tags.LT_FUNCTION_MIN_FLOAT)).value();
			amm = ((TagBigNumber) container.getTag(Tags.LT_FUNCTION_MAX_FLOAT)).value();
		}
		if (tag.hasTag(Tags.ATTRIBUTE_UUIDMOST)) um = (long) (double) ((TagBigNumber) tag.getTag(Tags.ATTRIBUTE_UUIDMOST)).value();
		if (tag.hasTag(Tags.ATTRIBUTE_UUIDLEAST)) ul = (long) (double) ((TagBigNumber) tag.getTag(Tags.ATTRIBUTE_UUIDLEAST)).value();

		AttributeModifier m = new AttributeModifier(a, n, s.toArray(new String[s.size()]), o, am, amm, um, ul);
		m.findName(tag);
		return m;
	}

	public final double amount, amountMax;
	public final Attribute attribute;
	public final boolean isInLootTable;
	public final String name;
	public final byte operation;
	public final String[] slots;
	public final long UUIDMost, UUIDLeast;

	public AttributeModifier()
	{
		this(ObjectRegistry.attributes.find("generic.armor"), "", new String[]
		{ "mainhand" }, (byte) 0, 0, ThreadLocalRandom.current().nextLong(), ThreadLocalRandom.current().nextLong());
	}

	public AttributeModifier(Attribute attribute, String name, String[] slots, byte operation, double amount, double amountMax, long UUIDMost, long UUIDLeast)
	{
		super();
		this.attribute = attribute;
		this.name = name;
		this.slots = slots;
		this.operation = operation;
		this.amount = amount;
		this.amountMax = amountMax;
		this.UUIDMost = UUIDMost;
		this.UUIDLeast = UUIDLeast;
		this.isInLootTable = this.slots.length > 1;
	}

	public AttributeModifier(Attribute attribute, String name, String[] slots, byte operation, double amount, long UUIDMost, long UUIDLeast)
	{
		this(attribute, name, slots, operation, amount, -1, UUIDMost, UUIDLeast);
	}

	@Override
	public CGPanel createPanel(ListProperties properties)
	{
		PanelAttributeModifier p = new PanelAttributeModifier(properties.isTrue("isApplied"), properties.isTrue("random_slots"), properties.hasCustomObjects());
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
		String display = "";
		if (this.amount >= 0) display += "+";
		display += this.amount;
		if (this.operation == OP_MULTIPLY) display += "%";
		display += " " + this.attribute.name().toString();
		return display;
	}

	@Override
	@Deprecated
	public TagCompound toTag(TemplateCompound container, boolean includeName)
	{
		return this.toTag(container, false, includeName);
	}

	/** @param isApplied - True if is applied to an entity. Thus attribute and slot won't be included. */
	public TagCompound toTag(TemplateCompound container, boolean isApplied, boolean includeName)
	{
		boolean lt = this.isInLootTable;
		ArrayList<Tag> tags = new ArrayList<Tag>();
		tags.add((lt ? Tags.ATTRIBUTE_modifier_name : Tags.ATTRIBUTE_MODIFIER_NAME).create(this.name));
		tags.add((lt ? Tags.ATTRIBUTE_operation : Tags.ATTRIBUTE_OPERATION).create(this.operation));
		if (this.amountMax != -1) tags.add(Tags.ATTRIBUTE_amount_range.create(Tags.LT_FUNCTION_MIN_FLOAT.create(this.amount),
				Tags.LT_FUNCTION_MAX_FLOAT.create(this.amountMax)));
		else tags.add((lt ? Tags.ATTRIBUTE_amount : Tags.ATTRIBUTE_AMOUNT).create(this.amount));
		if (!lt) tags.add(Tags.ATTRIBUTE_UUIDMOST.create(this.UUIDMost));
		if (!lt) tags.add(Tags.ATTRIBUTE_UUIDLEAST.create(this.UUIDLeast));
		if (!isApplied)
		{
			tags.add((lt ? Tags.ATTRIBUTE_attribute_name : Tags.ATTRIBUTE_ATTRIBUTE_NAME).create(this.attribute.id));
			if (!lt) tags.add(Tags.ATTRIBUTE_SLOT.create(this.slots[0]));
			else
			{
				TagString[] s = new TagString[this.slots.length];
				for (int i = 0; i < s.length; ++i)
					s[i] = Tags.DEFAULT_STRING.create(this.slots[i]);
				tags.add(Tags.ATTRIBUTE_slots.create(s));
			}
		}
		if (includeName) tags.add(this.nameTag());
		TagCompound t = container.create(tags.toArray(new Tag[tags.size()]));
		return t;
	}
}
