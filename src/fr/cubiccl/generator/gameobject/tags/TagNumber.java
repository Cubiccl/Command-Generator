package fr.cubiccl.generator.gameobject.tags;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonValue;

import fr.cubiccl.generator.gameobject.templatetags.TemplateNumber;

public class TagNumber extends Tag
{
	public static final String[] SUFFIX =
	{ "", "b", "s", "", "l", "f", "d", "", "" };

	public int value;

	@Deprecated
	public TagNumber(TemplateNumber template, int value)
	{
		super(template);
		this.value = value;
	}

	@Override
	public JsonValue toJson()
	{
		return Json.value(Integer.toString(this.value()) + SUFFIX[((TemplateNumber) this.template).tagType]);
	}

	@Override
	public Integer value()
	{
		return this.value;
	}

}
