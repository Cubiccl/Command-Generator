package fr.cubiccl.generator.gameobject.templatetags.custom;

import fr.cubiccl.generator.gameobject.AppliedAttribute;
import fr.cubiccl.generator.gameobject.baseobjects.BaseObject;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.tags.TagList;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateList;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.utils.PanelObjectList;

public class TemplateAttributes extends TemplateList
{

	@Override
	protected CGPanel createPanel(BaseObject<?> object, Tag previousValue)
	{
		AppliedAttribute[] attributes = new AppliedAttribute[0];
		if (previousValue != null)
		{
			TagList t = (TagList) previousValue;
			attributes = new AppliedAttribute[t.size()];
			for (int i = 0; i < attributes.length; i++)
				attributes[i] = new AppliedAttribute().fromNBT((TagCompound) t.getTag(i));
		}
		PanelObjectList<AppliedAttribute> p = new PanelObjectList<AppliedAttribute>(null, (String) null, AppliedAttribute.class);
		p.setName(this.title());
		return p;
	}

	@Override
	public TagList generateTag(BaseObject<?> object, CGPanel panel)
	{
		@SuppressWarnings("unchecked")
		AppliedAttribute[] list = ((PanelObjectList<AppliedAttribute>) panel).values();
		TagCompound[] tags = new TagCompound[list.length];
		for (int i = 0; i < tags.length; i++)
			tags[i] = list[i].toNBT(Tags.DEFAULT_COMPOUND);
		return this.create(tags);
	}
}
