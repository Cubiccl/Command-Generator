package fr.cubiccl.generator.gameobject.tags;

import fr.cubiccl.generator.gameobject.templatetags.TemplateTag;

public class TagList extends Tag
{
	protected final Tag[] tags;

	public TagList(TemplateTag template, Tag... tags)
	{
		super(template);
		this.tags = tags;
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
	public String valueForCommand()
	{
		String value = "[";
		for (int i = 0; i < this.tags.length; ++i)
		{
			if (i != 0) value += ",";
			if (this.isJson)
			{
				if (this.tags[i] instanceof TagCompound || this.tags[i] instanceof TagList || this.tags[i] instanceof TagString) value += this.tags[i].valueForCommand();
				value += "\"" + this.tags[i].valueForCommand() + "\"";
			} else value += this.tags[i].valueForCommand();
		}
		return value + "]";
	}

}
