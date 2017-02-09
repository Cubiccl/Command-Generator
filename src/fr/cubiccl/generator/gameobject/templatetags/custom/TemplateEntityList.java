package fr.cubiccl.generator.gameobject.templatetags.custom;

import java.awt.Component;
import java.util.ArrayList;

import fr.cubiccl.generator.gameobject.LivingEntity;
import fr.cubiccl.generator.gameobject.baseobjects.BaseObject;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.tags.TagList;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateList;
import fr.cubiccl.generator.gui.component.interfaces.IObjectList;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.label.ImageLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelEntity;
import fr.cubiccl.generator.gui.component.panel.utils.PanelObjectList;

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
		public boolean addObject(CGPanel panel, int editIndex)
		{
			if (editIndex == -1) this.entities.add(((PanelEntity) panel).generate());
			else this.entities.set(editIndex, ((PanelEntity) panel).generate());
			return true;
		}

		@Override
		public CGPanel createAddPanel(int editIndex)
		{
			PanelEntity p = new PanelEntity(null, true, true, false);
			if (editIndex != -1)
			{
				p.setEntity(this.entities.get(editIndex).entity);
				p.setTags(this.entities.get(editIndex).nbt.value());
			}
			return p;
		}

		@Override
		public Component getDisplayComponent(int index)
		{
			CGPanel p = new CGPanel();
			p.add(new CGLabel(this.entities.get(index).entity.name()));
			p.add(new ImageLabel(this.entities.get(index).entity.texture()));
			return p;
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

	public TemplateEntityList(String id, byte applicationType, String[] applicable)
	{
		super(id, applicationType, applicable);
	}

	@Override
	protected CGPanel createPanel(BaseObject object, Tag previousValue)
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
	public TagList generateTag(BaseObject object, CGPanel panel)
	{
		ArrayList<LivingEntity> entities = ((EntityList) ((PanelObjectList) panel).getObjectList()).entities;
		TagCompound[] t = new TagCompound[entities.size()];
		for (int i = 0; i < t.length; i++)
			t[i] = entities.get(i).toTag(Tags.DEFAULT_COMPOUND);
		return new TagList(this, t);
	}

}
