package fr.cubiccl.generator.gameobject;

import fr.cubiccl.generator.gameobject.baseobjects.BaseObject;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.utils.Text;

public class Sound implements NamedObject, BaseObject
{

	/** This Sound's ID. */
	public final String id;

	public Sound(String id)
	{
		this.id = id;
		ObjectRegistry.sounds.register(this);
	}

	@Override
	public String id()
	{
		return this.id;
	}

	@Override
	public Text name()
	{
		return new Text(this.id, false);
	}

}
