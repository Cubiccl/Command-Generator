package fr.cubiccl.generator.gameobject.templatetags.custom;

import fr.cubiccl.generator.gameobject.ObjectRegistry;
import fr.cubiccl.generator.gameobject.baseobjects.Block;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagString;
import fr.cubiccl.generator.gameobject.templatetags.TemplateString;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelBlockSelection;
import fr.cubiccl.generator.gui.component.panel.utils.ConfirmPanel;

public class TemplateItem extends TemplateString
{
	public int damage;
	private String[] ids;

	public TemplateItem(String id, byte tagType, String[] applicable)
	{
		super(id, tagType, applicable);
		this.ids = null;
		this.damage = -1;
	}

	@Override
	protected ConfirmPanel createPanel(String objectId, Tag previousValue)
	{
		PanelBlockSelection p = new PanelBlockSelection();
		if (this.ids != null)
		{
			Block[] blocks = new Block[this.ids.length];
			for (int i = 0; i < blocks.length; ++i)
				blocks[i] = ObjectRegistry.getBlockFromID(this.ids[i]);
			p.setBlocks(blocks);
		}

		Block previous = p.selectedBlock();
		if (previousValue != null)
		{
			previous = ObjectRegistry.getBlockFromID(((TagString) previousValue).value());
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
	public TagString generateTag(ConfirmPanel panel)
	{
		this.damage = ((PanelBlockSelection) panel).selectedDamage();
		return new TagString(this, ((PanelBlockSelection) panel).selectedBlock().idString);
	}

	public void setLimited(String[] ids)
	{
		this.ids = ids;
	}

}
