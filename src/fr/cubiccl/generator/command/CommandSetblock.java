package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;

import fr.cubiccl.generator.gameobject.Coordinates;
import fr.cubiccl.generator.gameobject.PlacedBlock;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gameobject.tags.NBTReader;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gui.component.combobox.OptionCombobox;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelBlock;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelCoordinates;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Lang;
import fr.cubiccl.generator.utils.Text;

public class CommandSetblock extends Command
{
	private OptionCombobox comboboxMode;
	private PanelBlock panelBlock;
	private PanelCoordinates panelCoordinates;

	public CommandSetblock()
	{
		super("setblock", "setblock <x> <y> <z> <block> [dataValue|state] [oldBlockHandling] [dataTag]", 5, 6, 7, 8);
	}

	@Override
	public CGPanel createUI()
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

		this.panelCoordinates.addArgumentChangeListener(this);
		this.panelBlock.addArgumentChangeListener(this);

		return panel;
	}

	@Override
	protected void resetUI()
	{
		this.panelBlock.setData(0);
		this.panelBlock.setTags(new Tag[0]);
	}

	@Override
	protected Text description()
	{
		Text d = this.defaultDescription();
		d.addReplacement("<coordinates>", this.panelCoordinates.displayCoordinates());
		d.addReplacement("<block>", Lang.translateObject(this.panelBlock.selectedBlock(), this.panelBlock.selectedDamage()));
		return d;
	}

	@Override
	public String generate() throws CommandGenerationException
	{
		PlacedBlock block = this.panelBlock.generate();
		String nbt = block.getNbt().valueForCommand();
		return this.id + " " + this.panelCoordinates.generate().toCommand() + " " + block.getBlock().id() + " " + block.getData() + " "
				+ this.comboboxMode.getValue() + (nbt.equals("{}") ? "" : " " + nbt);
	}

	@Override
	protected void readArgument(int index, String argument, String[] fullCommand) throws CommandGenerationException
	{
		// setblock <x> <y> <z> <block> [dataValue] [oldBlockHandling] [dataTag]
		if (index == 1) this.panelCoordinates.setupFrom(Coordinates.createFrom(argument, fullCommand[2], fullCommand[3]));
		if (index == 4) this.panelBlock.setBlock(ObjectRegistry.blocks.find(argument));
		if (index == 5 && !argument.equals("-1")) try
		{
			this.panelBlock.setData(Integer.parseInt(argument));
		} catch (Exception e)
		{
			this.panelBlock.setData(this.panelBlock.selectedBlock().damageFromState(argument));
		}
		if (index == 6) this.comboboxMode.setValue(argument);
		if (index == 7) this.panelBlock.setTags(((TagCompound) NBTReader.read(argument, true, false)).value());
	}

}
