package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;

import fr.cubiccl.generator.gui.component.combobox.OptionCombobox;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.textfield.CGEntry;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;
import fr.cubiccl.generator.utils.WrongValueException;

public class CommandWeather extends Command
{
	private OptionCombobox comboboxWeather;
	private CGEntry entryDuration;

	public CommandWeather()
	{
		super("weather");
	}

	@Override
	public CGPanel createGUI()
	{
		CGPanel panel = new CGPanel();
		GridBagConstraints gbc = panel.createGridBagLayout();

		++gbc.gridwidth;
		panel.add(this.labelDescription(), gbc);
		--gbc.gridwidth;
		++gbc.gridy;
		panel.add(new CGLabel("weather.select").setHasColumn(true), gbc);
		++gbc.gridx;
		panel.add(this.comboboxWeather = new OptionCombobox("weather", "clear", "rain", "thunder"), gbc);
		--gbc.gridx;
		++gbc.gridy;
		++gbc.gridwidth;
		panel.add((this.entryDuration = new CGEntry(new Text("weather.duration"), "0", Text.INTEGER)).container, gbc);

		this.entryDuration.addIntFilter();

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
			if (i < 0) throw new WrongValueException(this.entryDuration.label.getAbsoluteText(), new Text("error.integer.positive"),
					this.entryDuration.getText());
		} catch (NumberFormatException e)
		{
			throw new WrongValueException(this.entryDuration.label.getAbsoluteText(), new Text("error.integer.positive"), this.entryDuration.getText());
		}
		return command + " " + this.entryDuration.getText();
	}

}
