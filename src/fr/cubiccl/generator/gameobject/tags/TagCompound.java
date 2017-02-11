package fr.cubiccl.generator.gameobject.tags;

import java.util.ArrayList;

import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gameobject.templatetags.TemplateTag;

public class TagCompound extends TagList
{

	public TagCompound(TemplateCompound template, Tag... tags)
	{
		super(template, tags);
	}

	public String[] findApplications()
	{
		if (this.size() == 0) return new String[0];
		if (this.size() == 1) return this.getTag(0).template.getApplicable();
		ArrayList<String> available = new ArrayList<String>(), toRemove = new ArrayList<String>();
		for (String string : this.getTag(0).template.getApplicable())
			available.add(string);

		for (int i = 1; i < this.size(); ++i)
		{
			toRemove.clear();
			for (String a : available)
			{
				boolean found = false;
				for (String string : this.getTag(i).template.getApplicable())
					if (string.equals(a))
					{
						found = true;
						break;
					}
				if (!found) toRemove.add(a);
			}

			for (String a : toRemove)
				available.remove(a);
			if (available.size() == 0) break;
		}

		return available.toArray(new String[available.size()]);
	}

	public Tag getTag(TemplateTag tag)
	{
		return this.getTagFromId(tag.id());
	}

	public Tag getTagFromId(String id)
	{
		for (Tag tag : this.tags)
			if (tag.id().equals(id)) return tag;
		return null;
	}

	public boolean hasTag(String id)
	{
		for (Tag tag : this.tags)
			if (tag.id().equals(id)) return true;
		return false;
	}

	public boolean hasTag(TemplateTag tag)
	{
		return this.hasTag(tag.id()) && this.getTag(tag).template.tagType == tag.tagType;
	}

	@Override
	public String valueForCommand()
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
