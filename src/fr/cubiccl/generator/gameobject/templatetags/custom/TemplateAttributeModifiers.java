package fr.cubiccl.generator.gameobject.templatetags.custom;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import fr.cubiccl.generator.gameobject.AttributeModifier;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.tags.TagList;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateList;
import fr.cubiccl.generator.gui.component.interfaces.IObjectList;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelAttributeModifier;
import fr.cubiccl.generator.gui.component.panel.utils.PanelObjectList;
import fr.cubiccl.generator.utils.Text;

public class TemplateAttributeModifiers extends TemplateList
{
	private class AttributeModifierList implements IObjectList
	{

		private ArrayList<AttributeModifier> modifiers = new ArrayList<AttributeModifier>();

		@Override
		public boolean addObject(CGPanel panel)
		{
			AttributeModifier m = ((PanelAttributeModifier) panel).generateModifier();
			if (m == null) return false;
			this.modifiers.add(m);
			return true;
		}

		@Override
		public CGPanel createAddPanel()
		{
			return new PanelAttributeModifier(false);
		}

		@Override
		public Text getName(int index)
		{
			return new Text(this.modifiers.get(index).name, false);
		}

		@Override
		public BufferedImage getTexture(int index)
		{
			return null;
		}

		@Override
		public String[] getValues()
		{
			String[] values = new String[this.modifiers.size()];
			for (int i = 0; i < values.length; i++)
				values[i] = this.getName(i).toString();
			return values;
		}

		@Override
		public void removeObject(int index)
		{
			this.modifiers.remove(index);
		}

	}

	public TemplateAttributeModifiers(String id, byte tagType, String... applicable)
	{
		super(id, tagType, applicable);
	}

	@Override
	protected CGPanel createPanel(String objectId, Tag previousValue)
	{
		PanelObjectList p = new PanelObjectList(new AttributeModifierList());
		p.setName(this.title());
		AttributeModifier[] ench = new AttributeModifier[0];
		// TODO from value
		return p;
	}

	@Override
	public TagList generateTag(CGPanel panel)
	{
		AttributeModifier[] values = ((AttributeModifierList) ((PanelObjectList) panel).objectList).modifiers.toArray(new AttributeModifier[0]);
		TagCompound[] tags = new TagCompound[values.length];
		for (int i = 0; i < tags.length; ++i)
			tags[i] = values[i].toTag(Tags.DEFAULT_COMPOUND, false);
		return new TagList(this, tags);
	}

}
