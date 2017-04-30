package fr.cubiccl.generator.gui.component.panel.tag;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;

import fr.cubiccl.generator.gameobject.utils.TestValue;
import fr.cubiccl.generator.gui.component.button.CGRadioButton;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.textfield.CGEntry;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

public class PanelRangedValue extends CGPanel implements ActionListener
{
	private static final long serialVersionUID = -6013895507174678230L;

	private CGRadioButton buttonFixed, buttonRanged;
	private CGEntry entryMin, entryMax, entryFixed;

	public PanelRangedValue(String titleID, Text description, Text hintText)
	{
		super(titleID);
		GridBagConstraints gbc = this.createGridBagLayout();
		gbc.gridwidth = 2;
		this.add(new CGLabel(description), gbc);
		--gbc.gridwidth;

		++gbc.gridy;
		this.add(this.buttonFixed = new CGRadioButton("value.fixed"), gbc);
		++gbc.gridx;
		this.add(this.buttonRanged = new CGRadioButton("value.ranged"), gbc);

		gbc.gridx = 0;
		++gbc.gridy;
		++gbc.gridwidth;
		this.add((this.entryFixed = new CGEntry(new Text("score.value"), hintText)).container, gbc);
		this.add((this.entryMin = new CGEntry(new Text("score.value.min"), hintText)).container, gbc);
		++gbc.gridy;
		this.add((this.entryMax = new CGEntry(new Text("scoreboard.test.max"), hintText)).container, gbc);

		ButtonGroup group = new ButtonGroup();
		group.add(this.buttonFixed);
		group.add(this.buttonRanged);

		this.buttonFixed.setSelected(true);
		this.buttonFixed.addActionListener(this);
		this.buttonRanged.addActionListener(this);

		this.onModeChange();
	}

	public PanelRangedValue(Text description, Text hintText)
	{
		this(null, description, hintText);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		this.onModeChange();
	}

	public void checkInput(byte checkMode) throws CommandGenerationException
	{
		if (this.buttonFixed.isSelected()) this.entryFixed.checkValue(checkMode);
		else
		{
			this.entryMin.checkValue(checkMode);
			this.entryMax.checkValueSuperior(checkMode, this.min());
		}
	}

	public TestValue generateValue(TestValue value)
	{
		value.isRanged = this.isRanged();
		if (this.isRanged())
		{
			value.valueMin = this.min();
			value.valueMax = this.max();
		} else value.valueMin = this.value();
		return value;
	}

	public boolean isRanged()
	{
		return this.buttonRanged.isSelected();
	}

	public double max()
	{
		return Double.parseDouble(this.entryMax.getText());
	}

	public double min()
	{
		return Double.parseDouble(this.entryMin.getText());
	}

	private void onModeChange()
	{
		this.entryFixed.container.setVisible(this.buttonFixed.isSelected());
		this.entryMax.container.setVisible(this.buttonRanged.isSelected());
		this.entryMin.container.setVisible(this.buttonRanged.isSelected());
	}

	public void setFixed(double value)
	{
		this.buttonFixed.setSelected(true);
		this.entryFixed.setText(Double.toString(value));
		this.onModeChange();
	}

	public void setFixed(int value)
	{
		this.buttonFixed.setSelected(true);
		this.entryFixed.setText(Integer.toString(value));
		this.onModeChange();
	}

	public void setRanged(double min, double max)
	{
		this.buttonRanged.setSelected(true);
		this.entryMax.setText(Double.toString(max));
		this.entryMin.setText(Double.toString(min));
		this.onModeChange();
	}

	public void setRanged(int min, int max)
	{
		this.buttonRanged.setSelected(true);
		this.entryMax.setText(Integer.toString(max));
		this.entryMin.setText(Integer.toString(min));
		this.onModeChange();
	}

	public void setupFrom(TestValue value)
	{
		if (value.isRanged) if (value.isInt()) this.setRanged((int) value.valueMin, (int) value.valueMax);
		else this.setRanged(value.valueMin, value.valueMax);
		else if (value.isInt()) this.setFixed((int) value.valueMin);
		else this.setFixed(value.valueMin);
	}

	public double value()
	{
		return Double.parseDouble(this.entryFixed.getText());
	}

}
