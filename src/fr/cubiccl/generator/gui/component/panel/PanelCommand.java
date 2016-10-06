package fr.cubiccl.generator.gui.component.panel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;

import fr.cubiccl.generator.gui.component.button.CButton;
import fr.cubiccl.generator.gui.component.button.CCheckBox;
import fr.cubiccl.generator.gui.component.label.CLabel;
import fr.cubiccl.generator.gui.component.textfield.CTextField;

public class PanelCommand extends CPanel implements ActionListener
{
	public static final int HEIGHT = 60;
	private static final long serialVersionUID = -5645072079663496893L;

	private CButton buttonCopy;
	private CCheckBox checkboxEdit;
	private CLabel labelCommand;
	public final CTextField textfieldCommand;

	public PanelCommand()
	{
		this.setBorder(BorderFactory.createRaisedBevelBorder());

		this.labelCommand = new CLabel("command.label").setHasColumn(true);

		this.textfieldCommand = new CTextField();
		this.textfieldCommand.setEditable(false);

		this.checkboxEdit = new CCheckBox("command.edit");
		this.checkboxEdit.addActionListener(this);

		this.buttonCopy = new CButton("command.copy");
		this.buttonCopy.addActionListener(this);

		this.createLayout();
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.checkboxEdit) this.textfieldCommand.setEditable(this.checkboxEdit.isSelected());
		if (e.getSource() == this.buttonCopy) Toolkit.getDefaultToolkit().getSystemClipboard()
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
		this.add(this.textfieldCommand, gbc);
		++gbc.gridx;
		this.add(this.checkboxEdit, gbc);
		++gbc.gridx;
		this.add(this.buttonCopy, gbc);
	}

}
