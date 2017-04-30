package fr.cubiccl.generator.gui.component.panel.advancement;

import java.util.ArrayList;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gameobject.baseobjects.Entity;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.utils.TestValue;
import fr.cubiccl.generator.gui.component.combobox.ObjectCombobox;
import fr.cubiccl.generator.gui.component.panel.tag.PanelRangedValue;
import fr.cubiccl.generator.gui.component.panel.utils.PanelTestValues;
import fr.cubiccl.generator.gui.component.textfield.CGEntry;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

public class PanelCriteriaEntity extends PanelTestValues
{
	private static final long serialVersionUID = 2027530477236120267L;

	private ObjectCombobox<Entity> comboboxEntity;
	public final TestValue distance;
	private PanelRangedValue panelDistance;

	public PanelCriteriaEntity()
	{
		this(null);
	}

	public PanelCriteriaEntity(String titleID)
	{
		super(titleID);
		this.distance = new TestValue(Tags.CRITERIA_DISTANCE, Tags.CRITERIA_DISTANCE_);
		this.addComponent("criteria.entity.id", (this.comboboxEntity = new ObjectCombobox<Entity>(ObjectRegistry.entities.list(true))).container);
		this.addComponent("criteria.entity.distance", this.panelDistance = new PanelRangedValue("criteria.entity.distance", null, Text.INTEGER));
	}

	public boolean checkInput()
	{
		if (!this.isSelected(this.panelDistance)) return true;
		try
		{
			this.panelDistance.checkInput(CGEntry.INTEGER);
			return true;
		} catch (CommandGenerationException e)
		{
			CommandGenerator.report(e);
			return false;
		}
	}

	public Tag[] generateTags()
	{
		ArrayList<Tag> tags = new ArrayList<Tag>();
		if (this.isSelected(this.comboboxEntity.container)) tags.add(Tags.CRITERIA_ENTITY_ID.create(this.comboboxEntity.getSelectedObject().id()));
		if (this.isSelected(this.panelDistance)) tags.add(this.panelDistance.generateValue(distance).toTag());
		return tags.toArray(new Tag[tags.size()]);
	}

	public void setupFrom(TagCompound tag)
	{
		if (tag.hasTag(Tags.CRITERIA_ENTITY_ID))
		{
			this.select(this.comboboxEntity.container);
			this.comboboxEntity.setSelected(ObjectRegistry.entities.find(tag.getTag(Tags.CRITERIA_ENTITY_ID).value()));
		}
		if (this.distance.findValue(tag))
		{
			this.select(this.panelDistance);
			this.panelDistance.setupFrom(this.distance);
		}
	}

}
