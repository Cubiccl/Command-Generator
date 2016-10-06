package fr.cubiccl.generator.gameobject.baseobjects;

import java.awt.image.BufferedImage;

import fr.cubiccl.generator.gameobject.NamedObject;
import fr.cubiccl.generator.gameobject.ObjectRegistry;
import fr.cubiccl.generator.utils.Lang;
import fr.cubiccl.generator.utils.Textures;

public class EffectType implements NamedObject
{

	/** This Effect's ID. */
	public final int idInt;
	/** This Effect's ID. */
	public final String idString;

	public EffectType(int idInt, String idString)
	{
		this.idInt = idInt;
		this.idString = idString;
		ObjectRegistry.registerEffect(this);
	}

	@Override
	public String name()
	{
		return Lang.translate("effect." + this.idString);
	}

	public BufferedImage texture()
	{
		return Textures.getTexture("effect." + this.idString);
	}

}
