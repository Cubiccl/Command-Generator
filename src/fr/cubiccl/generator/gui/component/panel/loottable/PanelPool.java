package fr.cubiccl.generator.gui.component.panel.loottable;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;

import fr.cubiccl.generator.gameobject.loottable.LootTableEntry;
import fr.cubiccl.generator.gameobject.loottable.LootTablePool;
import fr.cubiccl.generator.gui.component.button.CGRadioButton;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.utils.PanelObjectList;
import fr.cubiccl.generator.gui.component.textfield.CGEntry;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

public class PanelPool extends CGPanel implements ActionListener
{
	private static final long serialVersionUID = 5520482302949227591L;

	private CGRadioButton buttonRollsExact, buttonRollsRange, buttonBonusExact, buttonBonusRange;
	private CGEntry entryRollsMin, entryRollsMax, entryBonusMin, entryBonusMax;
	private PanelConditionList listConditions;
	private PanelObjectList<LootTableEntry> listEntries;

	public PanelPool()
	{
		GridBagConstraints gbc = this.createGridBagLayout();

		this.add(this.buttonRollsExact = new CGRadioButton("loottable.rolls.exact"), gbc);
		++gbc.gridx;
		this.add(this.buttonRollsRange = new CGRadioButton("loottable.rolls.range"), gbc);

		gbc.gridx = 0;
		++gbc.gridy;
		gbc.gridwidth = 2;
		this.add((this.entryRollsMin = new CGEntry(new Text("loottable.rolls"), "0", Text.INTEGER)).container, gbc);
		++gbc.gridy;
		this.add((this.entryRollsMax = new CGEntry(new Text("loottable.rolls.max"), "0", Text.INTEGER)).container, gbc);

		++gbc.gridy;
		gbc.gridwidth = 1;
		this.add(this.buttonBonusExact = new CGRadioButton("loottable.bonus.exact"), gbc);
		++gbc.gridx;
		this.add(this.buttonBonusRange = new CGRadioButton("loottable.bonus.range"), gbc);

		gbc.gridx = 0;
		++gbc.gridy;
		gbc.gridwidth = 2;
		this.add((this.entryBonusMin = new CGEntry(new Text("loottable.bonus"), "0", Text.INTEGER)).container, gbc);
		++gbc.gridy;
		this.add((this.entryBonusMax = new CGEntry(new Text("loottable.bonus.max"), "0", Text.INTEGER)).container, gbc);

		++gbc.gridy;
		gbc.gridwidth = 2;
		this.add(new CGLabel("loottable.conditions.description"), gbc);
		++gbc.gridy;
		this.add(this.listConditions = new PanelConditionList("loottable.conditions", "loottable.condition"), gbc);
		++gbc.gridy;
		this.add(new CGLabel("loottable.entries.description"), gbc);
		++gbc.gridy;
		this.add(this.listEntries = new PanelObjectList<LootTableEntry>("loottable.entries", "loottable.entry", LootTableEntry.class), gbc);

		this.buttonRollsExact.addActionListener(this);
		this.buttonRollsRange.addActionListener(this);
		this.buttonBonusExact.addActionListener(this);
		this.buttonBonusRange.addActionListener(this);

		ButtonGroup group = new ButtonGroup();
		group.add(this.buttonRollsExact);
		group.add(this.buttonRollsRange);
		group = new ButtonGroup();
		group.add(this.buttonBonusExact);
		group.add(this.buttonBonusRange);
		this.buttonRollsExact.setSelected(true);
		this.buttonBonusExact.setSelected(true);
		this.updateDisplay();
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		this.updateDisplay();
	}

	public LootTablePool generatePool() throws CommandGenerationException
	{
		int rollsMin = 0, rollsMax = -1;
		double bonusRollsMin = 0, bonusRollsMax = -1;

		this.entryRollsMin.checkValueSuperior(CGEntry.INTEGER, 0);
		this.entryBonusMin.checkValueSuperior(CGEntry.FLOAT, 0);

		rollsMin = Integer.parseInt(this.entryRollsMin.getText());
		bonusRollsMin = Double.parseDouble(this.entryBonusMin.getText());

		if (this.buttonRollsRange.isSelected())
		{
			this.entryRollsMax.checkValueSuperior(CGEntry.INTEGER, rollsMin);
			rollsMax = Integer.parseInt(this.entryRollsMax.getText());
		}
		if (this.buttonBonusRange.isSelected())
		{
			this.entryBonusMax.checkValueSuperior(CGEntry.FLOAT, bonusRollsMin);
			bonusRollsMax = Double.parseDouble(this.entryBonusMax.getText());
		}

		return new LootTablePool(this.listConditions.values(), rollsMin, rollsMax, bonusRollsMin, bonusRollsMax, this.listEntries.values());
	}

	public void setupFrom(LootTablePool pool)
	{
		if (pool.rollsMax != -1)
		{
			this.buttonRollsRange.setSelected(true);
			this.entryRollsMax.setText(Integer.toString(pool.rollsMax));
		} else
		{
			this.buttonRollsExact.setSelected(true);
			this.entryRollsMax.setText("0");
		}
		if (pool.bonusRollsMax != -1)
		{
			this.buttonBonusRange.setSelected(true);
			this.entryBonusMax.setText(Double.toString(pool.bonusRollsMax));
		} else
		{
			this.buttonBonusExact.setSelected(true);
			this.entryBonusMax.setText("0");
		}
		this.entryRollsMin.setText(Integer.toString(pool.rollsMin));
		this.entryBonusMin.setText(Double.toString(pool.bonusRollsMin));

		this.listConditions.setValues(pool.conditions);
		this.listEntries.setValues(pool.entries);

		this.updateDisplay();
	}

	private void updateDisplay()
	{
		if (this.buttonRollsExact.isSelected()) this.entryRollsMin.label.setTextID("loottable.rolls");
		else this.entryRollsMin.label.setTextID("loottable.rolls.min");
		if (this.buttonBonusExact.isSelected()) this.entryBonusMin.label.setTextID("loottable.bonus");
		else this.entryBonusMin.label.setTextID("loottable.bonus.min");

		this.entryBonusMax.container.setVisible(this.buttonBonusRange.isSelected());
		this.entryRollsMax.container.setVisible(this.buttonRollsRange.isSelected());
	}

}
