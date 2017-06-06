package fr.cubiccl.generator.gameobject.baseobjects;

import java.awt.image.BufferedImage;

import org.jdom2.Element;

import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.utils.Text;

/** A default Recipe from Minecraft. */
public class DefaultAdvancement extends BaseObject
{
	/** This Advancement's category. */
	public final String category;
	/** The list of criterias of this Advancement. */
	public final String[] criteria;
	/** The damage value of the display Item. -1 for no particular damage value. */
	public int data = -1;
	/** This Advancement's ID. */
	public final String id;
	/** The display Item. */
	public final Item item;

	public DefaultAdvancement(String id, Item item, String category, String... criteria)
	{
		this.id = id;
		this.item = item;
		this.criteria = criteria;
		this.category = category;
		if (id != null) ObjectRegistry.advancements.register(this);
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
		if (this.criteria.length != 1 || !("minecraft:" + this.criteria[0]).equals(this.item.id())) for (String criterion : this.criteria)
			root.addContent(new Element("criterion").setText(criterion));
		return root;
	}

}
