package fr.cubiccl.generator.gameobject.templatetags.custom;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Comparator;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gameobject.Enchantment;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.tags.TagList;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateList;
import fr.cubiccl.generator.gui.component.interfaces.IObjectList;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelEnchantment;
import fr.cubiccl.generator.gui.component.panel.utils.PanelObjectList;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

public class TemplateEnchantmentList extends TemplateList
{
	private static class EnchantmentList implements IObjectList
	{
		private ArrayList<Enchantment> enchantments;

		public EnchantmentList(Enchantment[] blocks)
		{
			this.enchantments = new ArrayList<Enchantment>();
			for (Enchantment block : blocks)
				this.enchantments.add(block);
		}

		@Override
		public boolean addObject(CGPanel panel)
		{
			Enchantment ench = null;
			try
			{
				ench = ((PanelEnchantment) panel).generateEnchantment();
			} catch (CommandGenerationException e)
			{
				CommandGenerator.report(e);
				return false;
			}
			if (!this.enchantments.contains(ench))
			{
				this.enchantments.add(ench);
				this.enchantments.sort(new Comparator<Enchantment>()
				{

					@Override
					public int compare(Enchantment o1, Enchantment o2)
					{
						return o1.type.idInt - o2.type.idInt;
					}
				});
			}
			return true;
		}

		@Override
		public CGPanel createAddPanel()
		{
			return new PanelEnchantment(false);
		}

		@Override
		public Text getName(int index)
		{
			return this.enchantments.get(index).type.name();
		}

		@Override
		public BufferedImage getTexture(int index)
		{
			return null;
		}

		@Override
		public String[] getValues()
		{
			String[] values = new String[this.enchantments.size()];
			for (int i = 0; i < values.length; ++i)
				values[i] = this.enchantments.get(i).toString();
			return values;
		}

		@Override
		public void remove(int index)
		{
			this.enchantments.remove(index);
		}

	}

	public TemplateEnchantmentList(String id, byte tagType, String[] applicable)
	{
		super(id, tagType, applicable);
	}

	@Override
	protected CGPanel createPanel(String objectId, Tag previousValue)
	{
		Enchantment[] ench = new Enchantment[0];
		// TODO from value
		PanelObjectList p = new PanelObjectList(new EnchantmentList(ench));
		p.setName(this.title());
		return p;
	}

	@Override
	public TagList generateTag(CGPanel panel)
	{
		Enchantment[] values = ((EnchantmentList) ((PanelObjectList) panel).objectList).enchantments.toArray(new Enchantment[0]);
		TagCompound[] tags = new TagCompound[values.length];
		for (int i = 0; i < tags.length; ++i)
			tags[i] = values[i].toTag(Tags.DEFAULT_COMPOUND);
		return new TagList(this, tags);
	}

}
