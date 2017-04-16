package fr.cubiccl.generator.gui.component.panel.gameobject.display;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

import fr.cubi.cubigui.DisplayUtils;
import fr.cubiccl.generator.gameobject.Enchantment;
import fr.cubiccl.generator.gameobject.ItemStack;

public class PanelItemDisplay extends JPanel
{
	public static final Color BACKGROUND = Color.BLACK, BORDER = new Color(40, 0, 90);
	public static final int PIXEL = 5, LINE = PIXEL * 4, MARGIN = PIXEL * 4;
	private static final long serialVersionUID = -4291911868708274736L;

	private String[] info;
	private boolean isResized = false;
	private ItemStack item;

	public PanelItemDisplay()
	{
		this(null);
	}

	public PanelItemDisplay(ItemStack item)
	{
		this.display(item);
	}

	public void display(ItemStack item)
	{
		this.item = item;
		this.findInfo();
		this.isResized = false;
		this.repaint();
	}

	private void doResize(Graphics g)
	{
		int width = this.item == null || this.item.getItem() == null ? 0 : this.item.texture().getWidth();
		g.setFont(DisplayUtils.FONT);
		for (String i : info)
			width = Math.max(width, g.getFontMetrics().stringWidth(i));

		int height = (this.info.length - 1) * LINE + MARGIN * 2;
		if (this.item != null) height += PIXEL + this.item.texture().getHeight();
		this.setVisible(false);
		this.setPreferredSize(new Dimension(width + MARGIN * 2, height));
		this.setVisible(true);
		this.isResized = true;
	}

	private void findInfo()
	{
		ArrayList<String> infos = new ArrayList<String>();

		if (this.item != null && this.item.getItem() != null)
		{
			infos.add(this.item.displayName());
			for (String lore : this.item.findLore())
				infos.add(lore);
			for (Enchantment e : this.item.findEnchantments())
				infos.add(e.toString());
		}

		this.info = infos.toArray(new String[infos.size()]);
	}

	@Override
	public void paint(Graphics g)
	{
		super.paint(g);

		if (!this.isResized) this.doResize(g);

		int width = this.getWidth(), height = this.getHeight() - 1;

		g.setColor(BACKGROUND);
		g.fillRect(PIXEL, 0, width - PIXEL * 2, height);
		g.fillRect(0, PIXEL, width, height - PIXEL * 2);

		g.setColor(BORDER);
		g.fillRect(PIXEL, PIXEL, PIXEL, height - PIXEL * 2);
		g.fillRect(PIXEL, PIXEL, width - PIXEL * 2, PIXEL);
		g.fillRect(width - PIXEL * 2, PIXEL, PIXEL, height - PIXEL * 2);
		g.fillRect(PIXEL, height - PIXEL * 2, width - PIXEL * 2, PIXEL);

		if (this.item == null || this.item.getItem() != null) return;

		g.drawImage(this.item.texture(), MARGIN, MARGIN - PIXEL * 3 / 2, null);

		this.paintInfo(g);
	}

	private void paintInfo(Graphics g)
	{
		g.setColor(Color.WHITE);
		g.setFont(DisplayUtils.FONT);
		int image = this.item.texture().getHeight();
		for (int i = 0; i < this.info.length; ++i)
		{
			if (i == 1) g.setColor(Color.GRAY);
			g.drawString(this.info[i], MARGIN, MARGIN + PIXEL + image + i * LINE);
		}

	}
}
