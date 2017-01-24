package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;

import fr.cubiccl.generator.gui.component.combobox.OptionCombobox;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelCoordinates;
import fr.cubiccl.generator.utils.CommandGenerationException;

public class CommandTestforblocks extends Command
{
	private OptionCombobox comboboxMode;
	private PanelCoordinates panelCoordinatesSourceStart, panelCoordinatesSourceEnd, panelCoordinatesDestination;

	public CommandTestforblocks()
	{
		super("testforblocks");
	}

	@Override
	public CGPanel createGUI()
	{
		CGPanel panel = new CGPanel();
		GridBagConstraints gbc = panel.createGridBagLayout();

		++gbc.gridwidth;
		panel.add(this.labelDescription(), gbc);
		++gbc.gridy;
		--gbc.gridwidth;
		panel.add(this.panelCoordinatesSourceStart = new PanelCoordinates("testforblocks.source.start"), gbc);
		++gbc.gridx;
		panel.add(this.panelCoordinatesSourceEnd = new PanelCoordinates("testforblocks.source.end"), gbc);
		--gbc.gridx;
		++gbc.gridy;
		panel.add(this.panelCoordinatesDestination = new PanelCoordinates("testforblocks.destination"), gbc);
		++gbc.gridx;
		panel.add(this.comboboxMode = new OptionCombobox("testforblocks.mode", "all", "masked"), gbc);

		return panel;
	}

	@Override
	public String generate() throws CommandGenerationException
	{
		return this.id + " " + this.panelCoordinatesSourceStart.generateCoordinates().toCommand() + " "
				+ this.panelCoordinatesSourceEnd.generateCoordinates().toCommand() + " " + this.panelCoordinatesDestination.generateCoordinates().toCommand()
				+ " " + this.comboboxMode.getValue();
	}

}
