package fr.cubiccl.generator.gameobject;

import fr.cubiccl.generator.utils.Text;

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
	public Text name()
	{
		return new Text(this.id, false);
	}

}
