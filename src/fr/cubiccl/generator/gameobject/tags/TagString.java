package fr.cubiccl.generator.gameobject.tags;

public class TagString extends TagValue
{

	public TagString(String id, String value)
	{
		super(id, value);
	}

	@Override
	public String value()
	{
		return "\"" + super.value().replace("\"", "\\\"") + "\"";
	}

}
