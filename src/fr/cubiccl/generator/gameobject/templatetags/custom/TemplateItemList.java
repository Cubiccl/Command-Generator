package fr.cubiccl.generator.gameobject.templatetags.custom;

import fr.cubiccl.generator.gameobject.baseobjects.BaseObject;
import fr.cubiccl.generator.gameobject.baseobjects.Item;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagList;
import fr.cubiccl.generator.gameobject.tags.TagString;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateList;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.utils.PanelObjectList;

public class TemplateItemList extends TemplateList
{
	public TemplateItemList(String id, byte applicationType, String[] applicable)
	{
		super(id, applicationType, applicable);
	}

	@Override
	protected CGPanel createPanel(BaseObject object, Tag previousValue)
	{
		PanelObjectList<Item> p = new PanelObjectList<Item>(null, (String) null, Item.class);
		if (previousValue != null)
		{
			Item[] blocks = new Item[0];
			TagList t = ((TagList) previousValue);
			blocks = new Item[t.size()];
			for (int i = 0; i < blocks.length; ++i)
				blocks[i] = ObjectRegistry.items.find((String) t.getTag(i).value());
			p.setValues(blocks);
		}
		p.setName(this.title());
		return p;
	}

	@Override
	public TagList generateTag(BaseObject object, CGPanel panel)
	{
		@SuppressWarnings("unchecked")
		Item[] values = ((PanelObjectList<Item>) panel).values();
		TagString[] tags = new TagString[values.length];
		for (int i = 0; i < tags.length; ++i)
			tags[i] = Tags.DEFAULT_STRING.create(values[i].id());
		return this.create(tags);
	}

}
