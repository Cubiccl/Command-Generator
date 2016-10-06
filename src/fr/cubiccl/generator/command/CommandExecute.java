package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import fr.cubiccl.generator.gameobject.PlacedBlock;
import fr.cubiccl.generator.gui.component.button.CCheckBox;
import fr.cubiccl.generator.gui.component.panel.CPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelBlock;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelCoordinates;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelTarget;
import fr.cubiccl.generator.utils.CommandGenerationException;

public class CommandExecute extends Command implements ActionListener
{
	private CCheckBox checkboxBlock;
	private PanelBlock panelBlock;
	private PanelCoordinates panelCoordinates, panelBlockCoordinates;
	private PanelTarget panelTarget;

	public CommandExecute()
	{
		super("execute");
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		boolean block = this.checkboxBlock.isSelected();
		this.panelBlock.setVisible(block);
		this.panelBlockCoordinates.setVisible(block);
	}

	@Override
	public CPanel createGUI()
	{
		CPanel panel = new CPanel();
		GridBagConstraints gbc = panel.createGridBagLayout();

		panel.add(this.labelDescription(), gbc);
		++gbc.gridy;
		panel.add(this.panelTarget = new PanelTarget("execute.target", PanelTarget.ALL_ENTITIES), gbc);
		++gbc.gridy;
		panel.add(this.panelCoordinates = new PanelCoordinates("execute.coordinates"), gbc);
		++gbc.gridy;
		panel.add(this.checkboxBlock = new CCheckBox("execute.detect"), gbc);
		++gbc.gridy;
		panel.add(this.panelBlock = new PanelBlock("execute.block"), gbc);
		++gbc.gridy;
		panel.add(this.panelBlockCoordinates = new PanelCoordinates("execute.coordinates.block"), gbc);

		this.panelCoordinates.setRelativeText("execute.relative");
		this.panelBlock.setVisible(false);
		this.panelBlockCoordinates.setVisible(false);
		this.checkboxBlock.addActionListener(this);

		return panel;
	}

	@Override
	public String generate() throws CommandGenerationException
	{
		String command = "/execute " + this.panelTarget.generateTarget().toCommand() + " " + this.panelCoordinates.generateCoordinates().toCommand() + " ";
		if (this.checkboxBlock.isSelected())
		{
			PlacedBlock block = this.panelBlock.generateBlock();
			command += "detect " + this.panelBlockCoordinates.generateCoordinates().toCommand() + " " + block.block.idString + " " + block.data + " ";
		}
		return command;
	}
}
