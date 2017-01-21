package fr.cubiccl.generator.gui.component.panel.gameobject;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

import fr.cubiccl.generator.gui.component.interfaces.MCInventory;

public class MCInventoryDrawer extends MouseAdapter implements KeyListener
{
	public static final int MOVE_RIGHT = 0, MOVE_LEFT = 1, MOVE_UP = 2, MOVE_DOWN = 3;

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
	public void keyPressed(KeyEvent e)
	{
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) this.inventory.onMove(MOVE_RIGHT);
		else if (e.getKeyCode() == KeyEvent.VK_LEFT) this.inventory.onMove(MOVE_LEFT);
		else if (e.getKeyCode() == KeyEvent.VK_UP) this.inventory.onMove(MOVE_UP);
		else if (e.getKeyCode() == KeyEvent.VK_DOWN) this.inventory.onMove(MOVE_DOWN);
	}

	@Override
	public void keyReleased(KeyEvent e)
	{}

	@Override
	public void keyTyped(KeyEvent e)
	{}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		super.mouseClicked(e);
		if (SwingUtilities.isRightMouseButton(e)) this.inventory.onRightClick();
		else if (SwingUtilities.isLeftMouseButton(e)) this.inventory.onClick();
	}

	@Override
	public void mouseDragged(MouseEvent e)
	{
		super.mouseDragged(e);
		this.inventory.onMove(e.getX(), e.getY());
		if (SwingUtilities.isLeftMouseButton(e)) this.inventory.onClick();
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
