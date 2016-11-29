package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;

import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelCoordinates;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelTarget;
import fr.cubiccl.generator.utils.CommandGenerationException;

public class CommandSpawnpoint extends Command
{
	private PanelCoordinates panelCoordinates;
	private PanelTarget panelTarget;

	public CommandSpawnpoint()
	{
		super("spawnpoint");
	}

	@Override
	public CGPanel createGUI()
	{
		CGPanel panel = new CGPanel();
		GridBagConstraints gbc = panel.createGridBagLayout();

		panel.add(this.labelDescription(), gbc);
		++gbc.gridy;
		panel.add(this.panelCoordinates = new PanelCoordinates("spawnpoint.coordinates"), gbc);
		++gbc.gridy;
		panel.add(this.panelTarget = new PanelTarget(PanelTarget.PLAYERS_ONLY), gbc);

		return panel;
	}

	@Override
	public String generate() throws CommandGenerationException
	{
		return "/spawnpoint " + this.panelCoordinates.generateCoordinates().toCommand() + " " + this.panelTarget.generateTarget().toCommand();
	}

}
