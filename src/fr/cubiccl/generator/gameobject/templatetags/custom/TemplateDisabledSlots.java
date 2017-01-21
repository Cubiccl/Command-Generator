package fr.cubiccl.generator.gameobject.templatetags.custom;

import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagNumber;
import fr.cubiccl.generator.gameobject.templatetags.TemplateNumber;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.tag.PanelDisabledSlots;

public class TemplateDisabledSlots extends TemplateNumber
{

	public TemplateDisabledSlots(String id, byte tagType, String[] applicable)
	{
		super(id, tagType, TagNumber.INTEGER, applicable);
	}

	@Override
	protected CGPanel createPanel(String objectId, Tag previousValue)
	{
		PanelDisabledSlots p = new PanelDisabledSlots();
		if (previousValue != null) p.setupFrom(((TagNumber) previousValue).value());
		p.setName(this.title());
		return p;
	}

	@Override
	public TagNumber generateTag(CGPanel panel)
	{
		return new TagNumber(this, ((PanelDisabledSlots) panel).generateValue());
	}

	@Override
	protected boolean isInputValid(CGPanel panel)
	{
		return true;
	}
}
