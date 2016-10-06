package fr.cubiccl.generator.gameobject.tags;

public abstract class Tag
{
	public final String id;
	protected boolean isJson;

	public Tag(String id)
	{
		this.id = id;
	}

	public void setJson(boolean isJson)
	{
		this.isJson = isJson;
	}

	public String toCommand()
	{
		if (this.isJson)
		{
			if (this instanceof TagCompound || this instanceof TagList || this instanceof TagString) return "\"" + this.id + "\":" + this.value();
			return "\"" + this.id + "\":\"" + this.value() + "\"";
		}
		return this.id + ":" + this.value();
	}

	public abstract String value();

}
