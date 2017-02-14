package fr.cubiccl.generator.gameobject.tags;

import fr.cubiccl.generator.gameobject.templatetags.TemplateNumber;
import fr.cubiccl.generator.utils.Utils;

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
	public String valueForCommand(int indent)
	{
		if (((TemplateNumber) this.template).tagType == Tag.LONG) return Utils.doubleToString(this.value())
				+ TagNumber.SUFFIX[((TemplateNumber) this.template).tagType];
		return Utils.doubleToString(this.value()) + TagNumber.SUFFIX[((TemplateNumber) this.template).tagType];

	}
}
