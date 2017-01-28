package fr.cubiccl.generator.gui.component.panel.utils;

import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gui.component.CGList;
import fr.cubiccl.generator.gui.component.button.CGRadioButton;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.utils.IStateListener;
import fr.cubiccl.generator.utils.Settings;
import fr.cubiccl.generator.utils.Settings.Version;
import fr.cubiccl.generator.utils.Text;

public class PanelSettings extends ConfirmPanel implements IStateListener<PanelSettings>
{
	private static final long serialVersionUID = 761491984316094420L;

	private CGRadioButton buttonSlashYes, buttonSlashNo;
	private CGList listVersion;

	public PanelSettings()
	{
		super("settings.title", null);
		this.buttonOK.setText(new Text("settings.save"));
		Version[] versions = Version.getVersions();
		String[] names = new String[versions.length];
		for (int i = 0; i < names.length; ++i)
			names[i] = versions[i].name;

		CGPanel panelSlash = new CGPanel("settings.slash.title");
		GridBagConstraints gbc = panelSlash.createGridBagLayout();
		panelSlash.add(new CGLabel("settings.slash.description"), gbc);
		++gbc.gridy;

		JPanel p = new JPanel(new GridLayout(1, 2));
		p.add(this.buttonSlashYes = new CGRadioButton("general.yes"));
		p.add(this.buttonSlashNo = new CGRadioButton("general.no"));
		panelSlash.add(p, gbc);

		CGPanel panelVersion = new CGPanel("settings.version.title");
		gbc = panelVersion.createGridBagLayout();
		panelVersion.add(new CGLabel("settings.version.description"), gbc);
		++gbc.gridy;
		panelVersion.add(this.listVersion = new CGList(names), gbc);

		CGPanel panelMain = new CGPanel();
		gbc = panelMain.createGridBagLayout();
		panelMain.add(panelSlash, gbc);
		++gbc.gridy;
		panelMain.add(panelVersion, gbc);
		this.setMainComponent(panelMain);

		ButtonGroup group = new ButtonGroup();
		group.add(this.buttonSlashYes);
		group.add(this.buttonSlashNo);
		this.buttonSlashYes.setSelected(Boolean.parseBoolean(Settings.getSetting(Settings.SLASH)));
		this.buttonSlashNo.setSelected(!this.buttonSlashYes.isSelected());

		this.listVersion.setSelectedValue(Settings.version().name, true);
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
		Settings.setSetting(Settings.MINECRAFT_VERSION, this.listVersion.getValue());
		CommandGenerator.window.menubar.toggleMenu(true);
		return true;
	}

}
