package fr.cubiccl.generator.gameobject.templatetags.custom;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import fr.cubiccl.generator.gameobject.ObjectRegistry;
import fr.cubiccl.generator.gameobject.baseobjects.Block;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagList;
import fr.cubiccl.generator.gameobject.tags.TagString;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateList;
import fr.cubiccl.generator.gui.component.interfaces.IObjectList;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelBlockSelection;
import fr.cubiccl.generator.gui.component.panel.utils.PanelObjectList;
import fr.cubiccl.generator.utils.Text;
import fr.cubiccl.generator.utils.Utils.BlockComparator;

public class TemplateBlockList extends TemplateList
{
	private static class BlockList implements IObjectList
	{
		private ArrayList<Block> blocks;

		public BlockList(Block[] blocks)
		{
			this.blocks = new ArrayList<Block>();
			for (Block block : blocks)
				this.blocks.add(block);
		}

		@Override
		public boolean addObject(CGPanel panel)
		{
			Block b = ((PanelBlockSelection) panel).selectedBlock();
			if (!this.blocks.contains(b))
			{
				this.blocks.add(b);
				this.blocks.sort(new BlockComparator());
			}
			return true;
		}

		@Override
		public CGPanel createAddPanel()
		{
			return new PanelBlockSelection(false);
		}

		@Override
		public Text getName(int index)
		{
			return this.blocks.get(index).mainName();
		}

		@Override
		public BufferedImage getTexture(int index)
		{
			return this.blocks.get(index).texture(0);
		}

		@Override
		public String[] getValues()
		{
			String[] values = new String[this.blocks.size()];
			for (int i = 0; i < values.length; ++i)
				values[i] = this.blocks.get(i).idString;
			return values;
		}

		@Override
		public void remove(int index)
		{
			this.blocks.remove(index);
		}

	}

	public TemplateBlockList(String id, byte tagType, String[] applicable)
	{
		super(id, tagType, applicable);
	}

	@Override
	protected CGPanel createPanel(String objectId, Tag previousValue)
	{
		Block[] blocks = new Block[0];
		if (previousValue != null)
		{
			TagList t = ((TagList) previousValue);
			blocks = new Block[t.size()];
			for (int i = 0; i < blocks.length; ++i)
				blocks[i] = ObjectRegistry.getBlockFromID((String) t.getTag(i).value());
		}
		PanelObjectList p = new PanelObjectList(new BlockList(blocks));
		p.setName("tag.title." + this.id);
		return p;
	}

	@Override
	public TagList generateTag(CGPanel panel)
	{
		String[] values = ((PanelObjectList) panel).objectList.getValues();
		TagString[] tags = new TagString[values.length];
		for (int i = 0; i < tags.length; ++i)
			tags[i] = new TagString(Tags.DEFAULT_STRING, values[i]);
		return new TagList(this, tags);
	}

}
