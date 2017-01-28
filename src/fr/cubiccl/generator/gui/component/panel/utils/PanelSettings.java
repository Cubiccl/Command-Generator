package fr.cubiccl.generator.gui.component.panel.utils;

import java.awt.GridBagConstraints;
import java.awt.GridLayout;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gui.component.button.CGRadioButton;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.utils.IStateListener;
import fr.cubiccl.generator.utils.Settings;
import fr.cubiccl.generator.utils.Text;

public class PanelSettings extends ConfirmPanel implements IStateListener<PanelSettings>
{
	private static final long serialVersionUID = 761491984316094420L;

	private CGRadioButton buttonSlashYes, buttonSlashNo;

	public PanelSettings()
	{
		super("settings.title", null);
		this.buttonOK.setText(new Text("settings.save"));

		CGPanel panelSlash = new CGPanel("settings.slash.title");
		GridBagConstraints gbc = panelSlash.createGridBagLayout();
		panelSlash.add(new CGLabel("settings.slash.description"), gbc);
		++gbc.gridy;

		JPanel p = new JPanel(new GridLayout(1, 2));
		p.add(this.buttonSlashYes = new CGRadioButton("general.yes"));
		p.add(this.buttonSlashNo = new CGRadioButton("general.no"));
		panelSlash.add(p, gbc);

		CGPanel panelMain = new CGPanel();
		gbc = panelMain.createGridBagLayout();
		panelMain.add(panelSlash, gbc);
		this.setMainComponent(panelMain);

		ButtonGroup group = new ButtonGroup();
		group.add(this.buttonSlashYes);
		group.add(this.buttonSlashNo);
		this.buttonSlashYes.setSelected(Boolean.parseBoolean(Settings.getSetting(Settings.SLASH)));
		this.buttonSlashNo.setSelected(!this.buttonSlashYes.isSelected());
	}

	@Override
	public boolean shouldStateClose(PanelSettings panel)
	{
		Settings.setSetting(Settings.SLASH, Boolean.toString(this.buttonSlashYes.isSelected()));
		CommandGenerator.window.menubar.toggleMenu(true);
		return true;
	}

}
