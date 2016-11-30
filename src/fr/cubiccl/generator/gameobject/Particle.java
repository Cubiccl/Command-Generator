package fr.cubiccl.generator.gameobject;

import fr.cubiccl.generator.utils.Text;

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
	public Text name()
	{
		return new Text(this.id, false);
	}

}
