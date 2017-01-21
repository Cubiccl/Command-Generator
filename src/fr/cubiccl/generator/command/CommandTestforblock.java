package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;

import fr.cubiccl.generator.gameobject.PlacedBlock;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelBlock;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelCoordinates;
import fr.cubiccl.generator.utils.CommandGenerationException;

public class CommandTestforblock extends Command
{
	private PanelBlock panelBlock;
	private PanelCoordinates panelCoordinates;

	public CommandTestforblock()
	{
		super("testforblock");
	}

	@Override
	public CGPanel createGUI()
	{
		CGPanel panel = new CGPanel();
		GridBagConstraints gbc = panel.createGridBagLayout();

		panel.add(this.labelDescription(), gbc);
		++gbc.gridy;
		panel.add(this.panelCoordinates = new PanelCoordinates("testforblock.coordinates"), gbc);
		++gbc.gridy;
		panel.add(this.panelBlock = new PanelBlock("testforblock.block"), gbc);

		return panel;
	}

	@Override
	public String generate() throws CommandGenerationException
	{
		PlacedBlock block = this.panelBlock.generateBlock();
		String nbt = block.nbt.valueForCommand();
		return "/testforblock " + this.panelCoordinates.generateCoordinates().toCommand() + " " + block.block.id() + " " + block.data
				+ (nbt.equals("{}") ? "" : " " + nbt);
	}

}
