package fr.cubiccl.generator.gameobject.baseobjects;

import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;

public class Sound extends BaseObject
{

	/** This Sound's ID. */
	private final String id;

	public Sound(String id)
	{
		this.id = "minecraft:" + id;
		ObjectRegistry.sounds.register(this);
	}

	@Override
	public String id()
	{
		return this.id;
	}

}
