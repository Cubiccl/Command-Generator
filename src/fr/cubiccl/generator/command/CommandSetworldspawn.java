package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;

import fr.cubiccl.generator.gameobject.Coordinates;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelCoordinates;
import fr.cubiccl.generator.utils.CommandGenerationException;

public class CommandSetworldspawn extends Command
{
	private PanelCoordinates panelCoordinates;

	public CommandSetworldspawn()
	{
		super("setworldspawn", "setworldspawn <x> <y> <z>", 4);
	}

	@Override
	public CGPanel createGUI()
	{
		CGPanel panel = new CGPanel();
		GridBagConstraints gbc = panel.createGridBagLayout();

		panel.add(this.labelDescription(), gbc);
		++gbc.gridy;
		panel.add(this.panelCoordinates = new PanelCoordinates("spawnpoint.coordinates"), gbc);

		return panel;
	}

	@Override
	public String generate() throws CommandGenerationException
	{
		return this.id + " " + this.panelCoordinates.generate().toCommand();
	}

	@Override
	protected void readArgument(int index, String argument, String[] fullCommand) throws CommandGenerationException
	{
		if (index == 1) this.panelCoordinates.setupFrom(Coordinates.createFrom(argument, fullCommand[2], fullCommand[3]));
	}

}
