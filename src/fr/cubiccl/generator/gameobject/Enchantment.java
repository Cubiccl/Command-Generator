package fr.cubiccl.generator.gameobject;

import java.awt.Component;

import org.jdom2.Element;

import fr.cubiccl.generator.gameobject.baseobjects.EnchantmentType;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.tags.TagNumber;
import fr.cubiccl.generator.gameobject.tags.TagString;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gameobject.utils.TestValue;
import fr.cubiccl.generator.gui.component.interfaces.IObjectList;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelEnchantment;
import fr.cubiccl.generator.gui.component.panel.utils.ListProperties;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

/** Represents an Enchantment applied to an Item. */
public class Enchantment extends GameObject<Enchantment> implements IObjectList<Enchantment>
{

	/** Creates an Enchantment from the input NBT Tag.
	 * 
	 * @param tag - The NBT Tag describing the Enchantment.
	 * @return The created Enchantment. */
	public static Enchantment createFrom(TagCompound tag)
	{
		EnchantmentType type = ObjectRegistry.enchantments.first();
		int level = 1;
		for (Tag t : tag.value())
		{
			if (t.id().equals(Tags.ENCHANTMENT_ID.id())) type = ObjectRegistry.enchantments.find(((TagNumber) t).valueInt());
			if (t.id().equals(Tags.ENCHANTMENT_IDSTRING.id())) type = ObjectRegistry.enchantments.find(((TagString) t).value());
			if (t.id().equals(Tags.ENCHANTMENT_LVL.id())) level = ((TagNumber) t).valueInt();
		}
		Enchantment e = new Enchantment(type, level);
		e.level.findValue(tag);
		e.findName(tag);
		return e;
	}

	/** This Enchantment's level. */
	private TestValue level;
	/** This {@link EnchantmentType Enchantment type}. */
	private EnchantmentType type;

	public Enchantment()
	{
		this(ObjectRegistry.enchantments.find("protection"), 1);
	}

	public Enchantment(EnchantmentType type, int level)
	{
		this.type = type;
		this.level = new TestValue(Tags.ENCHANTMENT_LVLINT, Tags.ENCHANTMENT_LVLRANGE);
		this.level.isRanged = false;
		this.level.valueMin = level;
	}

	@Override
	public CGPanel createPanel(ListProperties properties)
	{
		PanelEnchantment p = new PanelEnchantment(false, properties.hasCustomObjects(), properties.isTrue("testing"));
		p.setupFrom(this);
		return p;
	}

	/** Creates an Enchantment from the input XML element.
	 * 
	 * @param xml - The XML element describing the Enchantment.
	 * @return The created Enchantment. */
	public Enchantment fromXML(Element xml)
	{
		this.type = ObjectRegistry.enchantments.find(xml.getChildText("id"));
		this.setLevel(Integer.parseInt(xml.getChildText("level")));
		this.findProperties(xml);
		return this;
	}

	@Override
	public Component getDisplayComponent()
	{
		return new CGLabel(new Text(this.toString(), false));
	}

	/** @return The level of this Enchantment, when fixed. */
	public int getLevel()
	{
		return (int) this.level.valueMin;
	}

	@Override
	public String getName(int index)
	{
		return this.customName() != null && !this.customName().equals("") ? this.customName() : this.toString();
	}

	/** Getter for {@link Enchantment#type}. */
	public EnchantmentType getType()
	{
		return type;
	}

	/** Changes the level of this Enchantment, when fixed. */
	public void setLevel(int level)
	{
		this.level.valueMin = level;
		this.onChange();
	}

	/** Setter for {@link Enchantment#type}. */
	public void setType(EnchantmentType type)
	{
		this.type = type;
		this.onChange();
	}

	@Override
	public String toCommand()
	{
		return this.type.idString + " " + this.getLevel();
	}

	@Override
	public String toString()
	{
		if (this.level.isRanged) return this.type.name().toString() + " " + this.getLevel() + "-" + (int) this.level.valueMax;
		return this.type.name().toString() + " " + this.getLevel();
	}

	@Override
	public TagCompound toTag(TemplateCompound container)
	{
		return container.create(Tags.ENCHANTMENT_ID.create(this.type.idInt), Tags.ENCHANTMENT_LVL.create(this.getLevel()));
	}

	public TagCompound toTagForTest(TemplateCompound container)
	{
		return container.create(Tags.ENCHANTMENT_IDSTRING.create(this.type.id()), this.level.toTag());
	}

	@Override
	public Element toXML()
	{
		Element root = this.createRoot("enchantment");
		root.addContent(new Element("id").setText(this.type.id()));
		root.addContent(new Element("level").setText(Integer.toString(this.getLevel())));
		return root;
	}

	@Override
	public Enchantment update(CGPanel panel) throws CommandGenerationException
	{
		Enchantment e = ((PanelEnchantment) panel).generate();
		this.level = e.level;
		this.type = e.type;
		return this;
	}

	/** @return The level of this Enchantment, when tested. */
	public TestValue value()
	{
		return this.level;
	}

}
