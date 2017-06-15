package fr.cubiccl.generator.gameobject.baseobjects;

import java.awt.image.BufferedImage;

import org.jdom2.Element;

import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.utils.Text;

public class Achievement extends BaseObject<Achievement>
{

	/** This Achievement's ID. */
	private String id;
	/** The Item to use for this Achievement's Texture. */
	private Item textureItem;

	public Achievement()
	{}

	public Achievement(String id, Item item)
	{
		this.id = "minecraft:" + id;
		this.textureItem = item;
	}

	@Override
	public Achievement fromXML(Element xml)
	{
		this.id = "minecraft:" + xml.getAttributeValue("id");
		this.textureItem = ObjectRegistry.items.find(xml.getAttributeValue("item"));
		return this;
	}

	@Override
	public String id()
	{
		return this.id;
	}

	@Override
	public Text name()
	{
		return new Text("achievement." + this.id);
	}

	@Override
	public Achievement register()
	{
		ObjectRegistry.achievements.register(this);
		return this;
	}

	@Override
	public BufferedImage texture()
	{
		return this.textureItem.texture();
	}

	/** Getter for {@link Achievement#textureItem}. */
	public Item textureItem()
	{
		return this.textureItem;
	}

	@Override
	public String toString()
	{
		return this.name().toString();
	}

	@Override
	public Element toXML()
	{
		return new Element("achievement").setAttribute("id", this.id().substring("minecraft:".length())).setAttribute("item",
				this.textureItem.id().substring("minecraft:".length()));
	}

}
