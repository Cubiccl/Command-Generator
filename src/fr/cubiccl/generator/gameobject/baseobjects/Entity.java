package fr.cubiccl.generator.gameobject.baseobjects;

import java.awt.image.BufferedImage;

import fr.cubiccl.generator.gameobject.NamedObject;
import fr.cubiccl.generator.gameobject.ObjectRegistry;
import fr.cubiccl.generator.utils.Text;
import fr.cubiccl.generator.utils.Textures;

public class Entity implements NamedObject
{

	/** This Entity's ID. */
	public final String id;

	public Entity(String id)
	{
		this.id = id;
		ObjectRegistry.registerEntity(this);
	}

	@Override
	public Text name()
	{
		return new Text("entity." + this.id);
	}

	public BufferedImage texture()
	{
		return Textures.getTexture("entity." + this.id);
	}

}
