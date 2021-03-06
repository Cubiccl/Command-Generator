package fr.cubiccl.generator.gameobject.templatetags.custom;

import java.util.ArrayList;

import org.jdom2.Element;

import fr.cubiccl.generator.gameobject.ItemStack;
import fr.cubiccl.generator.gameobject.baseobjects.BaseObject;
import fr.cubiccl.generator.gameobject.baseobjects.Item;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gameobject.templatetags.TemplateTag;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelItem;

public class TemplateItem extends TemplateCompound
{
	private String autoselect;
	private String[] ids;

	public TemplateItem()
	{
		super();
		this.ids = null;
		this.autoselect = null;
	}

	@Override
	protected CGPanel createPanel(BaseObject<?> object, Tag previousValue)
	{
		Item[] items = this.ids != null ? ObjectRegistry.items.find(this.ids) : ObjectRegistry.items.list(ObjectRegistry.SORT_NUMERICALLY);

		PanelItem p = new PanelItem(null, items);

		if (this.autoselect != null) p.setupFrom(new ItemStack(ObjectRegistry.items.find(this.autoselect), 0, 1));
		if (previousValue != null) p.setupFrom(new ItemStack().fromNBT((TagCompound) previousValue));
		p.setName(this.title());
		return p;
	}

	@Override
	public TemplateTag fromXML(Element xml)
	{
		if (xml.getChild("limited") != null)
		{
			ArrayList<String> values = new ArrayList<String>();
			for (Element v : xml.getChild("limited").getChildren())
				values.add(v.getText());
			this.setLimited(values.toArray(new String[values.size()]));
		}
		if (xml.getChild("itemautoselect") != null) this.setAutoselect(xml.getChildText("itemautoselect"));
		return super.fromXML(xml);
	}

	@Override
	public Tag generateTag(BaseObject<?> object, CGPanel panel)
	{
		ItemStack i = ((PanelItem) panel).generate();
		i.slot = -1;
		return i.toNBT(this);
	}

	public void setAutoselect(String id)
	{
		this.autoselect = id;
	}

	public void setLimited(String[] ids)
	{
		this.ids = ids;
	}

	@Override
	public Element toXML()
	{
		Element root = super.toXML();
		if (this.autoselect != null) root.addContent(new Element("itemautoselect").setText(this.autoselect));
		if (this.ids != null)
		{
			Element limited = new Element("limited");
			for (String v : this.ids)
				limited.addContent(new Element("v").setText(v));
			root.addContent(limited);
		}
		return root;
	}

}
