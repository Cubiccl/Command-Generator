package fr.cubiccl.generator.utils;

public class CommandGenerationException extends Exception
{
	private static final long serialVersionUID = -7627633010587931222L;

	private final String message;

	public CommandGenerationException(String message)
	{
		this.message = message;
	}

	@Override
	public String getMessage()
	{
		return this.message;
	}

}
