package fr.cubiccl.generator.gameobject.templatetags.custom.advancements;

import fr.cubiccl.generator.gameobject.baseobjects.BaseObject;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.advancement.PanelCriteriaEntity;

public class TemplateCriteriaEntity extends TemplateCompound
{

	public TemplateCriteriaEntity(String id, String... applicable)
	{
		super(id, Tag.UNAVAILABLE, applicable);
	}

	@Override
	protected CGPanel createPanel(BaseObject<?> object, Tag previousValue)
	{
		PanelCriteriaEntity p = new PanelCriteriaEntity();
		if (previousValue != null) p.setupFrom((TagCompound) previousValue);
		return p;
	}

	@Override
	protected Tag generateTag(BaseObject<?> object, CGPanel panel)
	{
		return this.create(((PanelCriteriaEntity) panel).generateTags());
	}

	@Override
	protected boolean isInputValid(BaseObject<?> object, CGPanel panel)
	{
		return ((PanelCriteriaEntity) panel).checkInput();
	}

}
