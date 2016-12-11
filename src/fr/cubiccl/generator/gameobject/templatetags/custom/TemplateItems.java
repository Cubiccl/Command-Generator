package fr.cubiccl.generator.gameobject.templatetags.custom;

import fr.cubiccl.generator.gameobject.Container;
import fr.cubiccl.generator.gameobject.ObjectRegistry;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagList;
import fr.cubiccl.generator.gameobject.templatetags.TemplateList;
import fr.cubiccl.generator.gui.component.panel.tag.ItemsPanel;
import fr.cubiccl.generator.gui.component.panel.utils.ConfirmPanel;

public class TemplateItems extends TemplateList
{

	public TemplateItems(String id, byte tagType, String[] applicable)
	{
		super(id, tagType, applicable);
	}

	@Override
	protected ConfirmPanel createPanel(String objectId, Tag previousValue)
	{
		Container container = ObjectRegistry.getContainerFromID(objectId);
		ItemsPanel p = new ItemsPanel(container);
		if (previousValue != null) p.setupFrom((TagList) previousValue);
		return new ConfirmPanel(container.name(), p);
	}

	@Override
	public TagList generateTag(ConfirmPanel panel)
	{
		return ((ItemsPanel) panel.component).generateItems(this);
	}

}
