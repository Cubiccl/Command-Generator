package fr.cubiccl.generator.gameobject.templatetags.custom;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import fr.cubiccl.generator.gameobject.LivingEntity;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.tags.TagList;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateList;
import fr.cubiccl.generator.gui.component.interfaces.IObjectList;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelEntity;
import fr.cubiccl.generator.gui.component.panel.utils.PanelObjectList;
import fr.cubiccl.generator.utils.Text;

public class TemplateEntityList extends TemplateList
{

	private class EntityList implements IObjectList
	{

		private ArrayList<LivingEntity> entities = new ArrayList<LivingEntity>();

		public EntityList(LivingEntity[] entities)
		{
			for (LivingEntity e : entities)
				this.entities.add(e);
		}

		@Override
		public boolean addObject(CGPanel panel)
		{
			this.entities.add(((PanelEntity) panel).generateEntity());
			return true;
		}

		@Override
		public CGPanel createAddPanel()
		{
			return new PanelEntity(null);
		}

		@Override
		public Text getName(int index)
		{
			return this.entities.get(index).entity.name();
		}

		@Override
		public BufferedImage getTexture(int index)
		{
			return this.entities.get(index).entity.texture();
		}

		@Override
		public String[] getValues()
		{
			String[] values = new String[this.entities.size()];
			for (int i = 0; i < values.length; i++)
				values[i] = this.entities.get(i).entity.name().toString();
			return values;
		}

		@Override
		public void removeObject(int index)
		{
			this.entities.remove(index);
		}

	}

	public TemplateEntityList(String id, byte tagType, String[] applicable)
	{
		super(id, tagType, applicable);
	}

	@Override
	protected CGPanel createPanel(String objectId, Tag previousValue)
	{
		LivingEntity[] entities = new LivingEntity[0];
		if (previousValue != null)
		{
			TagList t = (TagList) previousValue;
			entities = new LivingEntity[t.size()];
			for (int i = 0; i < entities.length; i++)
				entities[i] = LivingEntity.createFrom((TagCompound) t.getTag(i));
		}
		PanelObjectList p = new PanelObjectList(new EntityList(entities));
		p.setName(this.title());
		return p;
	}

	@Override
	public TagList generateTag(CGPanel panel)
	{
		ArrayList<LivingEntity> entities = ((EntityList) ((PanelObjectList) panel).objectList).entities;
		TagCompound[] t = new TagCompound[entities.size()];
		for (int i = 0; i < t.length; i++)
			t[i] = entities.get(i).toTag(Tags.DEFAULT_COMPOUND);
		return new TagList(this, t);
	}

}
