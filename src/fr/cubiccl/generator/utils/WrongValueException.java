package fr.cubiccl.generator.utils;

public class WrongValueException extends CommandGenerationException
{
	private static final long serialVersionUID = 6375997214101477012L;

	public WrongValueException(Text valueName, Text errorMessage, String value)
	{
		super(errorMessage);

		this.message.addReplacement(new Replacement("<name>", valueName));
		this.message.addReplacement(new Replacement("XXX", new Text(value, false)));
	}
}
