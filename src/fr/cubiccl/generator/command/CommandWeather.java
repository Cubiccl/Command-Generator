package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;

import fr.cubiccl.generator.gui.component.combobox.OptionCombobox;
import fr.cubiccl.generator.gui.component.label.CLabel;
import fr.cubiccl.generator.gui.component.panel.CPanel;
import fr.cubiccl.generator.gui.component.textfield.CEntry;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Lang;
import fr.cubiccl.generator.utils.WrongValueException;

public class CommandWeather extends Command
{
	private OptionCombobox comboboxWeather;
	private CEntry entryDuration;

	public CommandWeather()
	{
		super("weather");
	}

	@Override
	public CPanel createGUI()
	{
		CPanel panel = new CPanel();
		GridBagConstraints gbc = panel.createGridBagLayout();

		++gbc.gridwidth;
		panel.add(this.labelDescription(), gbc);
		--gbc.gridwidth;
		++gbc.gridy;
		panel.add(new CLabel("weather.select").setHasColumn(true), gbc);
		++gbc.gridx;
		panel.add(this.comboboxWeather = new OptionCombobox("weather", "clear", "rain", "thunder"), gbc);
		--gbc.gridx;
		++gbc.gridy;
		++gbc.gridwidth;
		panel.add((this.entryDuration = new CEntry("weather.duration")).container, gbc);

		return panel;
	}

	@Override
	public String generate() throws CommandGenerationException
	{
		String command = "/weather " + this.comboboxWeather.getValue();
		if (this.entryDuration.getText().equals("")) return command;

		try
		{
			int i = Integer.parseInt(this.entryDuration.getText());
			if (i < 0) throw new WrongValueException(this.entryDuration.label.getAbsoluteText(), Lang.translate("error.integer.positive"), this.entryDuration.getText());
		} catch (NumberFormatException e)
		{
			throw new WrongValueException(this.entryDuration.label.getAbsoluteText(), Lang.translate("error.integer.positive"), this.entryDuration.getText());
		}
		return command + " " + this.entryDuration.getText();
	}

}
