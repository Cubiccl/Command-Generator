package fr.cubiccl.generator.gameobject.templatetags;

import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gui.component.panel.CGPanel;

public class TemplateList extends TemplateTag
{

	public TemplateList(String id, int tagType, String... applicable)
	{
		super(id, tagType, applicable);
	}

	@Override
	protected CGPanel createPanel()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Tag generateTag(CGPanel panel)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean isInputValid(CGPanel panel)
	{
		// TODO Auto-generated method stub
		return false;
	}

}
