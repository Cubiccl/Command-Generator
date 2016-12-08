package fr.cubiccl.generator.gameobject.templatetags.custom;

import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagList;
import fr.cubiccl.generator.gameobject.templatetags.TemplateList;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.tag.PatternsPanel;
import fr.cubiccl.generator.gui.component.panel.utils.ConfirmPanel;

public class TemplatePatterns extends TemplateList
{
	private int base;

	public TemplatePatterns(String id, int tagType, String[] applicable)
	{
		super(id, tagType, applicable);
	}

	@Override
	protected CGPanel createPanel()
	{
		return new ConfirmPanel("banner.title", new PatternsPanel());
	}

	@Override
	public Tag generateTag(CGPanel panel)
	{
		this.base = ((PatternsPanel) ((ConfirmPanel) panel).component).getBaseColor();
		return new TagList(this, (((PatternsPanel) ((ConfirmPanel) panel).component).getPatterns()));
	}

	public int getBaseColor()
	{
		return this.base;
	}

	@Override
	protected boolean isInputValid(CGPanel panel)
	{
		return true;
	}

}
