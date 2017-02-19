package fr.cubiccl.generator.gameobject.templatetags.custom;

import fr.cubiccl.generator.gameobject.LivingEntity;
import fr.cubiccl.generator.gameobject.baseobjects.BaseObject;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.tags.TagList;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateList;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.utils.PanelObjectList;

public class TemplateEntityList extends TemplateList
{

	public TemplateEntityList(String id, byte applicationType, String[] applicable)
	{
		super(id, applicationType, applicable);
	}

	@Override
	protected CGPanel createPanel(BaseObject object, Tag previousValue)
	{
		PanelObjectList<LivingEntity> p = new PanelObjectList<LivingEntity>(null, (String) null, LivingEntity.class);
		if (previousValue != null)
		{
			TagList t = (TagList) previousValue;
			for (int i = 0; i < t.size(); i++)
				p.add(LivingEntity.createFrom((TagCompound) t.getTag(i)));
		}
		p.setName(this.title());
		return p;
	}

	@Override
	public TagList generateTag(BaseObject object, CGPanel panel)
	{
		@SuppressWarnings("unchecked")
		LivingEntity[] entities = ((PanelObjectList<LivingEntity>) panel).values();
		TagCompound[] t = new TagCompound[entities.length];
		for (int i = 0; i < t.length; i++)
			t[i] = entities[i].toTag(Tags.DEFAULT_COMPOUND);
		return this.create(t);
	}

}
