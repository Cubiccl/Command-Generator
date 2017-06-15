package fr.cubiccl.generator.gameobject.templatetags.custom.advancements;

import fr.cubiccl.generator.gameobject.baseobjects.BaseObject;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.advancement.PanelCriteriaDistance;

public class TemplateCriteriaDistance extends TemplateCompound
{

	public TemplateCriteriaDistance(String id, String... applicable)
	{
		super(id, Tag.UNAVAILABLE, applicable);
	}

	@Override
	protected CGPanel createPanel(BaseObject<?> object, Tag previousValue)
	{
		PanelCriteriaDistance p = new PanelCriteriaDistance();
		if (previousValue != null) p.setupFrom((TagCompound) previousValue);
		return p;
	}

	@Override
	protected Tag generateTag(BaseObject<?> object, CGPanel panel)
	{
		return ((PanelCriteriaDistance) panel).generateValue(this);
	}

	@Override
	protected boolean isInputValid(BaseObject<?> object, CGPanel panel)
	{
		return ((PanelCriteriaDistance) panel).checkInput();
	}

}
