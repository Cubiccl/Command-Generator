package fr.cubiccl.generator.gameobject.baseobjects;

import fr.cubiccl.generator.gameobject.NamedObject;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.utils.Text;

public class Attribute implements NamedObject, BaseObject
{

	/** This Attribute's ID. */
	public final String id;

	public Attribute(String id)
	{
		this.id = "minecraft:" + id;
		ObjectRegistry.attributes.register(this);
	}

	@Override
	public String id()
	{
		return this.id;
	}

	@Override
	public Text name()
	{
		return new Text("attribute." + this.id);
	}

}
