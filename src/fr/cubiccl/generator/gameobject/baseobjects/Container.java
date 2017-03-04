package fr.cubiccl.generator.gameobject.baseobjects;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Comparator;

import org.jdom2.Element;

import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.utils.Text;
import fr.cubiccl.generator.utils.Textures;

public class Container extends BaseObject
{

	public String[] applicable;
	public final String id;
	public Slot[] slots;

	public Container(String id, String[] applicable, Slot... slots)
	{
		this.id = id;
		this.applicable = applicable;
		this.slots = slots;
		ObjectRegistry.containers.register(this);
	}

	public void addApplication(String objectId)
	{
		ArrayList<String> applicable = new ArrayList<String>();
		for (String app : this.applicable)
			applicable.add(app);
		applicable.add(objectId);
		applicable.sort(Comparator.naturalOrder());
		this.applicable = applicable.toArray(new String[0]);
	}

	public void addSlot(Slot s)
	{
		ArrayList<Slot> slots = new ArrayList<Slot>();
		for (Slot slot : this.slots)
			slots.add(slot);
		slots.add(s);
		slots.sort(Comparator.naturalOrder());
		this.slots = slots.toArray(new Slot[0]);
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

	public void removeApplication(String objectId)
	{
		ArrayList<String> applicable = new ArrayList<String>();
		for (String app : this.applicable)
			applicable.add(app);
		applicable.remove(objectId);
		applicable.sort(Comparator.naturalOrder());
		this.applicable = applicable.toArray(new String[0]);
	}

	public void removeSlot(Slot s)
	{
		ArrayList<Slot> slots = new ArrayList<Slot>();
		for (Slot slot : this.slots)
			slots.add(slot);
		slots.remove(s);
		slots.sort(Comparator.naturalOrder());
		this.slots = slots.toArray(new Slot[0]);
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
