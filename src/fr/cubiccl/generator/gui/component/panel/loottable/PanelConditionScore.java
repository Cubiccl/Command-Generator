package fr.cubiccl.generator.gui.component.panel.loottable;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;

import fr.cubiccl.generator.gui.component.button.CGRadioButton;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.loottable.PanelCondition.ScoreCondition;
import fr.cubiccl.generator.gui.component.textfield.CGEntry;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

public class PanelConditionScore extends CGPanel implements ActionListener
{
	private static final long serialVersionUID = 3873966739089354128L;

	private CGRadioButton buttonFixed, buttonRanged;
	private CGEntry entryMin, entryMax, entryName;

	public PanelConditionScore()
	{
		GridBagConstraints gbc = this.createGridBagLayout();
		this.add(this.buttonFixed = new CGRadioButton("lt_condition.entity_scores.exact"), gbc);
		++gbc.gridx;
		this.add(this.buttonRanged = new CGRadioButton("lt_condition.entity_scores.range"), gbc);

		gbc.gridx = 0;
		++gbc.gridy;
		++gbc.gridwidth;
		this.add((this.entryName = new CGEntry("score.name")).container, gbc);
		++gbc.gridy;
		this.add((this.entryMin = new CGEntry(null, Text.INTEGER)).container, gbc);
		++gbc.gridy;
		this.add((this.entryMax = new CGEntry(new Text("scoreboard.test.max"), Text.INTEGER)).container, gbc);

		ButtonGroup group = new ButtonGroup();
		group.add(this.buttonFixed);
		group.add(this.buttonRanged);

		this.buttonFixed.addActionListener(this);
		this.buttonRanged.addActionListener(this);

		this.entryMin.addIntFilter();
		this.entryMax.addIntFilter();
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		this.updateDisplay();
	}

	public ScoreCondition generate() throws CommandGenerationException
	{
		this.entryName.checkValue(CGEntry.STRING);
		this.entryMin.checkValue(CGEntry.INTEGER);
		if (this.buttonFixed.isSelected()) return new ScoreCondition(this.entryName.getText(), Integer.parseInt(this.entryMin.getText()), 0, false);
		this.entryMax.checkValue(CGEntry.INTEGER);
		return new ScoreCondition(this.entryName.getText(), Integer.parseInt(this.entryMin.getText()), Integer.parseInt(this.entryMax.getText()), true);
	}

	public void setupFrom(ScoreCondition condition)
	{
		this.buttonFixed.setSelected(!condition.isRange);
		this.buttonRanged.setSelected(condition.isRange);

		this.entryName.setText(condition.objective);
		this.entryMin.setText(Integer.toString(condition.min));
		if (condition.isRange) this.entryMax.setText(Integer.toString(condition.max));

		this.updateDisplay();
	}

	private void updateDisplay()
	{
		if (this.buttonFixed.isSelected()) this.entryMin.label.setTextID("score.value");
		else this.entryMin.label.setTextID("score.value.min");
		this.entryMax.container.setVisible(this.buttonRanged.isSelected());
	}

}
