package fr.cubiccl.generator.gameobject.templatetags.custom;

import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagNumber;
import fr.cubiccl.generator.gameobject.templatetags.TemplateNumber;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.tag.PanelHideFlags;

public class TemplateHideFlags extends TemplateNumber
{

	public TemplateHideFlags(String id, byte tagType, String[] applicable)
	{
		super(id, tagType, applicable);
	}

	@Override
	protected CGPanel createPanel(String objectId, Tag previousValue)
	{
		PanelHideFlags p = new PanelHideFlags();
		if (previousValue != null) p.setupFrom((int) previousValue.value());
		p.setName(this.title());
		return p;
	}

	@Override
	public Tag generateTag(CGPanel panel)
	{
		return new TagNumber(this, ((PanelHideFlags) panel).getValue());
	}

	@Override
	protected boolean isInputValid(CGPanel panel)
	{
		return true;
	}

}
