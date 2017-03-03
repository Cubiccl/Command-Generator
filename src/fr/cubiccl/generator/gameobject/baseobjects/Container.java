package fr.cubiccl.generator.gameobject.baseobjects;

import java.awt.image.BufferedImage;

import org.jdom2.Element;

import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.utils.Text;
import fr.cubiccl.generator.utils.Textures;

public class Container extends BaseObject
{

	public final String[] applicable;
	public final String id;
	public final Slot[] slots;

	// public final int startsAt;

	public Container(String id, String[] applicable, Slot... slots)
	{
		this.id = id;
		// this.startsAt = startsAt; //TODO VERIFY STARTS AT
		this.applicable = applicable;
		this.slots = slots;
		ObjectRegistry.containers.register(this);
	}

	public Container(String id, int startsAt, String[] applicable, Slot... slots)
	{
		this.id = id;
		// this.startsAt = startsAt; //TODO VERIFY STARTS AT
		this.applicable = applicable;
		this.slots = slots;
		ObjectRegistry.containers.register(this);
	}

	@Override
	public String id()
	{
		return this.id;
	}

	@Override
	public Text name()
	{
		return new Text("container." + this.id);
	}

	@Override
	public BufferedImage texture()
	{
		return Textures.getTexture("container." + this.id());
	}

	@Override
	public Element toXML()
	{
		Element root = new Element("container");
		root.setAttribute("id", this.id);

		Element applicable = new Element("applicable");
		for (String app : this.applicable)
			applicable.addContent(new Element("app").setText(app));
		root.addContent(applicable);

		Element slots = new Element("slots");
		for (Slot slot : this.slots)
			slots.addContent(slot.toXML());
		root.addContent(slots);

		return root;
	}

}
