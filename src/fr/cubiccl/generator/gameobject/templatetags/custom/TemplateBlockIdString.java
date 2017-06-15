package fr.cubiccl.generator.gameobject.templatetags.custom;

import fr.cubiccl.generator.gameobject.baseobjects.BaseObject;
import fr.cubiccl.generator.gameobject.baseobjects.Block;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagString;
import fr.cubiccl.generator.gameobject.templatetags.TemplateString;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelBlockSelection;

public class TemplateBlockIdString extends TemplateString
{
	public int damage = -1;

	public TemplateBlockIdString()
	{
		super();
	}

	public TemplateBlockIdString(String id, byte applicationType, String... applicable)
	{
		super(id, applicationType, applicable);
	}

	@Override
	protected CGPanel createPanel(BaseObject<?> object, Tag previousValue)
	{
		PanelBlockSelection p = new PanelBlockSelection(false);

		Block previous = p.selectedBlock();
		if (previousValue != null)
		{
			previous = ObjectRegistry.blocks.find(((TagString) previousValue).value());
			p.setSelected(previous);
		}
		if (this.damage != -1 && previous.isDamageValid(this.damage)) p.setDamage(this.damage);
		return p;
	}

	@Override
	public TagString generateTag(BaseObject<?> object, CGPanel panel)
	{
		this.damage = ((PanelBlockSelection) panel).selectedDamage();
		return this.create(((PanelBlockSelection) panel).selectedBlock().id());
	}

	@Override
	protected boolean isInputValid(BaseObject<?> object, CGPanel panel)
	{
		return true;
	}

}
