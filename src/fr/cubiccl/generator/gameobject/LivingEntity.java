package fr.cubiccl.generator.gameobject;

import java.util.ArrayList;

import fr.cubiccl.generator.gameobject.baseobjects.Entity;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.tags.TagString;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;

public class LivingEntity
{
	public static LivingEntity createFrom(TagCompound tag)
	{
		Entity e = ObjectRegistry.entities.first();
		TagCompound nbt = tag;
		if (tag.hasTag("id")) e = ObjectRegistry.entities.find(((TagString) tag.getTagFromId("id")).value());
		return new LivingEntity(e, nbt);
	}

	public final Entity entity;
	public final TagCompound nbt;

	public LivingEntity(Entity entity, TagCompound nbt)
	{
		this.entity = entity;
		this.nbt = nbt;
	}

	public TagCompound toTag(TemplateCompound container)
	{
		ArrayList<Tag> tags = new ArrayList<Tag>();
		tags.add(new TagString(Tags.ENTITY_ID, this.entity.id()));
		for (Tag t : this.nbt.value())
			tags.add(t);
		return new TagCompound(container, tags.toArray(new Tag[tags.size()]));
	}

}
