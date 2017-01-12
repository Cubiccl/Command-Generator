package fr.cubiccl.generator.utils;

import java.util.ArrayList;

public class Text
{
	public static final Text INTEGER = new Text("general.integer"), NUMBER = new Text("general.number"), OBJECTIVE = new Text("score.name"), VALUE = new Text(
			"score.value");

	public final boolean doTranslate;
	public final String id;
	private ArrayList<Replacement> replacements;

	public Text(String id, boolean doTranslate, Replacement... replacements)
	{
		this.id = id;
		this.doTranslate = doTranslate;
		this.replacements = new ArrayList<Replacement>();
		for (Replacement replacement : replacements)
			this.addReplacement(replacement);
	}

	public Text(String id, Replacement... replacements)
	{
		this(id, true, replacements);
	}

	public void addReplacement(Replacement replacement)
	{
		this.replacements.add(replacement);
	}

	@Override
	public String toString()
	{
		if (!this.doTranslate) return this.id;

		String output = Lang.translate(this.id);
		for (Replacement replacement : this.replacements)
			output = replacement.apply(output);

		return output;
	}
}
