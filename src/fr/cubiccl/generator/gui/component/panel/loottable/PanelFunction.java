package fr.cubiccl.generator.gui.component.panel.loottable;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ButtonGroup;

import fr.cubiccl.generator.gameobject.AttributeModifier;
import fr.cubiccl.generator.gameobject.baseobjects.EnchantmentType;
import fr.cubiccl.generator.gameobject.loottable.LootTableFunction;
import fr.cubiccl.generator.gameobject.loottable.LootTableFunction.Function;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gameobject.tags.*;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gameobject.templatetags.TemplateNumber;
import fr.cubiccl.generator.gui.component.button.CGCheckBox;
import fr.cubiccl.generator.gui.component.button.CGRadioButton;
import fr.cubiccl.generator.gui.component.combobox.CGComboBox;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelItem;
import fr.cubiccl.generator.gui.component.panel.utils.PanelObjectList;
import fr.cubiccl.generator.gui.component.textfield.CGEntry;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Utils;

public class PanelFunction extends CGPanel implements ActionListener
{
	private static final long serialVersionUID = -4719925430759085969L;

	private PanelObjectList<AttributeModifier> attributes;
	private CGRadioButton buttonFixed, buttonRange;
	private CGCheckBox checkboxTreasure;
	private CGComboBox comboboxFunction;
	private PanelConditionList conditions;
	private PanelObjectList<EnchantmentType> enchantments;
	private CGEntry entryMin, entryMax, entryLimit;
	private CGLabel labelFunction;
	private PanelItem panelNbt;

	public PanelFunction()
	{
		GridBagConstraints gbc = this.createGridBagLayout();

		String[] names = new String[Function.values().length];
		for (int i = 0; i < names.length; ++i)
			names[i] = Function.values()[i].translate().toString();

		++gbc.gridwidth;
		this.add(this.comboboxFunction = new CGComboBox(names), gbc);
		++gbc.gridy;
		this.add(this.labelFunction = new CGLabel((String) null), gbc);

		++gbc.gridy;
		--gbc.gridwidth;
		this.add(this.buttonFixed = new CGRadioButton("lt_function.fixed"), gbc);
		++gbc.gridx;
		this.add(this.buttonRange = new CGRadioButton("lt_function.ranged"), gbc);

		--gbc.gridx;
		++gbc.gridy;
		++gbc.gridwidth;
		this.add(this.enchantments = new PanelObjectList<EnchantmentType>("lt_function.enchantments", (String) null, EnchantmentType.class), gbc);
		this.add(
				this.attributes = new PanelObjectList<AttributeModifier>("lt_function.modifiers", (String) null, AttributeModifier.class, "random_slots", true),
				gbc);
		this.add(this.panelNbt = new PanelItem("lt_function.nbt"), gbc);
		this.add((this.entryMin = new CGEntry(null)).container, gbc);
		++gbc.gridy;
		this.add((this.entryMax = new CGEntry("scoreboard.test.max")).container, gbc);
		++gbc.gridy;
		this.add(this.checkboxTreasure = new CGCheckBox("lt_function.enchantment.treasure"), gbc);
		this.add((this.entryLimit = new CGEntry("lt_function.looting.limit")).container, gbc);

		this.add(new CGLabel("loottable.conditions.description"), gbc);
		++gbc.gridy;
		this.add(this.conditions = new PanelConditionList("loottable.conditions", "loottable.condition"), gbc);

		ButtonGroup group = new ButtonGroup();
		group.add(this.buttonFixed);
		group.add(this.buttonRange);
		this.buttonFixed.setSelected(true);

		this.comboboxFunction.addActionListener(this);
		this.buttonFixed.addActionListener(this);
		this.buttonRange.addActionListener(this);
		this.entryMin.addNumberFilter();
		this.entryMax.addNumberFilter();
		this.entryLimit.addIntFilter();
		this.panelNbt.setHasAmount(false);
		this.panelNbt.setHasData(false);
		this.updateDisplay();
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		this.updateDisplay();
	}

	public LootTableFunction generate() throws CommandGenerationException
	{
		Function f = this.selectedFunction();
		ArrayList<Tag> tags = new ArrayList<Tag>();

		if (f == Function.SET_ATTRIBUTES)
		{
			AttributeModifier[] modifiers = this.attributes.values();
			TagCompound[] t = new TagCompound[modifiers.length];
			for (int i = 0; i < t.length; ++i)
				t[i] = modifiers[i].toTag(Tags.DEFAULT_COMPOUND, false);
			tags.add(Tags.LT_FUNCTION_MODIFIERS.create(t));
		} else if (f == Function.ENCHANT_RANDOMLY)
		{
			EnchantmentType[] enchants = this.enchantments.values();
			TagString[] ids = new TagString[enchants.length];
			for (int i = 0; i < ids.length; ++i)
				ids[i] = Tags.DEFAULT_STRING.create(enchants[i].id());
			tags.add(Tags.LT_FUNCTION_ENCHANTMENTS.create(ids));
		} else if (f == Function.SET_NBT) tags.add(Tags.LT_FUNCTION_NBT.create(this.panelNbt.generate().getNbt().valueForCommand()));
		else if (f != Function.FURNACE_SMELT)
		{
			if (f == Function.SET_DAMAGE) this.entryMin.checkValueInBounds(CGEntry.FLOAT, 0, 100);
			else this.entryMin.checkValueSuperior(CGEntry.INTEGER, 0);
			if (this.buttonFixed.isSelected())
			{
				TemplateNumber t = Tags.LT_FUNCTION_LEVELS;
				if (f == Function.LOOTING_ENCHANT || f == Function.SET_COUNT) t = Tags.LT_FUNCTION_COUNT;
				if (f == Function.SET_DAMAGE) t = Tags.LT_FUNCTION_DAMAGE;
				if (f == Function.SET_DATA) t = Tags.LT_FUNCTION_DATA;

				if (f == Function.SET_DAMAGE) tags.add(t.create(Double.parseDouble(this.entryMin.getText()) / 100.0));
				else tags.add(t.create(Integer.parseInt(this.entryMin.getText())));
			} else
			{
				if (f == Function.SET_DAMAGE) this.entryMax.checkValueInBounds(CGEntry.FLOAT, Double.parseDouble(this.entryMin.getText()), 100);
				else this.entryMax.checkValueSuperior(CGEntry.INTEGER, Integer.parseInt(this.entryMin.getText()));
				TemplateCompound t = Tags.LT_FUNCTION_LEVELS_RANGE;
				if (f == Function.LOOTING_ENCHANT || f == Function.SET_COUNT) t = Tags.LT_FUNCTION_COUNT_RANGE;
				if (f == Function.SET_DAMAGE) t = Tags.LT_FUNCTION_DAMAGE_RANGE;
				if (f == Function.SET_DATA) t = Tags.LT_FUNCTION_DATA_RANGE;
				TemplateNumber min = Tags.LT_FUNCTION_MIN, max = Tags.LT_FUNCTION_MAX;
				if (f == Function.SET_DAMAGE)
				{
					min = Tags.LT_FUNCTION_MIN_FLOAT;
					max = Tags.LT_FUNCTION_MAX_FLOAT;
				}

				if (f == Function.SET_DAMAGE) tags.add(t.create(min.create(Double.parseDouble(this.entryMin.getText()) / 100.0),
						max.create(Double.parseDouble(this.entryMax.getText()) / 100.0)));
				else tags.add(t.create(min.create(Integer.parseInt(this.entryMin.getText())), max.create(Integer.parseInt(this.entryMax.getText()))));
			}
		}

		if (f == Function.ENCHANT_WITH_LEVELS) tags.add(Tags.LT_FUNCTION_TREASURE.create(this.checkboxTreasure.isSelected()));
		if (f == Function.LOOTING_ENCHANT)
		{
			this.entryLimit.checkValueSuperior(CGEntry.INTEGER, 0);
			tags.add(Tags.LT_FUNCTION_LOOTING_LIMIT.create(Integer.parseInt(this.entryLimit.getText())));
		}

		return new LootTableFunction(f, this.conditions.values(), tags.toArray(new Tag[tags.size()]));
	}

	public Function selectedFunction()
	{
		for (Function f : Function.values())
			if (f.translate().toString().equals(this.comboboxFunction.getSelectedItem())) return f;
		return Function.values()[0];
	}

	public void setupFrom(LootTableFunction function)
	{
		Function f = function.function;
		this.comboboxFunction.setSelectedItem(f.translate().toString());
		TagCompound t = Tags.DEFAULT_COMPOUND.create(function.tags);
		if (t.size() != 0)
		{

			if (f == Function.SET_ATTRIBUTES && t.hasTag(Tags.LT_FUNCTION_MODIFIERS))
			{
				Tag[] m = ((TagList) t.getTag(Tags.LT_FUNCTION_MODIFIERS)).value();
				AttributeModifier[] modifiers = new AttributeModifier[m.length];
				for (int i = 0; i < m.length; ++i)
					modifiers[i] = AttributeModifier.createFrom((TagCompound) m[i]);
				this.attributes.setValues(modifiers);
			} else if (f == Function.ENCHANT_RANDOMLY && t.hasTag(Tags.LT_FUNCTION_ENCHANTMENTS))
			{
				Tag[] e = ((TagList) t.getTag(Tags.LT_FUNCTION_ENCHANTMENTS)).value();
				EnchantmentType[] enchantments = new EnchantmentType[e.length];
				for (int i = 0; i < e.length; ++i)
					enchantments[i] = ObjectRegistry.enchantments.find(((TagString) e[i]).value());
				this.enchantments.setValues(enchantments);
			} else if (f == Function.SET_NBT && t.hasTag(Tags.LT_FUNCTION_NBT))
			{
				TagCompound tag = (TagCompound) NBTReader.read(((TagString) t.getTag(Tags.LT_FUNCTION_NBT)).value(), true, true);
				String[] apps = tag.findApplications();
				if (apps.length != 0) this.panelNbt.setItem(ObjectRegistry.items.find(apps[0]));
				this.panelNbt.setTags(tag.value());
			} else if (f != Function.FURNACE_SMELT)
			{
				TemplateNumber valueTag = Tags.LT_FUNCTION_LEVELS;
				if (f == Function.LOOTING_ENCHANT || f == Function.SET_COUNT) valueTag = Tags.LT_FUNCTION_COUNT;
				else if (f == Function.SET_DAMAGE) valueTag = Tags.LT_FUNCTION_DAMAGE;
				else if (f == Function.SET_DATA) valueTag = Tags.LT_FUNCTION_DATA;
				boolean fixed = t.hasTag(valueTag);

				if (fixed)
				{
					this.buttonFixed.setSelected(true);
					if (f == Function.SET_DAMAGE) this.entryMin.setText(Utils.doubleToString(((TagBigNumber) t.getTag(valueTag)).value() * 100));
					else this.entryMin.setText(Integer.toString(((TagNumber) t.getTag(valueTag)).value()));
				} else
				{
					this.buttonRange.setSelected(true);
					TemplateCompound containerTag = Tags.LT_FUNCTION_LEVELS_RANGE;
					if (f == Function.LOOTING_ENCHANT || f == Function.SET_COUNT) containerTag = Tags.LT_FUNCTION_COUNT_RANGE;
					if (f == Function.SET_DAMAGE) containerTag = Tags.LT_FUNCTION_DAMAGE_RANGE;
					if (f == Function.SET_DATA) containerTag = Tags.LT_FUNCTION_DATA_RANGE;

					TemplateNumber min = Tags.LT_FUNCTION_MIN, max = Tags.LT_FUNCTION_MAX;
					if (f == Function.SET_DAMAGE)
					{
						min = Tags.LT_FUNCTION_MIN_FLOAT;
						max = Tags.LT_FUNCTION_MAX_FLOAT;
					}

					TagCompound container = (TagCompound) t.getTag(containerTag);

					if (f == Function.SET_DAMAGE)
					{
						this.entryMin.setText(Utils.doubleToString(((TagBigNumber) container.getTag(min)).value() * 100));
						this.entryMax.setText(Utils.doubleToString(((TagBigNumber) container.getTag(max)).value() * 100));
					} else
					{
						this.entryMin.setText(Integer.toString(((TagNumber) container.getTag(min)).value()));
						this.entryMax.setText(Integer.toString(((TagNumber) container.getTag(max)).value()));
					}
				}
			}

			if (f == Function.ENCHANT_WITH_LEVELS && t.hasTag(Tags.LT_FUNCTION_TREASURE)) this.checkboxTreasure.setSelected(((TagBoolean) t
					.getTag(Tags.LT_FUNCTION_TREASURE)).value());
			if (f == Function.LOOTING_ENCHANT && t.hasTag(Tags.LT_FUNCTION_LOOTING_LIMIT)) this.entryLimit.setText(Integer.toString(((TagNumber) t
					.getTag(Tags.LT_FUNCTION_LOOTING_LIMIT)).value()));
		}

		this.conditions.setValues(function.conditions);
		this.updateDisplay();
	}

	private void updateDisplay()
	{
		Function f = this.selectedFunction();
		this.labelFunction.setTextID("lt_function." + f.name + ".description");

		this.attributes.setVisible(f == Function.SET_ATTRIBUTES);
		this.panelNbt.setVisible(f == Function.SET_NBT);
		this.enchantments.setVisible(f == Function.ENCHANT_RANDOMLY);
		this.checkboxTreasure.setVisible(f == Function.ENCHANT_WITH_LEVELS);
		this.entryLimit.container.setVisible(f == Function.LOOTING_ENCHANT);

		boolean numInput = f == Function.SET_COUNT || f == Function.ENCHANT_WITH_LEVELS || f == Function.LOOTING_ENCHANT || f == Function.SET_COUNT
				|| f == Function.SET_DAMAGE || f == Function.SET_DATA;

		this.buttonFixed.setVisible(numInput);
		this.buttonRange.setVisible(numInput);
		this.entryMin.label.setTextID(this.buttonFixed.isSelected() ? "score.value" : "score.value.min");
		this.entryMin.container.setVisible(numInput);
		this.entryMax.container.setVisible(numInput && this.buttonRange.isSelected());
	}

}
