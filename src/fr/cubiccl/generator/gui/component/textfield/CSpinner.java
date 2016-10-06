package fr.cubiccl.generator.gui.component.textfield;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import fr.cubiccl.generator.gui.component.button.CButton;

public class CSpinner extends CEntry implements ActionListener
{
	private static final long serialVersionUID = -2618376540277157790L;

	private CButton buttonPlus, buttonMinus;
	private int index;
	private int[] values;

	public CSpinner(String textID, int... values)
	{
		super(textID);
		this.index = 0;
		this.values = values;

		GridBagConstraints gbc = this.container.gbc;
		++gbc.gridx;
		this.container.add(this.buttonMinus = new CButton(null), gbc);
		++gbc.gridx;
		this.container.add(this.buttonPlus = new CButton(null), gbc);

		this.buttonMinus.setText(" -  ");
		this.buttonPlus.setText(" + ");
		this.buttonMinus.addActionListener(this);
		this.buttonPlus.addActionListener(this);
		this.updateDisplay();
		this.addIntFilter();
		this.addKeyListener(new KeyListener()
		{

			@Override
			public void keyPressed(KeyEvent arg0)
			{}

			@Override
			public void keyReleased(KeyEvent e)
			{
				if (e.getKeyCode() == KeyEvent.VK_ENTER) verifyInput();
			}

			@Override
			public void keyTyped(KeyEvent arg0)
			{}
		});
		this.addFocusListener(new FocusListener()
		{

			@Override
			public void focusGained(FocusEvent e)
			{}

			@Override
			public void focusLost(FocusEvent e)
			{
				verifyInput();
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.buttonPlus)
		{
			this.verifyInput();
			if (this.index < this.values.length - 1) ++this.index;
		} else if (e.getSource() == this.buttonMinus)
		{
			this.verifyInput();
			if (this.index > 0) --this.index;
		}
		this.updateDisplay();
		for (ActionListener listener : this.getActionListeners())
			listener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, ""));
	}

	public int getValue()
	{
		if (this.values.length == 0) return 0;
		return this.values[this.index];
	}

	public void setValues(int... values)
	{
		this.values = values;
		if (this.index >= this.values.length) this.index = this.values.length - 1;
		this.updateDisplay();
	}

	private void updateDisplay()
	{
		this.setText(Integer.toString(this.getValue()));
	}

	private void verifyInput()
	{
		int input = Integer.parseInt(this.getText());

		for (int i = 0; i < this.values.length; ++i)
			if (this.values[i] == input)
			{
				this.index = i;
				this.updateDisplay();
				return;
			}

		int below = 0, belowD = input - this.values[0], above = this.values.length - 1, aboveD = this.values[this.values.length - 1] - input;
		for (int i = 0; i < this.values.length; ++i)
		{
			int value = this.values[i];
			if (value < input)
			{
				below = i;
				belowD = input - value;
			}
			if (value > input)
			{
				above = i;
				aboveD = value - input;
				break;
			}
		}
		if (aboveD > belowD) this.index = below;
		else this.index = above;
		this.updateDisplay();
	}

}
