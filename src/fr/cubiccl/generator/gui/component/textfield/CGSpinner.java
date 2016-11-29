package fr.cubiccl.generator.gui.component.textfield;

import java.awt.GridBagConstraints;

import fr.cubi.cubigui.CSpinner;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.utils.Text;

public class CGSpinner extends CSpinner
{
	private static final long serialVersionUID = -2618376540277157790L;

	public final CGLabel label;

	public CGSpinner(String textID, int... values)
	{
		this(new Text(textID), values);
	}

	public CGSpinner(Text text, int... values)
	{
		super(text.toString(), values);
		this.label = new CGLabel(text);
		this.label.setHasColumn(true);
		super.label.setVisible(false);

		GridBagConstraints gbc = this.container.getGBC();
		gbc.gridx = 0;
		this.container.add(this.label, gbc);
	}

}
