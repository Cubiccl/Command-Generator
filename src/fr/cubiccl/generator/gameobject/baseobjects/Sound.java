package fr.cubiccl.generator.gameobject.baseobjects;

import org.jdom2.Element;

import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;

public class Sound extends BaseObject<Sound>
{

	/** This Sound's ID. */
	private String id;

	public Sound()
	{}

	public Sound(String id)
	{
		this.id = "minecraft:" + id;
	}

	@Override
	public Sound fromXML(Element xml)
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
	public Sound register()
	{
		ObjectRegistry.sounds.register(this);
		return this;
	}

	@Override
	public Element toXML()
	{
		return new Element("sound").setAttribute("id", this.id().substring("minecraft:".length()));
	}

}
