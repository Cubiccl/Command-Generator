package fr.cubiccl.generator.gameobject.templatetags.custom;

import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.tag.PanelDisplay;

public class TemplateDisplay extends TemplateCompound
{

	public TemplateDisplay(String id, byte tagType, String[] applicable)
	{
		super(id, tagType, applicable);
	}

	@Override
	protected CGPanel createPanel(String objectId, Tag previousValue)
	{
		PanelDisplay p = new PanelDisplay();
		p.setName(this.title());
		if (previousValue != null) p.setupFrom((TagCompound) previousValue);
		return p;
	}

	@Override
	public TagCompound generateTag(CGPanel panel)
	{
		return ((PanelDisplay) panel).generateDisplay(this);
	}

}
