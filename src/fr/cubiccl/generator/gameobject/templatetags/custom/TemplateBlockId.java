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

	public TemplateBlockId(String id, byte applicationType, String[] applicable)
	{
		super(id, applicationType, TagNumber.SHORT, applicable);
		this.damage = -1;
	}

	@Override
	protected CGPanel createPanel(BaseObject object, Tag previousValue)
	{
		PanelBlockSelection p = new PanelBlockSelection();

		Block previous = p.selectedBlock();
		if (previousValue != null)
		{
			previous = ObjectRegistry.blocks.find(((TagNumber) previousValue).value());
			p.setSelected(previous);
		}
		if (this.damage != -1)
		{
			for (int i = 0; i < previous.damage.length; ++i)
				if (previous.damage[i] == this.damage)
				{
					p.setDamage(i);
					break;
				}
		}
		return p;
	}

	@Override
	public TagNumber generateTag(BaseObject object, CGPanel panel)
	{
		this.damage = ((PanelBlockSelection) panel).selectedDamage();
		return new TagNumber(this, ((PanelBlockSelection) panel).selectedBlock().idNum());
	}

	@Override
	protected boolean isInputValid(BaseObject object, CGPanel panel)
	{
		return true;
	}

}
