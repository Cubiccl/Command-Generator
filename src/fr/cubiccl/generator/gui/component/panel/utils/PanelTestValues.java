package fr.cubiccl.generator.gui.component.panel.utils;

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
	private CGPanel panelMenu;

	public PanelTestValues()
	{
		this(null);
	}

	public PanelTestValues(String titleID)
	{
		super(titleID);
		this.components = new HashMap<CGCheckBox, Component>();

		this.panelMenu = new CGPanel("advancement.check");
		this.panelMenu.createGridBagLayout();

		this.createGridBagLayout();
		this.gbc.anchor = GridBagConstraints.CENTER;
		this.add(this.panelMenu, this.gbc);
		++this.gbc.gridy;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() instanceof CGCheckBox && this.components.containsKey((CGCheckBox) e.getSource())) this.components.get((CGCheckBox) e.getSource())
				.setVisible(((CGCheckBox) e.getSource()).isSelected());
	}

	public CGCheckBox addComponent(String checkboxTextID, Component component)
	{
		CGCheckBox checkbox = new CGCheckBox(checkboxTextID);
		checkbox.addActionListener(this);
		this.components.put(checkbox, component);
		component.setVisible(false);

		GridBagConstraints gbc = this.panelMenu.getGBC();
		if ((this.components.size() - 1) % 5 == 0)
		{
			gbc.gridx = 0;
			++gbc.gridy;
		}

		this.panelMenu.add(checkbox, gbc);

		++this.gbc.gridy;
		this.add(component, this.gbc);
		++gbc.gridx;

		return checkbox;
	}

	public CGCheckBox checkboxFor(Component component)
	{
		for (CGCheckBox checkbox : this.components.keySet())
			if (this.components.get(checkbox) == component) return checkbox;
		return null;
	}

	public boolean isSelected(Component component)
	{
		CGCheckBox c = this.checkboxFor(component);
		return c != null && c.isSelected();
	}

	public void select(Component component)
	{
		this.setSelected(component, true);
	}

	public void setSelected(Component component, boolean selected)
	{
		CGCheckBox c = this.checkboxFor(component);
		if (c != null)
		{
			c.setSelected(selected);
			this.updateCheckboxes();
		}
	}

	public void updateCheckboxes()
	{
		for (CGCheckBox checkbox : this.components.keySet())
			this.components.get(checkbox).setVisible(checkbox.isSelected());
	}
}
