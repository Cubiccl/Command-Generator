package fr.cubiccl.generator.gui.component.panel.data;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;

import fr.cubiccl.generator.gameobject.baseobjects.EnchantmentType;
import fr.cubiccl.generator.gui.Dialogs;
import fr.cubiccl.generator.gui.component.button.CGButton;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.utils.Text;

public class PanelEnchantmentEdition extends CGPanel implements ActionListener
{
	private static final long serialVersionUID = 8814888562936761232L;

	private CGButton buttonLevel;
	private final EnchantmentType enchantment;
	private JLabel labelLevel;

	public PanelEnchantmentEdition(EnchantmentType enchantment)
	{
		this.enchantment = enchantment;
		GridBagConstraints gbc = this.createGridBagLayout();
		gbc.gridwidth = 3;
		this.add(new CGLabel(this.enchantment.name()), gbc);
		++gbc.gridy;
		this.add(new JLabel("ID: " + this.enchantment.id() + ", num: " + this.enchantment.idNum()), gbc);

		++gbc.gridy;
		gbc.gridwidth = 1;
		this.add(this.labelLevel = new JLabel(), gbc);
		++gbc.gridx;
		this.add(this.buttonLevel = new CGButton(new Text("Edit", false)), gbc);

		this.buttonLevel.addActionListener(this);

		this.updateDisplay();
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.buttonLevel) this.enchantment.maxLevel = Integer.parseInt(Dialogs.showInputDialog("New max data?",
				Integer.toString(this.enchantment.maxLevel)));
		this.updateDisplay();
	}

	private void updateDisplay()
	{
		this.labelLevel.setText("Max level: " + this.enchantment.maxLevel);
	}
}
