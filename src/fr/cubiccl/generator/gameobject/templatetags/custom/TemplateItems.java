package fr.cubiccl.generator.gameobject.templatetags.custom;

import fr.cubiccl.generator.gameobject.Container;
import fr.cubiccl.generator.gameobject.ObjectRegistry;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.templatetags.TemplateList;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.tag.ItemsPanel;
import fr.cubiccl.generator.gui.component.panel.utils.ConfirmPanel;

public class TemplateItems extends TemplateList
{

	public TemplateItems(String id, int tagType, String[] applicable)
	{
		super(id, tagType, applicable);
	}

	@Override
	protected CGPanel createPanel(String objectId)
	{
		Container container = ObjectRegistry.getContainerFromID(objectId);
		return new ConfirmPanel(container.name(), new ItemsPanel(container));
	}

	@Override
	public Tag generateTag(CGPanel panel)
	{
		return ((ItemsPanel) ((ConfirmPanel) panel).component).generateItems(this);
	}

}
