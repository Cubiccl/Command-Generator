package fr.cubiccl.generator.gui.component.menubar;

import java.awt.Font;

import javax.swing.JMenuItem;

import fr.cubiccl.generator.gui.DisplayUtils;
import fr.cubiccl.generator.gui.component.ui.CButtonUI;

public class CMenuItem extends JMenuItem
{
	private static final long serialVersionUID = 7525223427463117L;

	public CMenuItem()
	{
		this.setFont(DisplayUtils.FONT.deriveFont(Font.BOLD, 12));
		this.setBorderPainted(true);
		this.setUI(new CButtonUI());
	}

}
