package fr.cubiccl.generator.gui.component.menubar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuBar;
import javax.swing.JOptionPane;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gui.component.interfaces.ITranslated;
import fr.cubiccl.generator.utils.Lang;

public class CMenuBar extends JMenuBar implements ITranslated, ActionListener
{
	private static final long serialVersionUID = 2644541217645898670L;

	private CMenuItem settings, exit;

	public CMenuBar()
	{
		this.add(this.settings = new CMenuItem());
		this.add(this.exit = new CMenuItem());

		this.settings.addActionListener(this);
		this.exit.addActionListener(this);

		this.updateTranslations();
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.settings) JOptionPane.showMessageDialog(null, "Coming soon as DLC Kappa");
		if (e.getSource() == this.exit) CommandGenerator.window.dispose();
	}

	@Override
	public void updateTranslations()
	{
		this.settings.setText(Lang.translate("menu.settings"));
		this.exit.setText(Lang.translate("menu.exit"));
	}

}
