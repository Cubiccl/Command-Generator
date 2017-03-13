package fr.cubiccl.generator.gameobject.tags;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonValue;

import fr.cubiccl.generator.gameobject.templatetags.TemplateNumber;
import fr.cubiccl.generator.utils.Utils;

public class TagBigNumber extends Tag
{

	public final double value;

	@Deprecated
	public TagBigNumber(TemplateNumber template, double value)
	{
		super(template);
		this.value = value;
	}

	@Override
	public JsonValue toJson()
	{
		String v;
		if (((TemplateNumber) this.template).tagType == Tag.LONG) v = Utils.doubleToString(this.value())
				+ TagNumber.SUFFIX[((TemplateNumber) this.template).tagType];
		else v = Utils.doubleToString(this.value()) + TagNumber.SUFFIX[((TemplateNumber) this.template).tagType];
		return Json.value(v);
	}

	@Override
	public Double value()
	{
		return this.value;
	}
}
