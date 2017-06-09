package fr.cubiccl.generator.gameobject.tags;

import fr.cubiccl.generator.gameobject.templatetags.TemplateNumber;
import fr.cubiccl.generator.utils.Utils;

/** A Number NBT Tag. */
public class TagNumber extends Tag
{
	/** Suffixes to be added at the end of Number Tags values. <br />
	 * <ul>
	 * <li>Integer: none</li>
	 * <li>Byte: b</li>
	 * <li>Short: s</li>
	 * <li>Long: l</li>
	 * <li>Float: f</li>
	 * <li>Double: d</li>
	 * </ul> */
	public static final String[] SUFFIX =
	{ "", "b", "s", "", "l", "f", "d", "", "", "", "" };

	/** This Tag's value. */
	public double value;

	@Deprecated
	public TagNumber(TemplateNumber template, double value)
	{
		super(template);
		this.value = value;
	}

	/** @return The suffix to add at the end of this Tag's value. */
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

	/** @return this Tag's value as an Integer. */
	public int valueInt()
	{
		return (int) this.value;
	}

}
