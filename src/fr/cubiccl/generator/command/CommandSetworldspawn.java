package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;

import fr.cubiccl.generator.gui.component.panel.CPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelCoordinates;
import fr.cubiccl.generator.utils.CommandGenerationException;

public class CommandSetworldspawn extends Command
{
	private PanelCoordinates panelCoordinates;

	public CommandSetworldspawn()
	{
		super("setworldspawn");
	}

	@Override
	public CPanel createGUI()
	{
		CPanel panel = new CPanel();
		GridBagConstraints gbc = panel.createGridBagLayout();

		panel.add(this.labelDescription(), gbc);
		++gbc.gridy;
		panel.add(this.panelCoordinates = new PanelCoordinates("spawnpoint.coordinates"), gbc);

		return panel;
	}

	@Override
	public String generate() throws CommandGenerationException
	{
		return "/setworldspawn " + this.panelCoordinates.generateCoordinates().toCommand();
	}

}
