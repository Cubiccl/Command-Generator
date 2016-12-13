package fr.cubiccl.generator.gameobject.templatetags.custom;

import fr.cubiccl.generator.gameobject.ItemStack;
import fr.cubiccl.generator.gameobject.ObjectRegistry;
import fr.cubiccl.generator.gameobject.baseobjects.Item;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelItem;
import fr.cubiccl.generator.gui.component.panel.utils.ConfirmPanel;

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
	protected ConfirmPanel createPanel(String objectId, Tag previousValue)
	{
		Item[] items = ObjectRegistry.getItems(ObjectRegistry.SORT_NUMERICALLY);
		if (this.ids != null) items = ObjectRegistry.getItems(this.ids);

		PanelItem p = new PanelItem(null, items);

		if (this.autoselect != null) p.setupFrom(new ItemStack(ObjectRegistry.getItemFromID(this.autoselect), 0, 1));
		if (previousValue != null) p.setupFrom(ItemStack.createFrom((TagCompound) previousValue));
		return new ConfirmPanel("tag.title." + this.id, p);
	}

	@Override
	public Tag generateTag(ConfirmPanel panel)
	{
		return ((PanelItem) panel.component).generateItem().toTag(this);
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
