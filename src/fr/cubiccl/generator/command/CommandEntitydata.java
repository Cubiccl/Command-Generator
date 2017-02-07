package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;

import fr.cubiccl.generator.gameobject.LivingEntity;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gameobject.tags.NBTReader;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.target.Target;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelEntity;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelTarget;
import fr.cubiccl.generator.utils.CommandGenerationException;

public class CommandEntitydata extends Command
{
	private PanelEntity panelEntity;
	private PanelTarget panelTarget;

	public CommandEntitydata(String id)
	{
		super(id, id + " <entity> <dataTag>", 3);
	}

	@Override
	public CGPanel createGUI()
	{
		CGPanel panel = new CGPanel();
		GridBagConstraints gbc = panel.createGridBagLayout();

		panel.add(this.labelDescription(), gbc);
		++gbc.gridy;
		panel.add(this.panelTarget = new PanelTarget(PanelTarget.ENTITIES_ONLY), gbc);
		++gbc.gridy;
		panel.add(new CGLabel("entitydata.explain"), gbc);
		++gbc.gridy;
		panel.add(this.panelEntity = new PanelEntity("entitydata.nbt"), gbc);

		return panel;
	}

	@Override
	public String generate() throws CommandGenerationException
	{
		LivingEntity e = this.panelEntity.generate();
		return this.id + " " + this.panelTarget.generate().toCommand() + " " + e.nbt.valueForCommand();
	}

	@Override
	protected void readArgument(int index, String argument, String[] fullCommand) throws CommandGenerationException
	{
		// this.id <entity> <dataTag>

		if (index == 1) this.panelTarget.setupFrom(Target.createFrom(argument));
		if (index == 2)
		{
			TagCompound t = (TagCompound) NBTReader.read(argument, true, false);
			String[] applications = t.findApplications();
			if (applications.length != 0) this.panelEntity.setEntity(ObjectRegistry.entities.find(applications[0]));
			this.panelEntity.setTags(t.value());
		}
	}

}
