package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gameobject.Coordinates;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gameobject.tags.NBTParser;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.target.Target;
import fr.cubiccl.generator.gui.component.button.CGButton;
import fr.cubiccl.generator.gui.component.combobox.OptionCombobox;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelCoordinates;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelItem;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelSlotSelection;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelTarget;
import fr.cubiccl.generator.utils.*;

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
		super("replaceitem", "replaceitem block <x> <y> <z> <slot> <item> [amount] [data] [dataTag]\n"
				+ "replaceitem entity <entity> <slot> <item> [amount] [data] [dataTag]", 5, 6, 7, 8, 9, 10);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		boolean block = this.isBlock();
		if (e.getSource() == this.comboboxMode)
		{
			this.onParsingEnd();
		} else
		{
			PanelSlotSelection p = new PanelSlotSelection(new Text("replaceitem.slot.selection"), block ? CONTAINER_BLOCKS : CONTAINER_ENTITIES);
			p.showContainerFor(block ? this.selectedBlockSlot : this.selectedEntitySlot);
			CommandGenerator.stateManager.setState(p, this);
		}
		this.updateTranslations();
	}

	@Override
	public CGPanel createUI()
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

		this.panelTarget.addArgumentChangeListener(this);
		this.panelCoordinates.addArgumentChangeListener(this);
		this.panelItem.addArgumentChangeListener(this);

		return panel;
	}

	@Override
	protected void resetUI()
	{
		this.panelItem.setCount(1);
		this.panelItem.setDamage(0);
		this.panelItem.setTags(new Tag[0]);
	}

	@Override
	protected Text description()
	{
		if (this.isBlock()) return new Text("command." + this.id + ".block").addReplacement("<coordinates>", this.panelCoordinates.displayCoordinates())
				.addReplacement("<item>", Lang.translateObject(this.panelItem.selectedItem(), this.panelItem.selectedDamage()));
		return this.defaultDescription().addReplacement("<target>", this.panelTarget.displayTarget())
				.addReplacement("<item>", Lang.translateObject(this.panelItem.selectedItem(), this.panelItem.selectedDamage()));
	}

	@Override
	protected void onParsingEnd()
	{
		boolean block = this.isBlock();
		this.panelCoordinates.setVisible(block);
		this.panelTarget.setVisible(!block);
		if (block) this.labelSlot.setTextID(new Text("replaceitem.slot.selected", new Replacement("<slot>", this.selectedBlockSlot)));
		else this.labelSlot.setTextID(new Text("replaceitem.slot.selected", new Replacement("<slot>", this.selectedEntitySlot)));
	}

	@Override
	public String generate() throws CommandGenerationException
	{
		String command = this.id + " " + this.comboboxMode.getValue() + " ";
		if (this.isBlock()) command += this.panelCoordinates.generate().toCommand() + " " + this.selectedBlockSlot;
		else command += this.panelTarget.generate().toCommand() + " " + this.selectedEntitySlot;
		return command + " " + this.panelItem.generate().toCommand();
	}

	private boolean isBlock()
	{
		return this.comboboxMode.getValue().equals("block");
	}

	@Override
	protected void readArgument(int index, String argument, String[] fullCommand) throws CommandGenerationException
	{
		// replaceitem block <x> <y> <z> <slot> <item> [amount] [data] [dataTag]
		// replaceitem entity <entity> <slot> <item> [amount] [data] [dataTag]
		boolean block = fullCommand[1].equals("block");
		if (index == 1) this.comboboxMode.setValue(argument);
		if (block)
		{
			if (index == 2) this.panelCoordinates.setupFrom(Coordinates.createFrom(argument, fullCommand[3], fullCommand[4]));
			if (index == 5) this.selectedBlockSlot = argument;
			if (index == 6) this.panelItem.setItem(ObjectRegistry.items.find(argument));
			if (index == 7) try
			{
				this.panelItem.setCount(Integer.parseInt(argument));
			} catch (Exception e)
			{}
			if (index == 8) try
			{
				this.panelItem.setDamage(Integer.parseInt(argument));
			} catch (Exception e)
			{}
			if (index == 9) this.panelItem.setTags(((TagCompound) NBTParser.parse(argument, true, false)).value());
		} else
		{
			if (index == 2) this.panelTarget.setupFrom(Target.createFrom(argument));
			if (index == 3) this.selectedEntitySlot = argument;
			if (index == 4) this.panelItem.setItem(ObjectRegistry.items.find(argument));
			if (index == 5) try
			{
				this.panelItem.setCount(Integer.parseInt(argument));
			} catch (Exception e)
			{}
			if (index == 6) try
			{
				this.panelItem.setDamage(Integer.parseInt(argument));
			} catch (Exception e)
			{}
			if (index == 7) this.panelItem.setTags(((TagCompound) NBTParser.parse(argument, true, false)).value());
		}
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
