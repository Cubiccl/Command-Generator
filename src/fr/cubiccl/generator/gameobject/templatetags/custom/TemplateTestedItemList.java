package fr.cubiccl.generator.gameobject.templatetags.custom;

import fr.cubiccl.generator.gameobject.ItemStack;
import fr.cubiccl.generator.gameobject.baseobjects.BaseObject;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.tags.TagList;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateList;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.utils.PanelObjectList;

public class TemplateTestedItemList extends TemplateList
{

	public TemplateTestedItemList(String id, byte applicationType, String... applicable)
	{
		super(id, applicationType, applicable);
	}

	@Override
	protected CGPanel createPanel(BaseObject<?> object, Tag previousValue)
	{
		PanelObjectList<ItemStack> p = new PanelObjectList<ItemStack>(null, (String) null, ItemStack.class, "testing", true);
		if (previousValue != null)
		{
			ItemStack[] items = new ItemStack[0];
			TagList t = ((TagList) previousValue);
			items = new ItemStack[t.size()];
			for (int i = 0; i < items.length; ++i)
				items[i] = new ItemStack().fromNBT((TagCompound) t.getTag(i), true);
			p.setValues(items);
		}
		p.setName(this.title());
		return p;
	}

	@Override
	public TagList generateTag(BaseObject<?> object, CGPanel panel)
	{
		@SuppressWarnings("unchecked")
		ItemStack[] values = ((PanelObjectList<ItemStack>) panel).values();
		TagCompound[] tags = new TagCompound[values.length];
		for (int i = 0; i < tags.length; ++i)
			tags[i] = values[i].toTagForTest(Tags.DEFAULT_COMPOUND);
		return this.create(tags);
	}

}
