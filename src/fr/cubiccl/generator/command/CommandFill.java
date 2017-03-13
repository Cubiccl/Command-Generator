package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import fr.cubiccl.generator.gameobject.Coordinates;
import fr.cubiccl.generator.gameobject.PlacedBlock;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gameobject.tags.NBTReader;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gui.component.button.CGCheckBox;
import fr.cubiccl.generator.gui.component.combobox.OptionCombobox;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelBlock;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelCoordinates;
import fr.cubiccl.generator.utils.CommandGenerationException;

public class CommandFill extends Command implements ActionListener
{
	private CGCheckBox checkboxData;
	private OptionCombobox comboboxMode;
	private PanelBlock panelBlockFill, panelBlockReplace;
	private PanelCoordinates panelCoordinatesStart, panelCoordinatesEnd;

	public CommandFill()
	{
		super("fill", "fill <x1> <y1> <z1> <x2> <y2> <z2> <block> [dataValue] [oldBlockHandling] [dataTag]\n"
				+ "fill <x1> <y1> <z1> <x2> <y2> <z2> <block> <dataValue> replace <replaceTileName> [replaceDataValue]", 8, 9, 10, 11, 12);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.comboboxMode) this.finishReading();
		else this.panelBlockReplace.setHasData(!this.checkboxData.isSelected());
	}

	@Override
	public CGPanel createGUI()
	{
		CGPanel panel = new CGPanel();
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
		panel.add(this.checkboxData = new CGCheckBox("fill.block.replace.ignore_data"), gbc);
		++gbc.gridy;
		panel.add(this.panelBlockReplace = new PanelBlock("fill.block.replace"), gbc);

		this.panelBlockReplace.setVisible(false);
		this.panelBlockReplace.setHasNBT(false);
		this.comboboxMode.addActionListener(this);
		this.checkboxData.addActionListener(this);
		return panel;
	}

	@Override
	protected void defaultGui()
	{
		this.panelBlockFill.setBlock(ObjectRegistry.blocks.find("stone"));
		this.panelBlockFill.setData(0);
		this.comboboxMode.setValue("replace");
		this.checkboxData.setSelected(false);
		this.panelBlockReplace.setHasData(true);
	}

	@Override
	protected void finishReading()
	{
		boolean filter = this.comboboxMode.getValue().equals("filter");
		this.panelBlockReplace.setVisible(filter);
		this.panelBlockFill.setHasNBT(!filter);
		this.checkboxData.setVisible(filter);
	}

	@Override
	public String generate() throws CommandGenerationException
	{
		PlacedBlock block = this.panelBlockFill.generate();
		String command = this.id + " " + this.panelCoordinatesStart.generate().toCommand() + " " + this.panelCoordinatesEnd.generate().toCommand() + " "
				+ block.block.id() + " " + block.data + " ";

		if (!this.comboboxMode.getValue().equals("filter"))
		{
			String nbt = block.nbt.toCommand(false);
			return command + this.comboboxMode.getValue() + (nbt.equals("{}") ? "" : " " + nbt);
		}

		PlacedBlock block2 = this.panelBlockReplace.generate();
		return command + " replace " + block2.block.id() + (this.checkboxData.isSelected() ? "" : " " + block2.data);
	}

	@Override
	protected void readArgument(int index, String argument, String[] fullCommand) throws CommandGenerationException
	{
		// fill <x1> <y1> <z1> <x2> <y2> <z2> <block> [dataValue] [oldBlockHandling] [dataTag]
		// fill <x1> <y1> <z1> <x2> <y2> <z2> <block> <dataValue> replace <replaceTileName> [replaceDataValue]

		if (index == 1) this.panelCoordinatesStart.setupFrom(Coordinates.createFrom(argument, fullCommand[2], fullCommand[3]));
		if (index == 4) this.panelCoordinatesEnd.setupFrom(Coordinates.createFrom(argument, fullCommand[5], fullCommand[6]));
		if (index == 7) this.panelBlockFill.setBlock(ObjectRegistry.blocks.find(argument));
		if (index == 8 && !argument.equals("-1")) try
		{
			this.panelBlockFill.setData(Integer.parseInt(argument));
		} catch (Exception e)
		{}
		if (index == 9) this.comboboxMode.setValue(argument);
		if (index == 10)
		{
			if (!argument.startsWith("{"))
			{
				this.comboboxMode.setValue("filter");
				this.panelBlockReplace.setBlock(ObjectRegistry.blocks.find(argument));
			} else this.panelBlockFill.setTags(((TagCompound) NBTReader.read(argument, true, false)).value());
		}
		if (index == 11) try
		{
			this.panelBlockReplace.setData(Integer.parseInt(argument));
			this.checkboxData.setSelected(true);
			this.panelBlockReplace.setHasData(false);
		} catch (Exception e)
		{}
	}
}
