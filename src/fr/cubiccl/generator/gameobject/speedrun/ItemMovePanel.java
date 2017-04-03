package fr.cubiccl.generator.gameobject.speedrun;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;

import fr.cubiccl.generator.gui.component.button.CGRadioButton;
import fr.cubiccl.generator.gui.component.combobox.OptionCombobox;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.label.HelpLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelItem;
import fr.cubiccl.generator.gui.component.textfield.CGEntry;
import fr.cubiccl.generator.utils.CommandGenerationException;

public class ItemMovePanel extends CGPanel implements ActionListener
{
	private static final long serialVersionUID = 4330039223060054788L;

	private CGRadioButton buttonObtained, buttonMoved, buttonThrown;
	private OptionCombobox comboboxImportance, comboboxFrom, comboboxTo;
	private CGEntry entryName;
	private HelpLabel labelHelp;
	private CGLabel labelImportance, labelFrom, labelTo;
	private PanelItem panelItem;

	public ItemMovePanel()
	{
		CGPanel p = new CGPanel();
		GridBagConstraints gbc = p.createGridBagLayout();
		p.add(this.labelFrom = new CGLabel("speedrun.move.from").setHasColumn(true), gbc);
		++gbc.gridx;
		p.add(this.comboboxFrom = new OptionCombobox("speedrun.move", ItemMove.LOCATIONS), gbc);
		++gbc.gridx;
		p.add(this.labelTo = new CGLabel("speedrun.move.to").setHasColumn(true), gbc);
		++gbc.gridx;
		p.add(this.comboboxTo = new OptionCombobox("speedrun.move", ItemMove.LOCATIONS), gbc);

		gbc = this.createGridBagLayout();
		gbc.gridwidth = 3;
		this.add(this.panelItem = new PanelItem("speedrun.move.item"), gbc);
		++gbc.gridy;
		gbc.gridwidth = 1;
		this.add(this.labelImportance = new CGLabel("speedrun.move.importance").setHasColumn(true), gbc);
		++gbc.gridx;
		this.add(this.comboboxImportance = new OptionCombobox("speedrun.move.importance", "forced", "resource", "optionnal"), gbc);
		++gbc.gridx;
		this.add(this.labelHelp = new HelpLabel("speedrun.move.importance.help"), gbc);

		gbc.gridx = 0;
		++gbc.gridy;
		this.add(this.buttonObtained = new CGRadioButton("speedrun.move.obtained"), gbc);
		++gbc.gridx;
		this.add(this.buttonMoved = new CGRadioButton("speedrun.move.moved"), gbc);
		++gbc.gridx;
		this.add(this.buttonThrown = new CGRadioButton("speedrun.move.thrown"), gbc);

		gbc.gridx = 0;
		++gbc.gridy;
		gbc.gridwidth = 3;
		this.add(p, gbc);
		++gbc.gridy;
		this.add((this.entryName = new CGEntry("speedrun.move.name", "general.name")).container, gbc);

		ButtonGroup group = new ButtonGroup();
		group.add(this.buttonObtained);
		group.add(this.buttonMoved);
		group.add(this.buttonThrown);
		this.buttonObtained.addActionListener(this);
		this.buttonMoved.addActionListener(this);
		this.buttonThrown.addActionListener(this);

		this.panelItem.setHasNBT(false);
		this.panelItem.setHasDurability(false);
		
		this.onModeChange();
	}

	public ItemMovePanel(ItemMove itemMove)
	{
		this();
		this.setupFrom(itemMove);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		this.onModeChange();
	}

	public ItemStackS generateItem() throws CommandGenerationException
	{
		ItemStackS i = this.panelItem.generate().forSpeedrun((byte) this.comboboxImportance.getSelectedIndex());
		i.name = this.entryName.getText().equals("") ? null : this.entryName.getText();
		return i;
	}

	public byte getFrom()
	{
		if (this.buttonObtained.isSelected()) return -1;
		return (byte) this.comboboxFrom.getSelectedIndex();
	}

	public byte getTo()
	{
		if (this.buttonThrown.isSelected()) return -1;
		return (byte) this.comboboxTo.getSelectedIndex();
	}

	private void onModeChange()
	{
		this.labelImportance.setVisible(this.buttonObtained.isSelected());
		this.labelHelp.setVisible(this.buttonObtained.isSelected());
		this.comboboxImportance.setVisible(this.buttonObtained.isSelected());
		this.labelFrom.setVisible(!this.buttonObtained.isSelected());
		this.comboboxFrom.setVisible(!this.buttonObtained.isSelected());
		this.labelTo.setVisible(!this.buttonThrown.isSelected());
		this.comboboxTo.setVisible(!this.buttonThrown.isSelected());
	}

	private void setupFrom(ItemMove itemMove)
	{
		if (itemMove.getItem().name != null) this.entryName.setText(itemMove.getItem().name);
		this.panelItem.setupFrom(itemMove.getItem());
		this.comboboxImportance.setSelectedIndex(itemMove.getItem().importance);

		byte from = itemMove.from, to = itemMove.to;
		this.buttonMoved.setSelected(true);
		this.buttonObtained.setSelected(from == ItemMove.OUT);
		this.buttonThrown.setSelected(to == ItemMove.OUT);
		if (from != ItemMove.OUT) this.comboboxFrom.setSelectedIndex(from);
		if (to != ItemMove.OUT) this.comboboxTo.setSelectedIndex(to);
		this.onModeChange();
	}

}
