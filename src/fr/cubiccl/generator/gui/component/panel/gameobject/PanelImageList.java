package fr.cubiccl.generator.gui.component.panel.gameobject;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import fr.cubiccl.generator.gui.component.interfaces.IImageSelectionListener;
import fr.cubiccl.generator.gui.component.interfaces.MCInventory;

public class PanelImageList extends JPanel implements MCInventory
{
	public static final int DEFAULT_OBJECT_SIZE = PanelBlockSelection.BLOCK_SIZE, DEFAULT_GAP = PanelBlockSelection.GAP;
	private static final long serialVersionUID = -6130047693545713324L;

	private MCInventoryDrawer drawer;
	private int hovering;
	private BufferedImage[] images;
	public int objectSize, gap;
	private int objectsPerLine;
	private IImageSelectionListener parent;

	public PanelImageList(IImageSelectionListener parent)
	{
		this.parent = parent;
		this.images = new BufferedImage[0];
		this.hovering = -1;
		this.objectSize = DEFAULT_OBJECT_SIZE;
		this.gap = DEFAULT_GAP;
		this.addMouseListener(this.drawer = new MCInventoryDrawer(this));
		this.addMouseMotionListener(this.drawer);
		this.addKeyListener(this.drawer);
		this.setFocusable(true);
		this.setObjectsPerLine(20);
	}

	@Override
	public void onClick()
	{
		if (this.hovering != -1)
		{
			this.parent.selectObject(this.hovering);
			this.repaint();
		}
		this.requestFocus();
	}

	@Override
	public void onExit()
	{
		this.hovering = -1;
		this.repaint();
	}

	@Override
	public void onMove(int direction)
	{
		if (direction == MCInventoryDrawer.MOVE_RIGHT) this.parent.changeSelection(1);
		if (direction == MCInventoryDrawer.MOVE_LEFT) this.parent.changeSelection(-1);
		if (direction == MCInventoryDrawer.MOVE_UP) this.parent.changeSelection(-this.objectsPerLine);
		if (direction == MCInventoryDrawer.MOVE_DOWN) this.parent.changeSelection(this.objectsPerLine);
	}

	@Override
	public void onMove(int x, int y)
	{
		int previous = this.hovering;
		this.hovering = x / (this.objectSize + this.gap) + y / (this.objectSize + this.gap) * this.objectsPerLine;
		if (previous != this.hovering) this.repaint();
	}

	@Override
	public void paint(Graphics g)
	{
		super.paint(g);
		for (int i = 0; i < this.images.length; ++i)
		{
			int x = (i % this.objectsPerLine) * (this.objectSize + this.gap) + 1;
			int y = i / this.objectsPerLine * (this.objectSize + this.gap) + 1;
			this.drawer.drawBox(g, x, y, this.objectSize);
			if (i == this.parent.currentSelection()) this.drawer.drawSelection(g, x, y, this.objectSize);
			g.drawImage(this.images[i], x, y, this.objectSize, this.objectSize, null);
			if (i == this.hovering && i != this.parent.currentSelection()) this.drawer.drawHovering(g, x, y, this.objectSize);
		}
	}

	public void setImages(BufferedImage[] images)
	{
		this.images = images;
		if (this.images.length <= this.parent.currentSelection()) this.parent.selectObject(this.images.length - 1);
		this.repaint();
	}

	public void setObjectsPerLine(int objectsPerLine)
	{
		this.objectsPerLine = Math.max(20, objectsPerLine);
		int width = this.objectsPerLine * (this.objectSize + this.gap) - this.gap, height = (this.images.length / this.objectsPerLine + 1)
				* (this.objectSize + this.gap);
		this.setSize(width, height);
		this.setPreferredSize(new Dimension(width, height));
	}

}
