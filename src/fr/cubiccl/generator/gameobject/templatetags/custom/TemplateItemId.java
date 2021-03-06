package fr.cubiccl.generator.gameobject.templatetags.custom;

import java.util.ArrayList;

import org.jdom2.Element;

import fr.cubiccl.generator.gameobject.baseobjects.BaseObject;
import fr.cubiccl.generator.gameobject.baseobjects.Block;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagString;
import fr.cubiccl.generator.gameobject.templatetags.TemplateString;
import fr.cubiccl.generator.gameobject.templatetags.TemplateTag;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelBlockSelection;

public class TemplateItemId extends TemplateString
{
	public int damage;
	private String[] ids;

	public TemplateItemId()
	{
		super();
		this.ids = null;
		this.damage = -1;
	}

	@Override
	protected CGPanel createPanel(BaseObject<?> object, Tag previousValue)
	{
		PanelBlockSelection p = new PanelBlockSelection();
		if (this.ids != null) p.setBlocks(ObjectRegistry.blocks.find(this.ids));

		Block previous = p.selectedBlock();
		if (previousValue != null)
		{
			previous = ObjectRegistry.blocks.find(((TagString) previousValue).value());
			p.setSelected(previous);
		}
		if (this.damage != -1 && previous.isDamageValid(this.damage)) p.setDamage(this.damage);
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
		return super.fromXML(xml);
	}

	@Override
	public TagString generateTag(BaseObject<?> object, CGPanel panel)
	{
		this.damage = ((PanelBlockSelection) panel).selectedDamage();
		return this.create(((PanelBlockSelection) panel).selectedBlock().id());
	}

	public void setLimited(String[] ids)
	{
		this.ids = ids;
	}

	@Override
	public Element toXML()
	{
		Element root = super.toXML();
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
