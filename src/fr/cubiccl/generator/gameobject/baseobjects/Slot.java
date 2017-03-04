package fr.cubiccl.generator.gameobject.baseobjects;

import org.jdom2.Element;

public class Slot implements Comparable<Slot>
{
	public static final int SIZE = 16;

	public final int id;
	public int x, y;

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

	public boolean isSelected(int mouseX, int mouseY)
	{
		return mouseX >= this.x && mouseX <= this.x + SIZE && mouseY >= this.y && mouseY <= this.y + SIZE;
	}

	public Element toXML()
	{
		return new Element("slot").setAttribute("id", Integer.toString(this.id)).setAttribute("x", Integer.toString(this.x))
				.setAttribute("y", Integer.toString(this.y));
	}

}
