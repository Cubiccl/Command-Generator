package fr.cubiccl.generator.gui.component.panel.tag;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import fr.cubi.cubigui.DisplayUtils;
import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gameobject.Container;
import fr.cubiccl.generator.gameobject.ItemStack;
import fr.cubiccl.generator.gameobject.baseobjects.Slot;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagList;
import fr.cubiccl.generator.gameobject.templatetags.custom.TemplateItems;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelItem;
import fr.cubiccl.generator.gui.component.panel.utils.ConfirmPanel;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.IStateListener;

public class ItemsPanel extends CGPanel implements IStateListener<ConfirmPanel>
{
	public static final int MULTIPLIER = 5;
	private static final long serialVersionUID = -3136971123392536978L;

	public final Container container;
	private int currentSlot;
	private BufferedImage img;
	private ItemStack[] items;

	public ItemsPanel(Container container)
	{
		this.container = container;
		this.img = this.container.texture();
		this.items = new ItemStack[this.container.slots.length];
		if (this.img != null)
		{
			this.setPreferredSize(new Dimension(this.img.getWidth() * MULTIPLIER, this.img.getHeight() * MULTIPLIER));
			this.setMinimumSize(new Dimension(this.img.getWidth() * MULTIPLIER, this.img.getHeight() * MULTIPLIER));
		}
		this.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				super.mouseClicked(e);
				onClick();
			}
		});
		this.addMouseMotionListener(new MouseAdapter()
		{
			@Override
			public void mouseMoved(MouseEvent e)
			{
				super.mouseMoved(e);
				onMoveTo(e.getX(), e.getY());
			}
		});
	}

	public TagList generateItems(TemplateItems template)
	{
		ArrayList<Tag> tags = new ArrayList<Tag>();
		for (ItemStack item : this.items)
			if (item != null) tags.add(item.toTag());
		return new TagList(template, tags.toArray(new Tag[tags.size()]));
	}

	private void onClick()
	{
		if (this.currentSlot != -1) CommandGenerator.stateManager.setState(new ConfirmPanel((String) null, new PanelItem("general.item")), this);
	}

	private void onMoveTo(int x, int y)
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
			g.setColor(new Color(255, 255, 255, 100));
			Slot slot = this.container.slots[this.currentSlot];
			g.fillRect(slot.x * MULTIPLIER, slot.y * MULTIPLIER, Slot.SIZE * MULTIPLIER, Slot.SIZE * MULTIPLIER);
		}

		for (int i = 0; i < this.items.length; ++i)
		{
			ItemStack item = this.items[i];
			if (item != null)
			{
				g.drawImage(item.item.texture(item.data), this.container.slots[i].x * MULTIPLIER, this.container.slots[i].y * MULTIPLIER, Slot.SIZE
						* MULTIPLIER, Slot.SIZE * MULTIPLIER, null);
				if (item.amount > 1)
				{
					g.setFont(DisplayUtils.FONT.deriveFont(Font.BOLD, 25));
					g.setColor(Color.WHITE);
					g.drawString(Integer.toString(item.amount), this.container.slots[i].x * MULTIPLIER + Slot.SIZE * MULTIPLIER - g.getFont().getSize()
							- MULTIPLIER, this.container.slots[i].y * MULTIPLIER + Slot.SIZE * MULTIPLIER - g.getFont().getSize() / 4);
				}
			}
		}
	}

	@Override
	public boolean shouldStateClose(ConfirmPanel panel)
	{
		try
		{
			this.items[this.currentSlot] = ((PanelItem) panel.component).generateItem();
		} catch (CommandGenerationException e)
		{
			CommandGenerator.report(e);
			return false;
		}
		return true;
	}

}
