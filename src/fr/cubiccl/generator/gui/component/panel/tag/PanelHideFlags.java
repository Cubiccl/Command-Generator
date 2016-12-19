package fr.cubiccl.generator.gui.component.panel.tag;

import java.awt.GridBagConstraints;

import fr.cubiccl.generator.gui.component.button.CGCheckBox;
import fr.cubiccl.generator.gui.component.panel.CGPanel;

public class PanelHideFlags extends CGPanel
{
	private static final long serialVersionUID = 4137270795535481132L;

	private CGCheckBox boxAttributes, boxDestroy, boxPlace, boxEnchantments, boxOther, boxUnbreakable;

	public PanelHideFlags()
	{
		GridBagConstraints gbc = this.createGridBagLayout();
		this.add(this.boxAttributes = new CGCheckBox("flags.attributes"), gbc);
		++gbc.gridy;
		this.add(this.boxDestroy = new CGCheckBox("flags.can_destroy"), gbc);
		++gbc.gridy;
		this.add(this.boxPlace = new CGCheckBox("flags.can_place"), gbc);
		++gbc.gridy;
		this.add(this.boxEnchantments = new CGCheckBox("flags.enchantments"), gbc);
		++gbc.gridy;
		this.add(this.boxOther = new CGCheckBox("flags.other"), gbc);
		++gbc.gridy;
		this.add(this.boxUnbreakable = new CGCheckBox("flags.unbreakable"), gbc);
		++gbc.gridy;
	}

	public int getValue()
	{
		int value = 0;
		if (this.boxAttributes.isSelected()) value += 2;
		if (this.boxDestroy.isSelected()) value += 8;
		if (this.boxPlace.isSelected()) value += 16;
		if (this.boxEnchantments.isSelected()) value += 1;
		if (this.boxOther.isSelected()) value += 32;
		if (this.boxUnbreakable.isSelected()) value += 4;
		return value;
	}

	public void setupFrom(int value)
	{
		this.boxOther.setSelected(value >= 32);
		if (value >= 32) value -= 32;

		this.boxPlace.setSelected(value >= 16);
		if (value >= 16) value -= 16;

		this.boxDestroy.setSelected(value >= 8);
		if (value >= 8) value -= 8;

		this.boxUnbreakable.setSelected(value >= 4);
		if (value >= 4) value -= 4;

		this.boxAttributes.setSelected(value >= 2);
		if (value >= 2) value -= 2;

		this.boxEnchantments.setSelected(value >= 1);
	}

}
