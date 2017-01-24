package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import fr.cubiccl.generator.gameobject.PlacedBlock;
import fr.cubiccl.generator.gui.component.button.CGCheckBox;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelBlock;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelCoordinates;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelTarget;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

public class CommandExecute extends Command implements ActionListener
{
	private CGCheckBox checkboxBlock;
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
	public CGPanel createGUI()
	{
		CGPanel panel = new CGPanel();
		GridBagConstraints gbc = panel.createGridBagLayout();

		panel.add(this.labelDescription(), gbc);
		++gbc.gridy;
		panel.add(this.panelTarget = new PanelTarget("execute.target", PanelTarget.ALL_ENTITIES), gbc);
		++gbc.gridy;
		panel.add(this.panelCoordinates = new PanelCoordinates("execute.coordinates"), gbc);
		++gbc.gridy;
		panel.add(this.checkboxBlock = new CGCheckBox("execute.detect"), gbc);
		++gbc.gridy;
		panel.add(this.panelBlock = new PanelBlock("execute.block"), gbc);
		++gbc.gridy;
		panel.add(this.panelBlockCoordinates = new PanelCoordinates("execute.coordinates.block"), gbc);

		this.panelCoordinates.setRelativeText(new Text("execute.relative"));
		this.panelBlock.setVisible(false);
		this.panelBlockCoordinates.setVisible(false);
		this.checkboxBlock.addActionListener(this);

		return panel;
	}

	@Override
	public String generate() throws CommandGenerationException
	{
		String command = this.id + " " + this.panelTarget.generateTarget().toCommand() + " " + this.panelCoordinates.generateCoordinates().toCommand() + " ";
		if (this.checkboxBlock.isSelected())
		{
			PlacedBlock block = this.panelBlock.generateBlock();
			command += "detect " + this.panelBlockCoordinates.generateCoordinates().toCommand() + " " + block.block.id() + " " + block.data + " ";
		}
		return command;
	}
}
