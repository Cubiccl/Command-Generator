package fr.cubiccl.generator.gameobject;

import java.awt.Component;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import org.jdom2.Element;

import fr.cubiccl.generator.gameobject.baseobjects.Attribute;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gameobject.tags.*;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TagsMain;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gameobject.utils.TestValue;
import fr.cubiccl.generator.gui.component.interfaces.IObjectList;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelAttributeModifier;
import fr.cubiccl.generator.gui.component.panel.utils.ListProperties;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

/** Represents an AttributeModifier. */
public class AttributeModifier extends GameObject<AttributeModifier> implements IObjectList<AttributeModifier>
{
	/** Identifiers for operation modes.<br />
	 * <br />
	 * <table border="1">
	 * <tr>
	 * <td>ID</td>
	 * <td>Variable</td>
	 * <td>Mode</td>
	 * </tr>
	 * <tr>
	 * <td>0</td>
	 * <td>OP_ADD</td>
	 * <td>Add to base.</td>
	 * </tr>
	 * <tr>
	 * <td>1</td>
	 * <td>OP_MULTIPLY</td>
	 * <td>Multiply base.</td>
	 * </tr>
	 * <tr>
	 * <td>2</td>
	 * <td>OP_MULTIPLY_ALL</td>
	 * <td>Multiplies everything.</td>
	 * </tr>
	 * </table> */
	public static final byte OP_ADD = 0, OP_MULTIPLY = 1, OP_MULTIPLY_ALL = 2;

	/** Slot names. */
	public static final String[] SLOTS =
	{ "mainhand", "offhand", "feet", "legs", "chest", "head" };

	/** The amount to apply. When {@link AttributeModifier#amountMax} isn't -1, the minimum amount. */
	public double amount;
	/** If not -1, the maximum amount to apply. */
	public double amountMax;
	/** The {@link Attribute} to modify. */
	private Attribute attribute;
	/** <code>true</code> if is in test mode. */
	public boolean isInLootTable;
	/** A name for this Modifier. */
	public String name;
	/** The operation type.
	 * 
	 * @see AttributeModifier#OP_ADD */
	public byte operation;
	/** The slots this modifier is applied on. */
	public String[] slots;
	/** The UUID of this modifier. */
	public long UUIDMost, UUIDLeast;

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
	public AttributeModifier duplicate(AttributeModifier object)
	{
		this.amount = object.amount;
		this.amountMax = object.amountMax;
		this.attribute = object.attribute;
		this.isInLootTable = object.isInLootTable;
		this.name = object.name;
		this.operation = object.operation;
		this.UUIDMost = object.UUIDMost;
		this.UUIDLeast = object.UUIDLeast;
		this.slots = new String[object.slots.length];
		System.arraycopy(object.slots, 0, this.slots, 0, this.slots.length);
		return this;
	}

	@Override
	public AttributeModifier fromNBT(TagCompound nbt)
	{
		ArrayList<String> s = new ArrayList<String>();

		if (nbt.hasTag(Tags.ATTRIBUTE_ATTRIBUTE_NAME)) this.attribute = ObjectRegistry.attributes.find(nbt.getTag(Tags.ATTRIBUTE_ATTRIBUTE_NAME).value());
		if (nbt.hasTag(Tags.ATTRIBUTE_attribute_name)) this.attribute = ObjectRegistry.attributes.find(nbt.getTag(Tags.ATTRIBUTE_attribute_name).value());
		if (nbt.hasTag(Tags.ATTRIBUTE_MODIFIER_NAME)) this.name = nbt.getTag(Tags.ATTRIBUTE_MODIFIER_NAME).value();
		if (nbt.hasTag(Tags.ATTRIBUTE_modifier_name)) this.name = nbt.getTag(Tags.ATTRIBUTE_modifier_name).value();
		if (nbt.hasTag(Tags.ATTRIBUTE_SLOT)) s.add(nbt.getTag(Tags.ATTRIBUTE_SLOT).value());
		if (nbt.hasTag(Tags.ATTRIBUTE_slots))
		{
			for (Tag slot : nbt.getTag(Tags.ATTRIBUTE_slots).value())
				s.add(((TagString) slot).value());
		}
		if (nbt.hasTag(Tags.ATTRIBUTE_OPERATION)) this.operation = (byte) nbt.getTag(Tags.ATTRIBUTE_OPERATION).valueInt();
		if (nbt.hasTag(Tags.ATTRIBUTE_operation)) this.operation = (byte) nbt.getTag(Tags.ATTRIBUTE_operation).valueInt();
		if (nbt.hasTag(Tags.ATTRIBUTE_AMOUNT)) this.amount = nbt.getTag(Tags.ATTRIBUTE_AMOUNT).value();
		if (nbt.hasTag(Tags.ATTRIBUTE_amount)) this.amount = nbt.getTag(Tags.ATTRIBUTE_amount).value();
		if (nbt.hasTag(Tags.ATTRIBUTE_amount_range))
		{
			TagCompound container = nbt.getTag(Tags.ATTRIBUTE_amount_range);
			this.amount = container.getTag(TagsMain.VALUE_MIN_FLOAT).value();
			this.amountMax = container.getTag(TagsMain.VALUE_MAX_FLOAT).value();
		}
		if (nbt.hasTag(Tags.ATTRIBUTE_UUIDMOST)) this.UUIDMost = (long) (double) nbt.getTag(Tags.ATTRIBUTE_UUIDMOST).value();
		if (nbt.hasTag(Tags.ATTRIBUTE_UUIDLEAST)) this.UUIDLeast = (long) (double) nbt.getTag(Tags.ATTRIBUTE_UUIDLEAST).value();

		this.slots = s.toArray(new String[s.size()]);
		this.findName(nbt);
		return this;
	}

	@Override
	public AttributeModifier fromXML(Element xml)
	{
		ArrayList<String> slots = new ArrayList<String>();
		for (Element slot : xml.getChildren("slot"))
			slots.add(slot.getText());

		this.name = xml.getChildText("name");
		this.attribute = ObjectRegistry.attributes.find(xml.getChildText("attribute"));
		this.operation = Byte.parseByte(xml.getChildText("operation"));
		this.amount = Double.parseDouble(xml.getChildText("amount"));
		this.amountMax = Double.parseDouble(xml.getChildText("amountmax"));
		this.UUIDLeast = Long.parseLong(xml.getChildText("uuidleast"));
		this.UUIDMost = Long.parseLong(xml.getChildText("uuidmost"));
		this.findProperties(xml);
		return this;
	}

	/** Getter for {@link AttributeModifier#attribute}. */
	public Attribute getAttribute()
	{
		return attribute;
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

	/** Setter for {@link AttributeModifier#attribute}. */
	public void setAttribute(Attribute attribute)
	{
		this.attribute = attribute;
		this.onChange();
	}

	@Override
	public String toCommand()
	{
		return this.toString();
	}

	@Override
	@Deprecated
	public TagCompound toNBT(TemplateCompound container)
	{
		return this.toTag(container, false);
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

	/** @param isApplied - True if is applied to an entity. Thus attribute and slot won't be included. */
	public TagCompound toTag(TemplateCompound container, boolean isApplied)
	{
		boolean lt = this.isInLootTable;
		ArrayList<Tag> tags = new ArrayList<Tag>();
		tags.add((lt ? Tags.ATTRIBUTE_modifier_name : Tags.ATTRIBUTE_MODIFIER_NAME).create(this.name));
		tags.add((lt ? Tags.ATTRIBUTE_operation : Tags.ATTRIBUTE_OPERATION).create(this.operation));

		TestValue v = new TestValue(lt ? Tags.ATTRIBUTE_amount : Tags.ATTRIBUTE_AMOUNT, Tags.ATTRIBUTE_amount_range, TagsMain.VALUE_MAX_FLOAT,
				TagsMain.VALUE_MIN_FLOAT, this.amount);
		v.isRanged = this.amountMax != -1;
		v.valueMax = this.amountMax;
		tags.add(v.toTag());

		if (!lt) tags.add(Tags.ATTRIBUTE_UUIDMOST.create(this.UUIDMost));
		if (!lt) tags.add(Tags.ATTRIBUTE_UUIDLEAST.create(this.UUIDLeast));
		if (!isApplied)
		{
			tags.add((lt ? Tags.ATTRIBUTE_attribute_name : Tags.ATTRIBUTE_ATTRIBUTE_NAME).create(this.attribute.id()));
			if (!lt) tags.add(Tags.ATTRIBUTE_SLOT.create(this.slots[0]));
			else
			{
				TagString[] s = new TagString[this.slots.length];
				for (int i = 0; i < s.length; ++i)
					s[i] = Tags.DEFAULT_STRING.create(this.slots[i]);
				tags.add(Tags.ATTRIBUTE_slots.create(s));
			}
		}
		TagCompound t = container.create(tags.toArray(new Tag[tags.size()]));
		return t;
	}

	@Override
	public Element toXML()
	{
		Element root = this.createRoot("modifier");
		root.addContent(new Element("name").setText(this.name));
		root.addContent(new Element("attribute").setText(this.attribute.id()));
		root.addContent(new Element("operation").setText(Byte.toString(this.operation)));
		root.addContent(new Element("amount").setText(Double.toString(this.amount)));
		root.addContent(new Element("amountmax").setText(Double.toString(this.amountMax)));
		root.addContent(new Element("uuidleast").setText(Long.toString(this.UUIDLeast)));
		root.addContent(new Element("uuidmost").setText(Long.toString(this.UUIDMost)));
		for (String slot : this.slots)
			root.addContent(new Element("slot").setText(slot));
		return root;
	}

	@Override
	public AttributeModifier update(CGPanel panel) throws CommandGenerationException
	{
		AttributeModifier m = ((PanelAttributeModifier) panel).generate();
		this.amount = m.amount;
		this.amountMax = m.amountMax;
		this.attribute = m.attribute;
		this.isInLootTable = m.isInLootTable;
		this.name = m.name;
		this.operation = m.operation;
		this.slots = m.slots;
		this.UUIDMost = m.UUIDMost;
		this.UUIDLeast = m.UUIDLeast;
		return this;
	}
}
