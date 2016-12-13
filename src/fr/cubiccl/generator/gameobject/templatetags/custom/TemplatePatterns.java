package fr.cubiccl.generator.gameobject.templatetags.custom;

import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagList;
import fr.cubiccl.generator.gameobject.templatetags.TemplateList;
import fr.cubiccl.generator.gui.component.panel.tag.PatternsPanel;
import fr.cubiccl.generator.gui.component.panel.utils.ConfirmPanel;

public class TemplatePatterns extends TemplateList
{
	public int base;

	public TemplatePatterns(String id, byte tagType, String[] applicable)
	{
		super(id, tagType, applicable);
	}

	@Override
	protected ConfirmPanel createPanel(String objectId, Tag previousValue)
	{
		PatternsPanel p = new PatternsPanel();
		if (previousValue != null) p.setupFrom((TagList) previousValue);
		if (this.base != -1) p.setBaseColor(this.base);
		return new ConfirmPanel("banner.title", p);
	}

	@Override
	public Tag generateTag(ConfirmPanel panel)
	{
		this.base = ((PatternsPanel) panel.component).getBaseColor();
		return new TagList(this, ((PatternsPanel) panel.component).getPatterns());
	}

}
