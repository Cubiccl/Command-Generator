package fr.cubiccl.generator.gameobject.baseobjects;

import java.awt.Component;
import java.awt.image.BufferedImage;

import org.jdom2.Element;

import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gui.component.interfaces.IObjectList;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelEnchantment;
import fr.cubiccl.generator.gui.component.panel.utils.ListProperties;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

public class EnchantmentType extends BaseObject<EnchantmentType> implements IObjectList<EnchantmentType>
{

	/** This Enchantment's numerical ID. */
	private int idInt;
	/** This Enchantment's text ID. */
	private String idString;
	/** This Enchantment's maximum Level in survival. */
	private int maxLevel;

	public EnchantmentType()
	{
		this.idInt = -1;
	}

	public EnchantmentType(int idNum, String idStr)
	{
		this.idInt = idNum;
		this.idString = "minecraft:" + idStr;
	}

	@Override
	public CGPanel createPanel(ListProperties properties)
	{
		PanelEnchantment p = new PanelEnchantment(false, false);
		p.setEnchantment(this);
		p.setHasLevel(false);
		return p;
	}

	@Override
	public EnchantmentType duplicate(EnchantmentType object)
	{
		return this;
	}

	@Override
	public EnchantmentType fromXML(Element xml)
	{
		this.idString = "minecraft:" + xml.getAttributeValue("idstr");
		this.idInt = Integer.parseInt(xml.getAttributeValue("idint"));
		this.maxLevel = Integer.parseInt(xml.getAttributeValue("maxlevel"));
		return this;
	}

	@Override
	public Component getDisplayComponent()
	{
		return new CGLabel(this.name());
	}

	@Override
	public String getName(int index)
	{
		return this.name().toString();
	}

	@Override
	public String id()
	{
		return this.idString;
	}

	@Override
	public int idNum()
	{
		return this.idInt;
	}

	/** Getter for {@link EnchantmentType#maxLevel}. */
	public int maxLevel()
	{
		return this.maxLevel;
	}

	@Override
	public Text name()
	{
		if (this.idString == null) return null;
		return new Text("enchantment." + this.idString);
	}

	@Override
	public EnchantmentType register()
	{
		ObjectRegistry.enchantments.register(this);
		return this;
	}

	@Override
	public BufferedImage texture()
	{
		return null;
	}

	@Override
	public Element toXML()
	{
		Element e = new Element("enchantment").setAttribute("idint", Integer.toString(this.idInt)).setAttribute("idstr",
				this.id().substring("minecraft:".length()));
		e.setAttribute("maxlevel", Integer.toString(this.maxLevel));
		return e;
	}

	@Override
	public EnchantmentType update(CGPanel panel) throws CommandGenerationException
	{
		return ((PanelEnchantment) panel).selectedEnchantment();
	}

}
