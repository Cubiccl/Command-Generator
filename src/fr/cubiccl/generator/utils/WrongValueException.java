package fr.cubiccl.generator.utils;

/** Exception thrown when the user input a wrong value. */
public class WrongValueException extends CommandGenerationException
{
	private static final long serialVersionUID = 6375997214101477012L;

	/** @param valueName - The name of the value.
	 * @param errorMessage - The error message.
	 * @param value - The wrong value. */
	public WrongValueException(Text valueName, Text errorMessage, String value)
	{
		super(errorMessage);

		this.message.addReplacement(new Replacement("<name>", valueName));
		this.message.addReplacement(new Replacement("XXX", new Text(value, false)));
	}
}
