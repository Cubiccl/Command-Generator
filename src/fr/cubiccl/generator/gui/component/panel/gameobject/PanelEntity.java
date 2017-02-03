package fr.cubiccl.generator.gui.component.panel.gameobject;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import fr.cubiccl.generator.gameobject.LivingEntity;
import fr.cubiccl.generator.gameobject.baseobjects.Entity;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gui.component.combobox.ObjectCombobox;
import fr.cubiccl.generator.gui.component.label.ImageLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;

public class PanelEntity extends CGPanel implements ActionListener
{
	private static final long serialVersionUID = -7130115756741628375L;

	private ObjectCombobox<Entity> comboboxEntity;
	private ImageLabel labelImage;
	private PanelTags panelTags;

	public PanelEntity(String titleID)
	{
		this(titleID, true);
	}

	public PanelEntity(String titleID, boolean hasNBT)
	{
		super(titleID);
		GridBagConstraints gbc = this.createGridBagLayout();
		gbc.anchor = GridBagConstraints.WEST;

		this.add(this.labelImage = new ImageLabel(), gbc);
		++gbc.gridx;
		this.add((this.comboboxEntity = new ObjectCombobox<Entity>(ObjectRegistry.entities.list())).container, gbc);

		--gbc.gridx;
		++gbc.gridy;
		++gbc.gridwidth;
		this.panelTags = new PanelTags("entity.tags", Tag.ENTITY);
		if (hasNBT) this.add(this.panelTags, gbc);

		this.labelImage.setImage(this.selectedEntity().texture());
		this.comboboxEntity.addActionListener(this);
		this.panelTags.setTargetObject(this.selectedEntity());
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		this.labelImage.setImage(this.selectedEntity().texture());
		this.panelTags.setTargetObject(this.selectedEntity());
	}

	public LivingEntity generateEntity()
	{
		return new LivingEntity(this.selectedEntity(), this.panelTags.generateTags(Tags.ENTITY));
	}

	public Entity selectedEntity()
	{
		return this.comboboxEntity.getSelectedObject();
	}

	public void setEntity(Entity entity)
	{
		this.comboboxEntity.setSelected(entity);
		this.panelTags.setTargetObject(entity);
		this.labelImage.setImage(this.selectedEntity().texture());
	}

	public void setTags(Tag[] value)
	{
		this.panelTags.setTags(value);
	}

	public void setupFrom(LivingEntity entity)
	{
		this.setEntity(entity.entity);
		this.setTags(entity.nbt.value());
	}

}
