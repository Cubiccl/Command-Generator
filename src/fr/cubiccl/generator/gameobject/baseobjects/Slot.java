package fr.cubiccl.generator.gameobject.baseobjects;

import org.jdom2.Element;

import fr.cubiccl.generator.gameobject.utils.XMLSaveable;

/** Represents a single slot in a {@link Container} */
public class Slot implements Comparable<Slot>, XMLSaveable<Slot>
{
	/** Slot size for graphics. */
	public static final int SIZE = 16;

	/** This Slot's Identifier. */
	private int id;
	/** This Slot's x position in the Container. */
	private int x;
	/** This Slot's x position in the Container. */
	private int y;

	public Slot()
	{}

	Slot(int id, int x, int y)
	{
		this.id = id;
		this.x = x;
		this.y = y;
	}

	@Override
	public int compareTo(Slot o)
	{
		return this.id - o.id;
	}

	@Override
	public Slot fromXML(Element xml)
	{
		this.id = Integer.parseInt(xml.getAttributeValue("id"));
		this.x = Integer.parseInt(xml.getAttributeValue("x"));
		this.y = Integer.parseInt(xml.getAttributeValue("y"));
		return this;
	}

	/** Getter for {@link Slot#id}. */
	public int id()
	{
		return this.id;
	}

	/** @param mouseX - The X coordinate of the mouse.
	 * @param mouseY - The Y coordinate of the mouse.
	 * @return <code>true</code> if the input mouse coordinates are contained in this Slot. */
	public boolean isSelected(int mouseX, int mouseY)
	{
		return mouseX >= this.x && mouseX <= this.x + SIZE && mouseY >= this.y && mouseY <= this.y + SIZE;
	}

	@Override
	public Element toXML()
	{
		return new Element("slot").setAttribute("id", Integer.toString(this.id)).setAttribute("x", Integer.toString(this.x))
				.setAttribute("y", Integer.toString(this.y));
	}

	/** Getter for {@link Slot#x}. */
	public int x()
	{
		return this.x;
	}

	/** Getter for {@link Slot#y}. */
	public int y()
	{
		return this.y;
	}

}
