package fr.cubiccl.generator.gui.component.panel.utils;

import java.awt.GridLayout;

import javax.swing.JPanel;

import fr.cubiccl.generator.gui.component.combobox.OptionCombobox;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.utils.Text;

public class ComboboxPanel extends ConfirmPanel
{
	private static final long serialVersionUID = -3957744287487251022L;

	public final OptionCombobox combobox;

	public ComboboxPanel(Text description, String prefix, String... options)
	{
		super();
		JPanel panel = new JPanel(new GridLayout(2, 1));
		this.combobox = new OptionCombobox(prefix, options);
		panel.add(new CGLabel(description));
		panel.add(this.combobox);
		this.setMainComponent(panel);
	}

}
