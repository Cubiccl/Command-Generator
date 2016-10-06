package fr.cubiccl.generator.gui.component.ui;

import java.awt.Graphics;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicCheckBoxUI;

import fr.cubiccl.generator.gui.DisplayUtils;

public class CCheckBoxUI extends BasicCheckBoxUI
{

	@Override
	public void update(Graphics g, JComponent c)
	{
		if (!((AbstractButton) c).getModel().isEnabled()) c.setBackground(DisplayUtils.DISABLED);
		else if (((AbstractButton) c).getModel().isPressed()) c.setBackground(DisplayUtils.CLICKED);
		else if (((AbstractButton) c).getModel().isRollover()) c.setBackground(DisplayUtils.HOVERED);
		else c.setBackground(DisplayUtils.BACKGROUND);

		if (((AbstractButton) c).getModel().isSelected()) ((AbstractButton) c).setIcon(new ImageIcon("res/textures/gui/check.png"));
		else ((AbstractButton) c).setIcon(new ImageIcon("res/textures/gui/uncheck.png"));

		super.update(g, c);
	}

}
