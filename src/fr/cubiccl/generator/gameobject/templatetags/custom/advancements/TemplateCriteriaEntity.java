package fr.cubiccl.generator.gameobject.templatetags.custom.advancements;

import java.util.ArrayList;

import fr.cubiccl.generator.gameobject.baseobjects.BaseObject;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gameobject.utils.TestValue;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.advancement.PanelCriteriaEntity;

public class TemplateCriteriaEntity extends TemplateCompound
{

	public final TestValue distance;

	public TemplateCriteriaEntity(String id, byte applicationType, String... applicable)
	{
		super(id, applicationType, applicable);
		this.distance = new TestValue(Tags.CRITERIA_DISTANCE, Tags.CRITERIA_DISTANCE_);
	}

	@Override
	protected CGPanel createPanel(BaseObject object, Tag previousValue)
	{
		PanelCriteriaEntity p = new PanelCriteriaEntity("criteria.entity.id", "criteria.entity.distance");
		if (previousValue != null)
		{
			TagCompound t = (TagCompound) previousValue;
			if (t.hasTag(Tags.CRITERIA_ENTITY_ID)) p.setEntity(ObjectRegistry.entities.find(t.getTag(Tags.CRITERIA_ENTITY_ID).value()));
			if (this.distance.findValue(t)) p.setDistance(this.distance);
		}
		return p;
	}

	@Override
	protected Tag generateTag(BaseObject object, CGPanel panel)
	{
		PanelCriteriaEntity p = (PanelCriteriaEntity) panel;
		ArrayList<Tag> tags = new ArrayList<Tag>();
		if (p.getEntity() != null) tags.add(Tags.CRITERIA_ENTITY_ID.create(p.getEntity().id()));
		if (p.generateDistance(this.distance)) tags.add(this.distance.toTag());
		return this.create(tags.toArray(new Tag[tags.size()]));
	}

}
