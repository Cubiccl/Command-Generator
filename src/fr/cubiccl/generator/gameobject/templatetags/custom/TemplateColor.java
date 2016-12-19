package fr.cubiccl.generator.gameobject.templatetags.custom;

import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagNumber;
import fr.cubiccl.generator.gameobject.templatetags.TemplateNumber;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.utils.PanelColor;

public class TemplateColor extends TemplateNumber
{

	public TemplateColor(String id, byte tagType, String[] applicable)
	{
		super(id, tagType, applicable);
	}

	@Override
	protected CGPanel createPanel(String objectId, Tag previousValue)
	{
		PanelColor p = new PanelColor(null);
		if (previousValue != null) p.setupFrom(((TagNumber) previousValue).value);
		p.setName(this.title());
		return p;
	}

	@Override
	public TagNumber generateTag(CGPanel panel)
	{
		return new TagNumber(this, ((PanelColor) panel).getValue());
	}

	@Override
	protected boolean isInputValid(CGPanel panel)
	{
		return true;
	}

}
