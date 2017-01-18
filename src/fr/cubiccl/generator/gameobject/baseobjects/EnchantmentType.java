package fr.cubiccl.generator.gameobject.baseobjects;

import java.awt.image.BufferedImage;

import fr.cubiccl.generator.gameobject.NamedObject;
import fr.cubiccl.generator.gameobject.registries.NumIdObject;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.utils.Text;
import fr.cubiccl.generator.utils.Textures;

public class EnchantmentType implements NamedObject, BaseObject, NumIdObject
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
		ObjectRegistry.enchantments.register(this);
	}

	@Override
	public String id()
	{
		return this.idString;
	}

	@Override
	public Text name()
	{
		return new Text("enchantment." + this.idString);
	}

	@Override
	public Integer numId()
	{
		return this.idInt;
	}

	public BufferedImage texture()
	{
		return Textures.getTexture("enchantment." + this.idString);
	}

}
