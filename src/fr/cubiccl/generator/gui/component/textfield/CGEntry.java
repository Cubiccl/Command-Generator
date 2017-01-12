package fr.cubiccl.generator.gui.component.textfield;

import java.awt.GridBagConstraints;

import fr.cubi.cubigui.CPanel;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.utils.Text;

public class CGEntry extends CGTextField
{
	private static final long serialVersionUID = 1L;

	public final CPanel container;
	public final CGLabel label;

	public CGEntry(String textID)
	{
		this(textID == null ? null : new Text(textID), null);
	}

	public CGEntry(Text text, String defaultValue, Text suggestedText)
	{
		super(suggestedText);
		this.setText(defaultValue);
		this.label = new CGLabel(text);

		this.container = new CPanel();
		GridBagConstraints gbc = this.container.createGridBagLayout();
		this.container.add(this.label, gbc);
		++gbc.gridx;
		this.container.add(this, gbc);
	}

	public CGEntry(Text text, Text suggestedText)
	{
		this(text, "", suggestedText);
	}

}
