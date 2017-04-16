package fr.cubiccl.generator.gui.component.panel.gameobject.display;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import fr.cubiccl.generator.gameobject.advancements.Advancement;
import fr.cubiccl.generator.utils.Textures;

public class PanelAdvancementDisplay extends JPanel
{
	private static final int ITEM = 32, FRAME = 52, ITEMPAD = 10;
	private static final long serialVersionUID = -604631852048059610L;

	private Advancement advancement;
	private boolean sizeSet = false;

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
		this.sizeSet = false;
		this.repaint();
	}

	@Override
	public void paint(Graphics g)
	{
		super.paint(g);

		if (this.advancement == null) return;
		if (!this.sizeSet) this.setSize();

		if (this.advancement.frame.equals("challenge")) g.drawImage(Textures.getTexture("gui.challenge"), 0, 0, FRAME, FRAME, null);
		else if (this.advancement.frame.equals("goal")) g.drawImage(Textures.getTexture("gui.goal"), 0, 0, FRAME, FRAME, null);
		else g.drawImage(Textures.getTexture("gui.task"), 0, 0, FRAME, FRAME, null);

		g.drawImage(this.advancement.getItem().texture(), ITEMPAD, ITEMPAD, ITEM, ITEM, null);

	}

	private void setSize()
	{
		this.setPreferredSize(new Dimension(FRAME, FRAME));
		this.sizeSet = true;
		this.revalidate();
	}
}
