package fr.cubiccl.generator.gameobject;

import java.awt.Component;
import java.util.ArrayList;

import fr.cubiccl.generator.gameobject.baseobjects.Entity;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.tags.TagString;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gui.component.interfaces.IObjectList;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.label.ImageLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelEntity;
import fr.cubiccl.generator.gui.component.panel.utils.ListProperties;
import fr.cubiccl.generator.utils.CommandGenerationException;

public class LivingEntity extends GameObject implements IObjectList<LivingEntity>
{
	public static LivingEntity createFrom(TagCompound tag)
	{
		Entity e = ObjectRegistry.entities.first();
		TagCompound nbt = tag;
		if (tag.hasTag("id")) e = ObjectRegistry.entities.find(((TagString) tag.getTagFromId("id")).value());

		ArrayList<Tag> tags = new ArrayList<Tag>();
		for (Tag t : nbt.value())
			if (!t.id().equals(Tags.ENTITY_ID) && !t.id().equals(Tags.OBJECT_NAME)) tags.add(t);
		nbt = ((TemplateCompound) nbt.template).create(tags.toArray(new Tag[tags.size()]));

		LivingEntity en = new LivingEntity(e, nbt);
		en.findName(tag);
		return en;
	}

	public Entity entity;
	public TagCompound nbt;

	public LivingEntity()
	{
		this(ObjectRegistry.entities.find("area_effect_cloud"), Tags.DEFAULT_COMPOUND.create());
	}

	public LivingEntity(Entity entity, TagCompound nbt)
	{
		this.entity = entity;
		this.nbt = nbt;
	}

	@Override
	public CGPanel createPanel(ListProperties properties)
	{
		PanelEntity p = new PanelEntity(null, true, properties.hasCustomObjects(), false);
		p.setEntity(this.entity);
		p.setTags(this.nbt.value());
		return p;
	}

	@Override
	public Component getDisplayComponent()
	{
		CGPanel p = new CGPanel();
		p.add(new CGLabel(this.entity.name()));
		p.add(new ImageLabel(this.entity.texture()));
		return p;
	}

	@Override
	public String getName(int index)
	{
		return this.customName() != null && !this.customName().equals("") ? this.customName() : this.entity.name().toString();
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
		tags.add(Tags.ENTITY_ID.create(this.entity.id()));
		for (Tag t : this.nbt.value())
			tags.add(t);
		if (includeName) tags.add(this.nameTag());
		return container.create(tags.toArray(new Tag[tags.size()]));
	}

	@Override
	public LivingEntity update(CGPanel panel) throws CommandGenerationException
	{
		LivingEntity e = ((PanelEntity) panel).generate();
		this.entity = e.entity;
		this.nbt = e.nbt;
		return this;
	}

}
