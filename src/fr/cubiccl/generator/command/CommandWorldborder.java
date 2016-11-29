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
import fr.cubiccl.generator.utils.WrongValueException;

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
		panel.add((this.entryValue = new CGEntry("worldborder.add", "0")).container, gbc);
		++gbc.gridy;
		panel.add((this.entryValue2 = new CGEntry("worldborder.add2", "0")).container, gbc);

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
		Text name = this.entryValue.label.getAbsoluteText(), name2 = this.entryValue2.label.getAbsoluteText();
		String command = "/worldborder " + mode + " ";

		switch (mode)
		{
			case "add":
			case "set":
				try
				{
					int i = Integer.parseInt(value);
					if (mode.equals("set") && i < 0) throw new WrongValueException(name, new Text("error.integer.positive"), value);
				} catch (NumberFormatException e)
				{
					throw new WrongValueException(name, new Text("error.integer"), value);
				}

				boolean time = !value2.equals("");
				if (time) try
				{
					int i = Integer.parseInt(value2);
					if (i < 0) throw new WrongValueException(name2, new Text("error.integer.positive"), value2);
				} catch (NumberFormatException e)
				{
					throw new WrongValueException(name2, new Text("error.integer.positive"), value2);
				}

				return command + value + (time ? (" " + value2) : "");

			case "center":
				try
				{
					Integer.parseInt(value);
				} catch (NumberFormatException e)
				{
					throw new WrongValueException(name, new Text("error.integer"), value);
				}
				try
				{
					Integer.parseInt(value2);
				} catch (NumberFormatException e)
				{
					throw new WrongValueException(name2, new Text("error.integer"), value2);
				}
				return command + value + " " + value2;

			case "damage":
			case "warning":
				boolean isFloat = mode.equals("damage") && mode2.equals("amount");
				try
				{
					float f;
					if (isFloat) f = Float.parseFloat(value);
					else f = Integer.parseInt(value2);
					if (f < 0)
					{
						if (isFloat) throw new WrongValueException(name, new Text("error.number.positive"), value);
						throw new WrongValueException(name2, new Text("error.integer.positive"), value2);
					}
				} catch (NumberFormatException e)
				{
					if (isFloat) throw new WrongValueException(name, new Text("error.number.positive"), value);
					throw new WrongValueException(name2, new Text("error.integer.positive"), value2);
				}
				return command + mode2 + " " + value;

			default:
				return command;
		}
	}
}
