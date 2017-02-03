package fr.cubiccl.generator.gameobject.templatetags.custom;

import fr.cubiccl.generator.gameobject.AppliedAttribute.AttributeModifierList;
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
		AttributeModifierList list = new AttributeModifierList(false);
		if (previousValue != null) for (Tag t : ((TagList) previousValue).value())
			list.modifiers.add(AttributeModifier.createFrom((TagCompound) t));
		PanelObjectList p = new PanelObjectList(list);
		p.setName(this.title());
		return p;
	}

	@Override
	public TagList generateTag(BaseObject object, CGPanel panel)
	{
		AttributeModifier[] values = ((AttributeModifierList) ((PanelObjectList) panel).objectList).modifiers.toArray(new AttributeModifier[0]);
		TagCompound[] tags = new TagCompound[values.length];
		for (int i = 0; i < tags.length; ++i)
			tags[i] = values[i].toTag(Tags.DEFAULT_COMPOUND, false);
		return new TagList(this, tags);
	}

}
