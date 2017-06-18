package fr.cubiccl.generator.gameobject.tags;

import fr.cubiccl.generator.gameobject.templatetags.TemplateString;

/** A String NBT Tag. */
public class TagString extends Tag
{
	/** This Tag's value. */
	public final String value;

	@Deprecated
	public TagString(TemplateString template, String value)
	{
		super(template);
		this.value = value;
	}

	@Override
	public TagString duplicate()
	{
		return new TagString((TemplateString) this.template, this.value);
	}

	@Override
	public String value()
	{
		return this.value;
	}

	@Override
	public String valueForCommand(int indent)
	{
		return "\"" + this.value().replace("\\", "\\\\").replace("\"", "\\\"") + "\"";
	}

}
