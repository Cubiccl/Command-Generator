package fr.cubiccl.generator.gameobject;

import fr.cubiccl.generator.gameobject.baseobjects.EnchantmentType;

public class Enchantment
{

	public final int level;
	public final EnchantmentType type;

	public Enchantment(EnchantmentType type, int level)
	{
		this.type = type;
		this.level = level;
	}

	public String toCommand()
	{
		return this.type.idString + " " + this.level;
	}

}
