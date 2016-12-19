package fr.cubiccl.generator.gui.component.panel.utils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;

import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import fr.cubiccl.generator.gui.component.panel.CGPanel;

public class PanelColor extends CGPanel implements ChangeListener
{
	private static final long serialVersionUID = 7451366893530825914L;

	private JPanel panelColor;
	private JSlider sliderRed, sliderGreen, sliderBlue;

	public PanelColor(String titleID)
	{
		super(titleID);

		GridBagConstraints gbc = this.createGridBagLayout();
		this.add(this.sliderRed = new JSlider(0, 255), gbc);
		++gbc.gridy;
		this.add(this.sliderGreen = new JSlider(0, 255), gbc);
		++gbc.gridy;
		this.add(this.sliderBlue = new JSlider(0, 255), gbc);
		++gbc.gridx;
		gbc.gridy = 0;
		gbc.gridheight = 3;
		this.add(this.panelColor = new JPanel(), gbc);

		this.sliderRed.addChangeListener(this);
		this.sliderGreen.addChangeListener(this);
		this.sliderBlue.addChangeListener(this);
		this.panelColor.setPreferredSize(new Dimension(100, 100));
		this.setupFrom(0);
	}

	public int getValue()
	{
		return (this.sliderRed.getValue() << 16) + (this.sliderGreen.getValue() << 8) + this.sliderBlue.getValue();
	}

	private void onColorChange(int red, int green, int blue)
	{
		this.panelColor.setBackground(new Color(red, green, blue));
	}

	public void setupFrom(int value)
	{
		int red = (value >> 16) & 0xFF;
		int green = (value >> 8) & 0xFF;
		int blue = value & 0xFF;
		this.sliderRed.setValue(red);
		this.sliderGreen.setValue(green);
		this.sliderBlue.setValue(blue);
		this.onColorChange(red, green, blue);
	}

	@Override
	public void stateChanged(ChangeEvent e)
	{
		this.onColorChange(this.sliderRed.getValue(), this.sliderGreen.getValue(), this.sliderBlue.getValue());
	}

}
