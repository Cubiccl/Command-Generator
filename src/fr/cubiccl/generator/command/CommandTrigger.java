package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;

import fr.cubiccl.generator.gui.component.combobox.OptionCombobox;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.textfield.CGEntry;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.MissingValueException;
import fr.cubiccl.generator.utils.Text;
import fr.cubiccl.generator.utils.WrongValueException;

public class CommandTrigger extends Command
{
	private OptionCombobox comboboxMode;
	private CGEntry entryObjective, entryValue;

	public CommandTrigger()
	{
		super("trigger");
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
	public String generate() throws CommandGenerationException
	{
		if (this.entryObjective.getText().equals("")) throw new MissingValueException(this.entryObjective.label.getAbsoluteText());
		if (this.entryObjective.getText().contains(" ")) throw new WrongValueException(this.entryObjective.label.getAbsoluteText(), new Text("error.space"),
				this.entryObjective.getText());
		try
		{
			Integer.parseInt(this.entryValue.getText());
		} catch (NumberFormatException e)
		{
			throw new WrongValueException(this.entryValue.label.getAbsoluteText(), new Text("error.integer"), this.entryValue.getText());
		}
		return "/trigger " + this.entryObjective.getText() + " " + this.comboboxMode.getValue() + " " + this.entryValue.getText();
	}

}
