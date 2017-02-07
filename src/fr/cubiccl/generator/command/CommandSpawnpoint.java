package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;

import fr.cubiccl.generator.gameobject.Coordinates;
import fr.cubiccl.generator.gameobject.target.Target;
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
		super("spawnpoint", "spawnpoint <player> <x> <y> <z>", 5);
	}

	@Override
	public CGPanel createGUI()
	{
		CGPanel panel = new CGPanel();
		GridBagConstraints gbc = panel.createGridBagLayout();

		panel.add(this.labelDescription(), gbc);
		++gbc.gridy;
		panel.add(this.panelTarget = new PanelTarget(PanelTarget.PLAYERS_ONLY), gbc);
		++gbc.gridy;
		panel.add(this.panelCoordinates = new PanelCoordinates("spawnpoint.coordinates"), gbc);

		return panel;
	}

	@Override
	public String generate() throws CommandGenerationException
	{
		return this.id + " " + this.panelTarget.generate().toCommand() + " " + this.panelCoordinates.generate().toCommand();
	}

	@Override
	protected void readArgument(int index, String argument, String[] fullCommand) throws CommandGenerationException
	{
		if (index == 1) this.panelTarget.setupFrom(Target.createFrom(argument));
		if (index == 2) this.panelCoordinates.setupFrom(Coordinates.createFrom(argument, fullCommand[3], fullCommand[4]));
	}

}
