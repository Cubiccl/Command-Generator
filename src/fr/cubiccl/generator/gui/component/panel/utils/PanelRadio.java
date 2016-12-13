package fr.cubiccl.generator.gui.component.panel.utils;

import java.awt.GridLayout;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;

import fr.cubiccl.generator.gui.component.button.CGRadioButton;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.utils.Text;

public class PanelRadio extends ConfirmPanel
{
	private static final long serialVersionUID = 6177840747991757189L;

	private CGRadioButton[] buttons;

	public PanelRadio(Text description, String prefix, String... values)
	{
		super();
		JPanel panel = new JPanel(new GridLayout(2, 1));
		panel.add(new CGLabel(description));

		JPanel answers = new JPanel(new GridLayout(1, values.length));
		ButtonGroup group = new ButtonGroup();
		this.buttons = new CGRadioButton[values.length];
		for (int i = 0; i < values.length; ++i)
		{
			answers.add(this.buttons[i] = new CGRadioButton(prefix + "." + values[i]));
			group.add(this.buttons[i]);
		}

		this.buttons[0].setSelected(true);
		panel.add(answers);
		this.setMainComponent(panel);
	}

	public int getSelected()
	{
		for (int i = 0; i < this.buttons.length; ++i)
			if (this.buttons[i].isSelected()) return i;
		return 0;
	}

	public void setSelected(int i)
	{
		if (i >= 0 && i < this.buttons.length) this.buttons[i].setSelected(true);
	}

}
