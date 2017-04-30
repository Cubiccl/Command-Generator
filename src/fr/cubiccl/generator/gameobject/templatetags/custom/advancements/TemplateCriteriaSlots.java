package fr.cubiccl.generator.gameobject.templatetags.custom.advancements;

import fr.cubiccl.generator.gameobject.baseobjects.BaseObject;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.advancement.PanelSlots;

public class TemplateCriteriaSlots extends TemplateCompound
{

	public TemplateCriteriaSlots(String id, String... applicable)
	{
		super(id, Tag.UNAVAILABLE, applicable);
	}

	@Override
	protected CGPanel createPanel(BaseObject object, Tag previousValue)
	{
		PanelSlots panel = new PanelSlots();
		if (previousValue != null) panel.setupFrom((TagCompound) previousValue);
		return panel;
	}

	@Override
	protected TagCompound generateTag(BaseObject object, CGPanel panel)
	{
		return ((PanelSlots) panel).generateTag(this);
	}

	@Override
	protected boolean isInputValid(BaseObject object, CGPanel panel)
	{
		return ((PanelSlots) panel).checkInput();
	}

}
