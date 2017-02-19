package fr.cubiccl.generator.gui.component.panel.loottable;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;

import fr.cubiccl.generator.gameobject.loottable.LootTableEntry;
import fr.cubiccl.generator.gameobject.loottable.LootTableFunction;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gui.component.button.CGRadioButton;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.label.HelpLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelItem;
import fr.cubiccl.generator.gui.component.panel.utils.PanelObjectList;
import fr.cubiccl.generator.gui.component.textfield.CGEntry;
import fr.cubiccl.generator.utils.CommandGenerationException;

public class PanelEntry extends CGPanel implements ActionListener
{
	private static final long serialVersionUID = 3963884106757561416L;

	private CGRadioButton buttonItem, buttonTable;
	private CGEntry entryTable, entryWeight, entryQuality;
	private PanelItem item;
	private PanelConditionList listConditions;
	private PanelObjectList<LootTableFunction> listFunctions;

	public PanelEntry()
	{
		GridBagConstraints gbc = this.createGridBagLayout();
		this.add(this.buttonItem = new CGRadioButton("lt_entry.mode.item"), gbc);
		++gbc.gridx;
		this.add(this.buttonTable = new CGRadioButton("lt_entry.mode.table"), gbc);

		--gbc.gridx;
		++gbc.gridy;
		++gbc.gridwidth;
		this.add((this.entryTable = new CGEntry("lt_entry.table")).container, gbc);
		this.add(this.item = new PanelItem("lt_entry.item", false, false, false, ObjectRegistry.items.list(ObjectRegistry.SORT_NUMERICALLY)), gbc);
		++gbc.gridy;
		this.add((this.entryWeight = new CGEntry("lt_entry.weight")).container, gbc);
		++gbc.gridy;
		this.add((this.entryQuality = new CGEntry("lt_entry.quality")).container, gbc);
		++gbc.gridy;
		this.add(new CGLabel("loottable.conditions.description"), gbc);
		++gbc.gridy;
		this.add(this.listConditions = new PanelConditionList("loottable.conditions", "loottable.condition"), gbc);
		++gbc.gridy;
		this.add(new CGLabel("loottable.functions.description"), gbc);
		++gbc.gridy;
		this.add(this.listFunctions = new PanelObjectList<LootTableFunction>("loottable.functions", "loottable.function", LootTableFunction.class), gbc);

		ButtonGroup group = new ButtonGroup();
		group.add(this.buttonItem);
		group.add(this.buttonTable);

		this.item.setHasAmount(false);
		this.entryWeight.addIntFilter();
		this.entryQuality.addIntFilter();
		this.entryQuality.addHelpLabel(new HelpLabel("lt_entry.quality.help"));

		this.buttonItem.setSelected(true);
		this.buttonItem.addActionListener(this);
		this.buttonTable.addActionListener(this);
		this.updateDisplay();
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		this.updateDisplay();
	}

	public LootTableEntry generate() throws CommandGenerationException
	{
		this.entryWeight.checkValue(CGEntry.INTEGER);
		this.entryQuality.checkValue(CGEntry.INTEGER);

		String name = this.item.selectedItem().id();
		if (this.buttonTable.isSelected())
		{
			this.entryTable.checkValue(CGEntry.STRING);
			name = this.entryTable.getText();
		}
		return new LootTableEntry(this.listConditions.values(), this.buttonItem.isSelected() ? LootTableEntry.ITEM : LootTableEntry.LOOT_TABLE, name,
				this.listFunctions.values(), Integer.parseInt(this.entryWeight.getText()), Integer.parseInt(this.entryQuality.getText()));
	}

	public void setupFrom(LootTableEntry entry)
	{
		this.buttonItem.setSelected(entry.type == LootTableEntry.ITEM);
		this.buttonTable.setSelected(entry.type == LootTableEntry.LOOT_TABLE);

		if (entry.type == LootTableEntry.ITEM) this.item.setItem(ObjectRegistry.items.find(entry.name));
		else this.entryTable.setText(entry.name);

		this.entryWeight.setText(Integer.toString(entry.weight));
		this.entryQuality.setText(Integer.toString(entry.quality));

		this.listConditions.setValues(entry.conditions);
		this.listFunctions.setValues(entry.getFunctions());

		this.updateDisplay();
	}

	private void updateDisplay()
	{
		this.item.setVisible(this.buttonItem.isSelected());
		this.entryTable.container.setVisible(this.buttonTable.isSelected());
	}

}
