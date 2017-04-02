package fr.cubiccl.generator.gui.component.menubar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JMenuBar;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gui.component.interfaces.ITranslated;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelCustomObjects;
import fr.cubiccl.generator.gui.component.panel.utils.PanelCommandHistory;
import fr.cubiccl.generator.gui.component.panel.utils.PanelSettings;
import fr.cubiccl.generator.utils.Lang;
import fr.cubiccl.generator.utils.Settings;

public class CMenuBar extends JMenuBar implements ITranslated, ActionListener
{
	private static final long serialVersionUID = 2644541217645898670L;

	private CMenuItem objects, history, modeCommands, modeLootTables, modeSpeedrun, modeData, settings, exit;
	private boolean objectsEnabled = true;

	public CMenuBar()
	{
		this.add(this.objects = new CMenuItem());
		this.add(this.history = new CMenuItem());
		this.add(this.modeCommands = new CMenuItem());
		this.add(this.modeLootTables = new CMenuItem());
		this.add(this.modeSpeedrun = new CMenuItem());
		this.add(this.modeData = new CMenuItem());
		this.add(this.settings = new CMenuItem());
		this.add(Box.createHorizontalGlue());
		this.add(this.exit = new CMenuItem());

		this.objects.addActionListener(this);
		this.history.addActionListener(this);
		this.modeCommands.addActionListener(this);
		this.modeLootTables.addActionListener(this);
		this.modeData.addActionListener(this);
		this.modeSpeedrun.addActionListener(this);
		this.settings.addActionListener(this);
		this.exit.addActionListener(this);

		this.modeData.setText("Data mode");
		this.modeData.setVisible(false);
		this.modeCommands.setVisible(false);

		this.updateTranslations();
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.objects)
		{
			this.toggleObjects(false);
			PanelCustomObjects p = new PanelCustomObjects();
			CommandGenerator.stateManager.setState(p, null);
		} else if (e.getSource() == this.history)
		{
			this.toggleMenu(false);
			CommandGenerator.stateManager.setState(new PanelCommandHistory(), null);
		} else if (e.getSource() == this.modeCommands)
		{
			CommandGenerator.setCurrentMode(CommandGenerator.COMMANDS);
			this.modeCommands.setVisible(false);
			this.modeLootTables.setVisible(true);
			this.modeSpeedrun.setVisible(true);
			// this.modeData.setVisible(Settings.testMode);
		} else if (e.getSource() == this.modeLootTables)
		{
			CommandGenerator.setCurrentMode(CommandGenerator.LOOT_TABLES);
			this.modeCommands.setVisible(true);
			this.modeLootTables.setVisible(true);
			this.modeSpeedrun.setVisible(false);
			this.modeData.setVisible(Settings.testMode);
		} else if (e.getSource() == this.modeSpeedrun)
		{
			CommandGenerator.setCurrentMode(CommandGenerator.SPEEDRUN);
			this.modeCommands.setVisible(true);
			this.modeLootTables.setVisible(false);
			// this.modeData.setVisible(Settings.testMode);
		} else if (e.getSource() == this.modeData)
		{
			CommandGenerator.setCurrentMode(CommandGenerator.DATA);
			this.modeCommands.setVisible(true);
			this.modeLootTables.setVisible(true);
			this.modeSpeedrun.setVisible(true);
			// this.modeData.setVisible(false);
		} else if (e.getSource() == this.settings)
		{
			this.toggleMenu(false);
			PanelSettings p = new PanelSettings();
			CommandGenerator.stateManager.setState(p, p);
		} else if (e.getSource() == this.exit) CommandGenerator.window.dispose();
	}

	public void toggleMenu(boolean enabled)
	{
		if (enabled) this.objects.setEnabled(this.objectsEnabled);
		else this.objects.setEnabled(false);
		this.history.setEnabled(enabled);
		this.modeCommands.setEnabled(enabled);
		this.modeLootTables.setEnabled(enabled);
		this.modeSpeedrun.setEnabled(enabled);
		this.modeData.setEnabled(enabled);
		this.settings.setEnabled(enabled);
	}

	public void toggleObjects(boolean enabled)
	{
		this.objectsEnabled = enabled;
		this.objects.setEnabled(enabled);
	}

	@Override
	public void updateTranslations()
	{
		this.objects.setText(Lang.translate("menu.objects"));
		this.history.setText(Lang.translate("command.history"));
		this.modeCommands.setText(Lang.translate("menu.command"));
		this.modeLootTables.setText(Lang.translate("menu.loot_table"));
		this.modeSpeedrun.setText(Lang.translate("menu.speedrun"));
		this.settings.setText(Lang.translate("menu.settings"));
		this.exit.setText(Lang.translate("menu.exit"));
	}

}
