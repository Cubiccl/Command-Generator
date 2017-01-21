package fr.cubiccl.generator.gui.component.panel.tag;

import java.awt.GridBagConstraints;

import javax.swing.ButtonGroup;

import fr.cubiccl.generator.gui.component.button.CGRadioButton;
import fr.cubiccl.generator.gui.component.panel.CGPanel;

public class PanelDisabledSlots extends CGPanel
{
	private static final int SA = 1, BA = 2, PA = 4, CA = 8, HA = 16;
	private static final long serialVersionUID = 6696160657771591666L;
	private static final int SP = 65536, BP = 131072, PP = 262144, CP = 524288, HP = 1048576;
	private static final int SR = 256, BR = 512, PR = 1024, CR = 2048, HR = 4096;

	private CGRadioButton buttonAllHand, buttonAllBoots, buttonAllPants, buttonAllChestplate, buttonAllHelmet;
	private CGRadioButton buttonAllowHand, buttonAllowBoots, buttonAllowPants, buttonAllowChestplate, buttonAllowHelmet;
	private CGRadioButton buttonPlaceHand, buttonPlaceBoots, buttonPlacePants, buttonPlaceChestplate, buttonPlaceHelmet;
	private CGRadioButton buttonRemoveHand, buttonRemoveBoots, buttonRemovePants, buttonRemoveChestplate, buttonRemoveHelmet;

	public PanelDisabledSlots()
	{
		GridBagConstraints gbc = this.createGridBagLayout();
		CGPanel panelHand = new CGPanel("slot.hand"), panelBoots = new CGPanel("slot.boots"), panelPants = new CGPanel("slot.pants"), panelChestplate = new CGPanel(
				"slot.chestplate"), panelHelmet = new CGPanel("slot.helmet");

		panelHand.add(this.buttonAllowHand = new CGRadioButton("slot.allow"));
		panelHand.add(this.buttonPlaceHand = new CGRadioButton("slot.place"));
		panelHand.add(this.buttonRemoveHand = new CGRadioButton("slot.remove"));
		panelHand.add(this.buttonAllHand = new CGRadioButton("slot.all"));
		ButtonGroup group = new ButtonGroup();
		group.add(this.buttonAllHand);
		group.add(this.buttonPlaceHand);
		group.add(this.buttonRemoveHand);
		group.add(this.buttonAllowHand);

		panelBoots.add(this.buttonAllowBoots = new CGRadioButton("slot.allow"));
		panelBoots.add(this.buttonPlaceBoots = new CGRadioButton("slot.place"));
		panelBoots.add(this.buttonRemoveBoots = new CGRadioButton("slot.remove"));
		panelBoots.add(this.buttonAllBoots = new CGRadioButton("slot.all"));
		group = new ButtonGroup();
		group.add(this.buttonAllBoots);
		group.add(this.buttonPlaceBoots);
		group.add(this.buttonRemoveBoots);
		group.add(this.buttonAllowBoots);

		panelPants.add(this.buttonAllowPants = new CGRadioButton("slot.allow"));
		panelPants.add(this.buttonPlacePants = new CGRadioButton("slot.place"));
		panelPants.add(this.buttonRemovePants = new CGRadioButton("slot.remove"));
		panelPants.add(this.buttonAllPants = new CGRadioButton("slot.all"));
		group = new ButtonGroup();
		group.add(this.buttonAllPants);
		group.add(this.buttonPlacePants);
		group.add(this.buttonRemovePants);
		group.add(this.buttonAllowPants);

		panelChestplate.add(this.buttonAllowChestplate = new CGRadioButton("slot.allow"));
		panelChestplate.add(this.buttonPlaceChestplate = new CGRadioButton("slot.place"));
		panelChestplate.add(this.buttonRemoveChestplate = new CGRadioButton("slot.remove"));
		panelChestplate.add(this.buttonAllChestplate = new CGRadioButton("slot.all"));
		group = new ButtonGroup();
		group.add(this.buttonAllChestplate);
		group.add(this.buttonPlaceChestplate);
		group.add(this.buttonRemoveChestplate);
		group.add(this.buttonAllowChestplate);

		panelHelmet.add(this.buttonAllowHelmet = new CGRadioButton("slot.allow"));
		panelHelmet.add(this.buttonPlaceHelmet = new CGRadioButton("slot.place"));
		panelHelmet.add(this.buttonRemoveHelmet = new CGRadioButton("slot.remove"));
		panelHelmet.add(this.buttonAllHelmet = new CGRadioButton("slot.all"));
		group = new ButtonGroup();
		group.add(this.buttonAllHelmet);
		group.add(this.buttonPlaceHelmet);
		group.add(this.buttonRemoveHelmet);
		group.add(this.buttonAllowHelmet);

		this.add(panelHand, gbc);
		++gbc.gridy;
		this.add(panelHelmet, gbc);
		++gbc.gridy;
		this.add(panelChestplate, gbc);
		++gbc.gridy;
		this.add(panelPants, gbc);
		++gbc.gridy;
		this.add(panelBoots, gbc);

		this.buttonAllowHand.setSelected(true);
		this.buttonAllowHelmet.setSelected(true);
		this.buttonAllowChestplate.setSelected(true);
		this.buttonAllowPants.setSelected(true);
		this.buttonAllowBoots.setSelected(true);
	}

	public int generateValue()
	{
		int value = 0;

		if (this.buttonAllHand.isSelected()) value += SA;
		if (this.buttonAllBoots.isSelected()) value += BA;
		if (this.buttonAllPants.isSelected()) value += PA;
		if (this.buttonAllChestplate.isSelected()) value += CA;
		if (this.buttonAllHelmet.isSelected()) value += HA;

		if (this.buttonRemoveHand.isSelected()) value += SR;
		if (this.buttonRemoveBoots.isSelected()) value += BR;
		if (this.buttonRemovePants.isSelected()) value += PR;
		if (this.buttonRemoveChestplate.isSelected()) value += CR;
		if (this.buttonRemoveHelmet.isSelected()) value += HR;

		if (this.buttonPlaceHand.isSelected()) value += SP;
		if (this.buttonPlaceBoots.isSelected()) value += BP;
		if (this.buttonPlacePants.isSelected()) value += PP;
		if (this.buttonPlaceChestplate.isSelected()) value += CP;
		if (this.buttonPlaceHelmet.isSelected()) value += HP;

		return value;
	}

	public void setupFrom(int value)
	{
		if (value >= HP)
		{
			this.buttonPlaceHelmet.setSelected(true);
			value -= HP;
		}
		if (value >= CP)
		{
			this.buttonPlaceChestplate.setSelected(true);
			value -= CP;
		}
		if (value >= PP)
		{
			this.buttonPlacePants.setSelected(true);
			value -= PP;
		}
		if (value >= BP)
		{
			this.buttonPlaceBoots.setSelected(true);
			value -= BP;
		}
		if (value >= SP)
		{
			this.buttonPlaceHand.setSelected(true);
			value -= SP;
		}

		if (value >= HR)
		{
			this.buttonRemoveHelmet.setSelected(true);
			value -= HR;
		}
		if (value >= CR)
		{
			this.buttonRemoveChestplate.setSelected(true);
			value -= CR;
		}
		if (value >= PR)
		{
			this.buttonRemovePants.setSelected(true);
			value -= PR;
		}
		if (value >= BR)
		{
			this.buttonRemoveBoots.setSelected(true);
			value -= BR;
		}
		if (value >= SR)
		{
			this.buttonRemoveHand.setSelected(true);
			value -= SR;
		}

		if (value >= HA)
		{
			this.buttonAllHelmet.setSelected(true);
			value -= HA;
		}
		if (value >= CA)
		{
			this.buttonAllChestplate.setSelected(true);
			value -= CA;
		}
		if (value >= PA)
		{
			this.buttonAllPants.setSelected(true);
			value -= PA;
		}
		if (value >= BA)
		{
			this.buttonAllBoots.setSelected(true);
			value -= BA;
		}
		if (value >= SA)
		{
			this.buttonAllHand.setSelected(true);
			value -= SA;
		}

	}

}
