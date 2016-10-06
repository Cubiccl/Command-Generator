package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import fr.cubiccl.generator.gui.component.combobox.OptionCombobox;
import fr.cubiccl.generator.gui.component.panel.CPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelCoordinates;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelItem;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelTarget;
import fr.cubiccl.generator.gui.component.textfield.CSpinner;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Utils;

public class CommandReplaceitem extends Command implements ActionListener
{
	public static final int[][] SLOT_NUMBERS =
	{
	{ 0, 26 },
	{ 0, 8 },
	{ 0, 26 },
	{ 2, 16 },
	{ 0, 7 } };
	public static final String[] SLOTS_BLOCK =
	{ "container" }, SLOTS_ENTITY =
	{ "enderchest", "hotbar", "inventory", "horse.chest", "villager", "armor.chest", "armor.feet", "armor.head", "armor.legs", "weapon.mainhand",
			"weapon.offhand", "horse.saddle", "horse.armor" };
	private OptionCombobox comboboxMode, comboboxSlotType;
	private PanelCoordinates panelCoordinates;
	private PanelItem panelItem;
	private PanelTarget panelTarget;
	private CSpinner spinnerSlotNumber;

	public CommandReplaceitem()
	{
		super("replaceitem");
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.comboboxMode)
		{
			boolean block = this.comboboxMode.getValue().equals("block");
			this.panelCoordinates.setVisible(block);
			this.panelTarget.setVisible(!block);
			if (block)
			{
				this.comboboxSlotType.setOptions("replaceitem.slot", SLOTS_BLOCK);
				this.spinnerSlotNumber.setValues(Utils.generateArray(26));
			} else
			{
				this.comboboxSlotType.setOptions("replaceitem.slot", SLOTS_ENTITY);
				// this.comboboxSlotType.setSelectedIndex(this.comboboxSlotType.getSelectedIndex());
			}
		} else
		{
			boolean number = this.comboboxSlotType.getSelectedIndex() < 5;
			this.spinnerSlotNumber.container.setVisible(number);
			int index = this.comboboxSlotType.getSelectedIndex();
			if (number && this.comboboxMode.getValue().equals("entity")) this.spinnerSlotNumber.setValues(Utils.generateArray(SLOT_NUMBERS[index][0],
					SLOT_NUMBERS[index][1]));
		}
	}

	@Override
	public CPanel createGUI()
	{
		CPanel panel = new CPanel();
		GridBagConstraints gbc = panel.createGridBagLayout();

		++gbc.gridwidth;
		panel.add(this.labelDescription(), gbc);
		++gbc.gridy;
		panel.add(this.comboboxMode = new OptionCombobox("replaceitem.mode", "block", "entity"), gbc);
		++gbc.gridy;
		panel.add(this.panelCoordinates = new PanelCoordinates("replaceitem.block"), gbc);
		panel.add(this.panelTarget = new PanelTarget(PanelTarget.ALL_ENTITIES), gbc);
		++gbc.gridy;
		--gbc.gridwidth;
		panel.add(this.comboboxSlotType = new OptionCombobox("replaceitem.slot", SLOTS_BLOCK), gbc);
		++gbc.gridx;
		panel.add((this.spinnerSlotNumber = new CSpinner("replaceitem.slot_number", Utils.generateArray(26))).container, gbc);
		--gbc.gridx;
		++gbc.gridy;
		++gbc.gridwidth;
		panel.add(this.panelItem = new PanelItem("replaceitem.item"), gbc);

		this.comboboxMode.addActionListener(this);
		this.comboboxSlotType.addActionListener(this);
		this.panelTarget.setVisible(false);

		return panel;
	}

	@Override
	public String generate() throws CommandGenerationException
	{
		String command = "/replaceitem " + this.comboboxMode.getValue() + " ";
		if (this.comboboxMode.getValue().equals("block")) command += this.panelCoordinates.generateCoordinates().toCommand();
		else command += this.panelTarget.generateTarget().toCommand();
		return command
				+ " "
				+ (this.comboboxSlotType.getSelectedIndex() < 5 ? ("slot." + this.comboboxSlotType.getValue() + "." + this.spinnerSlotNumber.getValue())
						: ("slot." + this.comboboxSlotType.getValue())) + " " + this.panelItem.generateItem().toCommand();
	}
}
