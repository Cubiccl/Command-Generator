package fr.cubiccl.generator.gameobject;

import java.awt.Component;

import org.jdom2.Element;

import fr.cubiccl.generator.gameobject.baseobjects.EnchantmentType;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.tags.TagNumber;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gui.component.interfaces.IObjectList;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelEnchantment;
import fr.cubiccl.generator.gui.component.panel.utils.ListProperties;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

public class Enchantment extends GameObject implements IObjectList<Enchantment>
{

	public static Enchantment createFrom(Element enchant)
	{
		Enchantment e = new Enchantment(ObjectRegistry.enchantments.find(enchant.getChildText("id")), Integer.parseInt(enchant.getChildText("level")));
		e.findProperties(enchant);
		return e;
	}

	public static Enchantment createFrom(TagCompound tag)
	{
		EnchantmentType type = ObjectRegistry.enchantments.first();
		int level = 1;
		for (Tag t : tag.value())
		{
			if (t.id().equals(Tags.ENCHANTMENT_ID.id())) type = ObjectRegistry.enchantments.find(((TagNumber) t).valueInt());
			if (t.id().equals(Tags.ENCHANTMENT_LVL.id())) level = ((TagNumber) t).valueInt();
		}
		Enchantment e = new Enchantment(type, level);
		e.findName(tag);
		return e;
	}

	private int level;
	private EnchantmentType type;

	public Enchantment()
	{
		this(ObjectRegistry.enchantments.find("protection"), 1);
	}

	public Enchantment(EnchantmentType type, int level)
	{
		this.type = type;
		this.level = level;
	}

	@Override
	public CGPanel createPanel(ListProperties properties)
	{
		PanelEnchantment p = new PanelEnchantment(false, properties.hasCustomObjects());
		p.setupFrom(this);
		return p;
	}

	@Override
	public Component getDisplayComponent()
	{
		return new CGLabel(new Text(this.toString(), false));
	}

	public int getLevel()
	{
		return level;
	}

	@Override
	public String getName(int index)
	{
		return this.customName() != null && !this.customName().equals("") ? this.customName() : this.toString();
	}

	public EnchantmentType getType()
	{
		return type;
	}

	public void setLevel(int level)
	{
		this.level = level;
		this.onChange();
	}

	public void setType(EnchantmentType type)
	{
		this.type = type;
		this.onChange();
	}

	@Override
	public String toCommand()
	{
		return this.type.idString + " " + this.level;
	}

	@Override
	public String toString()
	{
		return this.type.name().toString() + " " + this.level;
	}

	@Override
	public TagCompound toTag(TemplateCompound container)
	{
		return container.create(Tags.ENCHANTMENT_ID.create(this.type.idInt), Tags.ENCHANTMENT_LVL.create(this.level));
	}

	@Override
	public Element toXML()
	{
		Element root = this.createRoot("enchantment");
		root.addContent(new Element("id").setText(this.type.id()));
		root.addContent(new Element("level").setText(Integer.toString(this.level)));
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

}
