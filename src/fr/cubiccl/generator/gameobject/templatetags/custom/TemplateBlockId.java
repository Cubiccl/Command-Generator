package fr.cubiccl.generator.gameobject.templatetags.custom;

import fr.cubiccl.generator.gameobject.baseobjects.BaseObject;
import fr.cubiccl.generator.gameobject.baseobjects.Block;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagNumber;
import fr.cubiccl.generator.gameobject.templatetags.TemplateNumber;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelBlockSelection;

public class TemplateBlockId extends TemplateNumber
{
	public int damage;

	public TemplateBlockId()
	{
		super();
		this.damage = -1;
		this.tagType = TagNumber.SHORT;
	}

	@Override
	protected CGPanel createPanel(BaseObject<?> object, Tag previousValue)
	{
		PanelBlockSelection p = new PanelBlockSelection();

		Block previous = p.selectedBlock();
		if (previousValue != null)
		{
			previous = ObjectRegistry.blocks.find(((TagNumber) previousValue).valueInt());
			p.setSelected(previous);
		}
		if (this.damage != -1 && previous.isDamageValid(this.damage)) p.setDamage(this.damage);
		return p;
	}

	@Override
	public TagNumber generateTag(BaseObject<?> object, CGPanel panel)
	{
		this.damage = ((PanelBlockSelection) panel).selectedDamage();
		return this.create(((PanelBlockSelection) panel).selectedBlock().idNum());
	}

	@Override
	protected boolean isInputValid(BaseObject<?> object, CGPanel panel)
	{
		return true;
	}

}
