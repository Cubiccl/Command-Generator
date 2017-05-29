package fr.cubiccl.generator.utils;

/** Replaces the content of a translation. */
public class Replacement
{
	/** The pattern to locate. */
	public final String pattern;
	/** What to replace with. */
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

	/** Applies this Replacement.
	 * 
	 * @param text - The text to apply to.
	 * @return The resulting text. */
	public String apply(String text)
	{
		return text.replaceAll(this.pattern, this.replacement.toString());
	}

}