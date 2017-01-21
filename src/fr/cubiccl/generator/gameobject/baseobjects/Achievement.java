package fr.cubiccl.generator.gameobject.baseobjects;

import java.awt.image.BufferedImage;

import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.utils.Text;

public class Achievement extends BaseObject
{

	/** The ID of this Achievement. */
	public final String id;
	/** The Item to use for this Achievement's Texture. */
	public final Item textureItem;

	public Achievement(String id, Item textureItem)
	{
		this.id = "minecraft:" + id;
		this.textureItem = textureItem;
		ObjectRegistry.achievements.register(this);
	}

	@Override
	public String id()
	{
		return this.id;
	}

	@Override
	public Text name()
	{
		return new Text("achievement." + this.id);
	}

	@Override
	public BufferedImage texture()
	{
		return this.textureItem.texture();
	}

}
