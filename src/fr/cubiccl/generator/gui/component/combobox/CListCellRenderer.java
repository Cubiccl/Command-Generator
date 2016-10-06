package fr.cubiccl.generator.gui.component.combobox;

import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;

import fr.cubiccl.generator.gui.DisplayUtils;

public class CListCellRenderer implements ListCellRenderer<String>
{
	private boolean paintingCombobox = false;

	@Override
	public Component getListCellRendererComponent(JList<? extends String> list, String value, int index, boolean isSelected, boolean cellHasFocus)
	{
		JLabel label = new JLabel(" " + value);
		label.setOpaque(true);
		label.setFont(list.getFont());
		if (this.paintingCombobox) label.setHorizontalAlignment(SwingConstants.CENTER);
		if (!list.isEnabled())
		{
			label.setBackground(DisplayUtils.DISABLED);
			label.setForeground(DisplayUtils.DISABLED.darker());
		} else if (isSelected)
		{
			label.setBackground(DisplayUtils.HOVERED);
			label.setBorder(BorderFactory.createLineBorder(DisplayUtils.HOVERED.darker()));
		} else label.setBackground(DisplayUtils.BACKGROUND);
		return label;
	}

	public void setPaintingCombobox(boolean painting)
	{
		this.paintingCombobox = painting;
	}

}
