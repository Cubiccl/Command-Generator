package fr.cubiccl.generator.gui.component.panel.utils;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gui.component.button.CGButton;
import fr.cubiccl.generator.gui.component.panel.CGPanel;

public class ConfirmPanel extends CGPanel implements ActionListener
{
	private static final long serialVersionUID = -7457797405576343429L;

	private CGButton buttonOK, buttonCancel;
	public Component component;
	private GridBagConstraints gbc;

	public ConfirmPanel()
	{
		this(null);
	}

	public ConfirmPanel(Component component)
	{
		this.gbc = this.createGridBagLayout();
		++gbc.gridy;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		this.add(this.buttonCancel = new CGButton("general.cancel"), gbc);
		++gbc.gridx;
		this.add(this.buttonOK = new CGButton("general.confirm"), gbc);
		++gbc.gridwidth;
		--gbc.gridx;
		--gbc.gridy;
		if (component != null) this.setMainComponent(component);

		this.buttonOK.addActionListener(this);
		this.buttonCancel.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		CommandGenerator.stateManager.clearState(e.getSource() == this.buttonOK);
	}

	public void setMainComponent(Component component)
	{
		if (this.component != null) this.remove(this.component);
		this.component = component;
		this.add(this.component, this.gbc);
		this.updateTranslations();
	}

}
