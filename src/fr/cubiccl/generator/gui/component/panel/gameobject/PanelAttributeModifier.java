package fr.cubiccl.generator.gui.component.panel.gameobject;

import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.ButtonGroup;

import fr.cubiccl.generator.gameobject.AttributeModifier;
import fr.cubiccl.generator.gameobject.baseobjects.Attribute;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gameobject.registries.ObjectSaver;
import fr.cubiccl.generator.gui.component.button.CGCheckBox;
import fr.cubiccl.generator.gui.component.button.CGRadioButton;
import fr.cubiccl.generator.gui.component.combobox.ObjectCombobox;
import fr.cubiccl.generator.gui.component.combobox.OptionCombobox;
import fr.cubiccl.generator.gui.component.interfaces.ICustomObject;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.utils.PanelCustomObject;
import fr.cubiccl.generator.gui.component.textfield.CGEntry;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;
import fr.cubiccl.generator.utils.Utils;

public class PanelAttributeModifier extends CGPanel implements ICustomObject<AttributeModifier>, ActionListener
{
	private static final long serialVersionUID = -8412726255073387389L;

	private CGRadioButton buttonFixed, buttonRange;
	private CGCheckBox checkboxMainHand, checkboxOffHand, checkboxHead, checkboxChest, checkboxLegs, checkboxFeet;
	private ObjectCombobox<Attribute> comboboxAttribute;
	private OptionCombobox comboboxOperation, comboboxSlot;
	private CGEntry entryName, entryAmount, entryAmountMax, entryUUIDMost, entryUUIDLeast;
	private boolean isApplied;
	private boolean isInLootTable;
	private CGLabel labelSlot, labelAttribute;

	public PanelAttributeModifier(boolean isApplied)
	{
		this(isApplied, true);
	}

	public PanelAttributeModifier(boolean isApplied, boolean customObjects)
	{
		this(isApplied, false, customObjects);
	}

	public PanelAttributeModifier(boolean isApplied, boolean isInLootTable, boolean customObjects)
	{
		this.isApplied = isApplied;
		this.isInLootTable = isInLootTable;

		CGPanel p = new CGPanel("attribute.modifier.slots");
		p.setLayout(new GridLayout(2, 3, 5, 5));
		p.add(this.checkboxMainHand = new CGCheckBox("attribute.modifier.slot.mainhand"));
		p.add(this.checkboxHead = new CGCheckBox("attribute.modifier.slot.head"));
		p.add(this.checkboxChest = new CGCheckBox("attribute.modifier.slot.chest"));
		p.add(this.checkboxOffHand = new CGCheckBox("attribute.modifier.slot.offhand"));
		p.add(this.checkboxLegs = new CGCheckBox("attribute.modifier.slot.legs"));
		p.add(this.checkboxFeet = new CGCheckBox("attribute.modifier.slot.feet"));

		CGPanel pAmount = new CGPanel();
		GridBagConstraints gbc = pAmount.createGridBagLayout();
		pAmount.add(this.buttonFixed = new CGRadioButton("attribute.modifier.amount.fixed"), gbc);
		++gbc.gridx;
		pAmount.add(this.buttonRange = new CGRadioButton("attribute.modifier.amount.ranged"), gbc);
		--gbc.gridx;
		++gbc.gridy;
		++gbc.gridwidth;
		pAmount.add((this.entryAmount = new CGEntry((String) null)).container, gbc);
		++gbc.gridy;
		pAmount.add((this.entryAmountMax = new CGEntry("attribute.modifier.amount.max")).container, gbc);

		gbc = this.createGridBagLayout();
		gbc.gridwidth = 2;
		this.add((this.entryName = new CGEntry(new Text("attribute.modifier.name"), new Text("attribute.modifier.name"))).container, gbc);

		++gbc.gridy;
		this.add(p, gbc);
		gbc.gridwidth = 1;
		this.add(this.labelSlot = new CGLabel("attribute.modifier.slot").setHasColumn(true), gbc);
		++gbc.gridx;
		this.add(this.comboboxSlot = new OptionCombobox("attribute.modifier.slot", AttributeModifier.SLOTS), gbc);

		--gbc.gridx;
		++gbc.gridy;
		this.add(this.labelAttribute = new CGLabel("attribute.modifier.attribute").setHasColumn(true), gbc);
		++gbc.gridx;
		this.add(this.comboboxAttribute = new ObjectCombobox<Attribute>(ObjectRegistry.attributes.list()), gbc);

		--gbc.gridx;
		++gbc.gridy;
		this.add(new CGLabel("attribute.modifier.operation").setHasColumn(true), gbc);
		++gbc.gridx;
		this.add(this.comboboxOperation = new OptionCombobox("attribute.modifier.operation", "add", "multiply", "multiply_all"), gbc);

		--gbc.gridx;
		++gbc.gridy;
		++gbc.gridwidth;
		this.add(pAmount, gbc);
		if (!this.isInLootTable) this.add((this.entryAmount = new CGEntry(new Text("attribute.modifier.amount"), "0", Text.NUMBER)).container, gbc);
		++gbc.gridy;
		this.add(
				(this.entryUUIDMost = new CGEntry(new Text("attribute.modifier.uuidmost"), Long.toString(ThreadLocalRandom.current().nextLong()), Text.INTEGER)).container,
				gbc);
		++gbc.gridy;
		this.add((this.entryUUIDLeast = new CGEntry(new Text("attribute.modifier.uuidleast"), Long.toString(ThreadLocalRandom.current().nextLong()),
				Text.INTEGER)).container, gbc);
		++gbc.gridy;
		this.add(new CGLabel("attribute.modifier.uuid.details"), gbc);
		++gbc.gridy;
		gbc.fill = GridBagConstraints.NONE;
		if (customObjects) this.add(new PanelCustomObject<AttributeModifier, AttributeModifier>(this, ObjectSaver.attributeModifiers), gbc);

		this.entryAmount.addNumberFilter();
		this.entryUUIDMost.addNumberFilter();
		this.entryUUIDLeast.addNumberFilter();

		p.setVisible(this.isInLootTable);
		pAmount.setVisible(this.isInLootTable);
		this.labelSlot.setVisible(!this.isInLootTable);
		this.comboboxSlot.setVisible(!this.isInLootTable);
		this.entryUUIDLeast.container.setVisible(!this.isInLootTable);
		this.entryUUIDMost.container.setVisible(!this.isInLootTable);

		if (this.isApplied)
		{
			this.comboboxAttribute.setVisible(false);
			this.comboboxSlot.setVisible(false);
			p.setVisible(false);
			this.labelAttribute.setVisible(false);
			this.labelSlot.setVisible(false);
		}

		ButtonGroup group = new ButtonGroup();
		group.add(this.buttonFixed);
		group.add(this.buttonRange);
		this.buttonFixed.setSelected(true);
		this.buttonFixed.addActionListener(this);
		this.buttonRange.addActionListener(this);
		this.updateDisplay();
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		this.updateDisplay();
	}

	@Override
	public AttributeModifier generate() throws CommandGenerationException
	{
		this.entryAmount.checkValue(CGEntry.NUMBER);
		if (this.isInLootTable && this.buttonRange.isSelected()) this.entryAmountMax.checkValue(CGEntry.NUMBER);
		this.entryUUIDLeast.checkValue(CGEntry.NUMBER);
		this.entryUUIDMost.checkValue(CGEntry.NUMBER);
		ArrayList<String> slots = new ArrayList<String>();
		if (this.isInLootTable)
		{
			if (this.checkboxMainHand.isSelected()) slots.add("mainhand");
			if (this.checkboxOffHand.isSelected()) slots.add("offhand");
			if (this.checkboxHead.isSelected()) slots.add("head");
			if (this.checkboxChest.isSelected()) slots.add("chest");
			if (this.checkboxLegs.isSelected()) slots.add("legs");
			if (this.checkboxFeet.isSelected()) slots.add("feet");
		} else slots.add(this.comboboxSlot.getValue());
		if (slots.isEmpty()) throw new CommandGenerationException(new Text("attribute.modifier.slots.missing"));

		return new AttributeModifier(this.comboboxAttribute.getSelectedObject(), this.entryName.getText(), slots.toArray(new String[slots.size()]),
				(byte) this.comboboxOperation.getSelectedIndex(), Double.parseDouble(this.entryAmount.getText()), this.isInLootTable
						&& this.buttonRange.isSelected() ? Double.parseDouble(this.entryAmountMax.getText()) : -1,
				Long.parseLong(this.entryUUIDMost.getText()), Long.parseLong(this.entryUUIDLeast.getText()));
	}

	@Override
	public void setupFrom(AttributeModifier modifier)
	{
		if (!this.isApplied) this.comboboxAttribute.setSelected(modifier.getAttribute());
		this.comboboxOperation.setSelectedIndex(modifier.operation);
		if (!this.isApplied)
		{
			if (this.isInLootTable)
			{
				this.checkboxMainHand.setSelected(false);
				this.checkboxOffHand.setSelected(false);
				this.checkboxHead.setSelected(false);
				this.checkboxChest.setSelected(false);
				this.checkboxLegs.setSelected(false);
				this.checkboxFeet.setSelected(false);
				for (String s : modifier.slots)
				{
					if (s.equals("mainhand")) this.checkboxMainHand.setSelected(true);
					if (s.equals("offhand")) this.checkboxOffHand.setSelected(true);
					if (s.equals("head")) this.checkboxHead.setSelected(true);
					if (s.equals("chest")) this.checkboxChest.setSelected(true);
					if (s.equals("legs")) this.checkboxLegs.setSelected(true);
					if (s.equals("feet")) this.checkboxFeet.setSelected(true);
				}
			} else this.comboboxSlot.setValue(modifier.slots[0]);
		}
		this.entryName.setText(modifier.name);
		this.entryAmount.setText(Utils.doubleToString(modifier.amount));
		this.buttonFixed.setSelected(true);
		if (modifier.isInLootTable && modifier.amountMax != -1)
		{
			this.buttonRange.setSelected(true);
			this.entryAmountMax.setText(Utils.doubleToString(modifier.amountMax));
		}
		this.entryUUIDMost.setText(Utils.doubleToString(modifier.UUIDMost));
		this.entryUUIDLeast.setText(Utils.doubleToString(modifier.UUIDLeast));
	}

	private void updateDisplay()
	{
		this.entryAmount.label.setTextID(this.buttonFixed.isSelected() ? "attribute.modifier.amount" : "attribute.modifier.amount.min");
		this.entryAmountMax.container.setVisible(this.buttonRange.isSelected());
	}

}
