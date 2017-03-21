package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;

import fr.cubiccl.generator.gui.component.combobox.OptionCombobox;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.textfield.CGEntry;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;
import fr.cubiccl.generator.utils.Utils;

public class CommandTrigger extends Command
{
	private OptionCombobox comboboxMode;
	private CGEntry entryObjective, entryValue;

	public CommandTrigger()
	{
		super("trigger", "trigger <objective> <add|set> <value>", 4);
	}

	@Override
	public CGPanel createGUI()
	{
		CGPanel panel = new CGPanel();
		GridBagConstraints gbc = panel.createGridBagLayout();

		++gbc.gridwidth;
		panel.add(this.labelDescription(), gbc);
		++gbc.gridy;
		panel.add((this.entryObjective = new CGEntry(Text.OBJECTIVE, Text.OBJECTIVE)).container, gbc);
		++gbc.gridy;
		panel.add(this.comboboxMode = new OptionCombobox("general.value", "add", "set"), gbc);
		++gbc.gridy;
		panel.add((this.entryValue = new CGEntry(Text.VALUE, "0", Text.INTEGER)).container, gbc);

		this.entryValue.addIntFilter();

		return panel;
	}

	@Override
	protected Text description()
	{
		String obj = this.entryObjective.getText(), value = this.entryValue.getText();

		try
		{
			Utils.checkStringId(null, obj);
		} catch (CommandGenerationException e)
		{
			obj = "???";
		}
		try
		{
			Utils.checkInteger(null, value);
		} catch (CommandGenerationException e)
		{
			value = "???";
		}

		if (this.comboboxMode.getValue().equals("add")) return new Text("command." + this.id + ".add").addReplacement("<value>", value).addReplacement(
				"<objective>", obj);
		return this.defaultDescription().addReplacement("<value>", value).addReplacement("<objective>", obj);
	}

	@Override
	public String generate() throws CommandGenerationException
	{
		this.entryObjective.checkValue(CGEntry.STRING);
		this.entryValue.checkValue(CGEntry.INTEGER);
		return this.id + " " + this.entryObjective.getText() + " " + this.comboboxMode.getValue() + " " + this.entryValue.getText();
	}

	@Override
	protected void readArgument(int index, String argument, String[] fullCommand) throws CommandGenerationException
	{
		// trigger <objective> <add|set> <value>
		if (index == 1) this.entryObjective.setText(argument);
		if (index == 2) this.comboboxMode.setValue(argument);
		if (index == 3) try
		{
			Integer.parseInt(argument);
			this.entryValue.setText(argument);
		} catch (Exception e)
		{}
	}
}
