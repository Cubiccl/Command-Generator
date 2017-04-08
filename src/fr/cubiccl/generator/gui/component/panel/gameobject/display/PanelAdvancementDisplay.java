package fr.cubiccl.generator.gui.component.panel.gameobject.display;

import java.awt.Graphics;

import javax.swing.JPanel;

import fr.cubiccl.generator.gameobject.advancements.Advancement;

public class PanelAdvancementDisplay extends JPanel
{
	private static final long serialVersionUID = -604631852048059610L;

	@SuppressWarnings("unused")
	private Advancement advancement;

	public PanelAdvancementDisplay()
	{
		this(null);
	}

	public PanelAdvancementDisplay(Advancement advancement)
	{
		this.display(advancement);
	}

	public void display(Advancement advancement)
	{
		this.advancement = advancement;
		this.repaint();
	}

	@Override
	public void paint(Graphics g)
	{
		super.paint(g);
		/* int width = this.getWidth(), height = this.getHeight() - 1;
		 * 
		 * g.setColor(BACKGROUND); g.fillRect(PIXEL, 0, width - PIXEL * 2, height); g.fillRect(0, PIXEL, width, height - PIXEL * 2);
		 * 
		 * g.setColor(BORDER); g.fillRect(PIXEL, PIXEL, PIXEL, height - PIXEL * 2); g.fillRect(PIXEL, PIXEL, width - PIXEL * 2, PIXEL); g.fillRect(width - PIXEL * 2, PIXEL, PIXEL, height - PIXEL * 2); g.fillRect(PIXEL, height - PIXEL * 2, width - PIXEL * 2, PIXEL);
		 * 
		 * if (this.block == null) return;
		 * 
		 * g.drawImage(this.block.texture(), MARGIN, MARGIN - PIXEL * 3 / 2, null);
		 * 
		 * this.paintInfo(g); */
	}
}
