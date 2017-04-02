package fr.cubiccl.generator.gui.component.panel.tag;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

import fr.cubi.cubigui.DisplayUtils;
import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gameobject.ItemStack;
import fr.cubiccl.generator.gameobject.baseobjects.Container;
import fr.cubiccl.generator.gameobject.baseobjects.Slot;
import fr.cubiccl.generator.gameobject.loottable.LootTable;
import fr.cubiccl.generator.gameobject.registries.ObjectSaver;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.tags.TagList;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.custom.TemplateItems;
import fr.cubiccl.generator.gui.Dialogs;
import fr.cubiccl.generator.gui.component.button.CGButton;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelItem;
import fr.cubiccl.generator.gui.component.panel.gameobject.display.PanelItemDisplay;
import fr.cubiccl.generator.gui.component.panel.utils.PanelObjectList;
import fr.cubiccl.generator.utils.IStateListener;
import fr.cubiccl.generator.utils.Lang;
import fr.cubiccl.generator.utils.Text;

public class PanelContainer extends SlotSelectionPanel implements IStateListener<PanelItem>, ActionListener
{
	private static final long serialVersionUID = 8606720961759649878L;

	private CGButton buttonEdit, buttonRemove, buttonGenerate;
	public CGPanel container;
	private ItemStack[] items;
	private CGPanel panelAction;
	private PanelItemDisplay panelItemDisplay;
	private int selected = -1;

	public PanelContainer(Container container)
	{
		super(container);
		this.items = new ItemStack[this.container().slots.length];

		this.panelAction = new CGPanel();
		GridBagConstraints gbc = this.panelAction.createGridBagLayout();
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.NONE;
		this.panelAction.add(this.panelItemDisplay = new PanelItemDisplay(), gbc);
		++gbc.gridy;
		--gbc.gridwidth;
		gbc.anchor = GridBagConstraints.WEST;
		this.panelAction.add(this.buttonEdit = new CGButton("general.edit"), gbc);
		++gbc.gridx;
		this.panelAction.add(this.buttonRemove = new CGButton("general.remove"), gbc);

		this.container = new CGPanel();
		gbc = this.container.createGridBagLayout();
		gbc.gridheight = 2;
		this.container.add(this, gbc);
		++gbc.gridx;
		gbc.gridheight = 1;
		this.container.add(this.panelAction, gbc);
		++gbc.gridy;
		this.container.add(this.buttonGenerate = new CGButton("container.generate"), gbc);
		this.buttonGenerate.setVisible(false);

		this.buttonEdit.addActionListener(this);
		this.buttonRemove.addActionListener(this);
		this.buttonGenerate.addActionListener(this);
		this.panelAction.setVisible(false);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.buttonEdit) this.edit();
		else if (e.getSource() == this.buttonRemove && this.items[this.selected] != null) this.delete();
		else if (e.getSource() == this.buttonGenerate)
		{
			PanelObjectList<LootTable> tables = new PanelObjectList<LootTable>(null, (Text) null, LootTable.class);
			tables.setName(new Text("container.generate.title"));
			tables.setValues(ObjectSaver.lootTables.list());
			tables.buttonAdd.setVisible(false);
			tables.buttonEdit.setVisible(false);
			tables.buttonRemove.setVisible(false);
			CommandGenerator.stateManager.setState(tables, new IStateListener<PanelObjectList<LootTable>>()
			{
				@Override
				public boolean shouldStateClose(PanelObjectList<LootTable> panel)
				{
					LootTable table = panel.getSelectedObject();
					if (table != null) generateFromLootTable(table);
					return table != null;
				}
			});
		}
	}

	private void delete()
	{
		if (!Dialogs.showConfirmMessage(new Text("container.confirm_deletion").toString(), Lang.translate("general.yes"), Lang.translate("general.no"))) return;
		this.items[this.selected] = null;
		this.select(-1);
		this.repaint();
	}

	private void edit()
	{
		PanelItem p = new PanelItem("general.item");
		if (this.items[this.selected] != null) p.setupFrom(this.items[this.selected]);
		CommandGenerator.stateManager.setState(p, this);
	}

	public void empty()
	{
		for (int i = 0; i < this.items.length; i++)
			this.items[i] = null;
		this.repaint();
	}

	private void generateFromLootTable(LootTable table)
	{
		ArrayList<ItemStack> generated = table.generateItems();
		this.empty();
		while (!generated.isEmpty())
		{
			// Get available slots
			ArrayList<Integer> slots = new ArrayList<Integer>();
			for (int i = 0; i < this.items.length; ++i)
				if (this.items[i] == null) slots.add(i);

			if (slots.size() == -1) return;
			// Find random slot
			this.items[slots.get(new Random().nextInt(slots.size()))] = generated.get(0);
			generated.remove(0); // Remove used Item
		}
		this.select(-1);
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
		return template.create(tags);
	}

	@Override
	public void onClick()
	{
		if (this.currentSlot() != -1)
		{
			this.select(this.currentSlot());
			if (this.items[this.selected] == null) this.edit();
		}
	}

	@Override
	public void onRightClick()
	{
		if (this.currentSlot() != -1) this.select(this.currentSlot());
		if (this.currentSlot() != -1 && this.items[this.currentSlot()] != null) this.delete();
	}

	@Override
	public void paint(Graphics g)
	{
		super.paint(g);
		if (this.img == null) return;
		g.drawImage(this.img, 0, 0, this.getWidth(), this.getHeight(), null);
		if (this.selected != -1)
		{
			Slot slot = this.container().slots[this.selected];
			this.drawer.drawSelection(g, slot.x * MULTIPLIER, slot.y * MULTIPLIER, Slot.SIZE * MULTIPLIER);
		}
		for (int i = 0; i < this.items.length; ++i)
		{
			ItemStack item = this.items[i];
			if (item != null)
			{
				int x = this.container().slots[i].x * MULTIPLIER, y = this.container().slots[i].y * MULTIPLIER, size = Slot.SIZE * MULTIPLIER;
				g.drawImage(item.texture(), x, y, size, size, null);
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

	private void select(int slot)
	{
		this.selected = slot;
		this.panelAction.setVisible(slot != -1);
		if (this.selected != -1) this.panelItemDisplay.display(this.items[this.selected]);
		this.repaint();
	}

	public void setupFrom(ItemStack[] items)
	{
		for (int i = 0; i < items.length; ++i)
			if (items[i] != null)
			{
				for (int j = 0; j < this.container().slots.length; ++j)
					if (this.container().slots[j].id == items[i].slot) this.items[j] = items[i];

			}
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
		this.items[this.selected] = panel.generate();
		this.items[this.selected].slot = this.container().slots[this.selected].id;// + this.container().startsAt;
		this.panelItemDisplay.display(this.items[this.selected]);
		return true;
	}

}
