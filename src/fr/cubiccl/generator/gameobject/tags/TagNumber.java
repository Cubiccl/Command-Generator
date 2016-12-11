package fr.cubiccl.generator.gameobject.tags;

import fr.cubiccl.generator.gameobject.templatetags.TemplateNumber;

public class TagNumber extends Tag
{
	public static final byte INTEGER = 0, BYTE = 1, SHORT = 2, BYTE_BOOLEAN = 3, LONG = 4, FLOAT = 5, DOUBLE = 6;
	public static final String[] SUFFIX =
	{ "", "b", "s", "b", "l", "f", "d" };

	public final int value;

	public TagNumber(TemplateNumber template, int value)
	{
		super(template);
		this.value = value;
	}

	@Override
	public Integer value()
	{
		return this.value;
	}

	@Override
	public String valueForCommand()
	{
		return Integer.toString(this.value()) + SUFFIX[((TemplateNumber) this.template).numberType];
	}

}
