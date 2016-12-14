package fr.cubiccl.generator.gameobject.templatetags.custom;

import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelTags;
import fr.cubiccl.generator.gui.component.panel.utils.ConfirmPanel;

public class TemplateBlockEntity extends TemplateCompound
{

	public TemplateBlockEntity(String id, byte tagType, String[] applicable)
	{
		super(id, tagType, applicable);
	}

	@Override
	protected ConfirmPanel createPanel(String objectId, Tag previousValue)
	{
		PanelTags p = new PanelTags(null, Tag.BLOCK);
		p.setObjectForTags(objectId);
		return new ConfirmPanel("tag.title." + this.id, p);
	}

	@Override
	public Tag generateTag(ConfirmPanel panel)
	{
		return ((PanelTags) panel.component).generateTags(this);
	}

}
