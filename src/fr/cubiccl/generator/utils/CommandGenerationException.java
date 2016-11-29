package fr.cubiccl.generator.utils;

public class CommandGenerationException extends Exception
{
	private static final long serialVersionUID = -7627633010587931222L;

	protected final Text message;

	public CommandGenerationException(Text message)
	{
		this.message = message;
	}

	@Override
	public String getMessage()
	{
		return this.message.toString();
	}

}
