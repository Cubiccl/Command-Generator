package fr.cubiccl.generator.gui.component.panel.gameobject;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import fr.cubiccl.generator.gameobject.LivingEntity;
import fr.cubiccl.generator.gameobject.ObjectRegistry;
import fr.cubiccl.generator.gameobject.baseobjects.Entity;
import fr.cubiccl.generator.gui.component.combobox.ObjectCombobox;
import fr.cubiccl.generator.gui.component.label.ImageLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;

public class PanelEntity extends CGPanel implements ActionListener
{
	private static final long serialVersionUID = -7130115756741628375L;

	private ObjectCombobox<Entity> comboboxEntity;
	private ImageLabel labelImage;

	public PanelEntity(String titleID)
	{
		super(titleID);
		GridBagConstraints gbc = this.createGridBagLayout();

		this.add(this.comboboxEntity = new ObjectCombobox<Entity>(ObjectRegistry.getEntities()), gbc);
		++gbc.gridy;
		this.add(this.labelImage = new ImageLabel(this.selectedEntity().texture()), gbc);

		this.comboboxEntity.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		this.labelImage.setImage(this.selectedEntity().texture());
	}

	public LivingEntity generateEntity()
	{
		return new LivingEntity(this.selectedEntity());
	}

	public Entity selectedEntity()
	{
		return this.comboboxEntity.getSelectedObject();
	}

}
