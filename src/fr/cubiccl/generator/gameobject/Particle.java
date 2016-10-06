package fr.cubiccl.generator.gameobject;


public class Particle implements NamedObject
{

	/** This Particle's ID. */
	public final String id;

	public Particle(String id)
	{
		this.id = id;
		ObjectRegistry.registerParticle(this);
	}

	@Override
	public String name()
	{
		return this.id;
	}

}
