package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import fr.cubiccl.generator.gui.component.combobox.OptionCombobox;
import fr.cubiccl.generator.gui.component.label.CLabel;
import fr.cubiccl.generator.gui.component.panel.CPanel;
import fr.cubiccl.generator.gui.component.textfield.CEntry;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Lang;
import fr.cubiccl.generator.utils.WrongValueException;

public class CommandTime extends Command implements ActionListener
{
	private OptionCombobox comboboxMode, comboboxQuery;
	private CEntry entryValue;

	public CommandTime()
	{
		super("time");
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		boolean query = this.comboboxMode.getValue().equals("query");
		this.entryValue.container.setVisible(!query);
		this.comboboxQuery.setVisible(query);
	}

	@Override
	public CPanel createGUI()
	{
		CPanel panel = new CPanel();
		GridBagConstraints gbc = panel.createGridBagLayout();

		++gbc.gridwidth;
		panel.add(this.labelDescription(), gbc);
		++gbc.gridy;
		--gbc.gridwidth;
		panel.add(new CLabel("general.mode").setHasColumn(true), gbc);
		++gbc.gridx;
		panel.add(this.comboboxMode = new OptionCombobox("time", "add", "set", "query"), gbc);
		--gbc.gridx;
		++gbc.gridy;
		++gbc.gridwidth;
		panel.add((this.entryValue = new CEntry("score.value", "0")).container, gbc);
		panel.add(this.comboboxQuery = new OptionCombobox("time.query", "daytime", "gametime", "day"), gbc);

		this.comboboxMode.addActionListener(this);
		this.comboboxQuery.setVisible(false);
		this.entryValue.addIntFilter();

		return panel;
	}

	@Override
	public String generate() throws CommandGenerationException
	{
		String command = "/time " + this.comboboxMode.getValue() + " ";
		String mode = this.comboboxMode.getValue();
		if (mode.equals("query")) return command + mode + " " + this.comboboxQuery.getValue();

		try
		{
			int i = Integer.parseInt(this.entryValue.getText());
			if (i < 0) throw new WrongValueException(this.entryValue.label.getAbsoluteText(), Lang.translate("error.integer.positive"), this.entryValue.getText());
		} catch (NumberFormatException e)
		{
			throw new WrongValueException(this.entryValue.label.getAbsoluteText(), Lang.translate("error.integer.positive"), this.entryValue.getText());
		}

		return command + " " + this.entryValue.getText();
	}

}
