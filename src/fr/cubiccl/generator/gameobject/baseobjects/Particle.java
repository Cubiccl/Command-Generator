package fr.cubiccl.generator.gameobject.baseobjects;

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

}
