package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gui.component.button.CGButton;
import fr.cubiccl.generator.gui.component.combobox.OptionCombobox;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelCoordinates;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelItem;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelSlotSelection;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelTarget;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.IStateListener;
import fr.cubiccl.generator.utils.Replacement;
import fr.cubiccl.generator.utils.Text;

public class CommandReplaceitem extends Command implements ActionListener, IStateListener<PanelSlotSelection>
{
	public static final String[] CONTAINER_BLOCKS =
	{ "slot.brewing_stand", "slot.chest", "slot.dispenser", "slot.furnace", "slot.hopper" }, CONTAINER_ENTITIES =
	{ "slot.armor", "slot.weapon", "slot.enderchest", "slot.hotbar", "slot.inventory", "slot.horse.armor", "slot.horse.saddle", "slot.horse.chest",
			"slot.villager" };
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

	private CGButton buttonSlot;
	private OptionCombobox comboboxMode;
	private CGLabel labelSlot;
	private PanelCoordinates panelCoordinates;
	private PanelItem panelItem;
	private PanelTarget panelTarget;
	private String selectedBlockSlot = "slot.container.0", selectedEntitySlot = "slot.weapon.mainhand";

	public CommandReplaceitem()
	{
		super("replaceitem");
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		boolean block = this.comboboxMode.getValue().equals("block");
		if (e.getSource() == this.comboboxMode)
		{
			this.panelCoordinates.setVisible(block);
			this.panelTarget.setVisible(!block);
			if (block) this.labelSlot.setTextID(new Text("replaceitem.slot.selected", new Replacement("<slot>", this.selectedBlockSlot)));
			else this.labelSlot.setTextID(new Text("replaceitem.slot.selected", new Replacement("<slot>", this.selectedEntitySlot)));
		} else
		{
			PanelSlotSelection p = new PanelSlotSelection(new Text("replaceitem.slot.selection"), block ? CONTAINER_BLOCKS : CONTAINER_ENTITIES);
			p.showContainerFor(block ? this.selectedBlockSlot : this.selectedEntitySlot);
			CommandGenerator.stateManager.setState(p, this);
		}
	}

	@Override
	public CGPanel createGUI()
	{
		CGPanel panel = new CGPanel();
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
		panel.add(this.labelSlot = new CGLabel(new Text("replaceitem.slot.selected", new Replacement("<slot>", this.selectedBlockSlot))), gbc);
		++gbc.gridx;
		panel.add(this.buttonSlot = new CGButton("replaceitem.slot.change"), gbc);
		--gbc.gridx;
		++gbc.gridy;
		++gbc.gridwidth;
		panel.add(this.panelItem = new PanelItem("replaceitem.item"), gbc);

		this.comboboxMode.addActionListener(this);
		this.buttonSlot.addActionListener(this);
		this.panelTarget.setVisible(false);

		return panel;
	}

	@Override
	public String generate() throws CommandGenerationException
	{
		String command = this.id + " " + this.comboboxMode.getValue() + " ";
		if (this.comboboxMode.getValue().equals("block")) command += this.panelCoordinates.generateCoordinates().toCommand() + " " + this.selectedBlockSlot;
		else command += this.panelTarget.generateTarget().toCommand() + " " + this.selectedEntitySlot;
		return command + " " + this.panelItem.generateItem().toCommand();
	}

	@Override
	public boolean shouldStateClose(PanelSlotSelection panel)
	{
		if (this.comboboxMode.getValue().equals("block"))
		{
			this.selectedBlockSlot = panel.selectedSlot();
			this.labelSlot.setTextID(new Text("replaceitem.slot.selected", new Replacement("<slot>", this.selectedBlockSlot)));
		} else
		{
			this.selectedEntitySlot = panel.selectedSlot();
			this.labelSlot.setTextID(new Text("replaceitem.slot.selected", new Replacement("<slot>", this.selectedEntitySlot)));
		}

		return true;
	}
}
