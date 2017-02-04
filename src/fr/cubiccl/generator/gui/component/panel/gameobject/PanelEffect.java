package fr.cubiccl.generator.gui.component.panel.gameobject;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import fr.cubiccl.generator.gameobject.Effect;
import fr.cubiccl.generator.gameobject.baseobjects.EffectType;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gameobject.registries.ObjectSaver;
import fr.cubiccl.generator.gui.component.button.CGCheckBox;
import fr.cubiccl.generator.gui.component.combobox.ObjectCombobox;
import fr.cubiccl.generator.gui.component.interfaces.ICustomObject;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.label.ImageLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.utils.PanelCustomObject;
import fr.cubiccl.generator.gui.component.textfield.CGEntry;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

public class PanelEffect extends CGPanel implements ActionListener, ICustomObject<Effect>
{
	private static final long serialVersionUID = 7100906772655502112L;

	private CGCheckBox checkboxHideParticles;
	private ObjectCombobox<EffectType> comboboxEffect;
	private CGEntry entryDuration, entryLevel;
	private ImageLabel labelTexture;

	public PanelEffect()
	{
		this(true);
	}

	public PanelEffect(boolean customObjects)
	{
		super("effect.title");
		EffectType[] effects = ObjectRegistry.effects.list();

		GridBagConstraints gbc = this.createGridBagLayout();
		gbc.anchor = GridBagConstraints.WEST;
		this.add(new CGLabel("effect.select").setHasColumn(true), gbc);
		++gbc.gridx;
		this.add(this.comboboxEffect = new ObjectCombobox<EffectType>(effects), gbc);
		++gbc.gridx;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		this.add(this.labelTexture = new ImageLabel(effects[0].texture()), gbc);
		++gbc.gridx;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.EAST;
		if (customObjects) this.add(new PanelCustomObject<Effect>(this, ObjectSaver.effects), gbc);
		gbc.gridx = 0;
		++gbc.gridy;
		gbc.gridwidth = 4;
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		this.add((this.entryDuration = new CGEntry(new Text("effect.duration"), "0", Text.INTEGER)).container, gbc);
		++gbc.gridy;
		this.add((this.entryLevel = new CGEntry(new Text("effect.level"), "0", Text.INTEGER)).container, gbc);
		++gbc.gridy;
		this.add(this.checkboxHideParticles = new CGCheckBox("effect.hide_particles"), gbc);

		this.entryDuration.addIntFilter();
		this.entryLevel.addIntFilter();

		this.comboboxEffect.addActionListener(this);
		this.updateTranslations();
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		this.labelTexture.setImage(this.comboboxEffect.getSelectedObject().texture());
	}

	@Override
	public Effect generate() throws CommandGenerationException
	{
		this.entryDuration.checkValueSuperior(CGEntry.INTEGER, 0);
		this.entryLevel.checkValueInBounds(CGEntry.INTEGER, 0, 255);

		return new Effect(this.comboboxEffect.getSelectedObject(), Integer.parseInt(this.entryDuration.getText()), Integer.parseInt(this.entryLevel.getText()),
				this.checkboxHideParticles.isSelected());
	}

	@Override
	public void setupFrom(Effect effect)
	{
		this.checkboxHideParticles.setSelected(effect.hideParticles);
		this.comboboxEffect.setSelected(effect.type);
		this.labelTexture.setImage(this.comboboxEffect.getSelectedObject().texture());
		this.entryDuration.setText(Integer.toString(effect.duration));
		this.entryLevel.setText(Integer.toString(effect.amplifier));
	}
}
