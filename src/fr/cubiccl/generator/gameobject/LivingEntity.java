package fr.cubiccl.generator.gameobject;

import fr.cubiccl.generator.gameobject.baseobjects.Entity;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.tags.TagString;

public class LivingEntity
{
	public final Entity entity;

	public LivingEntity(Entity entity)
	{
		this.entity = entity;
	}

	public Tag toTag()
	{
		return new TagCompound("entity", new TagString("type", this.entity.id));
	}

}
