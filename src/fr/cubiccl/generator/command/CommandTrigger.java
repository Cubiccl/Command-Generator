package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;

import fr.cubiccl.generator.gui.component.combobox.OptionCombobox;
import fr.cubiccl.generator.gui.component.panel.CPanel;
import fr.cubiccl.generator.gui.component.textfield.CEntry;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Lang;
import fr.cubiccl.generator.utils.MissingValueException;
import fr.cubiccl.generator.utils.WrongValueException;

public class CommandTrigger extends Command
{
	private OptionCombobox comboboxMode;
	private CEntry entryObjective, entryValue;

	public CommandTrigger()
	{
		super("trigger");
	}

	@Override
	public CPanel createGUI()
	{
		CPanel panel = new CPanel();
		GridBagConstraints gbc = panel.createGridBagLayout();

		++gbc.gridwidth;
		panel.add(this.labelDescription(), gbc);
		++gbc.gridy;
		panel.add((this.entryObjective = new CEntry("score.name")).container, gbc);
		++gbc.gridy;
		panel.add(this.comboboxMode = new OptionCombobox("general.value", "add", "set"), gbc);
		++gbc.gridy;
		panel.add((this.entryValue = new CEntry("score.value", "0")).container, gbc);

		this.entryValue.addIntFilter();

		return panel;
	}

	@Override
	public String generate() throws CommandGenerationException
	{
		if (this.entryObjective.getText().equals("")) throw new MissingValueException(this.entryObjective.label.getAbsoluteText());
		if (this.entryObjective.getText().contains(" ")) throw new WrongValueException(this.entryObjective.label.getAbsoluteText(), Lang.translate("error.space"),
				this.entryObjective.getText());
		try
		{
			Integer.parseInt(this.entryValue.getText());
		} catch (NumberFormatException e)
		{
			throw new WrongValueException(this.entryValue.label.getAbsoluteText(), Lang.translate("error.integer"), this.entryValue.getText());
		}
		return "/trigger " + this.entryObjective.getText() + " " + this.comboboxMode.getValue() + " " + this.entryValue.getText();
	}

}
