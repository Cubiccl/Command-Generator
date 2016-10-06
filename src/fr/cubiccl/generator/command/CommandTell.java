package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;

import fr.cubiccl.generator.gui.component.panel.CPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelTarget;
import fr.cubiccl.generator.gui.component.textfield.CEntry;
import fr.cubiccl.generator.utils.CommandGenerationException;

public class CommandTell extends Command
{
	private CEntry entryMessage;
	private PanelTarget panelTarget;

	public CommandTell()
	{
		super("tell");
	}

	@Override
	public CPanel createGUI()
	{
		CPanel panel = new CPanel();
		GridBagConstraints gbc = panel.createGridBagLayout();

		panel.add(this.labelDescription(), gbc);
		++gbc.gridy;
		panel.add((this.entryMessage = new CEntry("say.message")).container, gbc);
		++gbc.gridy;
		panel.add(this.panelTarget = new PanelTarget(PanelTarget.PLAYERS_ONLY), gbc);

		return panel;
	}

	@Override
	public String generate() throws CommandGenerationException
	{
		return "/tell " + this.panelTarget.generateTarget().toCommand() + " " + this.entryMessage.getText();
	}

}
