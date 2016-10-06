package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import fr.cubiccl.generator.gui.component.combobox.OptionCombobox;
import fr.cubiccl.generator.gui.component.panel.CPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelAchievement;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelTarget;
import fr.cubiccl.generator.utils.CommandGenerationException;

public class CommandAchievement extends Command implements ActionListener
{
	private OptionCombobox comboboxMode, comboboxNumber;
	private PanelAchievement panelAchievement;
	private PanelTarget panelTarget;

	public CommandAchievement()
	{
		super("achievement");
	}

	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		this.panelAchievement.setEnabled(this.comboboxMode.getValue().equals("all"));
	}

	@Override
	public CPanel createGUI()
	{
		CPanel panel = new CPanel();
		GridBagConstraints gbc = panel.createGridBagLayout();

		++gbc.gridwidth;
		panel.add(this.labelDescription(), gbc);
		++gbc.gridy;
		--gbc.gridwidth;
		panel.add(this.comboboxMode = new OptionCombobox("achievement", "give", "take"), gbc);
		++gbc.gridx;
		panel.add(this.comboboxNumber = new OptionCombobox("achievement", "one", "all"), gbc);
		--gbc.gridx;
		++gbc.gridy;
		++gbc.gridwidth;
		panel.add(this.panelAchievement = new PanelAchievement(), gbc);
		++gbc.gridy;
		panel.add(this.panelTarget = new PanelTarget("target.title.player", PanelTarget.PLAYERS_ONLY), gbc);

		this.comboboxMode.addActionListener(this);

		return panel;
	}

	@Override
	public String generate() throws CommandGenerationException
	{
		String command = "/" + this.id + " " + this.comboboxMode.getValue() + " ";
		if (this.comboboxNumber.getValue().equals("all")) command += "* ";
		else command += this.panelAchievement.getAchievement().id + " ";

		return command + this.panelTarget.generateTarget().toCommand();
	}

}
