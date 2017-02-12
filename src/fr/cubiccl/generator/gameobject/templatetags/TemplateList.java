package fr.cubiccl.generator.gameobject.templatetags;

import java.util.ArrayList;

import fr.cubiccl.generator.gameobject.baseobjects.BaseObject;
import fr.cubiccl.generator.gameobject.tags.NBTReader;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagList;
import fr.cubiccl.generator.gui.component.panel.CGPanel;

public abstract class TemplateList extends TemplateTag
{
	public static class DefaultList extends TemplateList
	{

		public DefaultList(String id)
		{
			this(id, Tag.UNAVAILABLE);
		}

		public DefaultList(String id, byte type)
		{
			super(id, type);
		}

		@Override
		protected CGPanel createPanel(BaseObject object, Tag previousValue)
		{
			return null;
		}

		@Override
		public Tag generateTag(BaseObject object, CGPanel panel)
		{
			return null;
		}

	}

	public TemplateList(String id, byte applicationType, String... applicable)
	{
		super(id, Tag.LIST, applicationType, applicable);
	}

	@Override
	public TagList readTag(String value, boolean isJson)
	{
		if (value.startsWith("[") && value.endsWith("]")) value = value.substring(1, value.length() - 1);
		String[] values = NBTReader.splitTagValues(value);
		ArrayList<Tag> tags = new ArrayList<Tag>();
		for (String v : values)
		{
			if (v.equals("")) continue;
			Tag t = NBTReader.read(v, true, isJson);
			if (t != null) tags.add(t);
		}
		return new TagList(this, tags.toArray(new Tag[tags.size()]));
	}

}
