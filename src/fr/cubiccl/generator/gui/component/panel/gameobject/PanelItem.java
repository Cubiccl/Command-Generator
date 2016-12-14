package fr.cubiccl.generator.gui.component.panel.gameobject;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gameobject.ItemStack;
import fr.cubiccl.generator.gameobject.ObjectRegistry;
import fr.cubiccl.generator.gameobject.baseobjects.Item;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gui.component.button.CGButton;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.label.ImageLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.textfield.CGSpinner;
import fr.cubiccl.generator.utils.IStateListener;
import fr.cubiccl.generator.utils.Utils;

public class PanelItem extends CGPanel implements ActionListener, IStateListener<PanelItemSelection>
{
	private static final long serialVersionUID = -8600189753659710473L;

	private Item[] availableItems;
	private CGButton buttonSelectItem;
	private int damage;
	private boolean hasData;
	private Item item;
	private CGLabel labelName;
	private ImageLabel labelTexture;
	private PanelTags panelTags;
	private CGSpinner spinnerAmount, spinnerDurability;

	public PanelItem(String titleID)
	{
		this(titleID, ObjectRegistry.getItems(ObjectRegistry.SORT_NUMERICALLY));
	}

	public PanelItem(String titleID, boolean hasData, Item[] items)
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
		this.add((this.spinnerAmount = new CGSpinner("item.amount", Utils.generateArray(64))).container, gbc);
		++gbc.gridy;
		this.add((this.spinnerDurability = new CGSpinner("item.durability", this.item.damage)).container, gbc);
		++gbc.gridy;
		this.add(this.panelTags = new PanelTags("item.tags", Tag.ITEM), gbc);

		this.buttonSelectItem.addActionListener(this);
		this.updateDisplay();
	}

	public PanelItem(String titleID, Item[] items)
	{
		this(titleID, true, items);
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

	public ItemStack generateItem()
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

	public void setEnabledContent(boolean data, boolean amount)
	{
		this.setHasData(data);
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

	public void setupFrom(ItemStack itemStack)
	{
		this.item = itemStack.item;
		this.damage = itemStack.damage;
		this.updateDisplay();
		this.spinnerAmount.setText(Integer.toString(itemStack.amount));
		this.spinnerDurability.setValues(this.item.damage);
		this.spinnerDurability.setText(Integer.toString(itemStack.damage));
		this.panelTags.setObjectForTags(this.item.idString);
	}

	@Override
	public boolean shouldStateClose(PanelItemSelection panel)
	{
		this.item = panel.selectedItem();
		this.damage = panel.selectedDamage();
		this.updateDisplay();
		this.spinnerDurability.setValues(this.item.damage);
		this.panelTags.setObjectForTags(this.item.idString);
		return true;
	}

	private void updateDisplay()
	{
		if (this.hasData) this.labelName.setText(this.selectedItem().name(this.damage).toString());
		else this.labelName.setText(this.selectedItem().mainName().toString());
		this.labelTexture.setImage(this.selectedItem().texture(this.damage));

		this.spinnerDurability.container.setVisible(this.hasData && this.item.hasDurability);
	}

}
