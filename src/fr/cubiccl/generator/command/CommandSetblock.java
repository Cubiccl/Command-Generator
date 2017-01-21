package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;

import fr.cubiccl.generator.gameobject.PlacedBlock;
import fr.cubiccl.generator.gui.component.combobox.OptionCombobox;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelBlock;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelCoordinates;
import fr.cubiccl.generator.utils.CommandGenerationException;

public class CommandSetblock extends Command
{
	private OptionCombobox comboboxMode;
	private PanelBlock panelBlock;
	private PanelCoordinates panelCoordinates;

	public CommandSetblock()
	{
		super("setblock");
	}

	@Override
	public CGPanel createGUI()
	{
		CGPanel panel = new CGPanel();
		GridBagConstraints gbc = panel.createGridBagLayout();

		panel.add(this.labelDescription(), gbc);
		++gbc.gridy;
		panel.add(this.panelCoordinates = new PanelCoordinates("setblock.coordinates"), gbc);
		++gbc.gridy;
		panel.add(this.panelBlock = new PanelBlock("setblock.block"), gbc);
		++gbc.gridy;
		panel.add(this.comboboxMode = new OptionCombobox("setblock.mode", "destroy", "keep", "replace"), gbc);

		return panel;
	}

	@Override
	public String generate() throws CommandGenerationException
	{
		PlacedBlock block = this.panelBlock.generateBlock();
		String nbt = block.nbt.valueForCommand();
		return "/setblock " + this.panelCoordinates.generateCoordinates().toCommand() + " " + block.block.id() + " " + block.data + " "
				+ this.comboboxMode.getValue() + (nbt.equals("{}") ? "" : " " + nbt);
	}

}
