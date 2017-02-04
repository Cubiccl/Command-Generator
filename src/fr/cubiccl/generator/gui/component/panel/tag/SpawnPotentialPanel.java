package fr.cubiccl.generator.gui.component.panel.tag;

import java.awt.GridBagConstraints;

import fr.cubiccl.generator.gameobject.templatetags.custom.TemplateSpawnPotentials.SpawnPotential;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelEntity;
import fr.cubiccl.generator.gui.component.textfield.CGEntry;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

public class SpawnPotentialPanel extends CGPanel
{
	private static final long serialVersionUID = 8968332706433265004L;

	private CGEntry entryWeight;
	private PanelEntity panelEntity;

	public SpawnPotentialPanel()
	{
		super(null);
		GridBagConstraints gbc = this.createGridBagLayout();
		this.add((this.entryWeight = new CGEntry(new Text("entity.weight"), "1", new Text(">= 1", false))).container, gbc);
		++gbc.gridy;
		this.add(this.panelEntity = new PanelEntity("entity.spawn"), gbc);

		this.entryWeight.addIntFilter();
	}

	public SpawnPotential createSpawnPotential() throws CommandGenerationException
	{
		this.entryWeight.checkValueSuperior(CGEntry.INTEGER, 1);
		return new SpawnPotential(this.panelEntity.generate(), Integer.parseInt(this.entryWeight.getText()));
	}

	public void setupFrom(SpawnPotential spawnPotential)
	{
		this.entryWeight.setText(Integer.toString(spawnPotential.weight));
		this.panelEntity.setEntity(spawnPotential.entity.entity);
		this.panelEntity.setTags(spawnPotential.entity.nbt.value());
	}

}
