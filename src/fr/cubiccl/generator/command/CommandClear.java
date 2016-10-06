package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import fr.cubiccl.generator.gameobject.ItemStack;
import fr.cubiccl.generator.gui.component.button.CCheckBox;
import fr.cubiccl.generator.gui.component.panel.CPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelItem;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelTarget;
import fr.cubiccl.generator.utils.CommandGenerationException;

public class CommandClear extends Command implements ActionListener
{
	private CCheckBox checkboxAllItem, checkboxIgnoreData, checkboxAll;
	private PanelItem panelItem;
	private PanelTarget panelTarget;

	public CommandClear()
	{
		super("clear");
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.checkboxAllItem)
		{
			boolean item = !this.checkboxAllItem.isSelected();
			this.panelItem.setVisible(item);
			this.checkboxIgnoreData.setVisible(item);
			this.checkboxAll.setVisible(item);
		} else if (e.getSource() == this.checkboxIgnoreData || e.getSource() == this.checkboxAll) this.panelItem.setEnabledContent(
				!this.checkboxIgnoreData.isSelected(), !this.checkboxAll.isSelected());
	}

	@Override
	public CPanel createGUI()
	{
		CPanel panel = new CPanel();
		GridBagConstraints gbc = panel.createGridBagLayout();

		panel.add(this.labelDescription(), gbc);
		++gbc.gridy;
		panel.add(this.panelTarget = new PanelTarget(PanelTarget.PLAYERS_ONLY), gbc);
		++gbc.gridy;
		panel.add(this.panelItem = new PanelItem("clear.item"), gbc);
		++gbc.gridy;
		panel.add(this.checkboxAllItem = new CCheckBox("clear.all_items"), gbc);
		++gbc.gridy;
		panel.add(this.checkboxIgnoreData = new CCheckBox("clear.ignore_data"), gbc);
		++gbc.gridy;
		panel.add(this.checkboxAll = new CCheckBox("clear.no_limit"), gbc);

		this.checkboxAllItem.addActionListener(this);
		this.checkboxIgnoreData.addActionListener(this);
		this.checkboxAll.addActionListener(this);

		return panel;
	}

	@Override
	public String generate() throws CommandGenerationException
	{
		ItemStack item = this.panelItem.generateItem();
		if (this.checkboxAllItem.isSelected()) return "/clear " + this.panelTarget.generateTarget().toCommand();
		int data = item.data, amount = item.amount;
		if (this.checkboxIgnoreData.isSelected()) data = -1;
		if (this.checkboxAll.isSelected()) amount = -1;
		return "/clear " + this.panelTarget.generateTarget().toCommand() + " " + item.item.idString + " " + data + " " + amount;
	}

}
