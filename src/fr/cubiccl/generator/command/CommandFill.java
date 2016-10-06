package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import fr.cubiccl.generator.gameobject.PlacedBlock;
import fr.cubiccl.generator.gui.component.combobox.OptionCombobox;
import fr.cubiccl.generator.gui.component.panel.CPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelBlock;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelCoordinates;
import fr.cubiccl.generator.utils.CommandGenerationException;

public class CommandFill extends Command implements ActionListener
{
	private OptionCombobox comboboxMode;
	private PanelBlock panelBlockFill, panelBlockReplace;
	private PanelCoordinates panelCoordinatesStart, panelCoordinatesEnd;

	public CommandFill()
	{
		super("fill");
	}

	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		this.panelBlockReplace.setVisible(this.comboboxMode.getValue().equals("filter"));
	}

	@Override
	public CPanel createGUI()
	{
		CPanel panel = new CPanel();
		GridBagConstraints gbc = panel.createGridBagLayout();

		panel.add(this.labelDescription(), gbc);
		++gbc.gridy;
		panel.add(this.panelCoordinatesStart = new PanelCoordinates("testforblocks.source.start"), gbc);
		++gbc.gridy;
		panel.add(this.panelCoordinatesEnd = new PanelCoordinates("testforblocks.source.end"), gbc);
		++gbc.gridy;
		panel.add(this.comboboxMode = new OptionCombobox("fill.mode", "replace", "destroy", "keep", "hollow", "outline", "filter"), gbc);
		++gbc.gridy;
		panel.add(this.panelBlockFill = new PanelBlock("fill.block"), gbc);
		++gbc.gridy;
		panel.add(this.panelBlockReplace = new PanelBlock("fill.block.replace"), gbc);

		this.panelBlockReplace.setVisible(false);
		this.comboboxMode.addActionListener(this);

		return panel;
	}

	@Override
	public String generate() throws CommandGenerationException
	{
		PlacedBlock block = this.panelBlockFill.generateBlock();
		String command = "/fill " + this.panelCoordinatesStart.generateCoordinates().toCommand() + " "
				+ this.panelCoordinatesEnd.generateCoordinates().toCommand() + " " + block.block.idString + " " + block.data + " ";
		if (!this.comboboxMode.getValue().equals("filter")) return command + this.comboboxMode.getValue();
		PlacedBlock block2 = this.panelBlockReplace.generateBlock();
		return command + " replace " + block2.block.idString + " " + block2.data;
	}
}
