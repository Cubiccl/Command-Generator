package fr.cubiccl.generator.gameobject.templatetags;

import java.util.ArrayList;

import org.jdom2.Element;

import fr.cubiccl.generator.gameobject.baseobjects.BaseObject;
import fr.cubiccl.generator.gameobject.tags.NBTParser;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagList;
import fr.cubiccl.generator.gui.component.panel.CGPanel;

/** An NBT Tag containing more NBT Tags, ordered and unnamed. */
public abstract class TemplateList extends TemplateTag
{
	/** Default List, with no behavior. Shouldn't be applicable. */
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
		protected CGPanel createPanel(BaseObject<?> object, Tag previousValue)
		{
			return null;
		}

		@Override
		public Tag generateTag(BaseObject<?> object, CGPanel panel)
		{
			return null;
		}

	}

	public TemplateList()
	{
		this(null, Tag.UNKNOWN);
	}

	public TemplateList(String id, byte applicationType, String... applicable)
	{
		super(id, Tag.LIST, applicationType, applicable);
	}

	/** Creates this NBT Tag with the input values.
	 * 
	 * @param value - The list of NBT Tags to set.
	 * @return The created NBT Tag. */
	@SuppressWarnings("deprecation")
	public TagList create(Tag... value)
	{
		return new TagList(this, value);
	}

	@Override
	public TemplateTag fromXML(Element xml)
	{
		this.tagType = Tag.LIST;
		return super.fromXML(xml);
	}

	@Override
	public TagList parseTag(String value, boolean isJson, boolean readUnknown)
	{
		if (value.startsWith("[") && value.endsWith("]")) value = value.substring(1, value.length() - 1);
		String[] values = NBTParser.splitTagValues(value);
		ArrayList<Tag> tags = new ArrayList<Tag>();
		for (String v : values)
		{
			if (v.equals("")) continue;
			Tag t = NBTParser.parse(v, true, isJson, readUnknown);
			if (t != null) tags.add(t);
		}
		return this.create(tags.toArray(new Tag[tags.size()]));
	}

}
