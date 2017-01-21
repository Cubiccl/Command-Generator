package fr.cubiccl.generator.gameobject.baseobjects;

import java.awt.image.BufferedImage;

import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.utils.Text;
import fr.cubiccl.generator.utils.Textures;

public class EffectType extends BaseObject
{

	/** This Effect's numerical ID. */
	public final int idInt;
	/** This Effect's string ID. */
	public final String idString;

	public EffectType(int idInt, String idString)
	{
		this.idInt = idInt;
		this.idString = "minecraft:" + idString;
		ObjectRegistry.effects.register(this);
	}

	@Override
	public String id()
	{
		return this.idString;
	}

	@Override
	public int idNum()
	{
		return this.idInt;
	}

	@Override
	public Text name()
	{
		return new Text("effect." + this.idString);
	}

	@Override
	public BufferedImage texture()
	{
		return Textures.getTexture("effect." + this.idString);
	}

}
