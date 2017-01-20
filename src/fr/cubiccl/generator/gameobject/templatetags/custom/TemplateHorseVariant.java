package fr.cubiccl.generator.gameobject.templatetags.custom;

import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagNumber;
import fr.cubiccl.generator.gameobject.templatetags.TemplateNumber;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.tag.PanelHorseVariant;

public class TemplateHorseVariant extends TemplateNumber
{

	public TemplateHorseVariant(String id, byte tagType, String[] applicable)
	{
		super(id, tagType, TagNumber.INTEGER, applicable);
		this.setNames("variant.base", "creamy", "white", "brown", "gray");
	}

	@Override
	protected CGPanel createPanel(String objectId, Tag previousValue)
	{
		if (objectId.equals("minecraft:llama")) return super.createPanel(objectId, previousValue);
		PanelHorseVariant p = new PanelHorseVariant();
		if (previousValue != null) p.setupFrom(((TagNumber) previousValue).value());
		p.setName(this.title());
		return p;
	}

	@Override
	public Tag generateTag(CGPanel panel)
	{
		if (panel instanceof PanelHorseVariant) return new TagNumber(this, ((PanelHorseVariant) panel).generateVariant());
		return super.generateTag(panel);
	}

	@Override
	protected boolean isInputValid(CGPanel panel)
	{
		if (panel instanceof PanelHorseVariant) return true;
		return super.isInputValid(panel);
	}

}
