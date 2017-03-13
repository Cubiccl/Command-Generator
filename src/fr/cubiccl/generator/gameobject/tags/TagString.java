package fr.cubiccl.generator.gameobject.tags;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonValue;

import fr.cubiccl.generator.gameobject.templatetags.TemplateString;

public class TagString extends Tag
{
	public final String value;

	@Deprecated
	public TagString(TemplateString template, String value)
	{
		super(template);
		this.value = value;
	}

	@Override
	public JsonValue toJson()
	{
		return Json.value(this.value);
	}

	@Override
	public String value()
	{
		return this.value;
	}

}
