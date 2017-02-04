package fr.cubiccl.generator.gameobject;

import java.util.ArrayList;

import fr.cubiccl.generator.gameobject.baseobjects.Entity;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.tags.TagString;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;

public class LivingEntity extends GameObject
{
	public static LivingEntity createFrom(TagCompound tag)
	{
		Entity e = ObjectRegistry.entities.first();
		TagCompound nbt = tag;
		if (tag.hasTag("id")) e = ObjectRegistry.entities.find(((TagString) tag.getTagFromId("id")).value());

		ArrayList<Tag> tags = new ArrayList<Tag>();
		for (Tag t : nbt.value())
			if (!t.id().equals(Tags.ENTITY_ID) && !t.id().equals(Tags.OBJECT_NAME)) tags.add(t);
		nbt = new TagCompound((TemplateCompound) nbt.template, tags.toArray(new Tag[tags.size()]));

		LivingEntity en = new LivingEntity(e, nbt);
		en.findName(tag);
		return en;
	}

	public final Entity entity;
	public final TagCompound nbt;

	public LivingEntity(Entity entity, TagCompound nbt)
	{
		this.entity = entity;
		this.nbt = nbt;
	}

	@Override
	public String toCommand()
	{
		return this.entity.id();
	}

	@Override
	public String toString()
	{
		return this.entity.name().toString();
	}

	@Override
	public TagCompound toTag(TemplateCompound container, boolean includeName)
	{
		ArrayList<Tag> tags = new ArrayList<Tag>();
		tags.add(new TagString(Tags.ENTITY_ID, this.entity.id()));
		for (Tag t : this.nbt.value())
			tags.add(t);
		if (includeName) tags.add(this.nameTag());
		return new TagCompound(container, tags.toArray(new Tag[tags.size()]));
	}

}
