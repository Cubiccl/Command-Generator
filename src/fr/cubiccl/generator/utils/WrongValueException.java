package fr.cubiccl.generator.utils;

public class WrongValueException extends CommandGenerationException
{
	private static final long serialVersionUID = 6375997214101477012L;

	private final String value, name;

	public WrongValueException(String valueName, String errorMessage, String value)
	{
		super(errorMessage);
		this.value = value;
		this.name = valueName;
	}

	@Override
	public String getMessage()
	{
		return super.getMessage().replace("<name>", this.name).replace("XXX", this.value);
	}

}
