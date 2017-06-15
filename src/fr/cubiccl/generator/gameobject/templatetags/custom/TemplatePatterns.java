package fr.cubiccl.generator.gameobject.templatetags.custom;

import fr.cubiccl.generator.gameobject.baseobjects.BaseObject;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagList;
import fr.cubiccl.generator.gameobject.templatetags.TemplateList;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.tag.PatternsPanel;

public class TemplatePatterns extends TemplateList
{
	public int base;

	@Override
	protected CGPanel createPanel(BaseObject<?> object, Tag previousValue)
	{
		PatternsPanel p = new PatternsPanel();
		if (previousValue != null) p.setupFrom((TagList) previousValue);
		if (this.base != -1) p.setBaseColor(this.base);
		p.setName("banner.title");
		return p;
	}

	@Override
	public Tag generateTag(BaseObject<?> object, CGPanel panel)
	{
		this.base = ((PatternsPanel) panel).getBaseColor();
		return this.create(((PatternsPanel) panel).getPatterns());
	}

}
