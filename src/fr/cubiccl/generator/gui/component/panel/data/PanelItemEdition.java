package fr.cubiccl.generator.gui.component.panel.data;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;

import fr.cubiccl.generator.gameobject.baseobjects.Item;
import fr.cubiccl.generator.gui.Dialogs;
import fr.cubiccl.generator.gui.component.button.CGButton;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.utils.Text;

public class PanelItemEdition extends CGPanel implements ActionListener
{
	private static final long serialVersionUID = 8814888562936761232L;

	private CGButton buttonMaxDamage, buttonDamage, buttonDurability, buttonTexture, buttonCustom;
	private final Item item;
	private JLabel labelDamage, labelTexture, labelDurability, labelCustom;

	public PanelItemEdition(Item item)
	{
		this.item = item;
		GridBagConstraints gbc = this.createGridBagLayout();
		gbc.gridwidth = 3;
		this.add(new CGLabel(this.item.name()), gbc);
		++gbc.gridy;
		this.add(new JLabel("ID: " + this.item.id() + ", num: " + this.item.idNum()), gbc);

		++gbc.gridy;
		gbc.gridwidth = 1;
		this.add(this.labelDamage = new JLabel(), gbc);
		++gbc.gridy;
		this.add(this.labelDurability = new JLabel(), gbc);
		++gbc.gridy;
		this.add(this.labelTexture = new JLabel(), gbc);
		++gbc.gridy;
		this.add(this.labelCustom = new JLabel(), gbc);

		++gbc.gridx;
		gbc.gridy = 2;
		this.add(this.buttonMaxDamage = new CGButton(new Text("Set max data", false)), gbc);
		++gbc.gridx;
		this.add(this.buttonDamage = new CGButton(new Text("Set custom data", false)), gbc);
		--gbc.gridx;
		++gbc.gridy;
		gbc.gridwidth = 2;
		this.add(this.buttonDurability = new CGButton(new Text("Edit", false)), gbc);
		++gbc.gridy;
		this.add(this.buttonTexture = new CGButton(new Text("Edit", false)), gbc);
		++gbc.gridy;
		this.add(this.buttonCustom = new CGButton(new Text("Edit", false)), gbc);

		this.buttonMaxDamage.addActionListener(this);
		this.buttonDamage.addActionListener(this);
		this.buttonDurability.addActionListener(this);
		this.buttonTexture.addActionListener(this);
		this.buttonCustom.addActionListener(this);

		this.updateDisplay();
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.buttonMaxDamage)
		{
			this.item.setMaxDamage(Integer.parseInt(Dialogs.showInputDialog("New max data?", Integer.toString(this.item.getMaxDamage()))));
		}
		if (e.getSource() == this.buttonDamage) try
		{
			int[] damage = this.item.getDamageValues();
			String d = Integer.toString(damage[0]);
			for (int i = 1; i < damage.length; ++i)
				d += " " + damage[i];
			d = Dialogs.showInputDialog("New data values? Separate with spaces", d);
			if (d == null) return;
			String[] values = d.split(" ");
			damage = new int[values.length];
			for (int i = 0; i < values.length; ++i)
				damage[i] = Integer.parseInt(values[i]);
			this.item.setDamageCustom(damage);
		} catch (Exception e2)
		{}
		if (e.getSource() == this.buttonTexture) try
		{
			this.item.textureType = Integer.parseInt(Dialogs.showInputDialog("New texture type?", Integer.toString(this.item.textureType)));
		} catch (Exception e2)
		{}
		if (e.getSource() == this.buttonDurability) try
		{
			int d = Integer.parseInt(Dialogs.showInputDialog("New durability? -1 for no durability", Integer.toString(this.item.getDurability())));
			this.item.hasDurability = d >= 0;
			if (this.item.hasDurability) this.item.setDurability(d);
			else this.item.setMaxDamage(0);
		} catch (Exception e2)
		{}
		if (e.getSource() == this.buttonCustom) try
		{
			String custom = Dialogs.showInputDialog("New custom item type? Effective after restart. null for normal item.", this.item.customObjectName);
			if (custom != null)
			{
				if (custom.equals("null")) this.item.customObjectName = null;
				else this.item.customObjectName = custom;
			}
		} catch (Exception e2)
		{}

		this.updateDisplay();
	}

	private void updateDisplay()
	{
		if (this.item.hasDurability) this.labelDamage.setText("Data value max: 0");
		else if (this.item.isDamageCustom())
		{
			int[] damage = this.item.getDamageValues();
			String d = Integer.toString(damage[0]);
			for (int i = 1; i < damage.length; ++i)
				d += ", " + damage[i];
			this.labelDamage.setText("Data values: " + d);
		} else this.labelDamage.setText("Data value max: " + (this.item.getMaxDamage()));
		this.labelDurability.setText(this.item.hasDurability ? "Durability: " + this.item.getDurability() : "No durability");
		this.labelTexture.setText("Texture type: " + this.item.textureType);
		this.labelCustom.setText("Custom item type: " + this.item.customObjectName);
	}
}
