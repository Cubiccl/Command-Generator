package fr.cubiccl.generator.gui.component.panel.loottable;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import fr.cubi.cubigui.DisplayUtils;
import fr.cubiccl.generator.gameobject.loottable.LootTable;
import fr.cubiccl.generator.gameobject.loottable.LootTablePool;

public class PanelLTDisplay extends JPanel
{
	private static class TableElement
	{
		public int children;
		public BufferedImage icon;
		public String name;
		public int x, y, width, height;

		public TableElement(String name, BufferedImage icon)
		{
			this(name, 0, icon);
		}

		public TableElement(String name, int children)
		{
			this(name, children, null);
		}

		public TableElement(String name, int children, BufferedImage icon)
		{
			this.name = name;
			this.children = children;
			this.icon = icon;

			// TODO calculate dimensions
			width = DisplayUtils.textWidth(this.name) * 3 / 4 + MARGIN * 2;
			height = ELEMENTH;

			if (this.icon != null) width += ICON;
		}

		public void paint(Graphics g)
		{
			// TODO paint element
			g.setColor(Color.GRAY);
			g.drawRect(x, y, width, height);
			g.setColor(Color.BLACK);
			g.drawString(this.name, x + MARGIN + (this.icon == null ? 0 : ICON), y + MARGIN + DisplayUtils.FONT.getSize());

			if (this.icon != null) g.drawImage(this.icon, x + MARGIN / 2, y + MARGIN, ICON, ICON, null);
		}
	}

	private static final long serialVersionUID = -6118643014402296156L;

	private static final int UNIT = 20, MARGIN = 5, ICON = 15, ELEMENTH = DisplayUtils.FONT.getSize() + MARGIN * 2;

	public final LootTable lootTable;
	private TableElement name;
	private ArrayList<TableElement> pools, entries;

	public PanelLTDisplay(LootTable lootTable)
	{
		super();
		this.lootTable = lootTable;
		this.pools = new ArrayList<TableElement>();
		this.entries = new ArrayList<TableElement>();
		this.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		this.update();
	}

	private TableElement[] entries(int poolIndex)
	{
		TableElement pool = this.pools.get(poolIndex);
		TableElement[] entries = new TableElement[pool.children];

		int start = 0;
		for (int i = 0; i < poolIndex; ++i)
			start += this.pools.get(i).children;

		for (int i = start; i < start + pool.children; ++i)
			entries[i - start] = this.entries.get(i);

		return entries;
	}

	@Override
	public void paintComponent(Graphics g)
	{
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		g.setFont(DisplayUtils.FONT);

		for (TableElement element : this.pools)
			element.paint(g);
		for (TableElement element : this.entries)
			element.paint(g);

		this.name.paint(g);

		g.setColor(Color.BLACK);
		int h = UNIT + ELEMENTH;
		if (this.pools.size() != 0)
		{
			g.drawLine(this.name.x + this.name.width / 2, h, this.name.x + this.name.width / 2, h + UNIT);
			h += UNIT;
			g.drawLine(this.pools.get(0).x + this.pools.get(0).width / 2, h, this.pools.get(this.pools.size() - 1).x
					+ this.pools.get(this.pools.size() - 1).width / 2, h);
		}

		for (TableElement pool : this.pools)
		{
			TableElement[] entries = this.entries(this.pools.indexOf(pool));
			g.drawLine(pool.x + pool.width / 2, pool.y, pool.x + pool.width / 2, h);
			if (entries.length != 0) g.drawLine(pool.x + pool.width / 2, pool.y + pool.height, pool.x + pool.width / 2, pool.y + pool.height + UNIT);

			int h2 = pool.y + pool.height + UNIT;

			for (TableElement entry : entries)
				g.drawLine(entry.x + entry.width / 2, entry.y, entry.x + entry.width / 2, entry.y - UNIT);
			if (entries.length != 0) g.drawLine(entries[0].x + entries[0].width / 2, h2, entries[entries.length - 1].x + entries[entries.length - 1].width / 2,
					h2);
		}
	}

	public void update()
	{
		this.pools.clear();
		this.entries.clear();

		this.name = new TableElement(this.lootTable.customName(), 0);

		for (int i = 0; i < this.lootTable.pools.size(); ++i)
		{
			LootTablePool pool = this.lootTable.pools.get(i);
			this.pools.add(new TableElement(pool.getName(i), pool.entries.length));
			for (int j = 0; j < pool.entries.length; ++j)
				this.entries.add(new TableElement(pool.entries[j].name(), pool.entries[j].getIcon()));
		}

		this.updateLayout();
	}

	private void updateLayout()
	{
		// Y
		for (TableElement element : this.pools)
			element.y = UNIT * 3 + ELEMENTH;

		int x = UNIT;
		for (TableElement element : this.entries)
		{
			element.x = x;
			x += element.width + UNIT;
			element.y = UNIT * 5 + ELEMENTH * 2;
		}

		int width = 0, total = 0;
		for (int i = 0; i < this.pools.size(); ++i)
		{
			width = 0;
			TableElement pool = this.pools.get(i);
			for (TableElement entry : this.entries(i))
				width += entry.width + UNIT;
			if (width == 0) width = pool.width;
			else width -= UNIT;
			pool.x = total + width / 2 - pool.width / 2 + UNIT;
			total += width + UNIT;
		}

		if (total == 0) total = UNIT + this.name.width;
		total += UNIT;

		this.setPreferredSize(new Dimension(total, UNIT * 7 + ELEMENTH * 2));

		this.name.x = total / 2 - this.name.width / 2;
		this.name.y = UNIT;
	}
}
