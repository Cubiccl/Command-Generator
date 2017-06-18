package fr.cubiccl.generator.gameobject.tags;

import fr.cubiccl.generator.gameobject.templatetags.TemplateTag;

/** Contains a list of tags, ordered and unnamed. */
public class TagList extends Tag
{
	/** The contained Tags. */
	protected Tag[] tags;

	@Deprecated
	public TagList(TemplateTag template, Tag... tags)
	{
		super(template);
		this.tags = tags;
	}

	/** Adds a Tag to this List.
	 * 
	 * @param tag - The Tag to add. */
	public void addTag(Tag tag)
	{
		Tag[] t = new Tag[this.tags.length + 1];
		for (int i = 0; i < this.tags.length; ++i)
			t[i] = this.tags[i];
		t[this.tags.length] = tag;
		this.tags = t;
	}

	@Override
	public TagList duplicate()
	{
		Tag[] v = new Tag[this.tags.length];
		for (int i = 0; i < v.length; ++i)
			v[i] = this.tags[i].duplicate();
		return new TagList(this.template, v);
	}

	/** @param index - The index of the requested Tag.
	 * @return The Tag at the input index. */
	public Tag getTag(int index)
	{
		if (index < 0 || index >= this.tags.length) return null;
		return this.tags[index];
	}

	@Override
	public TagList setJson(boolean isJson)
	{
		super.setJson(isJson);
		for (Tag tag : this.tags)
			tag.setJson(isJson);
		return this;
	}

	/** @return This List's size. */
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
