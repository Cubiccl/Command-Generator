package fr.cubiccl.generator.gameobject.tags;

import fr.cubiccl.generator.gameobject.templatetags.TemplateBoolean;

public class TagBoolean extends Tag
{
	private boolean value;

	public TagBoolean(TemplateBoolean template, boolean value)
	{
		super(template);
		this.value = value;
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
