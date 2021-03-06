package fr.cubiccl.generator.gameobject.tags;

import fr.cubiccl.generator.gameobject.templatetags.TemplateBoolean;

/** Boolean Tag. */
public class TagBoolean extends Tag
{
	/** This Tag's value. */
	private boolean value;

	@Deprecated
	public TagBoolean(TemplateBoolean template, boolean value)
	{
		super(template);
		this.value = value;
	}

	@Override
	public TagBoolean duplicate()
	{
		return new TagBoolean((TemplateBoolean) this.template, this.value);
	}

	@Override
	public Boolean value()
	{
		return this.value;
	}

	@Override
	public String valueForCommand(int indent)
	{
		return Boolean.toString(this.value);
	}

}
