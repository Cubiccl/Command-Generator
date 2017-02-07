package fr.cubiccl.generator.gui.component.panel.gameobject;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gameobject.ItemStack;
import fr.cubiccl.generator.gameobject.baseobjects.Item;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gameobject.registries.ObjectSaver;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gui.component.button.CGButton;
import fr.cubiccl.generator.gui.component.interfaces.ICustomObject;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.label.ImageLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.utils.PanelCustomObject;
import fr.cubiccl.generator.gui.component.textfield.CGSpinner;
import fr.cubiccl.generator.utils.IStateListener;
import fr.cubiccl.generator.utils.Utils;

public class PanelItem extends CGPanel implements ActionListener, IStateListener<PanelItemSelection>, ICustomObject<ItemStack>
{
	private static final long serialVersionUID = -8600189753659710473L;

	private Item[] availableItems;
	private CGButton buttonSelectItem;
	private int damage;
	private boolean hasData, hasDurability = true;
	private Item item;
	private CGLabel labelName;
	private ImageLabel labelTexture;
	private PanelTags panelTags;
	private CGSpinner spinnerAmount, spinnerDurability;

	public PanelItem(String titleID)
	{
		this(titleID, ObjectRegistry.items.list(ObjectRegistry.SORT_NUMERICALLY));
	}

	public PanelItem(String titleID, boolean hasData, boolean hasNBT, boolean customObjects, Item[] items)
	{
		super(titleID);
		this.hasData = hasData;
		this.availableItems = items;
		this.item = this.availableItems[0];
		this.damage = 0;

		GridBagConstraints gbc = this.createGridBagLayout();
		gbc.anchor = GridBagConstraints.CENTER;
		this.add(this.labelTexture = new ImageLabel(), gbc);
		++gbc.gridx;
		this.add(this.labelName = new CGLabel(""), gbc);
		++gbc.gridx;
		this.add(this.buttonSelectItem = new CGButton("item.select"), gbc);
		gbc.gridx = 0;
		++gbc.gridy;
		gbc.gridwidth = 3;
		this.add((this.spinnerAmount = new CGSpinner("item.amount", Utils.generateArray(1, 64))).container, gbc);
		++gbc.gridy;
		this.add((this.spinnerDurability = new CGSpinner("item.durability", this.item.damage)).container, gbc);
		++gbc.gridy;
		this.panelTags = new PanelTags("item.tags", Tag.ITEM);
		if (hasNBT) this.add(this.panelTags, gbc);
		gbc.fill = GridBagConstraints.NONE;
		++gbc.gridy;
		if (customObjects) this.add(new PanelCustomObject<ItemStack>(this, ObjectSaver.items), gbc);

		this.buttonSelectItem.addActionListener(this);
		this.panelTags.setTargetObject(this.item);
		this.updateDisplay();
	}

	public PanelItem(String titleID, Item[] items)
	{
		this(titleID, true, true, true, items);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.buttonSelectItem)
		{
			PanelItemSelection p = new PanelItemSelection(this.hasData);
			p.setItems(this.availableItems);
			p.setSelected(this.item);
			for (int i = 0; i < this.item.damage.length; ++i)
				if (this.item.damage[i] == this.damage)
				{
					p.setDamage(i);
					break;
				}
			CommandGenerator.stateManager.setState(p, this);
		}
	}

	@Override
	public ItemStack generate()
	{
		return new ItemStack(this.selectedItem(), this.selectedDamage(), this.selectedAmount(), this.getNBT(Tags.ITEM_NBT));
	}

	public TagCompound getNBT(TemplateCompound container)
	{
		return this.panelTags.generateTags(container);
	}

	public int selectedAmount()
	{
		return this.spinnerAmount.getValue();
	}

	public int selectedDamage()
	{
		return (this.hasData && this.selectedItem().hasDurability) ? this.spinnerDurability.getValue() : this.damage;
	}

	public Item selectedItem()
	{
		return this.item;
	}

	public void setCount(int count)
	{
		this.spinnerAmount.setText(Integer.toString(count));
	}

	public void setDamage(int damage)
	{
		if (this.item.hasDurability && this.item.isDataValid(damage)) this.spinnerDurability.setText(Integer.toString(damage));
		else if (this.item.isDataValid(damage))
		{
			this.damage = damage;
			this.updateDisplay();
		}
	}

	public void setEnabledContent(boolean data, boolean amount)
	{
		this.setHasData(data);
		this.setHasAmount(amount);
	}

	public void setHasAmount(boolean amount)
	{
		this.spinnerAmount.container.setVisible(amount);
	}

	public void setHasData(boolean hasData)
	{
		this.hasData = hasData;
		if (!this.hasData)
		{
			this.damage = 0;
			this.spinnerDurability.setText("0");
			this.updateDisplay();
		}
	}

	public void setHasDurability(boolean hasDurability)
	{
		this.hasDurability = hasDurability;
		this.updateDisplay();
	}

	public void setHasNBT(boolean hasNBT)
	{
		this.panelTags.setVisible(hasNBT);
	}

	public void setItem(Item item)
	{
		if (item == null) return;
		boolean found = false;
		for (Item i : this.availableItems)
			if (item == i)
			{
				found = true;
				break;
			}
		if (!found) return;
		this.item = item;
		this.damage = this.item.damage[0];
		this.panelTags.setTargetObject(this.item);
		this.spinnerDurability.setValues(this.item.damage);
		this.updateDisplay();
	}

	public void setTags(Tag[] tags)
	{
		this.panelTags.setTags(tags);
	}

	@Override
	public void setupFrom(ItemStack itemStack)
	{
		if (itemStack.item == null) return;
		this.setItem(itemStack.item);
		if (this.hasData) this.damage = itemStack.damage;
		this.updateDisplay();
		if (this.spinnerAmount.isVisible()) this.spinnerAmount.setText(Integer.toString(itemStack.amount));
		if (this.hasDurability) this.spinnerDurability.setText(Integer.toString(itemStack.damage));
		if (this.panelTags.isVisible()) this.panelTags.setTags(itemStack.nbt.value());
	}

	@Override
	public boolean shouldStateClose(PanelItemSelection panel)
	{
		this.setItem(panel.selectedItem());
		this.damage = panel.selectedDamage();
		this.updateDisplay();
		return true;
	}

	private void updateDisplay()
	{
		if (this.hasData) this.labelName.setText(this.selectedItem().name(this.damage).toString());
		else this.labelName.setText(this.selectedItem().mainName().toString());
		this.labelTexture.setImage(this.selectedItem().texture(this.damage));

		this.spinnerDurability.container.setVisible(this.hasData && this.hasDurability && this.item.hasDurability);
	}

	@Override
	public void updateTranslations()
	{
		super.updateTranslations();
		if (this.item != null) this.updateDisplay();
	}

}
