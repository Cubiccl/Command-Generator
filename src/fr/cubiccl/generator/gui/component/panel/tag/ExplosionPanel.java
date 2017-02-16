package fr.cubiccl.generator.gui.component.panel.tag;

import java.awt.GridBagConstraints;

import fr.cubiccl.generator.gameobject.Explosion;
import fr.cubiccl.generator.gameobject.Explosion.Color;
import fr.cubiccl.generator.gui.component.button.CGCheckBox;
import fr.cubiccl.generator.gui.component.combobox.OptionCombobox;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.utils.PanelObjectList;

public class ExplosionPanel extends CGPanel
{
	private static final long serialVersionUID = 2699563337471683919L;

	private CGCheckBox boxFlicker, boxTrail;
	private OptionCombobox comboboxType;
	private PanelObjectList<Color> panelPrimaryColors, panelFadeColors;

	public ExplosionPanel()
	{
		GridBagConstraints gbc = this.createGridBagLayout();
		this.add(new CGLabel("firework.type").setHasColumn(true), gbc);
		++gbc.gridx;
		this.add(this.comboboxType = new OptionCombobox("firework.type", "small", "large", "star", "creeper", "burst"), gbc);

		--gbc.gridx;
		++gbc.gridy;
		this.add(this.boxFlicker = new CGCheckBox("firework.flicker"), gbc);
		++gbc.gridx;
		this.add(this.boxTrail = new CGCheckBox("firework.trail"), gbc);

		--gbc.gridx;
		++gbc.gridy;
		++gbc.gridwidth;
		this.add(this.panelPrimaryColors = new PanelObjectList<Color>("firework.colors.primary", "firework.colors.primary", Color.class), gbc);
		++gbc.gridy;
		this.add(this.panelFadeColors = new PanelObjectList<Color>("firework.colors.fade", "firework.colors.fade", Color.class), gbc);

		this.setName("tag.title.Explosion");
	}

	public Explosion generateExplosion()
	{
		return new Explosion((byte) this.comboboxType.getSelectedIndex(), this.boxFlicker.isSelected(), this.boxTrail.isSelected(),
				this.panelPrimaryColors.values(), this.panelFadeColors.values());
	}

	public void setupFrom(Explosion explosion)
	{
		this.comboboxType.setSelectedIndex(explosion.type);
		this.boxFlicker.setSelected(explosion.flicker);
		this.boxTrail.setSelected(explosion.trail);
		this.panelPrimaryColors.setValues(explosion.primary);
		this.panelFadeColors.setValues(explosion.fade);
	}

}
