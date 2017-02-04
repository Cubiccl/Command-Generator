package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import fr.cubiccl.generator.gui.component.combobox.OptionCombobox;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelBlock;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelCoordinates;
import fr.cubiccl.generator.utils.CommandGenerationException;

public class CommandClone extends Command implements ActionListener
{
	private OptionCombobox comboboxMaskMode, comboboxCloneMode;
	private PanelBlock panelBlock;
	private PanelCoordinates panelCoordinatesSourceStart, panelCoordinatesSourceEnd, panelCoordinatesDestination;

	public CommandClone()
	{
		super("clone");
	}

	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		this.panelBlock.setVisible(this.comboboxMaskMode.getValue().equals("filtered"));
	}

	@Override
	public CGPanel createGUI()
	{
		CGPanel panel = new CGPanel();
		GridBagConstraints gbc = panel.createGridBagLayout();

		CGPanel panelModes = new CGPanel();
		GridBagConstraints gbc2 = panelModes.createGridBagLayout();
		gbc2.anchor = GridBagConstraints.NORTH;
		gbc2.fill = GridBagConstraints.HORIZONTAL;
		panelModes.add(this.comboboxCloneMode = new OptionCombobox("clone.mode", "normal", "force", "move"), gbc2);
		++gbc2.gridy;
		panelModes.add(this.comboboxMaskMode = new OptionCombobox("clone.mode", "replace", "masked", "filtered"), gbc2);

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
		gbc.anchor = GridBagConstraints.NORTH;
		panel.add(panelModes, gbc);
		--gbc.gridx;
		++gbc.gridy;
		++gbc.gridwidth;
		panel.add(this.panelBlock = new PanelBlock("clone.block"), gbc);

		this.panelBlock.setVisible(false);
		this.comboboxMaskMode.addActionListener(this);

		return panel;
	}

	@Override
	public String generate() throws CommandGenerationException
	{
		return this.id
				+ " "
				+ this.panelCoordinatesSourceStart.generate().toCommand()
				+ " "
				+ this.panelCoordinatesSourceEnd.generate().toCommand()
				+ " "
				+ this.panelCoordinatesDestination.generate().toCommand()
				+ " "
				+ this.comboboxMaskMode.getValue()
				+ " "
				+ this.comboboxCloneMode.getValue()
				+ (this.comboboxMaskMode.getValue().equals("filtered") ? (" " + this.panelBlock.selectedBlock().id() + " " + this.panelBlock.selectedDamage())
						: "");
	}
}
