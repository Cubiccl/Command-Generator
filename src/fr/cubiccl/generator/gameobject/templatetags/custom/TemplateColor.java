package fr.cubiccl.generator.gameobject.templatetags.custom;

import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagNumber;
import fr.cubiccl.generator.gameobject.templatetags.TemplateNumber;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.utils.PanelColor;
import fr.cubiccl.generator.utils.Utils;

public class TemplateColor extends TemplateNumber
{

	public TemplateColor(String id, byte tagType, String[] applicable)
	{
		super(id, tagType, applicable);
		this.setNames("color", Utils.WOOL_COLORS);
	}

	@Override
	protected CGPanel createPanel(String objectId, Tag previousValue)
	{
		if (objectId.equals("minecraft:sheep") || objectId.equals("minecraft:shulker")) return super.createPanel(objectId, previousValue);

		PanelColor p = new PanelColor(null);
		if (previousValue != null) p.setupFrom(((TagNumber) previousValue).value);
		p.setName(this.title());
		return p;
	}

	@Override
	public Tag generateTag(CGPanel panel)
	{
		if (panel instanceof PanelColor) return new TagNumber(this, ((PanelColor) panel).getValue());
		return super.generateTag(panel);
	}

	@Override
	protected boolean isInputValid(CGPanel panel)
	{
		if (panel instanceof PanelColor) return true;
		return super.isInputValid(panel);
	}

}
