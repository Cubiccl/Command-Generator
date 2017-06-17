package fr.cubiccl.generator.gameobject.baseobjects;

import java.awt.image.BufferedImage;
import java.util.List;

import org.jdom2.Element;

import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.utils.Text;

/** A default Recipe from Minecraft. */
public class DefaultAdvancement extends BaseObject<DefaultAdvancement>
{
	/** This Advancement's category. */
	private String category;
	/** The list of criterias of this Advancement. */
	private String[] criteria;
	/** The damage value of the display Item. -1 for no particular damage value. */
	private int data = -1;
	/** This Advancement's ID. */
	private String id;
	/** The display Item. */
	private Item item;

	public DefaultAdvancement()
	{
		this.data = -1;
	}

	/** Getter for {@link DefaultAdvancement#criteria}. */
	public String[] criteria()
	{
		return this.criteria.clone();
	}

	@Override
	public DefaultAdvancement fromXML(Element xml)
	{
		this.category = xml.getAttributeValue("category");
		this.id = xml.getAttributeValue("id");
		this.item = ObjectRegistry.items.find(xml.getAttributeValue("item"));
		if (xml.getAttribute("data") != null) this.data = Integer.parseInt(xml.getAttributeValue("data"));
		if (xml.getChildren("criterion").size() == 0) this.criteria = new String[]
		{ this.id };
		else
		{
			List<Element> c = xml.getChildren("criterion");
			this.criteria = new String[c.size()];
			for (int i = 0; i < criteria.length; ++i)
				this.criteria[i] = c.get(i).getText();
		}
		return this;
	}

	@Override
	public String id()
	{
		return "minecraft:" + this.category + "/" + this.id;
	}

	@Override
	public Text name()
	{
		return new Text("advancement." + this.category + "." + this.id);
	}

	@Override
	public DefaultAdvancement register()
	{
		ObjectRegistry.advancements.register(this);
		return this;
	}

	@Override
	public BufferedImage texture()
	{
		if (this.data != -1) return this.item.texture(this.data);
		return this.item.texture();
	}

	@Override
	public Element toXML()
	{
		Element root = new Element("advancement");
		root.setAttribute("category", this.category);
		root.setAttribute("id", this.id);
		root.setAttribute("item", this.item.id().substring("minecraft:".length()));
		if (this.data != -1) root.setAttribute("data", Integer.toString(this.data));
		if (this.criteria.length != 1 || !this.criteria[0].equals(this.id)) for (String criterion : this.criteria)
			root.addContent(new Element("criterion").setText(criterion));
		return root;
	}

}
