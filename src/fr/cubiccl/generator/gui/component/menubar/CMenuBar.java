package fr.cubiccl.generator.gui.component.menubar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JMenuBar;

import fr.cubi.cubigui.CMenu;
import fr.cubi.cubigui.CMenuItem;
import fr.cubi.cubigui.DisplayUtils;
import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gui.component.interfaces.ITranslated;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelCustomObjects;
import fr.cubiccl.generator.gui.component.panel.utils.PanelCommandHistory;
import fr.cubiccl.generator.gui.component.panel.utils.PanelSettings;
import fr.cubiccl.generator.utils.Lang;
import fr.cubiccl.generator.utils.Settings;
import fr.cubiccl.generator.utils.Text;

public class CMenuBar extends JMenuBar implements ITranslated, ActionListener
{
	private static final long serialVersionUID = 2644541217645898670L;

	private CMenu menuMode;
	private CMenuItem objects, history, modeCommands, modeLootTables, modeRecipes, modeAdvancements, modeData, modeMap, settings, about, exit;
	private boolean objectsEnabled = true;

	public CMenuBar()
	{
		this.menuMode = new CMenu("");
		this.menuMode.add(this.modeCommands = new CMenuItem(""));
		this.menuMode.add(this.modeLootTables = new CMenuItem(""));
		this.menuMode.add(this.modeRecipes = new CMenuItem(""));
		this.menuMode.add(this.modeAdvancements = new CMenuItem(""));
		this.menuMode.add(this.modeData = new CMenuItem(""));
		this.menuMode.add(this.modeMap = new CMenuItem(""));

		this.add(this.objects = new CMenuItem(""));
		this.add(this.history = new CMenuItem(""));
		this.add(this.menuMode);
		this.add(this.settings = new CMenuItem(""));
		this.add(Box.createHorizontalGlue());
		this.add(this.about = new CMenuItem(""));
		this.add(this.exit = new CMenuItem(""));

		this.objects.addActionListener(this);
		this.history.addActionListener(this);
		this.modeCommands.addActionListener(this);
		this.modeLootTables.addActionListener(this);
		this.modeRecipes.addActionListener(this);
		this.modeAdvancements.addActionListener(this);
		this.modeData.addActionListener(this);
		this.modeMap.addActionListener(this);
		this.settings.addActionListener(this);
		this.about.addActionListener(this);
		this.exit.addActionListener(this);

		this.modeData.setText("Data mode");
		this.modeData.setVisible(false);
		this.modeCommands.setVisible(false);
		this.modeMap.setVisible(false);

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
		} else if (e.getSource() == this.menuMode)
		;
		else if (e.getSource() == this.modeCommands) CommandGenerator.setCurrentMode(CommandGenerator.COMMANDS);
		else if (e.getSource() == this.modeLootTables) CommandGenerator.setCurrentMode(CommandGenerator.LOOT_TABLES);
		else if (e.getSource() == this.modeRecipes) CommandGenerator.setCurrentMode(CommandGenerator.RECIPES);
		else if (e.getSource() == this.modeAdvancements) CommandGenerator.setCurrentMode(CommandGenerator.ADVANCEMENTS);
		else if (e.getSource() == this.modeMap) CommandGenerator.setCurrentMode(CommandGenerator.MAP);
		else if (e.getSource() == this.modeData) CommandGenerator.setCurrentMode(CommandGenerator.DATA);
		else if (e.getSource() == this.settings)
		{
			this.toggleMenu(false);
			PanelSettings p = new PanelSettings();
			CommandGenerator.stateManager.setState(p, p);
		} else if (e.getSource() == this.about) DisplayUtils.showMessage(CommandGenerator.window, "",
				new Text("menu.about.text").addReplacement("<version>", Settings.GENERATOR_VERSION).toString());
		else if (e.getSource() == this.exit) CommandGenerator.window.dispose();

		this.modeCommands.setVisible(CommandGenerator.getCurrentMode() != CommandGenerator.COMMANDS);
		this.modeLootTables.setVisible(CommandGenerator.getCurrentMode() != CommandGenerator.LOOT_TABLES);
		this.modeRecipes.setVisible(CommandGenerator.getCurrentMode() != CommandGenerator.RECIPES);
		this.modeAdvancements.setVisible(CommandGenerator.getCurrentMode() != CommandGenerator.ADVANCEMENTS);
		this.modeMap.setVisible(CommandGenerator.getCurrentMode() != CommandGenerator.MAP);
	}

	public void toggleMenu(boolean enabled)
	{
		if (enabled) this.objects.setEnabled(this.objectsEnabled);
		else this.objects.setEnabled(false);
		this.history.setEnabled(enabled);
		this.menuMode.setEnabled(enabled);
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
		this.menuMode.setText(Lang.translate("menu.mode"));
		this.modeCommands.setText(Lang.translate("menu.command"));
		this.modeLootTables.setText(Lang.translate("menu.loot_table"));
		this.modeRecipes.setText(Lang.translate("menu.recipe"));
		this.modeAdvancements.setText(Lang.translate("menu.advancement"));
		this.modeMap.setText(Lang.translate("menu.map"));
		this.settings.setText(Lang.translate("menu.settings"));
		this.about.setText(Lang.translate("menu.about"));
		this.exit.setText(Lang.translate("menu.exit"));
	}

}
