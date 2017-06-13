package fr.cubiccl.generator.gui.component.panel.advancement;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gameobject.Enchantment;
import fr.cubiccl.generator.gameobject.ItemStack;
import fr.cubiccl.generator.gameobject.baseobjects.Item;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gameobject.tags.NBTParser;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.custom.TemplatePotion;
import fr.cubiccl.generator.gui.component.button.CGButton;
import fr.cubiccl.generator.gui.component.button.CGCheckBox;
import fr.cubiccl.generator.gui.component.combobox.OptionCombobox;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.label.ImageLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelItemSelection;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelTags;
import fr.cubiccl.generator.gui.component.panel.utils.PanelObjectList;
import fr.cubiccl.generator.gui.component.textfield.CGEntry;
import fr.cubiccl.generator.gui.component.textfield.CGSpinner;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.IStateListener;
import fr.cubiccl.generator.utils.Text;
import fr.cubiccl.generator.utils.Utils;

public class PanelTestedItem extends CGPanel implements ActionListener, IStateListener<PanelItemSelection>
{
	private static final long serialVersionUID = 6221671199256667173L;

	private CGButton buttonSelectItem;
	private CGCheckBox checkboxID, checkboxAmount, checkboxDamage, checkboxPotion, checkboxEnchantments;
	private OptionCombobox comboboxPotion;
	private int damage;
	private CGEntry entryDamage;
	private Item item;
	private CGLabel labelName, labelPotion;
	private ImageLabel labelTexture;
	private PanelObjectList<Enchantment> panelEnchantments;
	private CGPanel panelItem;
	private PanelTags panelTags;
	private CGSpinner spinnerAmount, spinnerDurability;

	public PanelTestedItem()
	{
		super();

		this.item = ObjectRegistry.items.first();
		this.damage = 0;

		CGPanel p = new CGPanel("advancement.check");
		GridBagConstraints gbc = p.createGridBagLayout();
		p.add(this.checkboxID = new CGCheckBox("item.id"), gbc);
		++gbc.gridx;
		p.add(this.checkboxAmount = new CGCheckBox("item.amount"), gbc);
		++gbc.gridx;
		p.add(this.checkboxDamage = new CGCheckBox("item.data"), gbc);
		++gbc.gridx;
		p.add(this.checkboxPotion = new CGCheckBox("tag.title.CustomPotionEffects"), gbc);
		++gbc.gridx;
		p.add(this.checkboxEnchantments = new CGCheckBox("tag.title.ench"), gbc);
		++gbc.gridx;

		this.panelItem = new CGPanel();
		gbc = this.panelItem.createGridBagLayout();
		gbc.anchor = GridBagConstraints.CENTER;
		this.panelItem.add(this.labelTexture = new ImageLabel(), gbc);
		++gbc.gridx;
		this.panelItem.add(this.labelName = new CGLabel(""), gbc);
		++gbc.gridx;
		this.panelItem.add(this.buttonSelectItem = new CGButton("item.select"), gbc);

		gbc = this.createGridBagLayout();
		gbc.gridwidth = 2;
		this.add(p, gbc);
		++gbc.gridy;
		this.add(this.panelItem, gbc);
		++gbc.gridy;
		this.add((this.spinnerAmount = new CGSpinner("item.amount", Utils.generateArray(1, 64))).container, gbc);
		++gbc.gridy;
		this.add((this.spinnerDurability = new CGSpinner("item.durability", Utils.generateArray(this.item.getDurability()))).container, gbc);
		this.add((this.entryDamage = new CGEntry(new Text("item.data"), "0", Text.INTEGER)).container, gbc);
		++gbc.gridy;
		gbc.gridwidth = 1;
		this.add(this.labelPotion = new CGLabel("tag.title.CustomPotionEffects").setHasColumn(true), gbc);
		++gbc.gridx;
		this.add(this.comboboxPotion = new OptionCombobox("tag.Potion", TemplatePotion.POTIONS), gbc);
		--gbc.gridx;
		++gbc.gridy;
		gbc.gridwidth = 2;
		this.add(this.panelEnchantments = new PanelObjectList<Enchantment>("tag.title.ench", (Text) null, Enchantment.class, "testing", true), gbc);
		++gbc.gridy;
		this.add(this.panelTags = new PanelTags("item.tags", Tag.ITEM), gbc);
		this.panelTags.setTargetObject(null);

		this.buttonSelectItem.addActionListener(this);
		this.checkboxID.addActionListener(this);
		this.checkboxAmount.addActionListener(this);
		this.checkboxDamage.addActionListener(this);
		this.checkboxPotion.addActionListener(this);
		this.checkboxEnchantments.addActionListener(this);

		this.onCheckbox();
		this.updateDisplay();
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.buttonSelectItem)
		{
			PanelItemSelection p = new PanelItemSelection(this.hasData());
			p.setSelected(this.item);
			int[] damage = this.item.getDamageValues();
			for (int i = 0; i < damage.length; ++i)
				if (damage[i] == this.damage)
				{
					p.setDamage(i);
					break;
				}
			CommandGenerator.stateManager.setState(p, this);
		} else this.onCheckbox();
	}

	public ItemStack generate() throws CommandGenerationException
	{
		ItemStack i = new ItemStack(null, -1, -1);
		if (this.checkboxID.isSelected()) i.setItem(this.item);
		if (this.hasData())
		{
			if (this.checkboxID.isSelected())
			{
				if (this.item.hasDurability) i.setDamage(this.spinnerDurability.getValue());
				else i.setDamage(this.damage);
			} else
			{
				this.entryDamage.checkValueSuperior(CGEntry.INTEGER, 0);
				i.setDamage(Integer.parseInt(this.entryDamage.getText()));
			}
		}
		if (this.checkboxAmount.isSelected()) i.amount = this.spinnerAmount.getValue();
		if (this.checkboxPotion.isSelected()) i.getNbt().addTag(Tags.CRITERIA_POTION.create(this.comboboxPotion.getValue()));
		if (this.checkboxEnchantments.isSelected())
		{
			Enchantment[] e = this.panelEnchantments.values();
			ArrayList<TagCompound> tags = new ArrayList<TagCompound>();
			for (Enchantment enchantment : e)
				tags.add(enchantment.toTagForTest(Tags.DEFAULT_COMPOUND));
			i.getNbt().addTag(Tags.ITEM_ENCHANTMENTS.create(tags.toArray(new TagCompound[tags.size()])));
		}
		TagCompound t = this.panelTags.generateTags(Tags.DEFAULT_COMPOUND);
		if (t.size() != 0) i.getNbt().addTag(Tags.CRITERIA_NBT.create(t.valueForCommand()));
		return i;
	}

	private boolean hasData()
	{
		return this.checkboxDamage.isSelected();
	}

	private void onCheckbox()
	{
		this.panelItem.setVisible(this.checkboxID.isSelected());
		this.spinnerDurability.container.setVisible(this.checkboxID.isSelected() && this.hasData() && this.item.hasDurability);
		this.entryDamage.container.setVisible(!this.checkboxID.isSelected() && this.hasData());
		this.spinnerAmount.container.setVisible(this.checkboxAmount.isSelected());
		this.labelPotion.setVisible(this.checkboxPotion.isSelected());
		this.comboboxPotion.setVisible(this.checkboxPotion.isSelected());
		this.panelEnchantments.setVisible(this.checkboxEnchantments.isSelected());
		if (!this.hasData())
		{
			this.damage = 0;
			this.updateDisplay();
		}
	}

	public void setItem(Item item)
	{
		if (item == null) return;
		this.item = item;
		this.damage = this.item.getDamageValues()[0];
		this.spinnerDurability.setValues(Utils.generateArray(this.item.getDurability()));
		this.updateDisplay();
	}

	public void setupFrom(ItemStack item)
	{
		if (item.getItem() != null)
		{
			this.item = item.getItem();
			this.checkboxID.setSelected(true);
		}
		if (item.getDamage() != -1)
		{
			if (this.checkboxID.isSelected())
			{
				if (this.item.hasDurability) this.spinnerDurability.setText(Integer.toString(item.getDamage()));
				else this.damage = item.getDamage();
			} else this.entryDamage.setText(Integer.toString(item.getDamage()));
			this.checkboxDamage.setSelected(true);
		}
		if (item.amount != -1)
		{
			this.spinnerAmount.setText(Integer.toString(item.amount));
			this.checkboxAmount.setSelected(true);
		}
		if (item.getNbt().hasTag(Tags.CRITERIA_POTION))
		{
			this.comboboxPotion.setValue(item.getNbt().getTag(Tags.CRITERIA_POTION).value());
			this.checkboxPotion.setSelected(true);
		}
		if (item.getNbt().hasTag(Tags.ITEM_ENCHANTMENTS))
		{
			this.panelEnchantments.clear();
			for (Tag tag : item.getNbt().getTag(Tags.ITEM_ENCHANTMENTS).value())
				this.panelEnchantments.add(new Enchantment().fromNBT((TagCompound) tag));
			this.checkboxEnchantments.setSelected(true);
		}
		if (item.getNbt().hasTag(Tags.CRITERIA_NBT)) this.panelTags.setValues(((TagCompound) NBTParser.parse(item.getNbt().getTag(Tags.CRITERIA_NBT).value(),
				true, true)).value());
		this.onCheckbox();
		this.updateDisplay();
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
		if (this.hasData()) this.labelName.setText(this.item.name(this.damage).toString());
		else this.labelName.setText(this.item.mainName().toString());
		this.labelTexture.setImage(this.item.texture(this.damage));
		this.spinnerDurability.container.setVisible(this.hasData() && this.item.hasDurability);
	}

}
