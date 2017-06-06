package fr.cubiccl.generator.gameobject.baseobjects;

import org.jdom2.Element;

/** Represents a single slot in a {@link Container} */
public class Slot implements Comparable<Slot>
{
	/** Slot size for graphics. */
	public static final int SIZE = 16;

	/** This Slot's Identifier. */
	public final int id;
	/** This Slot's x position in the Container. */
	public int x;
	/** This Slot's x position in the Container. */
	public int y;

	public Slot(int id)
	{
		this(id, 0, 0);
	}

	public Slot(int id, int x, int y)
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

	/** @param mouseX - The X coordinate of the mouse.
	 * @param mouseY - The Y coordinate of the mouse.
	 * @return <code>true</code> if the input mouse coordinates are contained in this Slot. */
	public boolean isSelected(int mouseX, int mouseY)
	{
		return mouseX >= this.x && mouseX <= this.x + SIZE && mouseY >= this.y && mouseY <= this.y + SIZE;
	}

	/** @return This Object in XML format for storage. */
	public Element toXML()
	{
		return new Element("slot").setAttribute("id", Integer.toString(this.id)).setAttribute("x", Integer.toString(this.x))
				.setAttribute("y", Integer.toString(this.y));
	}

}
