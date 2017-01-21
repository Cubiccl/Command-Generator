package fr.cubiccl.generator.gameobject.templatetags.custom;

import fr.cubiccl.generator.gameobject.baseobjects.BaseObject;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagList;
import fr.cubiccl.generator.gameobject.templatetags.TemplateList;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelListJsonMessage;

public class TemplateJson extends TemplateList
{

	public TemplateJson(String id, byte tagType, String[] applicable)
	{
		super(id, tagType, applicable);
	}

	@Override
	protected CGPanel createPanel(BaseObject object, Tag previousValue)
	{
		return new PanelListJsonMessage("tag.title." + this.id());
	}

	@Override
	public TagList generateTag(BaseObject object, CGPanel panel)
	{
		return ((PanelListJsonMessage) panel).generateMessage(this);
	}

}
