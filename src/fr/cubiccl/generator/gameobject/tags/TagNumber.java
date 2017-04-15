package fr.cubiccl.generator.gameobject.tags;

import fr.cubiccl.generator.gameobject.templatetags.TemplateNumber;
import fr.cubiccl.generator.utils.Utils;

public class TagNumber extends Tag
{
	public static final String[] SUFFIX =
	{ "", "b", "s", "", "l", "f", "d", "", "" };

	public double value;

	@Deprecated
	public TagNumber(TemplateNumber template, double value)
	{
		super(template);
		this.value = value;
	}

	private String suffix()
	{
		return ((TemplateNumber) this.template).suffix();
	}

	@Override
	public Double value()
	{
		return this.value;
	}

	@Override
	public String valueForCommand(int indent)
	{
		if (((TemplateNumber) this.template).isBigNumber())
		{
			if (((TemplateNumber) this.template).tagType == Tag.LONG) return Utils.doubleToString(this.value()) + this.suffix();
			return Utils.doubleToString(this.value()) + this.suffix();

		} else return Integer.toString((int) this.value) + this.suffix();
	}

	public int valueInt()
	{
		return (int) this.value;
	}

}
