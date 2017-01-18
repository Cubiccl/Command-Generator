package fr.cubiccl.generator.gameobject.templatetags.custom;

import fr.cubiccl.generator.gameobject.ItemStack;
import fr.cubiccl.generator.gameobject.baseobjects.Item;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelItem;

public class TemplateItem extends TemplateCompound
{
	private String autoselect;
	private String[] ids;

	public TemplateItem(String id, byte tagType, String[] applicable)
	{
		super(id, tagType, applicable);
		this.ids = null;
		this.autoselect = null;
	}

	@Override
	protected CGPanel createPanel(String objectId, Tag previousValue)
	{
		Item[] items = this.ids != null ? ObjectRegistry.items.find(this.ids) : ObjectRegistry.items.list(ObjectRegistry.SORT_NUMERICALLY);

		PanelItem p = new PanelItem(null, items);

		if (this.autoselect != null) p.setupFrom(new ItemStack(ObjectRegistry.items.find(this.autoselect), 0, 1));
		if (previousValue != null) p.setupFrom(ItemStack.createFrom((TagCompound) previousValue));
		p.setName(this.title());
		return p;
	}

	@Override
	public Tag generateTag(CGPanel panel)
	{
		return ((PanelItem) panel).generateItem().toTag(this);
	}

	public void setAutoselect(String id)
	{
		this.autoselect = id;
	}

	public void setLimited(String[] ids)
	{
		this.ids = ids;
	}

}
