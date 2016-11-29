package fr.cubiccl.generator.gameobject.tags;

import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;

public class TagCompound extends TagList
{

	public TagCompound(TemplateCompound template, Tag... tags)
	{
		super(template, tags);
	}

	public Tag getTagFromId(String id)
	{
		for (Tag tag : this.tags)
			if (tag.id().equals(id)) return tag;
		return null;
	}

	@Override
	public String value()
	{
		String value = "{";
		for (int i = 0; i < this.tags.length; ++i)
		{
			if (i != 0) value += ",";
			value += this.tags[i].toCommand();
		}
		return value + "}";
	}

}
