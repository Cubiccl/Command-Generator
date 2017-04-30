package fr.cubiccl.generator.gui.component.panel.advancement;

import javax.swing.BorderFactory;

import fr.cubiccl.generator.gameobject.baseobjects.Entity;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gameobject.utils.TestValue;
import fr.cubiccl.generator.gui.component.button.CGCheckBox;
import fr.cubiccl.generator.gui.component.combobox.ObjectCombobox;
import fr.cubiccl.generator.gui.component.panel.tag.PanelRangedValue;
import fr.cubiccl.generator.utils.Text;

public class PanelCriteriaEntity extends PanelTestValues
{
	private static final long serialVersionUID = 2027530477236120267L;

	private CGCheckBox checkboxEntity, checkboxDistance;
	private ObjectCombobox<Entity> comboboxEntity;
	private PanelRangedValue panelDistance;

	public PanelCriteriaEntity(String entityName, String distanceName)
	{
		this.checkboxEntity = this.addComponent(entityName, (this.comboboxEntity = new ObjectCombobox<Entity>(ObjectRegistry.entities.list(true))).container);
		this.checkboxDistance = this.addComponent(distanceName, this.panelDistance = new PanelRangedValue(null, Text.INTEGER));
		this.panelDistance.setBorder(BorderFactory.createTitledBorder((String) null));
	}

	public boolean generateDistance(TestValue distance)
	{
		if (this.checkboxDistance.isSelected())
		{
			this.panelDistance.generateValue(distance);
			return true;
		}
		return false;
	}

	public Entity getEntity()
	{
		if (this.checkboxEntity.isSelected()) return this.comboboxEntity.getSelectedObject();
		return null;
	}

	public void setDistance(TestValue distance)
	{
		this.checkboxDistance.setSelected(true);
		this.panelDistance.setupFrom(distance);
		this.updateCheckboxes();
	}

	public void setEntity(Entity entity)
	{
		this.checkboxEntity.setSelected(true);
		this.comboboxEntity.setSelected(entity);
		this.updateCheckboxes();
	}

}
