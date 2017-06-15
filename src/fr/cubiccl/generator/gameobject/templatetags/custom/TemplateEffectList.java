package fr.cubiccl.generator.gameobject.templatetags.custom;

import fr.cubiccl.generator.gameobject.Effect;
import fr.cubiccl.generator.gameobject.baseobjects.BaseObject;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.tags.TagList;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateList;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.utils.PanelObjectList;

public class TemplateEffectList extends TemplateList
{

	@Override
	protected CGPanel createPanel(BaseObject<?> object, Tag previousValue)
	{
		PanelObjectList<Effect> p = new PanelObjectList<Effect>(null, (String) null, Effect.class);
		if (previousValue != null) for (Tag t : ((TagList) previousValue).value())
			p.add(new Effect().fromNBT((TagCompound) t));
		p.setName(this.title());
		return p;
	}

	@Override
	public TagList generateTag(BaseObject<?> object, CGPanel panel)
	{
		@SuppressWarnings("unchecked")
		Effect[] values = ((PanelObjectList<Effect>) panel).values();
		TagCompound[] tags = new TagCompound[values.length];
		for (int i = 0; i < tags.length; ++i)
			tags[i] = values[i].toNBT(Tags.DEFAULT_COMPOUND);
		return this.create(tags);
	}

}
