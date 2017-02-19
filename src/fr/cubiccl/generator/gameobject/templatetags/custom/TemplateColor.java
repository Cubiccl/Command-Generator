package fr.cubiccl.generator.gameobject.templatetags.custom;

import fr.cubiccl.generator.gameobject.baseobjects.BaseObject;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagNumber;
import fr.cubiccl.generator.gameobject.templatetags.TemplateNumber;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.utils.PanelColor;
import fr.cubiccl.generator.utils.Utils;

public class TemplateColor extends TemplateNumber
{

	public TemplateColor(String id, byte applicationType, String[] applicable)
	{
		super(id, applicationType, applicable);
		this.setNames("color", Utils.WOOL_COLORS);
	}

	@Override
	protected CGPanel createPanel(BaseObject object, Tag previousValue)
	{
		if (object.id().equals("minecraft:sheep") || object.id().equals("minecraft:shulker")) return super.createPanel(object, previousValue);

		PanelColor p = new PanelColor(null);
		if (previousValue != null) p.setupFrom(((TagNumber) previousValue).value);
		p.setName(this.title());
		return p;
	}

	@Override
	public Tag generateTag(BaseObject object, CGPanel panel)
	{
		if (panel instanceof PanelColor) return this.create(((PanelColor) panel).getValue());
		return super.generateTag(object, panel);
	}

	@Override
	protected boolean isInputValid(BaseObject object, CGPanel panel)
	{
		if (panel instanceof PanelColor) return true;
		return super.isInputValid(object, panel);
	}

}
