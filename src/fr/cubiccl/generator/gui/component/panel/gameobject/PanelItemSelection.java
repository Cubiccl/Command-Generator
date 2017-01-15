package fr.cubiccl.generator.gui.component.panel.gameobject;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;

import fr.cubi.cubigui.CPanel;
import fr.cubiccl.generator.gameobject.ObjectRegistry;
import fr.cubiccl.generator.gameobject.baseobjects.Item;
import fr.cubiccl.generator.gui.component.CScrollPane;
import fr.cubiccl.generator.gui.component.combobox.ObjectCombobox;
import fr.cubiccl.generator.gui.component.interfaces.IImageSelectionListener;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.label.ImageLabel;
import fr.cubiccl.generator.gui.component.panel.utils.ConfirmPanel;
import fr.cubiccl.generator.gui.component.textfield.CGSpinner;

public class PanelItemSelection extends ConfirmPanel implements ComponentListener
{
	public static final int ITEM_SIZE = 48, GAP = 5;
	private static final long serialVersionUID = -3259302480348824205L;

	private ObjectCombobox<Item> comboboxItem;
	private PanelImageList damageSelector, itemSelector;
	private boolean hasData;
	private Item[] items;
	private ImageLabel labelImage;
	private CGLabel labelName;
	private CScrollPane scrollpane;
	private int selected, damage;
	private CGSpinner spinnerDamage;

	public PanelItemSelection()
	{
		this(true);
	}

	public PanelItemSelection(boolean hasData)
	{
		super("item.select", null);
		this.hasData = hasData;
		this.selected = 0;
		this.damage = 0;
		this.items = ObjectRegistry.getItems(ObjectRegistry.SORT_NUMERICALLY);

		CPanel p = new CPanel();
		GridBagConstraints gbc = p.createGridBagLayout();
		gbc.anchor = GridBagConstraints.CENTER;
		p.add(new CGLabel("block.id").setHasColumn(true), gbc);
		++gbc.gridx;
		p.add((this.comboboxItem = new ObjectCombobox<Item>(this.items)).container, gbc);
		++gbc.gridx;
		p.add((this.spinnerDamage = new CGSpinner("item.data", this.items[0].damage)).container, gbc);
		++gbc.gridx;
		p.add(this.labelImage = new ImageLabel(), gbc);
		++gbc.gridx;
		p.add(this.labelName = new CGLabel(""), gbc);
		gbc.gridx = 0;
		++gbc.gridy;
		gbc.gridwidth = 5;
		p.add(this.scrollpane = new CScrollPane(this.itemSelector = new PanelImageList(new IImageSelectionListener()
		{

			@Override
			public void changeSelection(int objectCount)
			{
				int newObject = selected + objectCount;
				if (newObject < 0) newObject = 0;
				else if (newObject >= items.length) newObject = items.length - 1;
				this.selectObject(newObject);
			}

			@Override
			public int currentSelection()
			{
				return selected;
			}

			@Override
			public void selectObject(int objectIndex)
			{
				if (objectIndex >= 0 && objectIndex < items.length) setSelected(objectIndex, true);
			}
		})), gbc);
		++gbc.gridy;
		if (this.hasData)
		{
			p.add(new CGLabel("item.data").setHasColumn(true), gbc);
			++gbc.gridy;
		}
		p.add(this.damageSelector = new PanelImageList(new IImageSelectionListener()
		{

			@Override
			public void changeSelection(int objectCount)
			{
				int newObject = damage + objectCount;
				if (newObject < 0) newObject = 0;
				else if (newObject >= selectedItem().damage.length) newObject = selectedItem().damage.length - 1;
				this.selectObject(newObject);
			}

			@Override
			public int currentSelection()
			{
				return damage;
			}

			@Override
			public void selectObject(int objectIndex)
			{
				if (objectIndex >= 0 && objectIndex < selectedItem().damage.length) setDamage(objectIndex);
			}
		}), gbc);

		BufferedImage[] images = new BufferedImage[this.items.length];
		for (int i = 0; i < images.length; ++i)
			images[i] = this.items[i].texture(0);
		this.itemSelector.setImages(images);

		if (!this.hasData)
		{
			this.damageSelector.setVisible(false);
			this.spinnerDamage.container.setVisible(false);
		}
		this.comboboxItem.addActionListener(this);
		this.spinnerDamage.addActionListener(this);
		this.setMainComponent(p);
		this.addComponentListener(this);
		this.setSelected(0, true);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		super.actionPerformed(e);
		if (e.getSource() == this.comboboxItem)
		{
			int index = 0;
			String value = this.comboboxItem.getValue();
			for (int i = 0; i < this.items.length; ++i)
				if (this.items[i].idString.equals(value))
				{
					index = i;
					break;
				}
			this.setSelected(index, false);
		}
		if (e.getSource() == this.spinnerDamage) this.setDamage(this.spinnerDamage.getValue());
	}

	@Override
	public void componentHidden(ComponentEvent e)
	{}

	@Override
	public void componentMoved(ComponentEvent e)
	{}

	@Override
	public void componentResized(ComponentEvent e)
	{
		int width = this.getWidth() - 80;
		int height = this.getHeight() - 300;
		width -= width % (ITEM_SIZE + GAP);
		height -= height % (ITEM_SIZE + GAP);
		this.scrollpane.setPreferredSize(new Dimension(width + 15, height));
		this.itemSelector.setObjectsPerLine(width / (ITEM_SIZE + GAP));
	}

	@Override
	public void componentShown(ComponentEvent e)
	{}

	public int selectedDamage()
	{
		if (!this.hasData) return this.selectedItem().damage[0];
		return this.selectedItem().damage[this.damage];
	}

	public Item selectedItem()
	{
		return this.items[this.selected];
	}

	public void setDamage(int damage)
	{
		this.damage = damage;
		this.damageSelector.repaint();
		this.spinnerDamage.setText(Integer.toString(damage));
		this.updateDisplay();
	}

	public void setItems(Item... items)
	{
		this.items = items;
		this.comboboxItem.setValues(this.items);
		BufferedImage[] images = new BufferedImage[this.items.length];
		for (int i = 0; i < images.length; ++i)
			images[i] = this.items[i].texture(0);
		this.itemSelector.setImages(images);
		this.itemSelector.repaint();
		this.damageSelector.repaint();
		this.updateDisplay();
	}

	public void setSelected(int selected, boolean sendUpdates)
	{
		this.selected = selected;
		if (sendUpdates) this.comboboxItem.setSelectedItem(this.selectedItem().name().toString());
		this.spinnerDamage.setValues(this.selectedItem().damage);
		this.itemSelector.repaint();

		BufferedImage[] images = new BufferedImage[this.selectedItem().hasDurability ? 1 : this.selectedItem().damage.length];
		for (int i = 0; i < images.length; i++)
			images[i] = this.selectedItem().texture(this.selectedItem().damage[i]);
		this.damageSelector.setImages(images);
		this.setDamage(Math.min(this.damage, this.selectedItem().damage.length - 1));
		this.updateDisplay();
	}

	public void setSelected(Item item)
	{
		for (int i = 0; i < this.items.length; ++i)
			if (this.items[i].idString.equals(item.idString))
			{
				this.setSelected(i, true);
				break;
			}
	}

	private void updateDisplay()
	{
		if (this.hasData) this.labelName.setTextID(this.selectedItem().name(this.selectedDamage()));
		else this.labelName.setTextID(this.selectedItem().mainName());
		this.labelImage.setImage(this.selectedItem().texture(this.selectedDamage()));
	}

}
