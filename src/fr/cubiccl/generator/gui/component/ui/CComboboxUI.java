package fr.cubiccl.generator.gui.component.ui;

import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicComboBoxUI;

import fr.cubiccl.generator.gui.DisplayUtils;
import fr.cubiccl.generator.gui.component.combobox.CComboBox;
import fr.cubiccl.generator.gui.component.combobox.CListCellRenderer;

public class CComboboxUI extends BasicComboBoxUI
{
	@Override
	public void paintCurrentValue(Graphics g, Rectangle bounds, boolean hasFocus)
	{
		if (this.comboBox.getSelectedItem() != null)
		{
			// Center value
			((CListCellRenderer) this.comboBox.getRenderer()).setPaintingCombobox(true);
			super.paintCurrentValue(g, new Rectangle(bounds.x, bounds.y, this.comboBox.getWidth() - 22, bounds.height), false);
			((CListCellRenderer) this.comboBox.getRenderer()).setPaintingCombobox(false);
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void update(Graphics g, JComponent c)
	{
		if (!((JComboBox) c).isEnabled()) c.setBackground(DisplayUtils.DISABLED);
		else if (((CComboBox) c).isPressed()) c.setBackground(DisplayUtils.CLICKED);
		else if (((CComboBox) c).isHovered()) c.setBackground(DisplayUtils.HOVERED);
		else c.setBackground(DisplayUtils.BACKGROUND);
		super.update(g, c);
	}

}
