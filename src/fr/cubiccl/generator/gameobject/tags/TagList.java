package fr.cubiccl.generator.gameobject.tags;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonValue;

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
	public JsonValue toJson()
	{
		JsonArray compound = new JsonArray();

		for (Tag tag : this.tags)
			compound.add(tag.toJson());

		return compound;
	}

	@Override
	public Tag[] value()
	{
		return this.tags;
	}

}
