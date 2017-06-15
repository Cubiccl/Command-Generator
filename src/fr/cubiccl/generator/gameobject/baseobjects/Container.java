package fr.cubiccl.generator.gameobject.baseobjects;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.jdom2.Element;

import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.utils.Text;
import fr.cubiccl.generator.utils.Textures;

public class Container extends BaseObject<Container>
{
	/** The crafting grid. */
	public static final Container CRAFTING = new Container();
	static
	{
		CRAFTING.id = "crafting";
		CRAFTING.applicable = new String[0];
		CRAFTING.slots = new Slot[]
		{ new Slot(0, 20, 17), new Slot(1, 38, 17), new Slot(2, 56, 17), new Slot(3, 20, 35), new Slot(4, 38, 35), new Slot(5, 56, 35), new Slot(6, 20, 53),
				new Slot(7, 38, 53), new Slot(8, 56, 53), new Slot(9, 114, 35) };
	}

	/** The list of Object IDs this Container can be applied to. */
	private String[] applicable;
	/** This Container's ID. */
	private String id;
	/** The list of Slots this Container has. */
	private Slot[] slots;

	public Container()
	{}

	/** Adds a new applicable Object ID.
	 * 
	 * @param objectId - The ID to add. */
	public void addApplication(String objectId)
	{
		ArrayList<String> applicable = new ArrayList<String>();
		for (String app : this.applicable)
			applicable.add(app);
		applicable.add(objectId);
		applicable.sort(Comparator.naturalOrder());
		this.applicable = applicable.toArray(new String[0]);
	}

	/** Adds a new Slot to this Container.
	 * 
	 * @param slot - The Slot to add. */
	public void addSlot(Slot slot)
	{
		ArrayList<Slot> slots = new ArrayList<Slot>();
		for (Slot s : this.slots)
			slots.add(s);
		slots.add(slot);
		slots.sort(Comparator.naturalOrder());
		this.slots = slots.toArray(new Slot[0]);
	}

	/** Getter for {@link Container#applicable}. */
	public String[] applicable()
	{
		return this.applicable.clone();
	}

	@Override
	public Container fromXML(Element xml)
	{
		this.id = xml.getAttributeValue("id");

		List<Element> list = xml.getChild("applicable").getChildren("app");
		this.applicable = new String[list.size()];
		for (int i = 0; i < this.applicable.length; ++i)
			this.applicable[i] = list.get(i).getText();

		list = xml.getChild("slots").getChildren("slot");
		this.slots = new Slot[list.size()];
		for (int i = 0; i < this.slots.length; ++i)
			this.slots[i] = new Slot().fromXML(list.get(i));

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
		return new Text("container." + this.id);
	}

	@Override
	public Container register()
	{
		ObjectRegistry.containers.register(this);
		return this;
	}

	/** Removes a new applicable Object ID.
	 * 
	 * @param objectId - The ID to remove. */
	public void removeApplication(String objectId)
	{
		ArrayList<String> applicable = new ArrayList<String>();
		for (String app : this.applicable)
			applicable.add(app);
		applicable.remove(objectId);
		applicable.sort(Comparator.naturalOrder());
		this.applicable = applicable.toArray(new String[0]);
	}

	/** Removes a new Slot to this Container.
	 * 
	 * @param slot - The Slot to remove. */
	public void removeSlot(Slot slot)
	{
		ArrayList<Slot> slots = new ArrayList<Slot>();
		for (Slot s : this.slots)
			slots.add(s);
		slots.remove(slot);
		slots.sort(Comparator.naturalOrder());
		this.slots = slots.toArray(new Slot[0]);
	}

	/** Getter for {@link Container#slots}. */
	public Slot[] slots()
	{
		return this.slots.clone();
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
