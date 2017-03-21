package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import fr.cubiccl.generator.gui.component.button.CGCheckBox;
import fr.cubiccl.generator.gui.component.combobox.OptionCombobox;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.textfield.CGEntry;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

public class CommandWeather extends Command implements ActionListener
{
	private CGCheckBox checkboxRandomDuration;
	private OptionCombobox comboboxWeather;
	private CGEntry entryDuration;

	public CommandWeather()
	{
		super("weather", "weather <weather> [duration]", 2, 3);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		this.finishReading();
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
		++gbc.gridy;
		panel.add(this.checkboxRandomDuration = new CGCheckBox("weather.duration.random"), gbc);

		this.entryDuration.addIntFilter();
		this.checkboxRandomDuration.addActionListener(this);

		return panel;
	}

	@Override
	protected void defaultGui()
	{
		this.checkboxRandomDuration.setSelected(false);
	}

	@Override
	protected Text description()
	{
		return this.defaultDescription().addReplacement("<weather>", new Text("weather." + this.comboboxWeather.getValue()));
	}

	@Override
	protected void finishReading()
	{
		this.entryDuration.container.setVisible(!this.checkboxRandomDuration.isSelected());
	}

	@Override
	public String generate() throws CommandGenerationException
	{
		String command = this.id + " " + this.comboboxWeather.getValue();
		if (this.entryDuration.getText().equals("")) return command;

		this.entryDuration.checkValueSuperior(CGEntry.INTEGER, 0);
		return command + (this.checkboxRandomDuration.isSelected() ? "" : " " + this.entryDuration.getText());
	}

	@Override
	protected void readArgument(int index, String argument, String[] fullCommand) throws CommandGenerationException
	{
		if (index == 1)
		{
			this.checkboxRandomDuration.setSelected(true);
			this.comboboxWeather.setValue(argument);
		}
		if (index == 2) try
		{
			Integer.parseInt(argument);
			this.entryDuration.setText(argument);
			this.checkboxRandomDuration.setSelected(false);
		} catch (Exception e)
		{}
	}
}
