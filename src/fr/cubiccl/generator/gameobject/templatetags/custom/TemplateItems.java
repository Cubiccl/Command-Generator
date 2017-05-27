package fr.cubiccl.generator.gameobject.templatetags.custom;

import org.jdom2.Element;

import fr.cubiccl.generator.gameobject.baseobjects.BaseObject;
import fr.cubiccl.generator.gameobject.baseobjects.Container;
import fr.cubiccl.generator.gameobject.baseobjects.Entity;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagList;
import fr.cubiccl.generator.gameobject.templatetags.TemplateList;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.tag.PanelContainer;

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
		Container container = (!this.hasSlot || object == Entity.PLAYER || object == null) ? ObjectRegistry.containers.find(this.id())
				: ObjectRegistry.containers.find(object.id());
		if (container == null) container = ObjectRegistry.containers.find(this.id());
		PanelContainer p = new PanelContainer(container);
		if (previousValue != null) p.setupFrom((TagList) previousValue);
		if (object == Entity.PLAYER) p.setName(this.title());
		else p.setName(container.name());
		return p.container;
	}

	@Override
	public TagList generateTag(BaseObject object, CGPanel panel)
	{
		return ((PanelContainer) panel.getComponent(0)).generateItems(this, this.hasSlot);
	}

	@Override
	public Element toXML()
	{
		Element root = super.toXML();
		if (!this.hasSlot) root.addContent(new Element("noslot").setText("true"));
		return root;
	}

}
