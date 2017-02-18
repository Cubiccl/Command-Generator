package fr.cubiccl.generator.gameobject.tags;

import fr.cubiccl.generator.gameobject.GameObject;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gameobject.templatetags.TemplateTag;

public abstract class Tag extends GameObject
{
	public static final byte BLOCK = 0, ITEM = 1, ENTITY = 2, UNAVAILABLE = 3, UNKNOWN = 4;
	protected static final String INDENT = "    ";
	public static final byte STRING = 0, BYTE = 1, SHORT = 2, INT = 3, LONG = 4, FLOAT = 5, DOUBLE = 6, LIST = 7, COMPOUND = 8, BOOLEAN = 9;

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
	@Deprecated
	public String toCommand()
	{
		return this.toCommand(-1);
	}

	/** @param indent - The current indentation. -1 if no indentation. */
	public String toCommand(int indent)
	{
		String display = "";
		if (indent != -1) for (int i = 0; i < indent; ++i)
			display += INDENT;

		if (this.isJson) display += "\"";
		display += this.id();
		if (this.isJson) display += "\"";
		display += ":" + this.valueForCommand(indent);
		return display;
	}

	@Override
	public String toString()
	{
		return this.toCommand();
	}

	@Override
	@Deprecated
	public TagCompound toTag(TemplateCompound container, boolean includeName)
	{
		return null;
	}

	public int type()
	{
		return this.template.applicationType;
	}

	public abstract Object value();

	public String valueForCommand()
	{
		return this.valueForCommand(-1);
	}

	public abstract String valueForCommand(int indent);

}
