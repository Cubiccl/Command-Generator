package fr.cubiccl.generator.gameobject.templatetags.custom;

import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagList;
import fr.cubiccl.generator.gameobject.templatetags.TemplateList;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.tag.PatternsPanel;

public class TemplatePatterns extends TemplateList
{
	public int base;

	public TemplatePatterns(String id, byte tagType, String[] applicable)
	{
		super(id, tagType, applicable);
	}

	@Override
	protected CGPanel createPanel(String objectId, Tag previousValue)
	{
		PatternsPanel p = new PatternsPanel();
		if (previousValue != null) p.setupFrom((TagList) previousValue);
		if (this.base != -1) p.setBaseColor(this.base);
		p.setName("banner.title");
		return p;
	}

	@Override
	public Tag generateTag(CGPanel panel)
	{
		this.base = ((PatternsPanel) panel).getBaseColor();
		return new TagList(this, ((PatternsPanel) panel).getPatterns());
	}

}
