package fr.cubiccl.generator.gui.component.panel.advancement;

import java.util.ArrayList;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gameobject.advancements.TestedEffect;
import fr.cubiccl.generator.gameobject.baseobjects.Entity;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gui.component.combobox.ObjectCombobox;
import fr.cubiccl.generator.gui.component.panel.utils.PanelObjectList;
import fr.cubiccl.generator.gui.component.panel.utils.PanelTestValues;
import fr.cubiccl.generator.utils.CommandGenerationException;

public class PanelCriteriaEntity extends PanelTestValues
{
	private static final long serialVersionUID = 2027530477236120267L;

	private ObjectCombobox<Entity> comboboxEntity;
	private PanelCriteriaDistance panelDistance;
	private PanelObjectList<TestedEffect> panelEffects;
	private PanelCriteriaLocation panelLocation;

	public PanelCriteriaEntity()
	{
		this(null);
	}

	public PanelCriteriaEntity(String titleID)
	{
		super(titleID);
		this.addComponent("criteria.entity.id", (this.comboboxEntity = new ObjectCombobox<Entity>(ObjectRegistry.entities.list(true))).container);
		this.addComponent("criteria.entity.distance", this.panelDistance = new PanelCriteriaDistance("criteria.entity.distance"));
		this.addComponent("criteria.entity.location", this.panelLocation = new PanelCriteriaLocation("criteria.entity.location"));
		this.addComponent("objects.effect", this.panelEffects = new PanelObjectList<TestedEffect>("objects.effect", (String) null, TestedEffect.class));
	}

	public boolean checkInput()
	{
		if (this.isSelected(this.panelDistance) && !this.panelDistance.checkInput()) return false;
		if (this.isSelected(this.panelLocation) && !this.panelLocation.checkInput()) return false;
		if (this.isSelected(this.panelEffects)) try
		{
			Tags.CRITERIA_EFFECTS.checkInput(this.panelEffects.values());
		} catch (CommandGenerationException e)
		{
			CommandGenerator.report(e);
			return false;
		}
		return true;
	}

	public Tag[] generateTags()
	{
		ArrayList<Tag> tags = new ArrayList<Tag>();
		if (this.isSelected(this.comboboxEntity.container)) tags.add(Tags.CRITERIA_ENTITY_ID.create(this.comboboxEntity.getSelectedObject().id()));
		if (this.isSelected(this.panelDistance)) tags.add(this.panelDistance.generateValue(Tags.CRITERIA_DISTANCE));
		if (this.isSelected(this.panelLocation)) tags.add(this.panelLocation.generateValue(Tags.CRITERIA_LOCATION));
		if (this.isSelected(this.panelEffects)) tags.add(Tags.CRITERIA_EFFECTS.create(this.panelEffects.values()));
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
		if (tag.hasTag(Tags.CRITERIA_LOCATION))
		{
			this.select(this.panelLocation);
			this.panelLocation.setupFrom(tag.getTag(Tags.CRITERIA_LOCATION));
		}
		if (tag.hasTag(Tags.CRITERIA_EFFECTS))
		{
			this.select(this.panelEffects);
			for (Tag t : tag.getTag(Tags.CRITERIA_EFFECTS).value())
				this.panelEffects.add(TestedEffect.createFrom((TagCompound) t));
		}
	}

}
