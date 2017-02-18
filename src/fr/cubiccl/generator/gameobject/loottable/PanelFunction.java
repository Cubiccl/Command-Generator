package fr.cubiccl.generator.gameobject.loottable;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ButtonGroup;

import fr.cubiccl.generator.gameobject.AttributeModifier;
import fr.cubiccl.generator.gameobject.baseobjects.EnchantmentType;
import fr.cubiccl.generator.gameobject.loottable.LootTableFunction.Function;
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
import fr.cubiccl.generator.gui.component.panel.loottable.PanelConditionList;
import fr.cubiccl.generator.gui.component.panel.utils.PanelObjectList;
import fr.cubiccl.generator.gui.component.textfield.CGEntry;
import fr.cubiccl.generator.utils.CommandGenerationException;

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
		--gbc.gridwidth;
		this.add(this.buttonFixed = new CGRadioButton("lt_function.fixed"), gbc);
		++gbc.gridx;
		this.add(this.buttonRange = new CGRadioButton("lt_function.ranged"), gbc);

		--gbc.gridx;
		++gbc.gridy;
		++gbc.gridwidth;
		this.add(this.enchantments = new PanelObjectList<EnchantmentType>("lt_function.enchantments", (String) null, EnchantmentType.class), gbc);
		this.add(this.attributes = new PanelObjectList<AttributeModifier>("lt_function.modifiers", (String) null, AttributeModifier.class), gbc);
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
				t[i] = modifiers[i].toTag(Tags.DEFAULT_COMPOUND);
			tags.add(new TagList(Tags.LT_FUNCTION_MODIFIERS, t));
		} else if (f == Function.ENCHANT_RANDOMLY)
		{
			EnchantmentType[] enchants = this.enchantments.values();
			TagString[] ids = new TagString[enchants.length];
			for (int i = 0; i < ids.length; ++i)
				ids[i] = new TagString(Tags.DEFAULT_STRING, enchants[i].id());
			tags.add(new TagList(Tags.LT_FUNCTION_ENCHANTMENTS, ids));
		} else if (f == Function.SET_NBT) tags.add(new TagString(Tags.LT_FUNCTION_NBT, this.panelNbt.generate().nbt.valueForCommand()));
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

				if (f == Function.SET_DAMAGE) tags.add(new TagBigNumber(t, Double.parseDouble(this.entryMin.getText()) / 100.0));
				else tags.add(new TagNumber(t, Integer.parseInt(this.entryMin.getText())));
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

				if (f == Function.SET_DAMAGE) tags.add(new TagCompound(t, new TagBigNumber(min, Double.parseDouble(this.entryMin.getText()) / 100.0),
						new TagBigNumber(max, Double.parseDouble(this.entryMax.getText()) / 100.0)));
				else tags.add(new TagCompound(t, new TagNumber(min, Integer.parseInt(this.entryMin.getText())), new TagNumber(max, Integer
						.parseInt(this.entryMax.getText()))));
			}
		}

		if (f == Function.ENCHANT_WITH_LEVELS) tags.add(new TagBoolean(Tags.LT_FUNCTION_TREASURE, this.checkboxTreasure.isSelected()));
		if (f == Function.LOOTING_ENCHANT)
		{
			this.entryLimit.checkValueSuperior(CGEntry.INTEGER, 0);
			tags.add(new TagNumber(Tags.LT_FUNCTION_LOOTING_LIMIT, Integer.parseInt(this.entryLimit.getText())));
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
		// TODO Auto-generated method stub

		this.updateDisplay();
	}

	private void updateDisplay()
	{
		Function f = this.selectedFunction();

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
