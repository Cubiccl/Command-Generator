package fr.cubiccl.generator.gameobject.tags;

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
	public Integer value()
	{
		return this.value;
	}

	@Override
	public String valueForCommand(int indent)
	{
		return Integer.toString(this.value()) + SUFFIX[((TemplateNumber) this.template).tagType];
	}

}
