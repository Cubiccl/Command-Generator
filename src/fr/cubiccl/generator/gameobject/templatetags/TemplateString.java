package fr.cubiccl.generator.gameobject.templatetags;

import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagString;
import fr.cubiccl.generator.gui.component.panel.utils.ConfirmPanel;
import fr.cubiccl.generator.gui.component.panel.utils.EntryPanel;

public class TemplateString extends TemplateTag
{

	public TemplateString(String id, byte tagType, String... applicable)
	{
		super(id, tagType, applicable);
	}

	@Override
	protected ConfirmPanel createPanel(String objectId, Tag previousValue)
	{
		EntryPanel p = new EntryPanel(this.description());
		if (previousValue != null) p.entry.setText(((TagString) previousValue).value());
		return p;
	}

	@Override
	public TagString generateTag(ConfirmPanel panel)
	{
		return new TagString(this, ((EntryPanel) panel).entry.getText());
	}

}
