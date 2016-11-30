package fr.cubiccl.generator.gameobject.baseobjects;

import fr.cubiccl.generator.gameobject.NamedObject;
import fr.cubiccl.generator.gameobject.ObjectRegistry;
import fr.cubiccl.generator.utils.Text;

public class Attribute implements NamedObject
{

	/** This Attribute's ID. */
	public final String id;

	public Attribute(String id)
	{
		this.id = id;
		ObjectRegistry.registerAttribute(this);
	}

	@Override
	public Text name()
	{
		return new Text("attribute." + this.id);
	}

}
