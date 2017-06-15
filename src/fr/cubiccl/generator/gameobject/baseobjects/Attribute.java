package fr.cubiccl.generator.gameobject.baseobjects;

import org.jdom2.Element;

import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.utils.Text;

public class Attribute extends BaseObject<Attribute>
{

	/** This Attribute's ID. */
	private String id;

	public Attribute()
	{}

	public Attribute(String id)
	{
		this.id = id;
	}

	@Override
	public Attribute fromXML(Element xml)
	{
		this.id = xml.getAttributeValue("id");
		return this;
	}

	@Override
	public String id()
	{
		return this.id;
	}

	@Override
	public Text name()
	{
		return new Text("attribute." + this.id);
	}

	@Override
	public Attribute register()
	{
		ObjectRegistry.attributes.register(this);
		return this;
	}

	@Override
	public Element toXML()
	{
		return new Element("achievement").setAttribute("id", this.id());
	}
}
