package fr.cubiccl.generator.gameobject.baseobjects;

import java.awt.image.BufferedImage;

import fr.cubiccl.generator.gameobject.NamedObject;
import fr.cubiccl.generator.gameobject.ObjectRegistry;
import fr.cubiccl.generator.utils.Text;
import fr.cubiccl.generator.utils.Textures;

public class EnchantmentType implements NamedObject
{

	/** This Enchantment's numerical ID. */
	public final int idInt;
	/** This Enchantment's text ID. */
	public final String idString;
	/** This Enchantment's maximum Level in survival. */
	public final int maxLevel;

	public EnchantmentType(int idInt, String idString, int maxLevel)
	{
		this.idInt = idInt;
		this.idString = idString;
		this.maxLevel = maxLevel;
		ObjectRegistry.registerEnchantment(this);
	}

	@Override
	public Text name()
	{
		return new Text("enchantment." + this.idString);
	}

	public BufferedImage texture()
	{
		return Textures.getTexture("enchantment." + this.idString);
	}

}
