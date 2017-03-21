package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import fr.cubiccl.generator.gameobject.Coordinates;
import fr.cubiccl.generator.gameobject.LivingEntity;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gameobject.tags.NBTReader;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelCoordinates;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelEntity;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

public class CommandSummon extends Command
{
	private PanelCoordinates panelCoordinates;
	private PanelEntity panelEntity;

	public CommandSummon()
	{
		super("summon", "summon <entity> [<x> <y> <z>] [dataTag]", 2, 3, 6);
	}

	@Override
	public CGPanel createGUI()
	{
		CGPanel panel = new CGPanel();
		GridBagConstraints gbc = panel.createGridBagLayout();

		panel.add(this.labelDescription(), gbc);
		++gbc.gridy;
		panel.add(this.panelCoordinates = new PanelCoordinates("summon.coordinates"), gbc);
		++gbc.gridy;
		panel.add(this.panelEntity = new PanelEntity("summon.entity", true, true, false), gbc);

		this.panelCoordinates.addArgumentChangeListener(this);
		this.panelEntity.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				updateTranslations();
			}
		});

		return panel;
	}

	@Override
	protected void defaultGui()
	{
		this.panelCoordinates.setupFrom(new Coordinates(0, 0, 0, true, true, true));
		this.panelEntity.setTags(new Tag[0]);
	}

	@Override
	protected Text description()
	{
		return this.defaultDescription().addReplacement("<entity>", this.panelEntity.selectedEntity().name())
				.addReplacement("<coordinates>", this.panelCoordinates.displayCoordinates());
	}

	@Override
	public String generate() throws CommandGenerationException
	{
		LivingEntity e = this.panelEntity.generate();
		return this.id + " " + e.entity.id + " " + this.panelCoordinates.generate().toCommand() + " " + e.nbt.valueForCommand();
	}

	@Override
	protected void readArgument(int index, String argument, String[] fullCommand) throws CommandGenerationException
	{
		// summon <entity> [x] [y] [z] [dataTag]
		if (index == 1) this.panelEntity.setEntity(ObjectRegistry.entities.find(argument));
		if (index == 2) this.panelCoordinates.setupFrom(Coordinates.createFrom(argument, fullCommand[3], fullCommand[4]));
		if (index == 5) this.panelEntity.setTags(((TagCompound) NBTReader.read(argument, true, false)).value());
	}

}
