package fr.cubiccl.generator.gameobject.templatetags.custom;

import fr.cubiccl.generator.gameobject.baseobjects.BaseObject;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.templatetags.TemplateNumber;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.tag.PanelHideFlags;

public class TemplateHideFlags extends TemplateNumber
{

	@Override
	protected CGPanel createPanel(BaseObject<?> object, Tag previousValue)
	{
		PanelHideFlags p = new PanelHideFlags();
		if (previousValue != null) p.setupFrom((int) previousValue.value());
		p.setName(this.title());
		return p;
	}

	@Override
	public Tag generateTag(BaseObject<?> object, CGPanel panel)
	{
		return this.create(((PanelHideFlags) panel).getValue());
	}

	@Override
	protected boolean isInputValid(BaseObject<?> object, CGPanel panel)
	{
		return true;
	}

}
