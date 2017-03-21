package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import fr.cubiccl.generator.gui.component.combobox.OptionCombobox;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.textfield.CGEntry;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;
import fr.cubiccl.generator.utils.Utils;

public class CommandWorldborder extends Command implements ActionListener, KeyListener
{
	private OptionCombobox comboboxMode, comboboxMode2;
	private CGEntry entryValue, entryValue2;

	public CommandWorldborder()
	{
		super("worldborder", "worldborder <add|set> <distance> [time]\n" + "worldborder center <x> <y>\n" + "worldborder get\n"
				+ "worldborder damage <amount|buffer> <value>\n" + "worldborder warning <distance|time> <value>", 2, 3, 4);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.comboboxMode || e.getSource() == this.comboboxMode2)
		{
			this.finishReading();
		}
		this.updateTranslations();
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

		this.entryValue.addNumberFilter();
		this.entryValue2.addNumberFilter();
		this.entryValue.addKeyListener(this);
		this.entryValue2.addKeyListener(this);

		return panel;
	}

	@Override
	protected Text description()
	{
		String value = this.entryValue.getText(), value2 = this.entryValue.getText();
		try
		{
			Utils.checkInteger(null, value);
		} catch (Exception e)
		{
			value = "???";
		}
		try
		{
			Utils.checkInteger(null, value2);
		} catch (Exception e)
		{
			value2 = "???";
		}
		if (!this.comboboxMode.getValue().equals("set")) return new Text("command." + this.id + "." + this.comboboxMode.getValue()).addReplacement("<value>",
				value).addReplacement("<value2>", value2);
		return this.defaultDescription().addReplacement("<value>", value);
	}

	@Override
	protected void finishReading()
	{
		String mode = this.comboboxMode.getValue();
		this.entryValue.container.setVisible(!mode.equals("get"));
		boolean value2 = mode.equals("center") || mode.equals("add") || mode.equals("set"), mode2 = mode.equals("damage") || mode.equals("warning");
		if (!mode.equals("get") && !mode2) this.entryValue.label.setTextID(new Text("worldborder." + mode));
		this.entryValue2.container.setVisible(value2);
		if (value2) this.entryValue2.label.setTextID(new Text("worldborder." + mode + "2"));
		this.comboboxMode2.setVisible(mode2);
		if (mode2)
		{
			if (mode.equals("damage")) this.comboboxMode2.setOptions("worldborder.mode", "amount", "buffer");
			else if (mode.equals("warning")) this.comboboxMode2.setOptions("worldborder.mode", "distance", "time");
			this.comboboxMode2.setSelectedIndex(this.comboboxMode2.getSelectedIndex());
			this.entryValue.label.setTextID(new Text("worldborder." + this.comboboxMode.getValue() + "." + this.comboboxMode2.getValue()));
		}

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

	@Override
	public void keyPressed(KeyEvent e)
	{}

	@Override
	public void keyReleased(KeyEvent e)
	{
		this.updateTranslations();
	}

	@Override
	public void keyTyped(KeyEvent e)
	{}

	@Override
	protected void readArgument(int index, String argument, String[] fullCommand) throws CommandGenerationException
	{
		// worldborder <add|set> <distance> [time]
		// worldborder center <x> <y>
		// worldborder get
		// worldborder damage <amount|buffer> <value>
		// worldborder warning <distance|time> <value>
		if (index == 1) this.comboboxMode.setValue(argument);
		if (index == 2) if (this.comboboxMode.getValue().equals("center")) try
		{
			Float.parseFloat(argument);
			this.entryValue.setText(argument);
		} catch (Exception e)
		{}
		else this.comboboxMode2.setValue(argument);
		if (index == 3) try
		{
			Float.parseFloat(argument);
			String mode = this.comboboxMode.getValue();
			if (mode.equals("center") || mode.equals("add") || mode.equals("set")) this.entryValue2.setText(argument);
			else this.entryValue.setText(argument);
		} catch (Exception e)
		{}
	}
}
