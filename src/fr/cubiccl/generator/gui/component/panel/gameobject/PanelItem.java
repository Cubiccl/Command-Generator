package fr.cubiccl.generator.gui.component.panel.gameobject;

import java.awt.GridBagConstraints;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Set;

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
import fr.cubiccl.generator.gui.component.interfaces.ITranslated;
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
	private Set<ITranslated> listeners;
	private PanelTags panelTags;
	private CGSpinner spinnerAmount, spinnerDurability;

	public PanelItem(String titleID)
	{
		this(titleID, ObjectRegistry.items.list(ObjectRegistry.SORT_NUMERICALLY));
	}

	public PanelItem(String titleID, boolean hasData, boolean hasNBT, boolean customObjects)
	{
		this(titleID, hasData, hasNBT, customObjects, ObjectRegistry.items.list(ObjectRegistry.SORT_NUMERICALLY));
	}

	public PanelItem(String titleID, boolean hasData, boolean hasNBT, boolean customObjects, Item[] items)
	{
		super(titleID);
		this.hasData = hasData;
		this.availableItems = items;
		this.item = this.availableItems[0];
		this.damage = 0;
		this.listeners = new HashSet<ITranslated>();

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
		this.add((this.spinnerDurability = new CGSpinner("item.durability", Utils.generateArray(this.item.getDurability()))).container, gbc);
		++gbc.gridy;
		this.panelTags = new PanelTags("item.tags", Tag.ITEM);
		if (hasNBT) this.add(this.panelTags, gbc);
		gbc.fill = GridBagConstraints.NONE;
		++gbc.gridy;
		if (customObjects) this.add(new PanelCustomObject<ItemStack, ItemStack>(this, ObjectSaver.items), gbc);

		this.buttonSelectItem.addActionListener(this);
		this.panelTags.setTargetObject(this.item);
		this.spinnerAmount.addFocusListener(new FocusListener()
		{

			@Override
			public void focusGained(FocusEvent e)
			{}

			@Override
			public void focusLost(FocusEvent e)
			{
				for (ITranslated listener : listeners)
					listener.updateTranslations();
			}
		});
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
			int[] damage = this.item.getDamageValues();
			for (int i = 0; i < damage.length; ++i)
				if (damage[i] == this.damage)
				{
					p.setDamage(i);
					break;
				}
			CommandGenerator.stateManager.setState(p, this);
		}
	}

	public void addArgumentChangeListener(ITranslated listener)
	{
		this.listeners.add(listener);
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

	public void removeArgumentChangeListener(ITranslated listener)
	{
		this.listeners.remove(listener);
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
		if (this.item.hasDurability && this.item.isDamageValid(damage)) this.spinnerDurability.setText(Integer.toString(damage));
		else if (this.item.isDamageValid(damage))
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
		this.damage = this.item.getDamageValues()[0];
		this.panelTags.setTargetObject(this.item);
		this.spinnerDurability.setValues(Utils.generateArray(this.item.getDurability()));
		this.updateDisplay();
	}

	public void setTags(Tag[] tags)
	{
		this.panelTags.setValues(tags);
	}

	@Override
	public void setupFrom(ItemStack itemStack)
	{
		if (itemStack.getItem() == null) return;
		this.setItem(itemStack.getItem());
		if (this.hasData) this.damage = itemStack.getDamage();
		this.updateDisplay();
		if (this.spinnerAmount.isVisible()) this.spinnerAmount.setText(Integer.toString(itemStack.amount));
		if (this.hasDurability) this.spinnerDurability.setText(Integer.toString(itemStack.getDamage()));
		if (this.panelTags.isVisible()) this.panelTags.setValues(itemStack.getNbt().value());
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
		for (ITranslated listener : this.listeners)
			listener.updateTranslations();
	}

	@Override
	public void updateTranslations()
	{
		super.updateTranslations();
		if (this.item != null) this.updateDisplay();
	}

}
