package fr.cubiccl.generator.gui.component.combobox;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComboBox;
import javax.swing.plaf.basic.BasicArrowButton;

import fr.cubiccl.generator.gui.DisplayUtils;
import fr.cubiccl.generator.gui.RoundedCornerBorder;
import fr.cubiccl.generator.gui.component.ui.CComboboxUI;

public class CComboBox extends JComboBox<String> implements MouseListener
{
	private static final long serialVersionUID = -5586551752242617407L;

	private boolean pressed, hovered;
	private String[] values;

	public CComboBox(String... values)
	{
		super(values);
		this.values = values;
		this.setBorder(new RoundedCornerBorder());
		this.setUI(new CComboboxUI());
		this.setRenderer(new CListCellRenderer());
		for (Component component : this.getComponents())
			if (component instanceof BasicArrowButton) this.remove(component);

		this.addMouseListener(this);
		this.setFont(DisplayUtils.FONT);
	}

	public String getValue()
	{
		return this.values[this.getSelectedIndex()];
	}

	public boolean isHovered()
	{
		return this.hovered;
	}

	public boolean isPressed()
	{
		return this.pressed;
	}

	@Override
	public void mouseClicked(MouseEvent arg0)
	{}

	@Override
	public void mouseEntered(MouseEvent arg0)
	{
		this.hovered = true;
		this.repaint();
	}

	@Override
	public void mouseExited(MouseEvent arg0)
	{
		this.hovered = false;
		this.repaint();
	}

	@Override
	public void mousePressed(MouseEvent arg0)
	{
		this.pressed = true;
		this.repaint();
	}

	@Override
	public void mouseReleased(MouseEvent arg0)
	{
		this.pressed = false;
		this.repaint();
	}

	public void setValues(String[] values)
	{
		this.values = values;
		int index = this.getSelectedIndex();
		this.setModel(new CComboBox(this.values).getModel());
		if (index > 0 && index < this.values.length) this.setSelectedIndex(index);

		int textWidth = 0;
		for (String value : this.values)
			textWidth = Math.max(DisplayUtils.textWidth(value), textWidth);
		this.setPreferredSize(new Dimension(Math.max(140, textWidth), 27));
	}

}
