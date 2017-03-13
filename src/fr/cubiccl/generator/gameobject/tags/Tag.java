package fr.cubiccl.generator.gameobject.tags;

import org.jdom2.Element;

import com.eclipsesource.json.JsonValue;

import fr.cubiccl.generator.gameobject.GameObject;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gameobject.templatetags.TemplateTag;
import fr.cubiccl.generator.utils.Utils;

public abstract class Tag extends GameObject
{
	public static final byte BLOCK = 0, ITEM = 1, ENTITY = 2, UNAVAILABLE = 3, UNKNOWN = 4;
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
		return this.toCommand(false);
	}

	public String toCommand(boolean shouldIndent)
	{
		return Utils.jsonToString(this.toJson(), shouldIndent, this.template.tagType == COMPOUND || this.template.tagType == LIST ? null : this.template.id());
	}

	public abstract JsonValue toJson();

	@Override
	@Deprecated
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

	@Override
	@Deprecated
	public Element toXML()
	{
		return null;
	}

	public int type()
	{
		return this.template.applicationType;
	}

	public abstract Object value();

}
