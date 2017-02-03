package fr.cubiccl.generator.gameobject.templatetags.custom;

import fr.cubiccl.generator.gameobject.baseobjects.BaseObject;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelTags;

public class TemplateBlockEntity extends TemplateCompound
{

	public TemplateBlockEntity(String id, byte applicationType, String[] applicable)
	{
		super(id, applicationType, applicable);
	}

	@Override
	protected PanelTags createPanel(BaseObject object, Tag previousValue)
	{
		PanelTags p = new PanelTags(null, Tag.BLOCK);
		p.setTargetObject(object);

		if (previousValue != null) p.setTags(((TagCompound) previousValue).value());

		p.setName(this.title());
		return p;
	}

	@Override
	public TagCompound generateTag(BaseObject object, CGPanel panel)
	{
		return ((PanelTags) panel).generateTags(this);
	}

}
