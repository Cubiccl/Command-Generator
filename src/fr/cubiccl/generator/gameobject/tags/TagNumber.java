package fr.cubiccl.generator.gameobject.tags;

public class TagNumber extends TagValue
{
	public static final int INTEGER = 0, BYTE = 1, SHORT = 2, FLOAT = 3, DOUBLE = 4;
	private static final String[] SUFFIX =
	{ "", "b", "s", "f", "d" };

	public final int type;

	/** Integer */
	public TagNumber(String id, String value)
	{
		this(id, value, INTEGER);
	}

	public TagNumber(String id, String value, int type)
	{
		super(id, value);
		this.type = type;
	}

	@Override
	public String value()
	{
		return super.value() + SUFFIX[this.type];
	}

}
