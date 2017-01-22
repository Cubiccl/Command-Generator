package fr.cubiccl.generator.gui.component.panel.gameobject;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.command.CommandReplaceitem;
import fr.cubiccl.generator.gameobject.baseobjects.Container;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gui.component.combobox.OptionCombobox;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.tag.SlotSelectionPanel;
import fr.cubiccl.generator.gui.component.panel.utils.ConfirmPanel;
import fr.cubiccl.generator.utils.Text;

public class PanelSlotSelection extends ConfirmPanel
{
	public static final String[] ARMOR =
	{ "head", "legs", "chest", "feet" }, HANDS =
	{ "mainhand", "offhand" };

	private static final long serialVersionUID = -116840334378580324L;

	private OptionCombobox combobox;
	private String[] containerIDs;
	private SlotSelectionPanel panelContainer;

	public PanelSlotSelection(Text title, String[] containerIDs)
	{
		super(title, null, false);
		this.containerIDs = containerIDs;

		CGPanel p = new CGPanel();
		GridBagConstraints gbc = p.createGridBagLayout();
		p.add(this.combobox = new OptionCombobox("container", this.containerIDs), gbc);
		++gbc.gridy;
		gbc.fill = GridBagConstraints.NONE;
		p.add(this.panelContainer = new SlotSelectionPanel(ObjectRegistry.containers.find(this.containerIDs[0]))
		{
			private static final long serialVersionUID = -3976198700577822760L;

			@Override
			public void onClick()
			{
				if (this.currentSlot() != -1) CommandGenerator.stateManager.clearState(true);
			}

			@Override
			public void onRightClick()
			{}
		}, gbc);

		this.combobox.addActionListener(this);
		this.setMainComponent(p);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.combobox) this.panelContainer.setContainer(ObjectRegistry.containers.find(this.combobox.getValue()));
		super.actionPerformed(e);
	}

	public String selectedSlot()
	{
		String cont = this.combobox.getValue();
		int slot = this.panelContainer.currentSlot();
		if (cont.equals("slot.armor")) return cont + "." + ARMOR[slot];
		if (cont.equals("slot.weapon")) return cont + "." + HANDS[slot];
		if (cont.equals("slot.horse.armor") || cont.equals("slot.horse.saddle")) return cont;
		for (String c : CommandReplaceitem.CONTAINER_BLOCKS)
			if (cont.equals(c)) return "slot.container." + slot;
		return cont + "." + slot;
	}

	public void showContainerFor(String slot)
	{
		Container c = ObjectRegistry.containers.find(this.containerIDs[0]);
		if (slot.equals("slot.horse.armor") || slot.equals("slot.horse.saddle")) c = ObjectRegistry.containers.find(slot);
		else if (slot.substring(0, slot.lastIndexOf('.')).equals("slot.container")) c = ObjectRegistry.containers.find("slot.chest");
		else c = ObjectRegistry.containers.find(slot.substring(0, slot.lastIndexOf('.')));
		this.panelContainer.setContainer(c);

		for (int i = 0; i < this.containerIDs.length; i++)
			if (this.containerIDs[i].equals(slot.substring(0, slot.lastIndexOf('.'))) || this.containerIDs[i].equals(slot))
			{
				this.combobox.setSelectedIndex(i);
				return;
			}
		this.combobox.setSelectedIndex(1);
	}

}
