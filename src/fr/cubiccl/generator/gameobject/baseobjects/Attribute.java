package fr.cubiccl.generator.gameobject.baseobjects;

import org.jdom2.Element;

import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.utils.Text;

public class Attribute extends BaseObject
{

	/** This Attribute's ID. */
	public final String id;

	public Attribute(String id)
	{
		this.id = id;
		ObjectRegistry.attributes.register(this);
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
	public Element toXML()
	{
		return new Element("achievement").setAttribute("id", this.id());
	}
}
