package fr.cubiccl.generator.gameobject.templatetags.custom;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gameobject.baseobjects.BaseObject;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagList;
import fr.cubiccl.generator.gameobject.tags.TagString;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateList;
import fr.cubiccl.generator.gui.component.interfaces.IObjectList;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.utils.EntryPanel;
import fr.cubiccl.generator.gui.component.panel.utils.PanelObjectList;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;
import fr.cubiccl.generator.utils.Utils;

public class TemplateStringList extends TemplateList
{

	private class StringList implements IObjectList
	{
		private ArrayList<String> strings = new ArrayList<String>();

		public StringList(String[] values)
		{
			for (String string : values)
				strings.add(string);
		}

		@Override
		public boolean addObject(CGPanel panel)
		{
			try
			{
				Utils.checkStringId(new Text("tag." + id()), ((EntryPanel) panel).entry.getText());
			} catch (CommandGenerationException e)
			{
				CommandGenerator.report(e);
				return false;
			}
			this.strings.add(((EntryPanel) panel).entry.getText());
			return true;
		}

		@Override
		public CGPanel createAddPanel()
		{
			return new EntryPanel("tag." + id());
		}

		@Override
		public Text getName(int index)
		{
			return new Text(this.strings.get(index).length() > 20 ? this.strings.get(index).substring(0, 21) : this.strings.get(index), false);
		}

		@Override
		public BufferedImage getTexture(int index)
		{
			return null;
		}

		@Override
		public String[] getValues()
		{
			String[] values = new String[this.strings.size()];
			for (int i = 0; i < values.length; i++)
				values[i] = this.strings.get(i).length() > 20 ? this.strings.get(i).substring(0, 21) : this.strings.get(i);

			return values;
		}

		@Override
		public void removeObject(int index)
		{
			this.strings.remove(index);
		}

	}

	public TemplateStringList(String id, byte tagType, String[] applicable)
	{
		super(id, tagType, applicable);
	}

	@Override
	protected CGPanel createPanel(BaseObject object, Tag previousValue)
	{
		String[] values = new String[0];
		if (previousValue != null)
		{
			TagList t = (TagList) previousValue;
			values = new String[t.size()];
			for (int i = 0; i < values.length; i++)
				values[i] = (String) t.getTag(i).value();
		}
		PanelObjectList p = new PanelObjectList(new StringList(values));
		p.setName(this.title());
		return p;
	}

	@Override
	public TagList generateTag(BaseObject object, CGPanel panel)
	{
		String[] s = ((StringList) ((PanelObjectList) panel).objectList).strings.toArray(new String[0]);
		TagString[] tags = new TagString[s.length];
		for (int i = 0; i < tags.length; i++)
			tags[i] = new TagString(Tags.DEFAULT_STRING, s[i]);
		return new TagList(this, tags);
	}
}
