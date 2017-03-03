package fr.cubiccl.generator.gameobject.baseobjects;

import org.jdom2.Element;

import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;

public class Particle extends BaseObject
{

	/** This Particle's ID. */
	public final String id;

	public Particle(String id)
	{
		this.id = "minecraft:" + id;
		ObjectRegistry.particles.register(this);
	}

	@Override
	public String id()
	{
		return this.id;
	}

	@Override
	public Element toXML()
	{
		return new Element("particle").setAttribute("id", this.id().substring("minecraft:".length()));
	}

}
