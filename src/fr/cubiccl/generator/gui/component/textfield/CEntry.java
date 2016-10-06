package fr.cubiccl.generator.gui.component.textfield;

import java.awt.GridBagConstraints;

import fr.cubiccl.generator.gui.component.label.CLabel;
import fr.cubiccl.generator.gui.component.panel.CPanel;

public class CEntry extends CTextField
{
	private static final long serialVersionUID = 1L;

	public final CPanel container;
	public final CLabel label;

	public CEntry(String textID)
	{
		this(textID, "");
	}

	public CEntry(String textID, String defaultValue)
	{
		super();
		this.setText(defaultValue);
		this.label = new CLabel(textID);
		this.label.setHasColumn(true);

		this.container = new CPanel();
		GridBagConstraints gbc = this.container.createGridBagLayout();
		this.container.add(this.label, gbc);
		++gbc.gridx;
		this.container.add(this, gbc);
	}

}
