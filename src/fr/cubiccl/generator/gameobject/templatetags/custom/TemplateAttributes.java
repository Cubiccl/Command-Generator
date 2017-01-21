package fr.cubiccl.generator.gameobject.templatetags.custom;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gameobject.AppliedAttribute;
import fr.cubiccl.generator.gameobject.baseobjects.BaseObject;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.tags.TagList;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateList;
import fr.cubiccl.generator.gui.component.interfaces.IObjectList;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelAttribute;
import fr.cubiccl.generator.gui.component.panel.utils.PanelObjectList;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

public class TemplateAttributes extends TemplateList
{
	private class AttributeList implements IObjectList
	{
		private ArrayList<AppliedAttribute> attributes = new ArrayList<AppliedAttribute>();

		public AttributeList(AppliedAttribute[] att)
		{
			for (AppliedAttribute a : att)
				this.attributes.add(a);
		}

		@Override
		public boolean addObject(CGPanel panel, int editIndex)
		{
			try
			{
				if (editIndex == -1) this.attributes.add(((PanelAttribute) panel).generateAttribute());
				else this.attributes.set(editIndex, ((PanelAttribute) panel).generateAttribute());
				return true;
			} catch (CommandGenerationException e)
			{
				CommandGenerator.report(e);
				return false;
			}
		}

		@Override
		public CGPanel createAddPanel(int editIndex)
		{
			PanelAttribute p = new PanelAttribute();
			if (editIndex != -1) p.setupFrom(this.attributes.get(editIndex));
			return p;
		}

		@Override
		public Text getName(int index)
		{
			return this.attributes.get(index).attribute.name();
		}

		@Override
		public BufferedImage getTexture(int index)
		{
			return null;
		}

		@Override
		public String[] getValues()
		{
			String[] values = new String[this.attributes.size()];
			for (int i = 0; i < values.length; i++)
				values[i] = this.getName(i).toString();
			return values;
		}

		@Override
		public void removeObject(int index)
		{
			this.attributes.remove(index);
		}

	}

	public TemplateAttributes(String id, byte tagType, String[] applicable)
	{
		super(id, tagType, applicable);
	}

	@Override
	protected CGPanel createPanel(BaseObject object, Tag previousValue)
	{
		AppliedAttribute[] attributes = new AppliedAttribute[0];
		if (previousValue != null)
		{
			TagList t = (TagList) previousValue;
			attributes = new AppliedAttribute[t.size()];
			for (int i = 0; i < attributes.length; i++)
				attributes[i] = AppliedAttribute.createFrom((TagCompound) t.getTag(i));
		}
		PanelObjectList p = new PanelObjectList(new AttributeList(attributes));
		p.setName(this.title());
		return p;
	}

	@Override
	public TagList generateTag(BaseObject object, CGPanel panel)
	{
		ArrayList<AppliedAttribute> list = ((AttributeList) ((PanelObjectList) panel).objectList).attributes;
		TagCompound[] tags = new TagCompound[list.size()];
		for (int i = 0; i < tags.length; i++)
			tags[i] = list.get(i).toTag(Tags.DEFAULT_COMPOUND);
		return new TagList(this, tags);
	}

}
