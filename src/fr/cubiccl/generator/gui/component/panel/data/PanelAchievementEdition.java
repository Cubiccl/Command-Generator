package fr.cubiccl.generator.gui.component.panel.data;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;

import fr.cubiccl.generator.gameobject.baseobjects.Achievement;
import fr.cubiccl.generator.gameobject.baseobjects.Item;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gui.component.combobox.ObjectCombobox;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;

public class PanelAchievementEdition extends CGPanel implements ActionListener
{
	private static final long serialVersionUID = 8814888562936761232L;

	private final Achievement achievement;
	private ObjectCombobox<Item> comboboxItem;

	public PanelAchievementEdition(Achievement achievement)
	{
		this.achievement = achievement;
		GridBagConstraints gbc = this.createGridBagLayout();
		gbc.gridwidth = 3;
		this.add(new CGLabel(this.achievement.name()), gbc);
		++gbc.gridy;
		this.add(new JLabel("ID: " + this.achievement.id()), gbc);

		++gbc.gridy;
		gbc.gridwidth = 1;
		this.add(new JLabel("Item: "), gbc);
		++gbc.gridx;
		this.add((this.comboboxItem = new ObjectCombobox<Item>(ObjectRegistry.items.list())).container, gbc);

		this.comboboxItem.setSelected(this.achievement.textureItem());
		this.comboboxItem.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		// if (e.getSource() == this.comboboxItem) this.achievement.textureItem = this.comboboxItem.getSelectedObject();
	}
}
