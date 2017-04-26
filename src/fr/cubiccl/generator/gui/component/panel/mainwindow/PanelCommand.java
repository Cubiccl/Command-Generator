package fr.cubiccl.generator.gui.component.panel.mainwindow;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;

import fr.cubiccl.generator.gui.component.button.CGButton;
import fr.cubiccl.generator.gui.component.button.CGCheckBox;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.textfield.CGTextField;

public class PanelCommand extends CGPanel implements ActionListener
{
	public static final int HEIGHT = 60;
	private static final long serialVersionUID = -5645072079663496893L;

	private CGButton buttonCopy;
	private CGCheckBox checkboxEdit;
	private CGLabel labelCommand;
	public final CGTextField textfieldCommand;

	public PanelCommand()
	{
		this.setBorder(BorderFactory.createRaisedBevelBorder());

		this.labelCommand = new CGLabel("command.label").setHasColumn(true);

		this.textfieldCommand = new CGTextField("command.output");
		this.textfieldCommand.setEditable(false);

		this.checkboxEdit = new CGCheckBox("command.edit");
		this.checkboxEdit.addActionListener(this);

		this.buttonCopy = new CGButton("command.copy");
		this.buttonCopy.addActionListener(this);

		this.createLayout();
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.checkboxEdit) this.textfieldCommand.setEditable(this.checkboxEdit.isSelected());
		if (e.getSource() == this.buttonCopy && !this.textfieldCommand.getText().equals("")) Toolkit.getDefaultToolkit().getSystemClipboard()
				.setContents(new StringSelection(this.textfieldCommand.getText()), null);
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
		gbc.fill = GridBagConstraints.HORIZONTAL;
		this.add(this.textfieldCommand, gbc);
		++gbc.gridx;
		gbc.fill = GridBagConstraints.NONE;
		this.add(this.checkboxEdit, gbc);
		++gbc.gridx;
		this.add(this.buttonCopy, gbc);
	}

}
