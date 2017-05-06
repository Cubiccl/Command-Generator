package fr.cubiccl.generator.gui.component.panel.advancement;

import java.util.ArrayList;

import fr.cubiccl.generator.gameobject.baseobjects.Entity;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gui.component.combobox.ObjectCombobox;
import fr.cubiccl.generator.gui.component.panel.utils.PanelTestValues;

public class PanelCriteriaEntity extends PanelTestValues
{
	private static final long serialVersionUID = 2027530477236120267L;

	private ObjectCombobox<Entity> comboboxEntity;
	private PanelCriteriaDistance panelDistance;

	public PanelCriteriaEntity()
	{
		this(null);
	}

	public PanelCriteriaEntity(String titleID)
	{
		super(titleID);
		this.addComponent("criteria.entity.id", (this.comboboxEntity = new ObjectCombobox<Entity>(ObjectRegistry.entities.list(true))).container);
		this.addComponent("criteria.entity.distance", this.panelDistance = new PanelCriteriaDistance("criteria.entity.distance"));
	}

	public boolean checkInput()
	{
		if (this.isSelected(this.panelDistance)) return this.panelDistance.checkInput();
		return true;
	}

	public Tag[] generateTags()
	{
		ArrayList<Tag> tags = new ArrayList<Tag>();
		if (this.isSelected(this.comboboxEntity.container)) tags.add(Tags.CRITERIA_ENTITY_ID.create(this.comboboxEntity.getSelectedObject().id()));
		if (this.isSelected(this.panelDistance)) tags.add(this.panelDistance.generateValue(Tags.CRITERIA_DISTANCE));
		return tags.toArray(new Tag[tags.size()]);
	}

	public void setupFrom(TagCompound tag)
	{
		if (tag.hasTag(Tags.CRITERIA_ENTITY_ID))
		{
			this.select(this.comboboxEntity.container);
			this.comboboxEntity.setSelected(ObjectRegistry.entities.find(tag.getTag(Tags.CRITERIA_ENTITY_ID).value()));
		}
		if (tag.hasTag(Tags.CRITERIA_DISTANCE))
		{
			this.select(this.panelDistance);
			this.panelDistance.setupFrom(tag.getTag(Tags.CRITERIA_DISTANCE));
		}
	}

}
