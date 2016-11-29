package fr.cubiccl.generator.utils;

public class Replacement
{
	public final String pattern;
	public final Text replacement;

	public Replacement(String pattern, String replacement)
	{
		super();
		this.pattern = pattern;
		this.replacement = new Text(replacement, false);
	}

	public Replacement(String pattern, Text replacement)
	{
		super();
		this.pattern = pattern;
		this.replacement = replacement;
	}

	public String apply(String text)
	{
		return text.replaceAll(this.pattern, this.replacement.toString());
	}

}