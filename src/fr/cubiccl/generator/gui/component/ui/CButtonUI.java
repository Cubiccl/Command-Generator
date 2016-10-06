package fr.cubiccl.generator.gui.component.ui;

import java.awt.Graphics;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicButtonUI;

import fr.cubiccl.generator.gui.DisplayUtils;

public class CButtonUI extends BasicButtonUI
{

	@Override
	public void update(Graphics g, JComponent c)
	{
		if (!((AbstractButton) c).getModel().isEnabled()) c.setBackground(DisplayUtils.DISABLED);
		else if (((AbstractButton) c).getModel().isPressed()) c.setBackground(DisplayUtils.CLICKED);
		else if (((AbstractButton) c).getModel().isRollover()) c.setBackground(DisplayUtils.HOVERED);
		else c.setBackground(DisplayUtils.BACKGROUND);
		super.update(g, c);
	}

}
