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
import fr.cubiccl.generator.utils.Text;

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
		this.updateTranslations();
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

		this.panelCoordinatesStart.addArgumentChangeListener(this);
		this.panelCoordinatesEnd.addArgumentChangeListener(this);
		this.panelBlockFill.addArgumentChangeListener(this);
		this.panelBlockReplace.addArgumentChangeListener(this);

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
	protected Text description()
	{
		if (this.isFiltering()) { return new Text("command." + this.id + ".filter")
				.addReplacement("<block>", this.panelBlockFill.selectedBlock().name(this.panelBlockFill.selectedDamage()))
				.addReplacement("<filter>", this.panelBlockReplace.selectedBlock().name(this.panelBlockFill.selectedDamage()))
				.addReplacement("<source_start>", this.panelCoordinatesStart.displayCoordinates())
				.addReplacement("<source_end>", this.panelCoordinatesEnd.displayCoordinates()); }

		return this.defaultDescription().addReplacement("<block>", this.panelBlockFill.selectedBlock().name(this.panelBlockFill.selectedDamage()))
				.addReplacement("<source_start>", this.panelCoordinatesStart.displayCoordinates())
				.addReplacement("<source_end>", this.panelCoordinatesEnd.displayCoordinates());
	}

	@Override
	protected void finishReading()
	{
		boolean filter = this.isFiltering();
		this.panelBlockReplace.setVisible(filter);
		this.panelBlockFill.setHasNBT(!filter);
		this.checkboxData.setVisible(filter);
	}

	@Override
	public String generate() throws CommandGenerationException
	{
		PlacedBlock block = this.panelBlockFill.generate();
		String command = this.id + " " + this.panelCoordinatesStart.generate().toCommand() + " " + this.panelCoordinatesEnd.generate().toCommand() + " "
				+ block.getBlock().id() + " " + block.getData() + " ";

		if (!this.isFiltering())
		{
			String nbt = block.getNbt().valueForCommand();
			return command + this.comboboxMode.getValue() + (nbt.equals("{}") ? "" : " " + nbt);
		}

		PlacedBlock block2 = this.panelBlockReplace.generate();
		return command + " replace " + block2.getBlock().id() + (this.checkboxData.isSelected() ? "" : " " + block2.getBlock());
	}

	private boolean isFiltering()
	{
		return this.comboboxMode.getValue().equals("filter");
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
		{
			this.panelBlockFill.setData(this.panelBlockFill.selectedBlock().damageFromState(argument));
		}
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
		{
			this.panelBlockReplace.setData(this.panelBlockReplace.selectedBlock().damageFromState(argument));
		}
	}
}
