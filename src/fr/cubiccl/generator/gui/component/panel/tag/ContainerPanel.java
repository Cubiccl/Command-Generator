package fr.cubiccl.generator.gui.component.panel.tag;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import fr.cubi.cubigui.DisplayUtils;
import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gameobject.ItemStack;
import fr.cubiccl.generator.gameobject.baseobjects.Container;
import fr.cubiccl.generator.gameobject.baseobjects.Slot;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.tags.TagList;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.custom.TemplateItems;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelItem;
import fr.cubiccl.generator.utils.IStateListener;
import fr.cubiccl.generator.utils.Lang;

public class ContainerPanel extends SlotSelectionPanel implements IStateListener<PanelItem>
{
	private static final long serialVersionUID = 8606720961759649878L;

	private ItemStack[] items;

	public ContainerPanel(Container container)
	{
		super(container);
		this.items = new ItemStack[this.container().slots.length];
	}

	public void empty()
	{
		for (int i = 0; i < this.items.length; i++)
			this.items[i] = null;
		this.repaint();
	}

	public ItemStack[] generateItems(boolean hasSlot)
	{
		if (!hasSlot) for (ItemStack item : this.items)
			if (item != null) item.slot = -1;

		ArrayList<ItemStack> toreturn = new ArrayList<ItemStack>();
		for (ItemStack item : this.items)
			if (item != null) try
			{
				toreturn.add(item.clone());
			} catch (CloneNotSupportedException e)
			{
				e.printStackTrace();
			}

		return toreturn.toArray(new ItemStack[toreturn.size()]);
	}

	public TagList generateItems(TemplateItems template, boolean hasSlot)
	{
		ItemStack[] generated = this.generateItems(hasSlot);
		TagCompound[] tags = new TagCompound[generated.length];
		for (int i = 0; i < tags.length; i++)
			tags[i] = generated[i].toTag(Tags.DEFAULT_COMPOUND);
		return new TagList(template, tags);
	}

	@Override
	public void onClick()
	{
		if (this.currentSlot() != -1)
		{
			PanelItem p = new PanelItem("general.item");
			if (this.items[this.currentSlot()] != null) p.setupFrom(this.items[this.currentSlot()]);
			CommandGenerator.stateManager.setState(p, this);
		}
	}

	@Override
	public void onRightClick()
	{
		if (this.currentSlot() != -1 && this.items[this.currentSlot()] != null)
		{
			if (JOptionPane.showConfirmDialog(this, Lang.translate("container.confirm_deletion"), "", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) != 0) return;
			this.items[this.currentSlot()] = null;
			this.repaint();
		}
	}

	@Override
	public void paint(Graphics g)
	{
		super.paint(g);
		if (this.img == null) return;
		g.drawImage(this.img, 0, 0, this.getWidth(), this.getHeight(), null);
		for (int i = 0; i < this.items.length; ++i)
		{
			ItemStack item = this.items[i];
			if (item != null)
			{
				int x = this.container().slots[i].x * MULTIPLIER, y = this.container().slots[i].y * MULTIPLIER, size = Slot.SIZE * MULTIPLIER;
				g.drawImage(item.item.texture(item.damage), x, y, size, size, null);
				if (item.amount > 1)
				{
					g.setFont(DisplayUtils.FONT.deriveFont(Font.BOLD, 25));
					g.setColor(Color.WHITE);
					g.drawString(Integer.toString(item.amount), x + Slot.SIZE * MULTIPLIER - g.getFont().getSize() - MULTIPLIER, y + Slot.SIZE * MULTIPLIER
							- g.getFont().getSize() / 4);
				}
			}
		}
		if (this.currentSlot() != -1)
		{
			Slot slot = this.container().slots[this.currentSlot()];
			this.drawer.drawHovering(g, slot.x * MULTIPLIER, slot.y * MULTIPLIER, Slot.SIZE * MULTIPLIER);
		}

	}

	public void setupFrom(ItemStack[] items)
	{
		for (int i = 0; i < items.length; ++i)
			if (items[i] != null) this.items[items[i].slot] = items[i];
		this.repaint();
	}

	public void setupFrom(TagList previousValue)
	{
		ItemStack[] items = new ItemStack[previousValue.size()];
		for (int i = 0; i < items.length; ++i)
		{
			ItemStack stack = ItemStack.createFrom((TagCompound) previousValue.getTag(i));
			items[i] = stack;
		}
		this.setupFrom(items);
	}

	@Override
	public boolean shouldStateClose(PanelItem panel)
	{
		this.items[this.currentSlot()] = panel.generateItem();
		this.items[this.currentSlot()].slot = this.currentSlot() + this.container().startsAt;
		return true;
	}

}
