package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;

import fr.cubiccl.generator.gameobject.Coordinates;
import fr.cubiccl.generator.gameobject.PlacedBlock;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gameobject.tags.NBTReader;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelBlock;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelCoordinates;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

public class CommandBlockdata extends Command
{
	private PanelBlock panelBlock;
	private PanelCoordinates panelCoordinates;

	public CommandBlockdata()
	{
		super("blockdata", "blockdata <x> <y> <z> <dataTag>", 5);
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
		panel.add(this.panelBlock = new PanelBlock("setblock.block_data"), gbc);

		return panel;
	}

	@Override
	protected Text description()
	{
		Text d = this.defaultDescription();
		d.addReplacement("<coordinates>", this.panelCoordinates.displayCoordinates());
		return d;
	}

	@Override
	public String generate() throws CommandGenerationException
	{
		PlacedBlock block = this.panelBlock.generate();
		return this.id + " " + this.panelCoordinates.generate().toCommand() + " " + block.nbt.valueForCommand();
	}

	@Override
	protected void readArgument(int index, String argument, String[] fullCommand) throws CommandGenerationException
	{
		if (index == 1) this.panelCoordinates.setupFrom(Coordinates.createFrom(argument, fullCommand[2], fullCommand[3]));
		else if (index == 4)
		{
			TagCompound t = (TagCompound) NBTReader.read(argument, true, false);
			String[] application = t.findApplications();
			if (application.length != 0) this.panelBlock.setBlock(ObjectRegistry.blocks.find(application[0]));
			this.panelBlock.setTags(t.value());
		}
	}
}
