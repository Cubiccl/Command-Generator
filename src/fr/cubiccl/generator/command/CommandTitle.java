package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gui.component.combobox.OptionCombobox;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelListJsonMessage;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelTarget;
import fr.cubiccl.generator.gui.component.textfield.CGEntry;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

public class CommandTitle extends Command implements ActionListener
{
	private OptionCombobox comboboxMode;
	private CGEntry entryFadeIn, entryStay, entryFadeOut;
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
		this.panelJson.setVisible(mode.equals("title") || mode.equals("subtitle") || mode.equals("actionbar"));
		this.entryFadeIn.container.setVisible(mode.equals("times"));
		this.entryStay.container.setVisible(mode.equals("times"));
		this.entryFadeOut.container.setVisible(mode.equals("times"));
	}

	@Override
	public CGPanel createGUI()
	{
		CGPanel panel = new CGPanel();
		GridBagConstraints gbc = panel.createGridBagLayout();

		panel.add(this.labelDescription(), gbc);
		++gbc.gridy;
		panel.add(this.comboboxMode = new OptionCombobox("title.mode", "title", "subtitle", "actionbar", "times", "clear", "reset"), gbc);
		++gbc.gridy;
		panel.add(this.panelTarget = new PanelTarget(PanelTarget.PLAYERS_ONLY), gbc);
		++gbc.gridy;
		panel.add(this.panelJson = new PanelListJsonMessage(), gbc);
		panel.add((this.entryFadeIn = new CGEntry(new Text("title.fade_in"), "0", Text.INTEGER)).container, gbc);
		++gbc.gridy;
		panel.add((this.entryStay = new CGEntry(new Text("title.stay"), "0", Text.INTEGER)).container, gbc);
		++gbc.gridy;
		panel.add((this.entryFadeOut = new CGEntry(new Text("title.fade_out"), "0", Text.INTEGER)).container, gbc);

		this.entryFadeIn.container.setVisible(false);
		this.entryStay.container.setVisible(false);
		this.entryFadeOut.container.setVisible(false);
		this.comboboxMode.addActionListener(this);

		this.entryFadeIn.addIntFilter();
		this.entryStay.addIntFilter();
		this.entryFadeOut.addIntFilter();

		return panel;
	}

	@Override
	public String generate() throws CommandGenerationException
	{
		String mode = this.comboboxMode.getValue();
		String command = "/title " + this.panelTarget.generateTarget().toCommand() + " " + mode;
		if (mode.equals("title") || mode.equals("subtitle") || mode.equals("actionbar")) return command + " "
				+ this.panelJson.generateMessage(Tags.JSON_LIST).valueForCommand();
		if (mode.equals("times"))
		{
			this.entryFadeIn.checkValueSuperior(CGEntry.INTEGER, 0);
			this.entryStay.checkValueSuperior(CGEntry.INTEGER, 0);
			this.entryFadeOut.checkValueSuperior(CGEntry.INTEGER, 0);
			return command + " " + this.entryFadeIn.getText() + " " + this.entryStay.getText() + " " + this.entryFadeOut.getText();
		}
		return command;
	}
}
