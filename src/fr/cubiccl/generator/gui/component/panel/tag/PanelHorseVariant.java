package fr.cubiccl.generator.gui.component.panel.tag;

import java.awt.GridBagConstraints;

import javax.swing.ButtonGroup;

import fr.cubiccl.generator.gui.component.button.CGRadioButton;
import fr.cubiccl.generator.gui.component.panel.CGPanel;

public class PanelHorseVariant extends CGPanel
{
	private static final String[] BASES =
	{ "white", "creamy", "chestnut", "brown", "black", "gray", "dark_brown" }, MARKINGS =
	{ "none", "white", "white_field", "white_dots", "black_dots" };
	private static final long serialVersionUID = -3588815636268332019L;

	private CGRadioButton[] buttonsBase, buttonsMarking;

	public PanelHorseVariant()
	{
		super();
		GridBagConstraints gbc = this.createGridBagLayout();
		CGPanel panelBase = new CGPanel("variant.base"), panelMarkings = new CGPanel("variant.markings");
		ButtonGroup base = new ButtonGroup(), marking = new ButtonGroup();
		this.buttonsBase = new CGRadioButton[BASES.length];
		this.buttonsMarking = new CGRadioButton[MARKINGS.length];

		for (int i = 0; i < BASES.length; ++i)
		{
			this.buttonsBase[i] = new CGRadioButton("variant.base." + BASES[i]);
			panelBase.add(this.buttonsBase[i]);
			base.add(this.buttonsBase[i]);
		}
		for (int i = 0; i < MARKINGS.length; ++i)
		{
			this.buttonsMarking[i] = new CGRadioButton("variant.marking." + MARKINGS[i]);
			panelMarkings.add(this.buttonsMarking[i]);
			marking.add(this.buttonsMarking[i]);
		}

		this.add(panelBase, gbc);
		++gbc.gridy;
		this.add(panelMarkings, gbc);

		this.buttonsBase[0].setSelected(true);
		this.buttonsMarking[0].setSelected(true);

	}

	public int generateVariant()
	{
		return this.selectedBase() + this.selectedMarking() * 256;
	}

	private void selectBase(int base)
	{
		for (int i = 0; i < this.buttonsBase.length; i++)
			this.buttonsBase[i].setSelected(i == base);
	}

	private int selectedBase()
	{
		for (int i = 0; i < this.buttonsBase.length; i++)
			if (this.buttonsBase[i].isSelected()) return i;
		return 0;
	}

	private int selectedMarking()
	{
		for (int i = 0; i < this.buttonsMarking.length; i++)
			if (this.buttonsMarking[i].isSelected()) return i;
		return 0;
	}

	private void selectMarking(int marking)
	{
		for (int i = 0; i < this.buttonsMarking.length; i++)
			this.buttonsMarking[i].setSelected(i == marking);
	}

	public void setupFrom(int variant)
	{
		int base = variant % 256;
		variant -= base;
		this.selectBase(base);
		this.selectMarking(variant / 256);
	}

}
