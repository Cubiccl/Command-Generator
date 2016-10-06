package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;

import fr.cubiccl.generator.gui.component.panel.CPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelListJsonMessage;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelTarget;
import fr.cubiccl.generator.utils.CommandGenerationException;

public class CommandTellraw extends Command
{
	private PanelListJsonMessage panelJson;
	private PanelTarget panelTarget;

	public CommandTellraw()
	{
		super("tellraw");
	}

	@Override
	public CPanel createGUI()
	{
		CPanel panel = new CPanel();
		GridBagConstraints gbc = panel.createGridBagLayout();

		panel.add(this.labelDescription(), gbc);
		++gbc.gridy;
		panel.add(this.panelTarget = new PanelTarget(PanelTarget.PLAYERS_ONLY), gbc);
		++gbc.gridy;
		panel.add(this.panelJson = new PanelListJsonMessage(), gbc);

		return panel;
	}

	@Override
	public String generate() throws CommandGenerationException
	{
		return "/tellraw " + this.panelTarget.generateTarget().toCommand() + " " + this.panelJson.generateMessage().value();
	}

}
