package fr.cubiccl.generator.gui.component.panel.gameobject;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import fr.cubiccl.generator.gameobject.LivingEntity;
import fr.cubiccl.generator.gameobject.baseobjects.Entity;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gameobject.registries.ObjectSaver;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gui.component.combobox.ObjectCombobox;
import fr.cubiccl.generator.gui.component.interfaces.ICustomObject;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.label.ImageLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.utils.PanelCustomObject;

public class PanelEntity extends CGPanel implements ActionListener, ICustomObject<LivingEntity>
{
	private static final long serialVersionUID = -7130115756741628375L;

	private ObjectCombobox<Entity> comboboxEntity;
	private CGLabel labelExplainTagOnly;
	private ImageLabel labelImage;
	private PanelTags panelTags;

	public PanelEntity(String titleID)
	{
		this(titleID, true, true, true);
	}

	public PanelEntity(String titleID, boolean hasNBT)
	{
		this(titleID, hasNBT, true, true);
	}

	public PanelEntity(String titleID, boolean hasNBT, boolean customObjects, boolean includePlayer)
	{
		super(titleID);
		GridBagConstraints gbc = this.createGridBagLayout();
		gbc.anchor = GridBagConstraints.WEST;

		gbc.gridheight = 2;
		this.add(this.labelImage = new ImageLabel(), gbc);
		gbc.gridheight = 1;
		++gbc.gridx;
		++gbc.gridwidth;
		this.add((this.comboboxEntity = new ObjectCombobox<Entity>(ObjectRegistry.entities.list(includePlayer))).container, gbc);
		++gbc.gridy;
		--gbc.gridwidth;
		this.add(this.labelExplainTagOnly = new CGLabel("entitydata.explain"), gbc);
		++gbc.gridx;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.fill = GridBagConstraints.NONE;
		if (customObjects) this.add(new PanelCustomObject<LivingEntity, LivingEntity>(this, ObjectSaver.entities), gbc);
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.fill = GridBagConstraints.HORIZONTAL;

		gbc.gridx = 0;
		++gbc.gridy;
		gbc.gridwidth = 3;
		this.panelTags = new PanelTags("entity.tags", Tag.ENTITY);
		if (hasNBT) this.add(this.panelTags, gbc);

		this.labelImage.setImage(this.selectedEntity().texture());
		this.labelExplainTagOnly.setVisible(false);
		this.comboboxEntity.addActionListener(this);
		this.panelTags.setTargetObject(this.selectedEntity());
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		this.labelImage.setImage(this.selectedEntity().texture());
		this.panelTags.setTargetObject(this.selectedEntity());
	}

	public void addActionListener(ActionListener actionListener)
	{
		this.comboboxEntity.addActionListener(actionListener);
	}

	@Override
	public LivingEntity generate()
	{
		return new LivingEntity(this.selectedEntity(), this.panelTags.generateTags(Tags.ENTITY));
	}

	public Entity selectedEntity()
	{
		return this.comboboxEntity.getSelectedObject();
	}

	public void setEntity(Entity entity)
	{
		if (entity == null) return;
		this.comboboxEntity.setSelected(entity);
		this.panelTags.setTargetObject(entity);
		this.labelImage.setImage(this.selectedEntity().texture());
	}

	public void setLabelExplainVisible(boolean visible)
	{
		this.labelExplainTagOnly.setVisible(visible);
	}

	public void setTags(Tag[] value)
	{
		this.panelTags.setTags(value);
	}

	@Override
	public void setupFrom(LivingEntity entity)
	{
		this.setEntity(entity.getEntity());
		if (this.panelTags.isVisible()) this.setTags(entity.getNbt().value());
	}

}
