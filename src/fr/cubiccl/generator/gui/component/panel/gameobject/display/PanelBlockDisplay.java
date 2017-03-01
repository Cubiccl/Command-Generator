package fr.cubiccl.generator.gui.component.panel.gameobject.display;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

import fr.cubi.cubigui.DisplayUtils;
import fr.cubiccl.generator.gameobject.PlacedBlock;
import fr.cubiccl.generator.utils.Replacement;
import fr.cubiccl.generator.utils.Text;

public class PanelBlockDisplay extends JPanel
{
	private static final Color BACKGROUND = PanelItemDisplay.BACKGROUND, BORDER = PanelItemDisplay.BORDER;
	private static final int PIXEL = PanelItemDisplay.PIXEL, LINE = PIXEL * 4, MARGIN = PIXEL * 4;
	private static final long serialVersionUID = -4291911868708274736L;

	private PlacedBlock block;
	private String[] info;
	private boolean isResized = false;

	public PanelBlockDisplay()
	{
		this(null);
	}

	public PanelBlockDisplay(PlacedBlock block)
	{
		this.display(block);
	}

	public void display(PlacedBlock block)
	{
		this.block = block;
		this.findInfo();
		this.isResized = false;
		this.repaint();
	}

	private void doResize(Graphics g)
	{
		int width = this.block == null ? 0 : this.block.block.texture(this.block.data).getWidth();
		g.setFont(DisplayUtils.FONT);
		for (String i : info)
			width = Math.max(width, g.getFontMetrics().stringWidth(i));

		int height = (this.info.length - 1) * LINE + MARGIN * 2;
		if (this.block != null) height += PIXEL + this.block.block.texture(this.block.data).getHeight();
		this.setVisible(false);
		this.setPreferredSize(new Dimension(width + MARGIN * 2, height));
		this.setVisible(true);
		this.isResized = true;
	}

	private void findInfo()
	{
		ArrayList<String> infos = new ArrayList<String>();

		if (this.block != null)
		{
			infos.add(this.block.block.name(this.block.data).toString());
			int size = this.block.containerSize();
			if (size != -1) infos.add(new Text("container.size", new Replacement("<size>", Integer.toString(size))).toString());
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

		if (this.block == null) return;

		g.drawImage(this.block.block.texture(this.block.data), MARGIN, MARGIN - PIXEL * 3 / 2, null);

		this.paintInfo(g);
	}

	private void paintInfo(Graphics g)
	{
		g.setColor(Color.WHITE);
		g.setFont(DisplayUtils.FONT);
		int image = this.block.block.texture(this.block.data).getHeight();
		for (int i = 0; i < this.info.length; ++i)
		{
			if (i == 1) g.setColor(Color.GRAY);
			g.drawString(this.info[i], MARGIN, MARGIN + PIXEL + image + i * LINE);
		}

	}
}
