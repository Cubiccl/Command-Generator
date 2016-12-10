package fr.cubiccl.generator.gui.component.panel.gameobject;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import fr.cubiccl.generator.gui.component.interfaces.MCInventory;

public class MCInventoryDrawer extends MouseAdapter
{
	private MCInventory inventory;

	public MCInventoryDrawer(MCInventory inventory)
	{
		this.inventory = inventory;
	}

	public void drawBox(Graphics g, int x, int y, int size)
	{
		g.setColor(Color.LIGHT_GRAY);
		g.drawRect(x - 1, y - 1, size + 2, size + 2);
	}

	public void drawHovering(Graphics g, int x, int y, int size)
	{
		g.setColor(new Color(255, 255, 255, 100));
		g.fillRect(x, y, size, size);
	}

	public void drawSelection(Graphics g, int x, int y, int size)
	{
		g.setColor(new Color(200, 200, 255));
		g.fillRect(x - 1, y - 1, size + 2, size + 2);
		g.setColor(Color.GRAY);
		g.drawRect(x - 1, y - 1, size + 2, size + 2);

	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		super.mouseClicked(e);
		this.inventory.onClick();
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		super.mouseExited(e);
		this.inventory.onExit();
	}

	@Override
	public void mouseMoved(MouseEvent e)
	{
		super.mouseMoved(e);
		this.inventory.onMove(e.getX(), e.getY());
	}

}
