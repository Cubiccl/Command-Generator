package fr.cubiccl.generator.gameobject.templatetags.custom;

import fr.cubiccl.generator.gameobject.baseobjects.BaseObject;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagNumber;
import fr.cubiccl.generator.gameobject.templatetags.TemplateNumber;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.tag.PanelDisabledSlots;

public class TemplateDisabledSlots extends TemplateNumber
{

	@Override
	protected CGPanel createPanel(BaseObject<?> object, Tag previousValue)
	{
		PanelDisabledSlots p = new PanelDisabledSlots();
		if (previousValue != null) p.setupFrom(((TagNumber) previousValue).valueInt());
		p.setName(this.title());
		return p;
	}

	@Override
	public TagNumber generateTag(BaseObject<?> object, CGPanel panel)
	{
		return this.create(((PanelDisabledSlots) panel).generateValue());
	}

	@Override
	protected boolean isInputValid(BaseObject<?> object, CGPanel panel)
	{
		return true;
	}
}
