package fr.cubiccl.generator.gameobject.templatetags.custom;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gameobject.LivingEntity;
import fr.cubiccl.generator.gameobject.baseobjects.Entity;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gameobject.tags.*;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gui.component.interfaces.IObjectList;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.tag.SpawnPotentialPanel;
import fr.cubiccl.generator.gui.component.panel.utils.PanelObjectList;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

public class TemplateSpawnPotentials extends TemplateCompound
{
	public static class SpawnPotential
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

		public SpawnPotential(LivingEntity entity, int weight)
		{
			this.entity = entity;
			this.weight = weight;
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

	private class SpawnPotentialList implements IObjectList
	{
		private ArrayList<SpawnPotential> spawnPotentials;

		private SpawnPotentialList(SpawnPotential[] potentials)
		{
			this.spawnPotentials = new ArrayList<SpawnPotential>();
			for (SpawnPotential spawnPotential : potentials)
				this.spawnPotentials.add(spawnPotential);
		}

		@Override
		public boolean addObject(CGPanel panel)
		{
			try
			{
				this.spawnPotentials.add(((SpawnPotentialPanel) panel).createSpawnPotential());
				return true;
			} catch (CommandGenerationException e)
			{
				CommandGenerator.report(e);
				return false;
			}
		}

		@Override
		public CGPanel createAddPanel()
		{
			return new SpawnPotentialPanel();
		}

		@Override
		public Text getName(int index)
		{
			return new Text(this.getValues()[index], false);
		}

		@Override
		public BufferedImage getTexture(int index)
		{
			return this.spawnPotentials.get(index).entity.entity.texture();
		}

		@Override
		public String[] getValues()
		{
			String[] values = new String[this.spawnPotentials.size()];
			for (int i = 0; i < values.length; i++)
				values[i] = this.spawnPotentials.get(i).toString();
			return values;
		}

		@Override
		public void removeObject(int index)
		{
			this.spawnPotentials.remove(index);
		}

	}

	public TemplateSpawnPotentials(String id, byte tagType, String[] applicable)
	{
		super(id, tagType, applicable);
	}

	@Override
	protected CGPanel createPanel(String objectId, Tag previousValue)
	{
		SpawnPotential[] potentials = new SpawnPotential[0];
		if (previousValue != null)
		{
			TagList t = ((TagList) previousValue);
			potentials = new SpawnPotential[t.size()];
			for (int i = 0; i < potentials.length; ++i)
				potentials[i] = SpawnPotential.createFrom(t.getTag(i));
		}

		PanelObjectList p = new PanelObjectList(new SpawnPotentialList(potentials));
		p.setName(this.title());
		return p;
	}

	@Override
	public Tag generateTag(CGPanel panel)
	{
		ArrayList<SpawnPotential> values = ((SpawnPotentialList) ((PanelObjectList) panel).objectList).spawnPotentials;
		TagCompound[] tags = new TagCompound[values.size()];
		for (int i = 0; i < tags.length; ++i)
			tags[i] = values.get(i).toTag(Tags.DEFAULT_COMPOUND);
		return new TagList(this, tags);
	}

}
