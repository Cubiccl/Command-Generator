package fr.cubiccl.generator.gameobject.templatetags.custom;

import fr.cubiccl.generator.gameobject.ObjectRegistry;
import fr.cubiccl.generator.gameobject.baseobjects.Block;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagList;
import fr.cubiccl.generator.gameobject.tags.TagString;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateList;
import fr.cubiccl.generator.gui.component.panel.tag.PanelBlockList;
import fr.cubiccl.generator.gui.component.panel.utils.ConfirmPanel;

public class TemplateCanDestroy extends TemplateList
{

	public TemplateCanDestroy(String id, byte tagType, String[] applicable)
	{
		super(id, tagType, applicable);
	}

	@Override
	protected ConfirmPanel createPanel(String objectId, Tag previousValue)
	{
		PanelBlockList p = new PanelBlockList();
		if (previousValue != null)
		{
			TagList t = ((TagList) previousValue);
			Block[] blocks = new Block[t.size()];
			for (int i = 0; i < blocks.length; ++i)
				blocks[i] = ObjectRegistry.getBlockFromID((String) t.getTag(i).value());
			p.setBlocks(blocks);
		}
		return new ConfirmPanel("tag.title." + this.id, p);
	}

	@Override
	public TagList generateTag(ConfirmPanel panel)
	{
		Block[] blocks = ((PanelBlockList) panel.component).selectedBlocks();
		TagString[] tags = new TagString[blocks.length];
		for (int i = 0; i < tags.length; ++i)
			tags[i] = new TagString(Tags.DEFAULT_STRING, blocks[i].idString);
		return new TagList(this, tags);
	}

}
