package fr.cubiccl.generator.gui.component.panel.data;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;

import fr.cubiccl.generator.gameobject.baseobjects.Block;
import fr.cubiccl.generator.gui.Dialogs;
import fr.cubiccl.generator.gui.component.button.CGButton;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.utils.Text;

public class PanelBlockEdition extends CGPanel implements ActionListener
{
	private static final long serialVersionUID = 8814888562936761232L;

	private final Block block;
	private CGButton buttonMaxDamage, buttonDamage, buttonTexture, buttonCustom;
	private JLabel labelDamage, labelTexture, labelCustom;

	public PanelBlockEdition(Block block)
	{
		this.block = block;
		GridBagConstraints gbc = this.createGridBagLayout();
		gbc.gridwidth = 3;
		this.add(new CGLabel(this.block.name()), gbc);
		++gbc.gridy;
		this.add(new JLabel("ID: " + this.block.id() + ", num: " + this.block.idNum()), gbc);

		++gbc.gridy;
		gbc.gridwidth = 1;
		this.add(this.labelDamage = new JLabel(), gbc);
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
		this.add(this.buttonTexture = new CGButton(new Text("Edit", false)), gbc);
		++gbc.gridy;
		this.add(this.buttonCustom = new CGButton(new Text("Edit", false)), gbc);

		this.buttonMaxDamage.addActionListener(this);
		this.buttonDamage.addActionListener(this);
		this.buttonTexture.addActionListener(this);
		this.buttonCustom.addActionListener(this);

		this.updateDisplay();
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.buttonMaxDamage)
		{
			this.block.setMaxDamage(Integer.parseInt(Dialogs.showInputDialog("New max data?", Integer.toString(this.block.getMaxDamage()))));
		}
		if (e.getSource() == this.buttonDamage) try
		{
			int[] damage = this.block.getDamageValues();
			String d = Integer.toString(damage[0]);
			for (int i = 1; i < damage.length; ++i)
				d += " " + damage[i];
			d = Dialogs.showInputDialog("New data values? Separate with spaces", d);
			if (d == null) return;
			String[] values = d.split(" ");
			damage = new int[values.length];
			for (int i = 0; i < values.length; ++i)
				damage[i] = Integer.parseInt(values[i]);
			this.block.setDamageCustom(damage);
		} catch (Exception e2)
		{}
		if (e.getSource() == this.buttonTexture) try
		{
			this.block.textureType = Integer.parseInt(Dialogs.showInputDialog("New texture type?", Integer.toString(this.block.textureType)));
		} catch (Exception e2)
		{}
		if (e.getSource() == this.buttonCustom) try
		{
			String custom = Dialogs.showInputDialog("New custom block type? Effective after restart. null for normal block.", this.block.customObjectName);
			if (custom != null)
			{
				if (custom.equals("null")) this.block.customObjectName = null;
				else this.block.customObjectName = custom;
			}
		} catch (Exception e2)
		{}

		this.updateDisplay();
	}

	private void updateDisplay()
	{
		if (this.block.isDamageCustom())
		{
			int[] damage = this.block.getDamageValues();
			String d = Integer.toString(damage[0]);
			for (int i = 1; i < damage.length; ++i)
				d += ", " + damage[i];
			this.labelDamage.setText("Data values: " + d);
		} else this.labelDamage.setText("Data value max: " + this.block.getMaxDamage());
		this.labelTexture.setText("Texture type: " + this.block.textureType);
		this.labelCustom.setText("Custom block type: " + this.block.customObjectName);
	}
}
