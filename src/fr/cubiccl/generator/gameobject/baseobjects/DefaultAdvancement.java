package fr.cubiccl.generator.gameobject.baseobjects;

import java.awt.image.BufferedImage;

import org.jdom2.Element;

import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.utils.Text;

public class DefaultAdvancement extends BaseObject
{
	public final String[] criteria;
	public final String id;
	public final Item item;

	public DefaultAdvancement(String id, Item item, String... criteria)
	{
		this.id = id;
		this.item = item;
		this.criteria = criteria;
		if (id != null) ObjectRegistry.advancements.register(this);
	}

	@Override
	public String id()
	{
		return "minecraft:story/" + this.id;
	}

	@Override
	public Text name()
	{
		return new Text("advancement." + this.id);
	}

	@Override
	public BufferedImage texture()
	{
		return this.item.texture();
	}

	@Override
	public Element toXML()
	{
		Element root = new Element("advancement");
		root.setAttribute("id", this.id);
		root.setAttribute("item", this.item.id().substring("minecraft:".length()));
		if (this.criteria.length != 1 || !("minecraft:" + this.criteria[0]).equals(this.item.id())) for (String criterion : this.criteria)
			root.addContent(new Element("criterion").setText(criterion));
		return root;
	}

}
