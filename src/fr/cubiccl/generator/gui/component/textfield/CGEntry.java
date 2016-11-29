package fr.cubiccl.generator.gui.component.textfield;

import java.awt.GridBagConstraints;

import fr.cubi.cubigui.CEntry;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.utils.Text;

public class CGEntry extends CEntry
{
	private static final long serialVersionUID = 1L;

	public final CGLabel label;

	public CGEntry(String textID, String defaultValue)
	{
		this(new Text(textID), defaultValue);
	}

	public CGEntry(Text text)
	{
		this(text, "");
	}

	public CGEntry(Text text, String defaultValue)
	{
		super(text.toString(), defaultValue);
		this.label = new CGLabel(text);
		this.label.setHasColumn(true);
		super.label.setVisible(false);

		GridBagConstraints gbc = this.container.getGBC();
		gbc.gridx = 0;
		this.container.add(this.label, gbc);
	}

}
