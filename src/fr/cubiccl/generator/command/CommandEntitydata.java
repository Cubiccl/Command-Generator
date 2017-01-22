package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;

import fr.cubiccl.generator.gameobject.LivingEntity;
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
		super(id);
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
		LivingEntity e = this.panelEntity.generateEntity();
		return "/" + this.id + " " + this.panelTarget.generateTarget().toCommand() + " " + e.nbt.valueForCommand();
	}

}
