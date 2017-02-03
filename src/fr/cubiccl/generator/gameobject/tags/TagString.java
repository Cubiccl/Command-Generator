package fr.cubiccl.generator.gameobject.tags;

import fr.cubiccl.generator.gameobject.templatetags.TemplateString;

public class TagString extends Tag
{
	public final String value;

	public TagString(TemplateString template, String value)
	{
		super(template);
		this.value = value;
	}

	@Override
	public String value()
	{
		return this.value;
	}

	@Override
	public String valueForCommand()
	{
		return "\"" + this.value().replace("\\", "\\\\").replace("\"", "\\\"") + "\"";
	}

}
