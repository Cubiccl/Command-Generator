package fr.cubiccl.generator.gameobject.templatetags.custom;

import fr.cubiccl.generator.gameobject.baseobjects.BaseObject;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagNumber;
import fr.cubiccl.generator.gameobject.templatetags.TemplateNumber;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.tag.PanelHorseVariant;

public class TemplateHorseVariant extends TemplateNumber
{

	public TemplateHorseVariant(String id, byte applicationType, String[] applicable)
	{
		super(id, applicationType, Tag.INT, applicable);
		this.setNames("variant.base", "creamy", "white", "brown", "gray");
	}

	@Override
	protected CGPanel createPanel(BaseObject object, Tag previousValue)
	{
		if (object.id().equals("minecraft:llama")) return super.createPanel(object, previousValue);
		PanelHorseVariant p = new PanelHorseVariant();
		if (previousValue != null) p.setupFrom(((TagNumber) previousValue).valueInt());
		p.setName(this.title());
		return p;
	}

	@Override
	public Tag generateTag(BaseObject object, CGPanel panel)
	{
		if (panel instanceof PanelHorseVariant) return this.create(((PanelHorseVariant) panel).generateVariant());
		return super.generateTag(object, panel);
	}

	@Override
	protected boolean isInputValid(BaseObject object, CGPanel panel)
	{
		if (panel instanceof PanelHorseVariant) return true;
		return super.isInputValid(object, panel);
	}

}
