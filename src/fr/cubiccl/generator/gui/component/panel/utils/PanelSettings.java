package fr.cubiccl.generator.gui.component.panel.utils;

import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gui.component.button.CGRadioButton;
import fr.cubiccl.generator.gui.component.combobox.CGComboBox;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.utils.IStateListener;
import fr.cubiccl.generator.utils.Settings;
import fr.cubiccl.generator.utils.Settings.Language;
import fr.cubiccl.generator.utils.Text;

public class PanelSettings extends ConfirmPanel implements IStateListener<PanelSettings>
{
	private static final long serialVersionUID = 761491984316094420L;

	private CGRadioButton buttonSlashYes, buttonSlashNo;
	private CGComboBox comboboxLanguage;
	private Language[] languages;

	public PanelSettings()
	{
		super("settings.title", null);
		this.languages = Language.getLanguages();
		this.buttonOK.setText(new Text("settings.save"));
		String[] langs = new String[this.languages.length];
		for (int i = 0; i < langs.length; ++i)
			langs[i] = this.languages[i].name;

		CGPanel panelSlash = new CGPanel("settings.slash.title");
		GridBagConstraints gbc = panelSlash.createGridBagLayout();
		panelSlash.add(new CGLabel("settings.slash.description"), gbc);
		++gbc.gridy;

		JPanel p = new JPanel(new GridLayout(1, 2));
		p.add(this.buttonSlashYes = new CGRadioButton("general.yes"));
		p.add(this.buttonSlashNo = new CGRadioButton("general.no"));
		panelSlash.add(p, gbc);

		CGPanel panelLang = new CGPanel("settings.lang");
		panelLang.add(this.comboboxLanguage = new CGComboBox(langs));

		CGPanel panelMain = new CGPanel();
		gbc = panelMain.createGridBagLayout();
		panelMain.add(panelSlash, gbc);
		++gbc.gridy;
		panelMain.add(panelLang, gbc);
		this.setMainComponent(panelMain);

		ButtonGroup group = new ButtonGroup();
		group.add(this.buttonSlashYes);
		group.add(this.buttonSlashNo);
		this.buttonSlashYes.setSelected(Boolean.parseBoolean(Settings.getSetting(Settings.SLASH)));
		this.buttonSlashNo.setSelected(!this.buttonSlashYes.isSelected());
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.buttonCancel) CommandGenerator.window.menubar.toggleMenu(true);
		super.actionPerformed(e);
	}

	@Override
	public boolean shouldStateClose(PanelSettings panel)
	{
		Settings.setSetting(Settings.SLASH, Boolean.toString(this.buttonSlashYes.isSelected()));
		Settings.setSetting(Settings.LANG, this.languages[this.comboboxLanguage.getSelectedIndex()].codeName);
		CommandGenerator.window.menubar.toggleMenu(true);
		return true;
	}

}
