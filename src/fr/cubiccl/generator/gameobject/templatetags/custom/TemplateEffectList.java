package fr.cubiccl.generator.gameobject.templatetags.custom;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Comparator;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gameobject.Effect;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.tags.TagList;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateList;
import fr.cubiccl.generator.gui.component.interfaces.IObjectList;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelEffect;
import fr.cubiccl.generator.gui.component.panel.utils.PanelObjectList;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

public class TemplateEffectList extends TemplateList
{
	private static class EffectList implements IObjectList
	{
		private ArrayList<Effect> effects;

		public EffectList(Effect[] effects)
		{
			this.effects = new ArrayList<Effect>();
			for (Effect effect : effects)
				this.effects.add(effect);
		}

		@Override
		public boolean addObject(CGPanel panel)
		{
			Effect ench = null;
			try
			{
				ench = ((PanelEffect) panel).generateEffect();
			} catch (CommandGenerationException e)
			{
				CommandGenerator.report(e);
				return false;
			}
			if (!this.effects.contains(ench))
			{
				this.effects.add(ench);
				this.effects.sort(new Comparator<Effect>()
				{

					@Override
					public int compare(Effect o1, Effect o2)
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
			return new PanelEffect();
		}

		@Override
		public Text getName(int index)
		{
			return this.effects.get(index).type.name();
		}

		@Override
		public BufferedImage getTexture(int index)
		{
			return this.effects.get(index).type.texture();
		}

		@Override
		public String[] getValues()
		{
			String[] values = new String[this.effects.size()];
			for (int i = 0; i < values.length; ++i)
				values[i] = this.effects.get(i).type.name().toString();
			return values;
		}

		@Override
		public void removeObject(int index)
		{
			this.effects.remove(index);
		}

	}

	public TemplateEffectList(String id, byte tagType, String[] applicable)
	{
		super(id, tagType, applicable);
	}

	@Override
	protected CGPanel createPanel(String objectId, Tag previousValue)
	{
		Effect[] ench = new Effect[0];
		// TODO from value
		PanelObjectList p = new PanelObjectList(new EffectList(ench));
		p.setName(this.title());
		return p;
	}

	@Override
	public TagList generateTag(CGPanel panel)
	{
		Effect[] values = ((EffectList) ((PanelObjectList) panel).objectList).effects.toArray(new Effect[0]);
		TagCompound[] tags = new TagCompound[values.length];
		for (int i = 0; i < tags.length; ++i)
			tags[i] = values[i].toTag(Tags.DEFAULT_COMPOUND);
		return new TagList(this, tags);
	}

}
