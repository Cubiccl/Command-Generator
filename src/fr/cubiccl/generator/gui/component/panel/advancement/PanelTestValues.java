package fr.cubiccl.generator.gui.component.panel.advancement;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import fr.cubiccl.generator.gui.component.button.CGCheckBox;
import fr.cubiccl.generator.gui.component.panel.CGPanel;

public class PanelTestValues extends CGPanel implements ActionListener
{
	private static final long serialVersionUID = -4681596141887182233L;

	private HashMap<CGCheckBox, Component> components;

	public PanelTestValues()
	{
		this.createGridBagLayout();
		this.gbc.anchor = GridBagConstraints.CENTER;
		this.components = new HashMap<CGCheckBox, Component>();
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() instanceof CGCheckBox) this.components.get((CGCheckBox) e.getSource()).setEnabled(((CGCheckBox) e.getSource()).isSelected());
	}

	public CGCheckBox addComponent(String checkboxTextID, Component component)
	{
		return this.addComponent(checkboxTextID, component, false);
	}

	public CGCheckBox addComponent(String checkboxTextID, Component component, boolean vertical)
	{
		CGCheckBox checkbox = new CGCheckBox(checkboxTextID);
		checkbox.addActionListener(this);
		this.components.put(checkbox, component);
		component.setEnabled(false);
		if (vertical) this.addVertical(checkbox, component);
		else this.addHorizontal(checkbox, component);
		return checkbox;
	}

	private void addHorizontal(CGCheckBox checkbox, Component component)
	{
		this.gbc.gridwidth = 1;
		this.add(checkbox, this.gbc);
		++gbc.gridx;
		this.add(component, this.gbc);
		--gbc.gridx;
		++gbc.gridy;
	}

	private void addVertical(CGCheckBox checkbox, Component component)
	{
		this.gbc.gridwidth = 2;
		this.add(checkbox, this.gbc);
		++gbc.gridy;
		this.add(component, this.gbc);
		++gbc.gridy;
	}

	public CGCheckBox checkboxFor(Component component)
	{
		for (CGCheckBox checkbox : this.components.keySet())
			if (this.components.get(checkbox) == component) return checkbox;
		return null;
	}

	public void updateCheckboxes()
	{
		for (CGCheckBox checkbox : this.components.keySet())
			this.components.get(checkbox).setEnabled(checkbox.isSelected());
	}
}
