package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import fr.cubiccl.generator.gameobject.Coordinates;
import fr.cubiccl.generator.gameobject.PlacedBlock;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gameobject.tags.NBTReader;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gui.component.button.CGCheckBox;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelBlock;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelCoordinates;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

public class CommandTestforblock extends Command implements ActionListener
{
	private CGCheckBox checkboxIgnoreData;
	private PanelBlock panelBlock;
	private PanelCoordinates panelCoordinates;

	public CommandTestforblock()
	{
		super("testforblock", "testforblock <x> <y> <z> <block> [dataValue] [dataTag]", 5, 6, 7);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		this.finishReading();
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
		++gbc.gridy;
		panel.add(this.checkboxIgnoreData = new CGCheckBox("testforblock.ignore_data"), gbc);

		this.checkboxIgnoreData.addActionListener(this);

		this.panelCoordinates.addArgumentChangeListener(this);
		this.panelBlock.addArgumentChangeListener(this);

		return panel;
	}

	@Override
	protected void defaultGui()
	{
		this.panelBlock.setTags(new Tag[0]);
		this.panelBlock.setData(0);
		this.panelBlock.setHasData(true);
		this.checkboxIgnoreData.setSelected(false);
	}

	@Override
	protected Text description()
	{
		Text d = this.defaultDescription();
		d.addReplacement("<coordinates>", this.panelCoordinates.displayCoordinates());
		d.addReplacement("<block>", this.panelBlock.selectedBlock().name(this.panelBlock.selectedDamage()));
		return d;
	}

	@Override
	protected void finishReading()
	{
		this.panelBlock.setHasData(!this.checkboxIgnoreData.isSelected());
	}

	@Override
	public String generate() throws CommandGenerationException
	{
		PlacedBlock block = this.panelBlock.generate();
		String nbt = block.getNbt().valueForCommand();
		return this.id + " " + this.panelCoordinates.generate().toCommand() + " " + block.getBlock().id() + " "
				+ (this.checkboxIgnoreData.isSelected() ? "-1" : block.getData()) + (nbt.equals("{}") ? "" : " " + nbt);
	}

	@Override
	protected void readArgument(int index, String argument, String[] fullCommand) throws CommandGenerationException
	{
		// testforblock <x> <y> <z> <block> [dataValue] [dataTag]
		if (index == 1) this.panelCoordinates.setupFrom(Coordinates.createFrom(argument, fullCommand[2], fullCommand[3]));
		if (index == 4)
		{
			this.panelBlock.setBlock(ObjectRegistry.blocks.find(argument));
			this.checkboxIgnoreData.setSelected(true);
		}
		if (index == 5 && !argument.equals("-1")) try
		{
			this.checkboxIgnoreData.setSelected(false);
			this.panelBlock.setData(Integer.parseInt(argument));
		} catch (Exception e)
		{
			this.panelBlock.setData(this.panelBlock.selectedBlock().damageFromState(argument));
		}
		if (index == 6) this.panelBlock.setTags(((TagCompound) NBTReader.read(argument, true, false)).value());
	}

}
