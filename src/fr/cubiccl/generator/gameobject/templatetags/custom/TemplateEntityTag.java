package fr.cubiccl.generator.gameobject.templatetags.custom;

import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelEntity;

public class TemplateEntityTag extends TemplateCompound
{

	public TemplateEntityTag(String id, byte tagType, String[] applicable)
	{
		super(id, tagType, applicable);
	}

	@Override
	protected PanelEntity createPanel(String objectId, Tag previousValue)
	{
		PanelEntity p = new PanelEntity(null);
		if (objectId.equals("minecraft:armor_stand")) p.selectEntity(ObjectRegistry.entities.find("armor_stand"));
		if (objectId.equals("minecraft:spawn_egg") || objectId.equals("minecraft:mob_spawner")) p.selectEntity(ObjectRegistry.entities.find("creeper"));

		if (previousValue != null)
		{
			TagCompound t = (TagCompound) previousValue;
			if (t.hasTag("id")) p.selectEntity(ObjectRegistry.entities.find((String) t.getTagFromId("id").value()));
			p.setTags(t.value());
		}

		p.setName(this.title());
		return p;
	}

	@Override
	public TagCompound generateTag(CGPanel panel)
	{
		return ((PanelEntity) panel).generateEntity().toTag(this);
	}

}
