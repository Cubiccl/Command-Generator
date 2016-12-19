package fr.cubiccl.generator.gameobject.templatetags.custom;

import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelTags;

public class TemplateBlockEntity extends TemplateCompound
{

	public TemplateBlockEntity(String id, byte tagType, String[] applicable)
	{
		super(id, tagType, applicable);
	}

	@Override
	protected PanelTags createPanel(String objectId, Tag previousValue)
	{
		PanelTags p = new PanelTags(null, Tag.BLOCK);
		p.setObjectForTags(objectId);
		p.setName(this.title());
		return p;
	}

	@Override
	public TagCompound generateTag(CGPanel panel)
	{
		return ((PanelTags) panel).generateTags(this);
	}

}
