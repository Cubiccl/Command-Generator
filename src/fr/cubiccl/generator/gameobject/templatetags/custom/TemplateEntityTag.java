package fr.cubiccl.generator.gameobject.templatetags.custom;

import fr.cubiccl.generator.gameobject.baseobjects.BaseObject;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelEntity;

public class TemplateEntityTag extends TemplateCompound
{

	public TemplateEntityTag(String id, byte applicationType, String[] applicable)
	{
		super(id, applicationType, applicable);
	}

	@Override
	protected PanelEntity createPanel(BaseObject object, Tag previousValue)
	{
		PanelEntity p = new PanelEntity(null);
		if (object.id().equals("minecraft:armor_stand")) p.setEntity(ObjectRegistry.entities.find("armor_stand"));
		if (object.id().equals("minecraft:spawn_egg") || object.id().equals("minecraft:mob_spawner")) p.setEntity(ObjectRegistry.entities.find("creeper"));

		if (previousValue != null)
		{
			TagCompound t = (TagCompound) previousValue;
			if (t.hasTag("id")) p.setEntity(ObjectRegistry.entities.find((String) t.getTagFromId("id").value()));
			p.setTags(t.value());
		}

		p.setName(this.title());
		return p;
	}

	@Override
	public TagCompound generateTag(BaseObject object, CGPanel panel)
	{
		return ((PanelEntity) panel).generateEntity().toTag(this);
	}

}
