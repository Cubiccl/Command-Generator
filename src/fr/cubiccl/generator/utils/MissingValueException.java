package fr.cubiccl.generator.utils;

public class MissingValueException extends CommandGenerationException
{
	private static final long serialVersionUID = 6375997214101477012L;

	private final String name;

	public MissingValueException(String valueName)
	{
		super(Lang.translate("error.value.missing"));
		this.name = valueName;
	}

	@Override
	public String getMessage()
	{
		return super.getMessage().replace("<name>", this.name);
	}

}
