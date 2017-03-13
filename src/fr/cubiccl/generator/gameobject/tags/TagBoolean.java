package fr.cubiccl.generator.gameobject.tags;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonValue;

import fr.cubiccl.generator.gameobject.templatetags.TemplateBoolean;

public class TagBoolean extends Tag
{
	private boolean value;

	@Deprecated
	public TagBoolean(TemplateBoolean template, boolean value)
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
	public Boolean value()
	{
		return this.value;
	}

}
