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

public class CommandTime extends Command implements ActionListener
{
	private OptionCombobox comboboxMode, comboboxQuery;
	private CGEntry entryValue;

	public CommandTime()
	{
		super("time", "time <add|query|set> <value>", 3);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		this.onParsingEnd();
		this.updateTranslations();
	}

	@Override
	public CGPanel createUI()
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
		this.entryValue.addKeyListener(new KeyListener()
		{
			@Override
			public void keyPressed(KeyEvent e)
			{}

			@Override
			public void keyReleased(KeyEvent e)
			{
				updateTranslations();
			}

			@Override
			public void keyTyped(KeyEvent e)
			{}
		});

		return panel;
	}

	@Override
	protected Text description()
	{
		if (this.comboboxMode.getValue().equals("query")) return new Text("command." + this.id + ".query");
		if (this.comboboxMode.getValue().equals("set")) try
		{
			Integer.parseInt(this.entryValue.getText());
			return new Text("command." + this.id + ".set").addReplacement("<time>", this.entryValue.getText());
		} catch (Exception e)
		{
			return new Text("command." + this.id + ".set").addReplacement("<time>", "???");
		}
		try
		{
			Integer.parseInt(this.entryValue.getText());
			return this.defaultDescription().addReplacement("<time>", this.entryValue.getText());
		} catch (Exception e)
		{
			return this.defaultDescription().addReplacement("<time>", "???");
		}
	}

	@Override
	protected void onParsingEnd()
	{
		boolean query = this.comboboxMode.getValue().equals("query");
		this.entryValue.container.setVisible(!query);
		this.comboboxQuery.setVisible(query);
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

	@Override
	protected void readArgument(int index, String argument, String[] fullCommand) throws CommandGenerationException
	{
		// time <add|query|set> <value>
		if (index == 1) this.comboboxMode.setValue(argument);
		if (index == 2) if (this.comboboxMode.getValue().equals("query")) this.comboboxQuery.setValue(argument);
		else try
		{
			Integer.parseInt(argument);
			this.entryValue.setText(argument);
		} catch (Exception e)
		{}
	}

}
