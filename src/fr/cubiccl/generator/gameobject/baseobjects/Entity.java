package fr.cubiccl.generator.gameobject.baseobjects;

import java.awt.image.BufferedImage;

import org.jdom2.Element;

import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.utils.Text;
import fr.cubiccl.generator.utils.Textures;

public class Entity extends BaseObject<Entity>
{
	/** The Player Entity. */
	public static final Entity PLAYER = new Entity();
	static
	{
		PLAYER.id = "minecraft:player";
	}

	/** This Entity's ID. */
	public String id;

	public Entity()
	{}

	public Entity(String id)
	{
		this.id = "minecraft:" + id;
	}

	@Override
	public Entity fromXML(Element xml)
	{
		this.id = "minecraft:" + xml.getAttributeValue("id");
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
		return new Text("entity." + this.id);
	}

	@Override
	public Entity register()
	{
		ObjectRegistry.entities.register(this);
		return this;
	}

	@Override
	public BufferedImage texture()
	{
		return Textures.getTexture("entity." + this.id);
	}

	@Override
	public String toString()
	{
		return this.name().toString();
	}

	@Override
	public Element toXML()
	{
		return new Element("entity").setAttribute("id", this.id().substring("minecraft:".length()));
	}

}
