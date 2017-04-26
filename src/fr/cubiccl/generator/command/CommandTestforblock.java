package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import fr.cubiccl.generator.gameobject.Coordinates;
import fr.cubiccl.generator.gameobject.PlacedBlock;
import fr.cubiccl.generator.gameobject.baseobjects.BlockState;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gameobject.tags.NBTReader;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelBlock;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelCoordinates;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelStateSelection;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Lang;
import fr.cubiccl.generator.utils.Text;

public class CommandTestforblock extends Command implements ActionListener
{
	private PanelBlock panelBlock;
	private PanelCoordinates panelCoordinates;
	private PanelStateSelection panelStates;

	public CommandTestforblock()
	{
		super("testforblock", "testforblock <x> <y> <z> <block> [dataValue|state] [dataTag]", 5, 6, 7);
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
		panel.add(this.panelStates = new PanelStateSelection("testforblock.states", this.panelBlock.selectedBlock()), gbc);

		this.panelCoordinates.addArgumentChangeListener(this);
		this.panelBlock.addArgumentChangeListener(this);
		this.panelBlock.setHasData(false);

		return panel;
	}

	@Override
	protected void defaultGui()
	{
		this.panelBlock.setTags(new Tag[0]);
		this.panelStates.clear();
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
		HashMap<String, String> states = this.panelStates.values;
		return this.id + " " + this.panelCoordinates.generate().toCommand() + " " + block.getBlock().id() + " "
				+ (states.size() == 0 ? "-1" : BlockState.toCommand(states)) + (nbt.equals("{}") ? "" : " " + nbt);
	}

	@Override
	protected void readArgument(int index, String argument, String[] fullCommand) throws CommandGenerationException
	{
		// testforblock <x> <y> <z> <block> [dataValue] [dataTag]
		if (index == 1) this.panelCoordinates.setupFrom(Coordinates.createFrom(argument, fullCommand[2], fullCommand[3]));
		if (index == 4) this.panelBlock.setBlock(ObjectRegistry.blocks.find(argument));
		if (index == 5 && !argument.equals("-1")) try
		{
			int damage = Integer.parseInt(argument);
			HashMap<String, String> states = this.panelBlock.selectedBlock().findStatesFromDamage(damage);
			for (String id : states.keySet())
				this.panelStates.setState(id, states.get(id));
		} catch (NumberFormatException e)
		{
			HashMap<String, String> states = BlockState.parseState(argument);
			for (String id : states.keySet())
				this.panelStates.setState(id, states.get(id));
		}
		if (index == 6) this.panelBlock.setTags(((TagCompound) NBTReader.read(argument, true, false)).value());
	}

	@Override
	public void updateTranslations()
	{
		super.updateTranslations();
		if (this.panelBlock != null && this.panelBlock.selectedBlock() != this.panelStates.currentBlock()) this.panelStates.setBlock(this.panelBlock
				.selectedBlock());
	}

}
