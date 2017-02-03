package fr.cubiccl.generator.gameobject.tags;

import fr.cubiccl.generator.gameobject.templatetags.TemplateNumber;

public class TagNumber extends Tag
{
	public static final byte INTEGER = 0, BYTE = 1, SHORT = 2, BYTE_BOOLEAN = 3, LONG = 4, FLOAT = 5, DOUBLE = 6;
	public static final String[] SUFFIX =
	{ "", "b", "s", "", "l", "f", "d", "", "" };
	public static final byte[] TYPE_TRANSITION = new byte[]
	{ Tag.INT, Tag.BYTE, Tag.SHORT, Tag.BYTE, Tag.LONG, Tag.FLOAT, Tag.DOUBLE };

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
		return Integer.toString(this.value()) + SUFFIX[((TemplateNumber) this.template).tagType];
	}

}
