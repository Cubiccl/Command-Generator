package fr.cubiccl.generator.gui.component.panel;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import fr.cubiccl.generator.gui.component.interfaces.ITranslated;
import fr.cubiccl.generator.utils.Lang;

public class CPanel extends JPanel implements ITranslated
{
	private static final long serialVersionUID = 1L;

	private String titleID;
	public GridBagConstraints gbc;

	public CPanel()
	{
		this(null);
	}

	public CPanel(String titleID)
	{
		this.titleID = titleID;
		this.updateTranslations();
	}

	public GridBagConstraints createGridBagLayout()
	{
		this.setLayout(new GridBagLayout());
		this.gbc = new GridBagConstraints();
		this.gbc.gridx = 0;
		this.gbc.gridy = 0;
		this.gbc.gridwidth = 1;
		this.gbc.gridheight = 1;
		this.gbc.insets = new Insets(5, 5, 5, 5);
		return this.gbc;
	}

	@Override
	public void updateTranslations()
	{
		if (this.titleID != null) this.setBorder(BorderFactory.createTitledBorder(Lang.translate(this.titleID)));
		for (Component component : this.getComponents())
			if (component instanceof ITranslated) ((ITranslated) component).updateTranslations();
	}

}
