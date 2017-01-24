package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;

import fr.cubiccl.generator.gui.component.combobox.OptionCombobox;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.textfield.CGEntry;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

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
		this.entryObjective.checkValue(CGEntry.STRING);
		this.entryValue.checkValue(CGEntry.INTEGER);
		return this.id + " " + this.entryObjective.getText() + " " + this.comboboxMode.getValue() + " " + this.entryValue.getText();
	}

}
