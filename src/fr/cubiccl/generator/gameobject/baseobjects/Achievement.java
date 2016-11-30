package fr.cubiccl.generator.gameobject.baseobjects;

import java.awt.image.BufferedImage;

import fr.cubiccl.generator.gameobject.NamedObject;
import fr.cubiccl.generator.gameobject.ObjectRegistry;
import fr.cubiccl.generator.utils.Text;

public class Achievement implements NamedObject
{

	/** The ID of this Achievement. */
	public final String id;
	/** The Item to use for this Achievement's Texture. */
	public final Item textureItem;

	public Achievement(String id, Item textureItem)
	{
		this.id = id;
		this.textureItem = textureItem;
		ObjectRegistry.registerAchievement(this);
	}

	@Override
	public Text name()
	{
		return new Text("achievement." + this.id);
	}

	public BufferedImage texture()
	{
		return this.textureItem.texture(this.textureItem.damage[0]);
	}

}
