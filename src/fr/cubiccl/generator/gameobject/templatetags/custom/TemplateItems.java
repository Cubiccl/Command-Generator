package fr.cubiccl.generator.gameobject.templatetags.custom;

import fr.cubiccl.generator.gameobject.baseobjects.BaseObject;
import fr.cubiccl.generator.gameobject.baseobjects.Container;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagList;
import fr.cubiccl.generator.gameobject.templatetags.TemplateList;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.tag.ContainerPanel;

public class TemplateItems extends TemplateList
{

	public boolean hasSlot = true;

	public TemplateItems(String id, byte applicationType, String[] applicable)
	{
		super(id, applicationType, applicable);
	}

	@Override
	protected CGPanel createPanel(BaseObject object, Tag previousValue)
	{
		Container container = !this.hasSlot ? ObjectRegistry.containers.find(this.id()) : ObjectRegistry.containers.find(object.id());
		ContainerPanel p = new ContainerPanel(container);
		if (previousValue != null) p.setupFrom((TagList) previousValue);
		p.setName(container.name());
		return p;
	}

	@Override
	public TagList generateTag(BaseObject object, CGPanel panel)
	{
		return ((ContainerPanel) panel).generateItems(this, this.hasSlot);
	}

}
