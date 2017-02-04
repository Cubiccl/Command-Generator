package fr.cubiccl.generator.gameobject.templatetags.custom;

import java.awt.Component;
import java.util.ArrayList;

import fr.cubiccl.generator.gameobject.baseobjects.BaseObject;
import fr.cubiccl.generator.gameobject.baseobjects.Block;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagList;
import fr.cubiccl.generator.gameobject.tags.TagString;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateList;
import fr.cubiccl.generator.gui.component.interfaces.IObjectList;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.label.ImageLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelBlockSelection;
import fr.cubiccl.generator.gui.component.panel.utils.PanelObjectList;

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
		public boolean addObject(CGPanel panel, int editIndex)
		{
			Block b = ((PanelBlockSelection) panel).selectedBlock();
			if (editIndex != -1)
			{
				if (this.blocks.contains(b)) this.removeObject(editIndex);
				else this.blocks.set(editIndex, b);
			}
			if (!this.blocks.contains(b))
			{
				this.blocks.add(b);
				this.blocks.sort(new ObjectComparatorIDNum());
			}
			return true;
		}

		@Override
		public CGPanel createAddPanel(int editIndex)
		{
			PanelBlockSelection p = new PanelBlockSelection(false);
			if (editIndex != -1) p.setSelected(this.blocks.get(editIndex));
			return p;
		}

		@Override
		public Component getDisplayComponent(int index)
		{
			CGPanel p = new CGPanel();
			p.add(new CGLabel(this.blocks.get(index).mainName()));
			p.add(new ImageLabel(this.blocks.get(index).texture(0)));
			return p;
		}

		@Override
		public String[] getValues()
		{
			String[] values = new String[this.blocks.size()];
			for (int i = 0; i < values.length; ++i)
				values[i] = this.blocks.get(i).id();
			return values;
		}

		@Override
		public void removeObject(int index)
		{
			this.blocks.remove(index);
		}

	}

	public TemplateBlockList(String id, byte applicationType, String[] applicable)
	{
		super(id, applicationType, applicable);
	}

	@Override
	protected CGPanel createPanel(BaseObject object, Tag previousValue)
	{
		Block[] blocks = new Block[0];
		if (previousValue != null)
		{
			TagList t = ((TagList) previousValue);
			blocks = new Block[t.size()];
			for (int i = 0; i < blocks.length; ++i)
				blocks[i] = ObjectRegistry.blocks.find((String) t.getTag(i).value());
		}
		PanelObjectList p = new PanelObjectList(new BlockList(blocks));
		p.setName(this.title());
		return p;
	}

	@Override
	public TagList generateTag(BaseObject object, CGPanel panel)
	{
		String[] values = ((PanelObjectList) panel).getObjectList().getValues();
		TagString[] tags = new TagString[values.length];
		for (int i = 0; i < tags.length; ++i)
			tags[i] = new TagString(Tags.DEFAULT_STRING, values[i]);
		return new TagList(this, tags);
	}

}
