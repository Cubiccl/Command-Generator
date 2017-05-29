package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import fr.cubiccl.generator.gameobject.Coordinates;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gui.component.combobox.OptionCombobox;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelBlock;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelCoordinates;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

public class CommandClone extends Command implements ActionListener
{
	private OptionCombobox comboboxMaskMode, comboboxCloneMode;
	private PanelBlock panelBlock;
	private PanelCoordinates panelCoordinatesSourceStart, panelCoordinatesSourceEnd, panelCoordinatesDestination;

	public CommandClone()
	{
		super("clone", "clone <source coordinates start> <source coordinates end> <destination coordinates> [maskMode] [cloneMode] [TileName] [dataValue]", 10,
				11, 12, 13, 14);
	}

	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		this.panelBlock.setVisible(this.comboboxMaskMode.getValue().equals("filtered"));
	}

	@Override
	public CGPanel createUI()
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
		this.panelBlock.setHasNBT(false);
		this.comboboxMaskMode.addActionListener(this);

		this.panelCoordinatesSourceStart.addArgumentChangeListener(this);
		this.panelCoordinatesSourceEnd.addArgumentChangeListener(this);
		this.panelCoordinatesDestination.addArgumentChangeListener(this);

		return panel;
	}

	@Override
	protected void resetUI()
	{
		this.comboboxMaskMode.setValue("replace");
		this.comboboxCloneMode.setValue("normal");
	}

	@Override
	protected Text description()
	{
		Text d = this.defaultDescription();
		d.addReplacement("<source_start>", this.panelCoordinatesSourceStart.displayCoordinates());
		d.addReplacement("<source_end>", this.panelCoordinatesSourceEnd.displayCoordinates());
		d.addReplacement("<destination>", this.panelCoordinatesDestination.displayCoordinates());
		return d;
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

	@Override
	protected void readArgument(int index, String argument, String[] fullCommand) throws CommandGenerationException
	{
		// clone <source coordinates start> <source coordinates end> <destination coordinates> [maskMode] [cloneMode] [TileName] [dataValue]
		if (index == 1) this.panelCoordinatesSourceStart.setupFrom(Coordinates.createFrom(argument, fullCommand[2], fullCommand[3]));
		if (index == 4) this.panelCoordinatesSourceEnd.setupFrom(Coordinates.createFrom(argument, fullCommand[5], fullCommand[6]));
		if (index == 7) this.panelCoordinatesDestination.setupFrom(Coordinates.createFrom(argument, fullCommand[8], fullCommand[9]));
		if (index == 10) this.comboboxMaskMode.setValue(argument);
		if (index == 11) this.comboboxCloneMode.setValue(argument);
		if (index == 12) this.panelBlock.setBlock(ObjectRegistry.blocks.find(argument));
		if (index == 13) try
		{
			this.panelBlock.setData(Integer.parseInt(argument));
		} catch (Exception e)
		{
			this.panelBlock.setData(this.panelBlock.selectedBlock().damageFromState(argument));
		}
	}
}
