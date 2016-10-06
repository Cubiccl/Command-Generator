package fr.cubiccl.generator.gameobject.baseobjects;

import fr.cubiccl.generator.gameobject.NamedObject;
import fr.cubiccl.generator.gameobject.ObjectRegistry;
import fr.cubiccl.generator.utils.Lang;

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
	public String name()
	{
		return Lang.translate("attribute." + this.id);
	}

}
