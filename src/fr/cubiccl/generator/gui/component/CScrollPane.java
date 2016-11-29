package fr.cubiccl.generator.gui.component;

import java.awt.Component;

import javax.swing.JScrollPane;

public class CScrollPane extends JScrollPane
{
	private static final long serialVersionUID = -8261339231722499546L;

	public CScrollPane(Component component)
	{
		if (component != null) this.setViewportView(component);
		this.setBorder(null);
		this.getVerticalScrollBar().setUnitIncrement(20);
		this.getHorizontalScrollBar().setUnitIncrement(20);
	}

}
