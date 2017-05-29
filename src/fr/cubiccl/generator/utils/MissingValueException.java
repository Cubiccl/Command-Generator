package fr.cubiccl.generator.utils;

/** Exception thrown when the user didn't input required data. */
public class MissingValueException extends CommandGenerationException
{
	private static final long serialVersionUID = 6375997214101477012L;

	/** @param valueName - The name of the missing value. */
	public MissingValueException(Text valueName)
	{
		super(new Text("error.value.missing", new Replacement("<name>", valueName)));
	}

}
