package fr.cubiccl.generator.gameobject.tags;

import fr.cubiccl.generator.gameobject.GameObject;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gameobject.templatetags.TemplateTag;

public abstract class Tag extends GameObject
{
	public static final byte BLOCK = 0, ITEM = 1, ENTITY = 2, UNAVAILABLE = 3;
	public static final byte STRING = 0, BYTE = 1, SHORT = 2, INT = 3, LONG = 4, FLOAT = 5, DOUBLE = 6, LIST = 7, COMPOUND = 8;
	protected boolean isJson;
	public final TemplateTag template;

	public Tag(TemplateTag template)
	{
		this.template = template;
	}

	public String id()
	{
		return this.template.id();
	}

	public void setJson(boolean isJson)
	{
		this.isJson = isJson;
	}

	@Override
	public String toCommand()
	{
		if (this.isJson) return "\"" + this.id() + "\":" + this.valueForCommand();
		return this.id() + ":" + this.valueForCommand();
	}

	@Override
	public String toString()
	{
		return this.toCommand();
	}

	@Override
	@Deprecated
	public TagCompound toTag(TemplateCompound container)
	{
		return null;
	}

	public int type()
	{
		return this.template.applicationType;
	}

	public abstract Object value();

	public abstract String valueForCommand();

}
