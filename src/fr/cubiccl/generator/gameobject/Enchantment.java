package fr.cubiccl.generator.gameobject;

import fr.cubiccl.generator.gameobject.baseobjects.EnchantmentType;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.tags.TagNumber;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;

public class Enchantment
{

	public static Enchantment createFrom(TagCompound tag)
	{
		EnchantmentType type = ObjectRegistry.getEnchantmentTypes()[0];
		int level = 1;
		for (Tag t : tag.value())
		{
			if (t.id().equals(Tags.ENCHANTMENT_ID.id)) type = ObjectRegistry.getEnchantmentFromID(((TagNumber) t).value());
			if (t.id().equals(Tags.ENCHANTMENT_LVL.id)) level = ((TagNumber) t).value();
		}
		return new Enchantment(type, level);
	}

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

	@Override
	public String toString()
	{
		return this.type.name().toString() + " " + this.level;
	}

	public TagCompound toTag(TemplateCompound container)
	{
		return new TagCompound(container, new TagNumber(Tags.ENCHANTMENT_ID, this.type.idInt), new TagNumber(Tags.ENCHANTMENT_LVL, this.level));
	}

}
