package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import fr.cubiccl.generator.gui.component.combobox.OptionCombobox;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.textfield.CGEntry;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

public class CommandTime extends Command implements ActionListener
{
	private OptionCombobox comboboxMode, comboboxQuery;
	private CGEntry entryValue;

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
	public CGPanel createGUI()
	{
		CGPanel panel = new CGPanel();
		GridBagConstraints gbc = panel.createGridBagLayout();

		++gbc.gridwidth;
		panel.add(this.labelDescription(), gbc);
		++gbc.gridy;
		--gbc.gridwidth;
		panel.add(new CGLabel("general.mode").setHasColumn(true), gbc);
		++gbc.gridx;
		panel.add(this.comboboxMode = new OptionCombobox("time", "add", "set", "query"), gbc);
		--gbc.gridx;
		++gbc.gridy;
		++gbc.gridwidth;
		panel.add((this.entryValue = new CGEntry(Text.VALUE, "0", Text.INTEGER)).container, gbc);
		panel.add(this.comboboxQuery = new OptionCombobox("time.query", "daytime", "gametime", "day"), gbc);

		this.comboboxMode.addActionListener(this);
		this.comboboxQuery.setVisible(false);
		this.entryValue.addIntFilter();

		return panel;
	}

	@Override
	public String generate() throws CommandGenerationException
	{
		String command = this.id + " " + this.comboboxMode.getValue() + " ";
		String mode = this.comboboxMode.getValue();
		if (mode.equals("query")) return command + this.comboboxQuery.getValue();

		this.entryValue.checkValueSuperior(CGEntry.INTEGER, 0);

		return command + this.entryValue.getText();
	}

}
