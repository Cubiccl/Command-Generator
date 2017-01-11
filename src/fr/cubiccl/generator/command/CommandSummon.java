package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;

import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelCoordinates;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelEntity;
import fr.cubiccl.generator.utils.CommandGenerationException;

public class CommandSummon extends Command
{
	private PanelCoordinates panelCoordinates;
	private PanelEntity panelEntity;

	public CommandSummon()
	{
		super("summon");
	}

	@Override
	public CGPanel createGUI()
	{
		CGPanel panel = new CGPanel();
		GridBagConstraints gbc = panel.createGridBagLayout();

		panel.add(this.labelDescription(), gbc);
		++gbc.gridy;
		panel.add(this.panelCoordinates = new PanelCoordinates("summon.coordinates"), gbc);
		++gbc.gridy;
		panel.add(this.panelEntity = new PanelEntity("summon.entity"), gbc);

		return panel;
	}

	@Override
	public String generate() throws CommandGenerationException
	{
		return "/summon " + this.panelEntity.selectedEntity().id + " " + this.panelCoordinates.generateCoordinates().toCommand();
	}

}
