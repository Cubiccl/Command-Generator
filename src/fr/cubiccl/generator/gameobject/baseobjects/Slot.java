package fr.cubiccl.generator.gameobject.baseobjects;

import org.jdom2.Element;

public class Slot
{
	public static final int SIZE = 16;

	public final int x, y, id;

	public Slot(int id, int x, int y)
	{
		this.id = id;
		this.x = x;
		this.y = y;
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
