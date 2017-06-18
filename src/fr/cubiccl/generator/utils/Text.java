package fr.cubiccl.generator.utils;

import java.awt.Component;
import java.util.ArrayList;

import fr.cubiccl.generator.gui.component.interfaces.IObjectList;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.utils.EntryPanel;
import fr.cubiccl.generator.gui.component.panel.utils.ListProperties;

/** Some text to display to the user. */
public class Text implements IObjectList<Text>
{
	public static final Text INTEGER = new Text("general.integer"), NUMBER = new Text("general.number"), OBJECTIVE = new Text("score.name"), VALUE = new Text(
			"score.value");

	/** <code>true</code> if this Text should be translated. */
	public boolean doTranslate;
	/** The ID to translate. Can also be the actual text if {@link Text#doTranslate} is false. */
	public String id;
	/** The Replacements to apply to the translated text, in order. */
	private ArrayList<Replacement> replacements;

	public Text()
	{
		this("", false);
	}

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

	/** Adds a Replacement. */
	public Text addReplacement(Replacement replacement)
	{
		this.replacements.add(replacement);
		return this;
	}

	/** Adds a Replacement. */
	public Text addReplacement(String pattern, String replacement)
	{
		this.addReplacement(new Replacement(pattern, replacement));
		return this;
	}

	/** Adds a Replacement. */
	public Text addReplacement(String pattern, Text replacement)
	{
		this.addReplacement(new Replacement(pattern, replacement));
		return this;
	}

	@Override
	public CGPanel createPanel(ListProperties properties)
	{
		Object desc = properties.get("description");
		EntryPanel p;
		if (desc instanceof String) p = new EntryPanel((String) desc);
		else p = new EntryPanel((Text) desc);
		p.entry.setText(this.id);
		return p;
	}

	@Override
	public Text duplicate(Text object)
	{
		this.doTranslate = object.doTranslate;
		this.id = object.id;
		this.replacements.clear();
		for (Replacement replacement : object.replacements)
			this.replacements.add(replacement.duplicate());
		return this;
	}

	@Override
	public Component getDisplayComponent()
	{
		Text t;
		if (this.toString().length() <= 40) t = this;
		else t = new Text(this.toString().substring(0, 37) + "...", false);
		return new CGLabel(t);
	}

	@Override
	public String getName(int index)
	{
		return this.toString().length() > 20 ? this.toString().substring(0, 17) + "..." : this.toString();
	}

	/** @param pattern - The pattern to locate.
	 * @return <code>true</code> if there is already a Replacement looking for the input <code>pattern</code>. */
	public boolean hasReplacement(String pattern)
	{
		for (Replacement replacement : this.replacements)
			if (replacement.pattern.equals(pattern)) return true;
		return false;
	}

	/** @return True if the translation files contain this text's ID and if each of the Replacements are translated as well. */
	public boolean isTranslated()
	{
		for (Replacement r : this.replacements)
			if (!r.replacement.isTranslated()) return false;
		return (!this.doTranslate || Lang.keyExists(this.id));
	}

	/** Removes all Replacements that replace the input <code>pattern</code>.
	 * 
	 * @param pattern - The pattern to locate. */
	public void removeReplacements(String pattern)
	{
		ArrayList<Replacement> r = new ArrayList<Replacement>();
		for (Replacement replacement : this.replacements)
			if (replacement.pattern.equals(pattern)) r.add(replacement);

		for (Replacement replacement : r)
			this.replacements.remove(replacement);
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

	@Override
	public Text update(CGPanel panel) throws CommandGenerationException
	{
		this.id = ((EntryPanel) panel).entry.getText();
		return this;
	}
}
