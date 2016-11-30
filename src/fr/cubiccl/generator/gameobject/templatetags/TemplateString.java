package fr.cubiccl.generator.gameobject.templatetags;

import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagString;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.utils.EntryPanel;
import fr.cubiccl.generator.utils.Text;

public class TemplateString extends TemplateTag
{

	public TemplateString(String id, int tagType, String... applicable)
	{
		super(id, tagType, applicable);
	}

	@Override
	protected CGPanel createPanel()
	{
		return new EntryPanel(new Text(this.id + ".desciption"));
	}

	@Override
	public Tag generateTag(CGPanel panel)
	{
		return new TagString(this, ((EntryPanel) panel).entry.getText());
	}

	@Override
	protected boolean isInputValid(CGPanel panel)
	{
		return true;
	}

}
