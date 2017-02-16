package fr.cubiccl.generator.gameobject.templatetags.custom;

import java.awt.Component;

import fr.cubiccl.generator.gameobject.LivingEntity;
import fr.cubiccl.generator.gameobject.baseobjects.BaseObject;
import fr.cubiccl.generator.gameobject.baseobjects.Entity;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gameobject.tags.*;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gameobject.templatetags.TemplateList;
import fr.cubiccl.generator.gui.component.interfaces.IObjectList;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.label.ImageLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.tag.SpawnPotentialPanel;
import fr.cubiccl.generator.gui.component.panel.utils.ListProperties;
import fr.cubiccl.generator.gui.component.panel.utils.PanelObjectList;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

public class TemplateSpawnPotentials extends TemplateList
{
	public static class SpawnPotential implements IObjectList<SpawnPotential>
	{
		public static SpawnPotential createFrom(Tag tag)
		{
			int w = 1;
			Entity e = ObjectRegistry.entities.first();
			TagCompound p = new TagCompound(Tags.DEFAULT_COMPOUND);

			for (Tag t : (Tag[]) tag.value())
			{
				if (t.id().equals(Tags.ENTITY_TYPE.id())) e = ObjectRegistry.entities.find((String) t.value());
				else if (t.id().equals(Tags.ENTITY_WEIGHT.id())) w = (int) t.value();
				else if (t.id().equals(Tags.ENTITY_PROPERTIES.id())) p = (TagCompound) t;
			}

			return new SpawnPotential(new LivingEntity(e, p), w);
		}

		public final LivingEntity entity;
		public final int weight;

		public SpawnPotential()
		{
			this(new LivingEntity(ObjectRegistry.entities.first(), new TagCompound(Tags.DEFAULT_COMPOUND)), 1);
		}

		public SpawnPotential(LivingEntity entity, int weight)
		{
			this.entity = entity;
			this.weight = weight;
		}

		@Override
		public CGPanel createPanel(ListProperties properties)
		{
			SpawnPotentialPanel p = new SpawnPotentialPanel();
			p.setupFrom(this);
			return p;
		}

		@Override
		public Component getDisplayComponent()
		{
			CGPanel p = new CGPanel();
			p.add(new CGLabel(new Text(this.toString(), false)));
			p.add(new ImageLabel(this.entity.entity.texture()));
			return p;
		}

		@Override
		public String getName(int index)
		{
			return this.entity.entity.name().toString();
		}

		@Override
		public SpawnPotential setupFrom(CGPanel panel) throws CommandGenerationException
		{
			return ((SpawnPotentialPanel) panel).createSpawnPotential();
		}

		@Override
		public String toString()
		{
			return this.weight + ", " + this.entity.entity.name().toString();
		}

		public TagCompound toTag(TemplateCompound container)
		{
			TagString type = new TagString(Tags.ENTITY_TYPE, this.entity.entity.id);
			TagNumber weight = new TagNumber(Tags.ENTITY_WEIGHT, this.weight);
			return new TagCompound(container, type, weight, this.entity.toTag(Tags.ENTITY_PROPERTIES));
		}

	}

	public TemplateSpawnPotentials(String id, byte applicationType, String[] applicable)
	{
		super(id, applicationType, applicable);
	}

	@Override
	protected CGPanel createPanel(BaseObject object, Tag previousValue)
	{
		PanelObjectList<SpawnPotential> p = new PanelObjectList<SpawnPotential>(null, (String) null, SpawnPotential.class);
		if (previousValue != null)
		{
			TagList t = ((TagList) previousValue);
			for (int i = 0; i < t.size(); ++i)
				p.add(SpawnPotential.createFrom(t.getTag(i)));
		}
		p.setName(this.title());
		return p;
	}

	@Override
	public Tag generateTag(BaseObject object, CGPanel panel)
	{
		@SuppressWarnings("unchecked")
		SpawnPotential[] values = ((PanelObjectList<SpawnPotential>) panel).values();
		TagCompound[] tags = new TagCompound[values.length];
		for (int i = 0; i < tags.length; ++i)
			tags[i] = values[i].toTag(Tags.DEFAULT_COMPOUND);
		return new TagList(this, tags);
	}

}
