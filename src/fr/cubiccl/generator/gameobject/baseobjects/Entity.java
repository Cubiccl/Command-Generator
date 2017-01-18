package fr.cubiccl.generator.gameobject.baseobjects;

import java.awt.image.BufferedImage;

import fr.cubiccl.generator.gameobject.IconedObject;
import fr.cubiccl.generator.gameobject.NamedObject;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.utils.Text;
import fr.cubiccl.generator.utils.Textures;

public class Entity implements NamedObject, IconedObject, BaseObject
{

	/** This Entity's ID. */
	public final String id;

	public Entity(String id)
	{
		this.id = id;
		ObjectRegistry.entities.register(this);
	}

	@Override
	public String id()
	{
		return this.id;
	}

	@Override
	public Text name()
	{
		return new Text("entity." + this.id);
	}

	@Override
	public BufferedImage texture()
	{
		return Textures.getTexture("entity." + this.id);
	}

}
