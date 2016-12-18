package fr.cubiccl.generator.gameobject.templatetags.custom;

import fr.cubiccl.generator.gameobject.ObjectRegistry;
import fr.cubiccl.generator.gameobject.baseobjects.Block;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagString;
import fr.cubiccl.generator.gameobject.templatetags.TemplateString;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelBlockSelection;

public class TemplateItemId extends TemplateString
{
	public int damage;
	private String[] ids;

	public TemplateItemId(String id, byte tagType, String[] applicable)
	{
		super(id, tagType, applicable);
		this.ids = null;
		this.damage = -1;
	}

	@Override
	protected CGPanel createPanel(String objectId, Tag previousValue)
	{
		PanelBlockSelection p = new PanelBlockSelection();
		if (this.ids != null) p.setBlocks(ObjectRegistry.getBlocks(this.ids));

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
	public TagString generateTag(CGPanel panel)
	{
		this.damage = ((PanelBlockSelection) panel).selectedDamage();
		return new TagString(this, ((PanelBlockSelection) panel).selectedBlock().idString);
	}

	public void setLimited(String[] ids)
	{
		this.ids = ids;
	}

}
