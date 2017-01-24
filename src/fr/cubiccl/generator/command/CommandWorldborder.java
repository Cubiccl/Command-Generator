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

public class CommandWorldborder extends Command implements ActionListener
{
	private OptionCombobox comboboxMode, comboboxMode2;
	private CGEntry entryValue, entryValue2;

	public CommandWorldborder()
	{
		super("worldborder");
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.comboboxMode)
		{
			String mode = this.comboboxMode.getValue();
			this.entryValue.container.setVisible(!mode.equals("get"));
			this.entryValue.label.setTextID(new Text("worldborder." + mode));
			boolean value2 = mode.equals("center") || mode.equals("add") || mode.equals("set"), mode2 = mode.equals("damage") || mode.equals("warning");
			this.entryValue2.container.setVisible(value2);
			if (value2) this.entryValue2.label.setTextID(new Text("worldborder." + mode + "2"));
			this.comboboxMode2.setVisible(mode2);
			if (mode2)
			{
				if (mode.equals("damage")) this.comboboxMode2.setOptions("worldborder.mode", "amount", "buffer");
				else if (mode.equals("warning")) this.comboboxMode2.setOptions("worldborder.mode", "distance", "time");
				this.comboboxMode2.setSelectedIndex(this.comboboxMode2.getSelectedIndex());
			}
		} else if (e.getSource() == this.comboboxMode2)
		{
			this.entryValue.label.setTextID(new Text("worldborder." + this.comboboxMode.getValue() + "." + this.comboboxMode2.getValue()));
		}
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
		panel.add(this.comboboxMode = new OptionCombobox("worldborder.mode", "add", "center", "damage", "get", "set", "warning"), gbc);
		--gbc.gridx;
		++gbc.gridy;
		++gbc.gridwidth;
		panel.add(this.comboboxMode2 = new OptionCombobox("worldborder.mode", "amount", "buffer"), gbc);
		++gbc.gridy;
		panel.add((this.entryValue = new CGEntry(new Text("worldborder.add"), "0", Text.INTEGER)).container, gbc);
		++gbc.gridy;
		panel.add((this.entryValue2 = new CGEntry(new Text("worldborder.add2"), "0", Text.INTEGER)).container, gbc);

		this.comboboxMode.addActionListener(this);
		this.comboboxMode2.addActionListener(this);
		this.comboboxMode2.setVisible(false);

		this.entryValue.addIntFilter();
		this.entryValue2.addIntFilter();

		return panel;
	}

	@Override
	public String generate() throws CommandGenerationException
	{
		String mode = this.comboboxMode.getValue(), mode2 = this.comboboxMode2.getValue(), value = this.entryValue.getText(), value2 = this.entryValue2
				.getText();
		String command = this.id + " " + mode + " ";

		switch (mode)
		{
			case "add":
			case "set":
				if (mode.equals("set")) this.entryValue.checkValueSuperior(CGEntry.INTEGER, 0);
				else this.entryValue.checkValue(CGEntry.INTEGER);

				boolean time = !value2.equals("");
				if (time) this.entryValue2.checkValue(CGEntry.INTEGER);

				return command + value + (time ? (" " + value2) : "");

			case "center":
				this.entryValue.checkValue(CGEntry.INTEGER);
				this.entryValue2.checkValue(CGEntry.INTEGER);
				return command + value + " " + value2;

			case "damage":
			case "warning":
				boolean isFloat = mode.equals("damage") && mode2.equals("amount");
				if (isFloat) this.entryValue.checkValueSuperior(CGEntry.FLOAT, 0);
				else this.entryValue.checkValueSuperior(CGEntry.INTEGER, 0);
				return command + mode2 + " " + value;

			default:
				return command;
		}
	}
}
