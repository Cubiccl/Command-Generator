package fr.cubiccl.generator.gameobject;

public class Sound implements NamedObject
{

	/** This Sound's ID. */
	public final String id;

	public Sound(String id)
	{
		this.id = id;
		ObjectRegistry.registerSound(this);
	}

	@Override
	public String name()
	{
		return this.id;
	}

}
