package fr.cubiccl.generator.gameobject.tags;

import fr.cubiccl.generator.gameobject.templatetags.TemplateNumber;

public class TagNumber extends Tag
{
	public static final byte INTEGER = 0, BYTE = 1, SHORT = 2, FLOAT = 3, DOUBLE = 4, BYTE_BOOLEAN = 5;
	private static final String[] SUFFIX =
	{ "", "b", "s", "f", "d", "b" };

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
		return this.value() + SUFFIX[((TemplateNumber) this.template).numberType];
	}

}
