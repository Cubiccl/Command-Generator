package fr.cubiccl.generator.command;

import java.util.ArrayList;

import javax.swing.BorderFactory;

import fr.cubiccl.generator.gui.component.interfaces.ITranslated;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Replacement;
import fr.cubiccl.generator.utils.Text;

public abstract class Command implements Comparable<Command>, ITranslated
{
	/** This Command's ID. Always at the start of the Command. */
	public final String id;
	/** Description label at the top of the UI. */
	private CGLabel labelDescription;
	/** Valid lengths for a generated Command. Negative values mean that any superior value is valid (-4 -> 4 or higher). */
	private final int[] lengths;
	/** This Command's UI. */
	private CGPanel panel;
	/** The Structure of this Command. */
	public final String structure;

	public Command(String id, String structure, int... lengths)
	{
		this.id = id;
		this.structure = structure;
		this.lengths = lengths;
		Commands.registerCommand(this);
	}

	protected void checkLength(String[] command, int... possibleLengths) throws CommandGenerationException
	{
		for (int i : possibleLengths)
			if (command.length == i) return;
		this.incorrectStructureError();
	}

	protected void checkLengthMinimum(String[] command, int minimumLength) throws CommandGenerationException
	{
		if (command.length < minimumLength) this.incorrectStructureError();
	}

	@Override
	public int compareTo(Command o)
	{
		return this.id.compareTo(o.id);
	}

	/** Creates this Command's UI.
	 * 
	 * @return The Panel containing the UI. */
	protected abstract CGPanel createUI();

	/** @return The Default description for this Command. */
	protected Text defaultDescription()
	{
		return new Text("command." + this.id);
	}

	/** @return The current description to display. Depends on the current state of the UI. */
	protected abstract Text description();

	/** Generates the Command. */
	public abstract String generate() throws CommandGenerationException;

	/** @return The UI for this Command. */
	public CGPanel getUI()
	{
		if (this.panel == null)
		{
			this.panel = this.createUI();
			this.resetUI();
		}
		this.updateTranslations();
		return this.panel;
	}

	/** Throws an incorrect structure error. */
	protected void incorrectStructureError() throws CommandGenerationException
	{
		throw new CommandGenerationException(new Text("error.command.structure", new Replacement("<structure>", "\n" + this.structure)));
	}

	/** @return The Label to use for the description. */
	protected CGLabel labelDescription()
	{
		this.labelDescription = new CGLabel("command." + this.id);
		this.labelDescription.setBorder(BorderFactory.createRaisedBevelBorder());
		return this.labelDescription;
	}

	/** Called at the end of Command parsing. */
	protected void onParsingEnd()
	{}

	/** This method is called when parsing a Command, for each argument. Should read the argument and load the part corresponding to it.
	 * 
	 * @param index - The current index in the Command.
	 * @param argument - The argument to read.
	 * @param fullCommand - The full Command, in case the current argument requires more information.
	 * @throws CommandGenerationException if the argument is invalid. */
	protected abstract void readArgument(int index, String argument, String[] fullCommand) throws CommandGenerationException;

	/** Resets the UI to its default state. Called after creating the UI and before parsing a Command. */
	protected void resetUI()
	{}

	/** Changes the UI so it matches the input Command.
	 * 
	 * @param command - The command to match.
	 * @return This Command's UI.
	 * @throws CommandGenerationException if the Command couldn't be parsed. */
	public CGPanel setupFrom(String command) throws CommandGenerationException
	{
		if (command.startsWith("/")) command = command.substring(1);
		if (!command.contains(" ")) this.incorrectStructureError();

		CGPanel p = this.getUI();
		String[] args = this.splitCommand(command.substring(this.id.length() + 1));

		if (this.lengths[0] < 0) this.checkLengthMinimum(args, -this.lengths[0]);
		else this.checkLength(args, this.lengths);

		this.resetUI();
		for (int i = 1; i < args.length; ++i)
			this.readArgument(i, args[i], args);
		this.onParsingEnd();

		return p;
	}

	/** Splits the Command into each argument.
	 * 
	 * @param command - The command to split.
	 * @return An array containing each part of the Command. */
	protected String[] splitCommand(String command)
	{
		String[] split = command.split(" ");
		ArrayList<String> values = new ArrayList<String>();
		values.add(this.id);
		boolean inTag = false;
		String current = "";
		int count = 1, l = this.lengths[0] < 0 ? -this.lengths[0] : -1;

		for (String string : split)
		{
			++count;
			if (string.startsWith("{") && !string.endsWith("}")) inTag = true;

			if (inTag || (l != -1 && count >= l)) current += current.equals("") ? string : " " + string;
			else values.add(string);

			if (!string.startsWith("{") && string.endsWith("}"))
			{
				inTag = false;
				values.add(current);
				current = "";
			}
		}

		if (l != -1) values.add(current);
		return values.toArray(new String[values.size()]);
	}

	@Override
	public void updateTranslations()
	{
		if (this.labelDescription != null) this.labelDescription.setTextID(this.description());
	}
}
