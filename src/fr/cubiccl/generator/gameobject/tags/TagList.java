package fr.cubiccl.generator.gameobject.tags;

import fr.cubiccl.generator.gameobject.templatetags.TemplateTag;

public class TagList extends Tag
{
	protected Tag[] tags;

	@Deprecated
	public TagList(TemplateTag template, Tag... tags)
	{
		super(template);
		this.tags = tags;
	}

	public void addTag(Tag tag)
	{
		Tag[] t = new Tag[this.tags.length + 1];
		for (int i = 0; i < t.length; ++i)
			t[i] = this.tags[i];
		t[this.tags.length] = tag;
		this.tags = t;
	}

	public Tag getTag(int index)
	{
		if (index < 0 || index >= this.tags.length) return null;
		return this.tags[index];
	}

	@Override
	public void setJson(boolean isJson)
	{
		super.setJson(isJson);
		for (Tag tag : this.tags)
			tag.setJson(isJson);
	}

	public int size()
	{
		return this.tags.length;
	}

	@Override
	public Tag[] value()
	{
		return this.tags;
	}

	@Override
	public String valueForCommand(int indent)
	{
		String value = "";

		if (indent != -1 && this.tags.length != 0)
		{
			value += "\n";
			for (int i = 0; i < indent; ++i)
				value += INDENT;
		}

		value += "[";

		for (int i = 0; i < this.tags.length; ++i)
		{
			if (i != 0) value += ",";
			if (indent != -1 && !(this.tags[i] instanceof TagCompound)) value += "\n";
			for (int j = 0; j < indent; ++j)
				value += INDENT;
			if (this.isJson)
			{
				if (this.tags[i] instanceof TagCompound || this.tags[i] instanceof TagList || this.tags[i] instanceof TagString) value += this.tags[i]
						.valueForCommand(indent == -1 ? -1 : indent + 1);
				else value += "\"" + this.tags[i].valueForCommand(indent == -1 ? -1 : indent + 1) + "\"";
			} else value += this.tags[i].valueForCommand(indent == -1 ? -1 : indent + 1);
		}

		if (indent != -1 && this.tags.length != 0)
		{
			value += "\n";
			for (int i = 0; i < indent; ++i)
				value += INDENT;
		}
		return value + "]";
	}

}
