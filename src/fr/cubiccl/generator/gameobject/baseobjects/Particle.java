package fr.cubiccl.generator.gameobject.baseobjects;

import org.jdom2.Element;

import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;

public class Particle extends BaseObject<Particle>
{

	/** This Particle's ID. */
	private String id;

	public Particle()
	{}

	public Particle(String id)
	{
		this.id = "minecraft:" + id;
	}

	@Override
	public Particle fromXML(Element xml)
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
	public Particle register()
	{
		ObjectRegistry.particles.register(this);
		return this;
	}

	@Override
	public Element toXML()
	{
		return new Element("particle").setAttribute("id", this.id().substring("minecraft:".length()));
	}

}
