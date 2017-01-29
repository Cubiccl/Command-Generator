package fr.cubiccl.generator.gui.component.menubar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JMenuBar;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gui.component.interfaces.ITranslated;
import fr.cubiccl.generator.gui.component.panel.utils.PanelCommandHistory;
import fr.cubiccl.generator.gui.component.panel.utils.PanelSettings;
import fr.cubiccl.generator.utils.Lang;

public class CMenuBar extends JMenuBar implements ITranslated, ActionListener
{
	private static final long serialVersionUID = 2644541217645898670L;

	private CMenuItem history, settings, exit;

	public CMenuBar()
	{
		this.add(this.history = new CMenuItem());
		this.add(this.settings = new CMenuItem());
		this.add(Box.createHorizontalGlue());
		this.add(this.exit = new CMenuItem());

		this.history.addActionListener(this);
		this.settings.addActionListener(this);
		this.exit.addActionListener(this);

		this.updateTranslations();
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.history)
		{
			this.toggleMenu(false);
			CommandGenerator.stateManager.setState(new PanelCommandHistory(), null);
		}
		if (e.getSource() == this.settings)
		{
			this.toggleMenu(false);
			PanelSettings p = new PanelSettings();
			CommandGenerator.stateManager.setState(p, p);
		}
		if (e.getSource() == this.exit) CommandGenerator.window.dispose();
	}

	public void toggleMenu(boolean enabled)
	{
		this.history.setEnabled(enabled);
		this.settings.setEnabled(enabled);
	}

	@Override
	public void updateTranslations()
	{
		this.history.setText(Lang.translate("command.history"));
		this.settings.setText(Lang.translate("menu.settings"));
		this.exit.setText(Lang.translate("menu.exit"));
	}

}
