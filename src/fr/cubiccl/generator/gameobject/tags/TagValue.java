package fr.cubiccl.generator.gameobject.tags;

public abstract class TagValue extends Tag
{

	private String value;

	public TagValue(String id, String value)
	{
		super(id);
		this.value = value;
	}

	@Override
	public String value()
	{
		return this.value;
	}

}
