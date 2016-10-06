package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import fr.cubiccl.generator.gui.component.combobox.OptionCombobox;
import fr.cubiccl.generator.gui.component.panel.CPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelListJsonMessage;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelTarget;
import fr.cubiccl.generator.gui.component.textfield.CEntry;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Utils;

public class CommandTitle extends Command implements ActionListener
{
	private OptionCombobox comboboxMode;
	private CEntry entryFadeIn, entryStay, entryFadeOut;
	private PanelListJsonMessage panelJson;
	private PanelTarget panelTarget;

	public CommandTitle()
	{
		super("title");
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		String mode = this.comboboxMode.getValue();
		this.panelJson.setVisible(mode.equals("title") || mode.equals("subtitle"));
		this.entryFadeIn.container.setVisible(mode.equals("times"));
		this.entryStay.container.setVisible(mode.equals("times"));
		this.entryFadeOut.container.setVisible(mode.equals("times"));
	}

	@Override
	public CPanel createGUI()
	{
		CPanel panel = new CPanel();
		GridBagConstraints gbc = panel.createGridBagLayout();

		panel.add(this.labelDescription(), gbc);
		++gbc.gridy;
		panel.add(this.comboboxMode = new OptionCombobox("title.mode", "title", "subtitle", "times", "clear", "reset"), gbc);
		++gbc.gridy;
		panel.add(this.panelTarget = new PanelTarget(PanelTarget.PLAYERS_ONLY), gbc);
		++gbc.gridy;
		panel.add(this.panelJson = new PanelListJsonMessage(), gbc);
		panel.add((this.entryFadeIn = new CEntry("title.fade_in")).container, gbc);
		++gbc.gridy;
		panel.add((this.entryStay = new CEntry("title.stay")).container, gbc);
		++gbc.gridy;
		panel.add((this.entryFadeOut = new CEntry("title.fade_out")).container, gbc);

		this.entryFadeIn.container.setVisible(false);
		this.entryStay.container.setVisible(false);
		this.entryFadeOut.container.setVisible(false);
		this.comboboxMode.addActionListener(this);

		return panel;
	}

	@Override
	public String generate() throws CommandGenerationException
	{
		String mode = this.comboboxMode.getValue();
		String command = "/title " + this.panelTarget.generateTarget().toCommand() + " " + mode;
		if (mode.equals("title") || mode.equals("subtitle")) return command + " " + this.panelJson.generateMessage().value();
		if (mode.equals("times"))
		{
			Utils.checkIntegerSuperior(this.entryFadeIn.label.getAbsoluteText(), this.entryFadeIn.getText(), 0);
			Utils.checkIntegerSuperior(this.entryStay.label.getAbsoluteText(), this.entryStay.getText(), 0);
			Utils.checkIntegerSuperior(this.entryFadeOut.label.getAbsoluteText(), this.entryFadeOut.getText(), 0);
			return command + " " + this.entryFadeIn.getText() + " " + this.entryStay.getText() + " " + this.entryFadeOut.getText();
		}
		return command;
	}
}
