package fr.cubiccl.generator.gameobject.tags;

import fr.cubiccl.generator.gameobject.templatetags.TemplateTag;

public abstract class Tag
{
	public static final byte BLOCK = 0, ITEM = 1, ENTITY = 2, UNAVAILABLE = 3;
	protected boolean isJson;
	public final TemplateTag template;

	public Tag(TemplateTag template)
	{
		this.template = template;
	}

	public String id()
	{
		return this.template.id;
	}

	public void setJson(boolean isJson)
	{
		this.isJson = isJson;
	}

	public String toCommand()
	{
		if (this.isJson)
		{
			if (this instanceof TagCompound || this instanceof TagList || this instanceof TagString) return "\"" + this.id() + "\":" + this.valueForCommand();
			return "\"" + this.id() + "\":\"" + this.valueForCommand() + "\"";
		}
		return this.id() + ":" + this.valueForCommand();
	}

	public int type()
	{
		return this.template.type;
	}

	public abstract Object value();

	public abstract String valueForCommand();

}
