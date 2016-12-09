package fr.cubiccl.generator.gameobject.templatetags;

import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagString;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.utils.EntryPanel;

public class TemplateString extends TemplateTag
{

	public TemplateString(String id, int tagType, String... applicable)
	{
		super(id, tagType, applicable);
	}

	@Override
	protected CGPanel createPanel(String objectId)
	{
		return new EntryPanel(this.description());
	}

	@Override
	public Tag generateTag(CGPanel panel)
	{
		return new TagString(this, ((EntryPanel) panel).entry.getText());
	}

}
