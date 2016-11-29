package fr.cubiccl.generator.gameobject.tags;

import fr.cubiccl.generator.gameobject.templatetags.TemplateString;

public class TagString extends TagValue
{

	public TagString(TemplateString template, String value)
	{
		super(template, value);
	}

	@Override
	public String value()
	{
		return "\"" + super.value().replace("\"", "\\\"") + "\"";
	}

}
