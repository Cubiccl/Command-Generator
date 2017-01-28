package fr.cubiccl.generator.gui.component.panel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.command.Command;
import fr.cubiccl.generator.command.Commands;
import fr.cubiccl.generator.gui.component.button.CGButton;
import fr.cubiccl.generator.gui.component.combobox.CGComboBox;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.utils.Text;

public class PanelCommandSelection extends CGPanel implements ActionListener
{
	public static final int HEIGHT = 60;
	private static final long serialVersionUID = 9144607045203777459L;

	public CGButton buttonCancelExecute;
	private CGButton buttonGenerate;
	private CGComboBox comboboxCommands;
	private CGLabel labelCommand;

	public PanelCommandSelection()
	{

		this.labelCommand = new CGLabel(new Text("command.select")).setHasColumn(true);

		this.comboboxCommands = new CGComboBox();
		this.comboboxCommands.addActionListener(this);

		this.buttonGenerate = new CGButton("command.generate");
		this.buttonGenerate.addActionListener(this);
		this.buttonGenerate.setFont(this.buttonGenerate.getFont().deriveFont(Font.BOLD, 20));
		this.buttonCancelExecute = new CGButton("command.cancel_execute");
		this.buttonCancelExecute.addActionListener(this);
		this.buttonCancelExecute.setVisible(false);
		this.createLayout();
		this.onVersionChange();
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.buttonGenerate) CommandGenerator.generate();
		if (e.getSource() == this.buttonCancelExecute) CommandGenerator.cancelExecute();
		if (e.getSource() == this.comboboxCommands) CommandGenerator.setSelected(Commands.getCommandFromID(this.comboboxCommands.getValue()));
	}

	private void createLayout()
	{
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(5, 5, 5, 5);

		this.add(this.labelCommand, gbc);
		++gbc.gridx;
		this.add(this.comboboxCommands, gbc);
		++gbc.gridx;
		this.add(this.buttonGenerate, gbc);
		++gbc.gridx;
		this.add(this.buttonCancelExecute, gbc);
	}

	public void onVersionChange()
	{
		String previous = this.comboboxCommands.getValue();
		Command[] commands = Commands.getCommands();
		String[] names = new String[commands.length];
		for (int i = 0; i < names.length; ++i)
			names[i] = commands[i].id;
		this.comboboxCommands.setValues(names);
		if (previous != null) this.comboboxCommands.setSelectedItem(previous);
	}

	@Override
	public void setEnabled(boolean enabled)
	{
		super.setEnabled(enabled);
		this.comboboxCommands.setEnabled(enabled);
		this.buttonGenerate.setEnabled(enabled);
		this.buttonCancelExecute.setEnabled(enabled);
		this.labelCommand.setForeground(enabled ? Color.BLACK : Color.GRAY);
	}

}
