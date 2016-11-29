package fr.cubiccl.generator.utils;

public class MissingValueException extends CommandGenerationException
{
	private static final long serialVersionUID = 6375997214101477012L;

	public MissingValueException(Text valueName)
	{
		super(new Text("error.value.missing", new Replacement("<name>", valueName)));
	}

}
