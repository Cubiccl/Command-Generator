package fr.cubiccl.generator.gameobject.templatetags;

import java.util.ArrayList;

import org.jdom2.Element;

import fr.cubiccl.generator.gameobject.baseobjects.BaseObject;
import fr.cubiccl.generator.gameobject.tags.NBTParser;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gui.component.panel.CGPanel;

public abstract class TemplateCompound extends TemplateTag
{
	public static class DefaultCompound extends TemplateCompound
	{

		public DefaultCompound(String id)
		{
			this(id, Tag.UNAVAILABLE);
		}

		public DefaultCompound(String id, byte type)
		{
			super(id, type, "ANY");
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

	public TemplateCompound()
	{
		this(null, Tag.UNKNOWN);
	}

	public TemplateCompound(String id, byte applicationType, String... applicable)
	{
		super(id, Tag.COMPOUND, applicationType, applicable);
	}

	public TagCompound create()
	{
		return this.create(new Tag[0]);
	}

	@SuppressWarnings("deprecation")
	public TagCompound create(Tag... value)
	{
		return new TagCompound(this, value);
	}

	@Override
	public TemplateTag fromXML(Element xml)
	{
		this.tagType = Tag.COMPOUND;
		return super.fromXML(xml);
	}

	@Override
	public Tag readTag(String value, boolean isJson, boolean readUnknown)
	{ // Change this --> Change TemplateRange.readTag()
		if (value.startsWith("{") && value.endsWith("}")) value = value.substring(1, value.length() - 1);
		String[] values = NBTParser.splitTagValues(value);
		ArrayList<Tag> tags = new ArrayList<Tag>();
		for (String v : values)
		{
			if (v.equals("")) continue;
			Tag t = NBTParser.parse(v, false, isJson, readUnknown);
			if (t != null) tags.add(t);
		}
		return this.create(tags.toArray(new Tag[tags.size()]));
	}

}
