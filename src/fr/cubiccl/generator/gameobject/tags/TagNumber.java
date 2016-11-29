package fr.cubiccl.generator.gameobject.tags;

import fr.cubiccl.generator.gameobject.templatetags.TemplateNumber;

public class TagNumber extends TagValue
{
	public static final int INTEGER = 0, BYTE = 1, SHORT = 2, FLOAT = 3, DOUBLE = 4;
	private static final String[] SUFFIX =
	{ "", "b", "s", "f", "d" };

	public TagNumber(TemplateNumber template, String value)
	{
		super(template, value);
	}

	@Override
	public String value()
	{
		return super.value() + SUFFIX[((TemplateNumber) this.template).numberType];
	}

}
