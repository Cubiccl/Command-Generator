package fr.cubiccl.generator.gui.component.panel.advancement;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingConstants;

import fr.cubiccl.generator.gameobject.advancements.Advancement;
import fr.cubiccl.generator.gui.component.button.CGButton;
import fr.cubiccl.generator.gui.component.combobox.OptionCombobox;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelItem;
import fr.cubiccl.generator.gui.component.textfield.CGEntry;
import fr.cubiccl.generator.utils.Text;

public class PanelAdvancement extends CGPanel implements ActionListener
{
	private static final long serialVersionUID = -773099244243224959L;

	public final Advancement advancement;
	private CGButton buttonSave, buttonCancel;
	private OptionCombobox comboboxFrame;
	private CGEntry entryName, entryDescription;
	private PanelItem panelItem;

	public PanelAdvancement(Advancement advancement)
	{
		this.advancement = advancement;
		CGLabel l = new CGLabel(new Text(this.advancement.customName(), false));
		l.setFont(l.getFont().deriveFont(Font.BOLD, 30));
		l.setHorizontalAlignment(SwingConstants.CENTER);

		GridBagConstraints gbc = this.createGridBagLayout();
		gbc.gridwidth = 2;
		this.add(l, gbc);
		++gbc.gridy;
		this.add(this.panelItem = new PanelItem("advancement.display_item"), gbc);
		++gbc.gridy;
		this.add(this.entryName = new CGEntry("general.name", "general.name"), gbc);
		++gbc.gridy;
		this.add(this.entryDescription = new CGEntry("general.description", "general.description"), gbc);

		++gbc.gridy;
		gbc.gridwidth = 1;
		this.add(new CGLabel("advancement.frame"), gbc);
		++gbc.gridx;
		this.add(this.comboboxFrame = new OptionCombobox("advancement.frame", "task", "frame2", "challenge"), gbc);

		gbc.gridx = 0;
		++gbc.gridy;
		this.add(this.buttonSave = new CGButton("objects.save"), gbc);
		++gbc.gridx;
		this.add(this.buttonCancel = new CGButton("general.cancel_edit"), gbc);

		this.panelItem.setHasDurability(false);
		this.panelItem.setHasData(false);
		this.panelItem.setHasNBT(false);
		this.panelItem.setHasAmount(false);

		this.buttonSave.addActionListener(this);
		this.buttonCancel.addActionListener(this);

		this.cancel();
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.buttonSave) this.save();
		else if (e.getSource() == this.buttonCancel) this.cancel();
	}

	private void cancel()
	{
		// TODO Auto-generated method stub
		this.panelItem.setItem(this.advancement.getItem());
		this.comboboxFrame.setValue(this.advancement.frame);
		this.entryName.setText(this.advancement.title);
		this.entryDescription.setText(this.advancement.description == null ? "" : this.advancement.description);
	}

	private void save()
	{
		// TODO Auto-generated method stub
		this.advancement.setItem(this.panelItem.selectedItem());
		this.advancement.frame = this.comboboxFrame.getValue();
		this.advancement.title = this.entryName.getText();
		this.advancement.description = this.entryDescription.getText();
	}

}
