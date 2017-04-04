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
	public static final Container CRAFTING = new Container("crafting", new String[0], new Slot(0, 20, 17), new Slot(1, 38, 17), new Slot(2, 56, 17), new Slot(
			3, 20, 35), new Slot(4, 38, 35), new Slot(5, 56, 35), new Slot(6, 20, 53), new Slot(7, 38, 53), new Slot(8, 56, 53), new Slot(9, 114, 35));

	public String[] applicable;
	public final String id;
	public Slot[] slots;

	public Container(String id, String[] applicable, Slot... slots)
	{
		this.id = id;
		this.applicable = applicable;
		this.slots = slots;
		if (!this.id.equals("crafting")) ObjectRegistry.containers.register(this);
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
