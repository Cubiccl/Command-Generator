package fr.cubiccl.generator.gameobject.tags;

import java.text.DecimalFormat;

import fr.cubiccl.generator.gameobject.templatetags.TemplateNumber;

public class TagBigNumber extends Tag
{

	public final double value;

	public TagBigNumber(TemplateNumber template, double value)
	{
		super(template);
		this.value = value;
	}

	@Override
	public Double value()
	{
		return this.value;
	}

	@Override
	public String valueForCommand()
	{
		if (((TemplateNumber) this.template).numberType == TagNumber.LONG) return new DecimalFormat("#").format(this.value())
				+ TagNumber.SUFFIX[((TemplateNumber) this.template).numberType];
		return Double.toString(this.value()) + TagNumber.SUFFIX[((TemplateNumber) this.template).numberType];

	}
}
