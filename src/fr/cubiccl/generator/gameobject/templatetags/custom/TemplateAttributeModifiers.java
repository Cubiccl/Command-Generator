package fr.cubiccl.generator.gameobject.templatetags.custom;

import fr.cubiccl.generator.gameobject.AttributeModifier;
import fr.cubiccl.generator.gameobject.baseobjects.BaseObject;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.tags.TagList;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateList;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.utils.PanelObjectList;

public class TemplateAttributeModifiers extends TemplateList
{
	public TemplateAttributeModifiers(String id, byte applicationType, String... applicable)
	{
		super(id, applicationType, applicable);
	}

	@Override
	protected CGPanel createPanel(BaseObject object, Tag previousValue)
	{
		PanelObjectList<AttributeModifier> p = new PanelObjectList<AttributeModifier>(null, (String) null, AttributeModifier.class, "isApplied", false);
		if (previousValue != null) for (Tag t : ((TagList) previousValue).value())
			p.add(AttributeModifier.createFrom((TagCompound) t));
		p.setName(this.title());
		return p;
	}

	@Override
	public TagList generateTag(BaseObject object, CGPanel panel)
	{
		@SuppressWarnings("unchecked")
		AttributeModifier[] values = ((PanelObjectList<AttributeModifier>) panel).values();
		TagCompound[] tags = new TagCompound[values.length];
		for (int i = 0; i < tags.length; ++i)
			tags[i] = values[i].toTag(Tags.DEFAULT_COMPOUND, false);
		return this.create(tags);
	}

}
