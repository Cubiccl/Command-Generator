package fr.cubiccl.generator.command;

import java.util.ArrayList;

import javax.swing.BorderFactory;

import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Replacement;
import fr.cubiccl.generator.utils.Text;

public abstract class Command
{
	public final String id, structure;
	private CGLabel labelDescription;
	private final int[] lengths;
	private CGPanel panel;

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

	protected abstract CGPanel createGUI();

	protected Text defaultDescription()
	{
		return new Text("command." + this.id);
	}

	/** Called when creating the GUI. Also called before the first argument is read. */
	protected void defaultGui()
	{}

	protected abstract Text description();

	/** Called after the last argument is read. */
	protected void finishReading()
	{}

	public abstract String generate() throws CommandGenerationException;

	public CGPanel getGUI()
	{
		if (this.panel == null)
		{
			this.panel = this.createGUI();
			this.defaultGui();
		}
		return this.panel;
	}

	protected void incorrectStructureError() throws CommandGenerationException
	{
		throw new CommandGenerationException(new Text("error.command.structure", new Replacement("<structure>", "\n" + this.structure)));
	}

	protected CGLabel labelDescription()
	{
		this.labelDescription = new CGLabel("command." + this.id);
		this.labelDescription.setBorder(BorderFactory.createRaisedBevelBorder());
		this.updateDescription();
		return this.labelDescription;
	}

	/** This method is called when loading a Command, for each argument. Should read the argument and load the part corresponding to it.
	 * 
	 * @param index - The current index in the Command.
	 * @param argument - The argument to read.
	 * @param fullCommand - The full Command, in case the current argument requires more information.
	 * @throws CommandGenerationException if the argument is invalid. */
	protected abstract void readArgument(int index, String argument, String[] fullCommand) throws CommandGenerationException;

	public CGPanel setupFrom(String command) throws CommandGenerationException
	{
		if (command.startsWith("/")) command = command.substring(1);
		if (!command.contains(" ")) this.incorrectStructureError();

		CGPanel p = this.getGUI();
		String[] args = this.splitCommand(command.substring(this.id.length() + 1));

		if (this.lengths[0] < 0) this.checkLengthMinimum(args, -this.lengths[0]);
		else this.checkLength(args, this.lengths);

		this.defaultGui();
		for (int i = 1; i < args.length; ++i)
			this.readArgument(i, args[i], args);
		this.finishReading();

		return p;
	}

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

	protected void updateDescription()
	{
		this.labelDescription.setTextID(this.description());
	}
}
