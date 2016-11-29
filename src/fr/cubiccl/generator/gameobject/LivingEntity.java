package fr.cubiccl.generator.gameobject;

import fr.cubiccl.generator.gameobject.baseobjects.Entity;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.tags.TagString;
import fr.cubiccl.generator.gameobject.templatetags.Tags;

public class LivingEntity
{
	public final Entity entity;

	public LivingEntity(Entity entity)
	{
		this.entity = entity;
	}

	public Tag toTag()
	{
		return new TagCompound(Tags.ENTITY, new TagString(Tags.ENTITY_TYPE, this.entity.id));
	}

}
