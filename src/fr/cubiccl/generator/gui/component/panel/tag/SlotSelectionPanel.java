package fr.cubiccl.generator.gui.component.panel.tag;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import fr.cubiccl.generator.gameobject.baseobjects.Container;
import fr.cubiccl.generator.gameobject.baseobjects.Slot;
import fr.cubiccl.generator.gui.component.interfaces.MCInventory;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.MCInventoryDrawer;

public abstract class SlotSelectionPanel extends CGPanel implements MCInventory
{
	public static final int MULTIPLIER = 5;
	private static final long serialVersionUID = -3136971123392536978L;

	private Container container;
	private int currentSlot;
	protected MCInventoryDrawer drawer;
	protected BufferedImage img;

	public SlotSelectionPanel(Container container)
	{
		this.setContainer(container);
		this.addMouseListener(this.drawer = new MCInventoryDrawer(this));
		this.addMouseMotionListener(this.drawer);
	}

	public Container container()
	{
		return this.container;
	}

	public int currentSlot()
	{
		return this.currentSlot;
	}

	@Override
	public abstract void onClick();

	@Override
	public void onExit()
	{
		this.currentSlot = -1;
		this.repaint();
	}

	@Override
	public void onMove(int direction)
	{}

	@Override
	public void onMove(int x, int y)
	{
		int previous = this.currentSlot;
		this.currentSlot = -1;
		for (int i = 0; i < this.container.slots.length; ++i)
			if (this.container.slots[i].isSelected(x / MULTIPLIER, y / MULTIPLIER))
			{
				this.currentSlot = i;
				break;
			}

		if (this.currentSlot != previous) this.repaint();
	}

	@Override
	public void paint(Graphics g)
	{
		super.paint(g);
		if (this.img == null) return;
		g.drawImage(this.img, 0, 0, this.getWidth(), this.getHeight(), null);
		if (this.currentSlot != -1)
		{
			Slot slot = this.container.slots[this.currentSlot];
			this.drawer.drawHovering(g, slot.x * MULTIPLIER, slot.y * MULTIPLIER, Slot.SIZE * MULTIPLIER);
		}
	}

	public void setContainer(Container container)
	{
		this.container = container;
		this.img = this.container.texture();
		if (this.img != null)
		{
			this.setPreferredSize(new Dimension(this.img.getWidth() * MULTIPLIER, this.img.getHeight() * MULTIPLIER));
			this.setMinimumSize(new Dimension(this.img.getWidth() * MULTIPLIER, this.img.getHeight() * MULTIPLIER));
			if (this.getParent() != null) this.getParent().revalidate();
		}
		this.repaint();
	}

}
