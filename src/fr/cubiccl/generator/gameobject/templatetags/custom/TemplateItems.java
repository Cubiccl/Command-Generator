package fr.cubiccl.generator.gameobject.templatetags.custom;

import fr.cubiccl.generator.gameobject.Container;
import fr.cubiccl.generator.gameobject.ObjectRegistry;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagList;
import fr.cubiccl.generator.gameobject.templatetags.TemplateList;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.tag.ContainerPanel;

public class TemplateItems extends TemplateList
{

	public TemplateItems(String id, byte tagType, String[] applicable)
	{
		super(id, tagType, applicable);
	}

	@Override
	protected CGPanel createPanel(String objectId, Tag previousValue)
	{
		Container container = ObjectRegistry.getContainerFromID(objectId);
		ContainerPanel p = new ContainerPanel(container);
		if (previousValue != null) p.setupFrom((TagList) previousValue);
		p.setName(container.name());
		return p;
	}

	@Override
	public TagList generateTag(CGPanel panel)
	{
		return ((ContainerPanel) panel).generateItems(this);
	}

}
