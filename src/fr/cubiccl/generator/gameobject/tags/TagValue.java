package fr.cubiccl.generator.gameobject.tags;

import fr.cubiccl.generator.gameobject.templatetags.TemplateTag;

public abstract class TagValue extends Tag
{

	private String value;

	public TagValue(TemplateTag template, String value)
	{
		super(template);
		this.value = value;
	}

	@Override
	public String value()
	{
		return this.value;
	}

}
