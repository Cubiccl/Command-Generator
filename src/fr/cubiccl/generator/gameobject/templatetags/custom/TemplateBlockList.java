package fr.cubiccl.generator.gameobject.templatetags.custom;

import fr.cubiccl.generator.gameobject.baseobjects.BaseObject;
import fr.cubiccl.generator.gameobject.baseobjects.Block;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagList;
import fr.cubiccl.generator.gameobject.tags.TagString;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateList;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.utils.PanelObjectList;

public class TemplateBlockList extends TemplateList
{

	@Override
	protected CGPanel createPanel(BaseObject<?> object, Tag previousValue)
	{
		PanelObjectList<Block> p = new PanelObjectList<Block>(null, (String) null, Block.class);
		if (previousValue != null)
		{
			Block[] blocks = new Block[0];
			TagList t = ((TagList) previousValue);
			blocks = new Block[t.size()];
			for (int i = 0; i < blocks.length; ++i)
				blocks[i] = ObjectRegistry.blocks.find((String) t.getTag(i).value());
			p.setValues(blocks);
		}
		p.setName(this.title());
		return p;
	}

	@Override
	public TagList generateTag(BaseObject<?> object, CGPanel panel)
	{
		@SuppressWarnings("unchecked")
		Block[] values = ((PanelObjectList<Block>) panel).values();
		TagString[] tags = new TagString[values.length];
		for (int i = 0; i < tags.length; ++i)
			tags[i] = Tags.DEFAULT_STRING.create(values[i].id());
		return this.create(tags);
	}

}
