package fr.cubiccl.generator.gui.component.panel.gameobject;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import fr.cubiccl.generator.gameobject.Effect;
import fr.cubiccl.generator.gameobject.ObjectRegistry;
import fr.cubiccl.generator.gameobject.baseobjects.EffectType;
import fr.cubiccl.generator.gui.component.button.CCheckBox;
import fr.cubiccl.generator.gui.component.combobox.ObjectCombobox;
import fr.cubiccl.generator.gui.component.label.CLabel;
import fr.cubiccl.generator.gui.component.label.ImageLabel;
import fr.cubiccl.generator.gui.component.panel.CPanel;
import fr.cubiccl.generator.gui.component.textfield.CEntry;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Lang;
import fr.cubiccl.generator.utils.WrongValueException;

public class PanelEffect extends CPanel implements ActionListener
{
	private static final long serialVersionUID = 7100906772655502112L;

	private CCheckBox checkboxHideParticles;
	private ObjectCombobox<EffectType> comboboxEffect;
	private CEntry entryDuration, entryLevel;
	private ImageLabel labelTexture;

	public PanelEffect()
	{
		super("effect.title");
		EffectType[] effects = ObjectRegistry.getEffectTypes();

		GridBagConstraints gbc = this.createGridBagLayout();
		this.add(new CLabel("effect.select").setHasColumn(true), gbc);
		++gbc.gridx;
		this.add(this.comboboxEffect = new ObjectCombobox<EffectType>(effects), gbc);
		++gbc.gridx;
		this.add(this.labelTexture = new ImageLabel(effects[0].texture()), gbc);
		gbc.gridx = 0;
		++gbc.gridy;
		gbc.gridwidth = 3;
		this.add((this.entryDuration = new CEntry("effect.duration", "0")).container, gbc);
		++gbc.gridy;
		this.add((this.entryLevel = new CEntry("effect.level", "0")).container, gbc);
		++gbc.gridy;
		this.add(this.checkboxHideParticles = new CCheckBox("effect.hide_particles"), gbc);

		this.entryDuration.addIntFilter();
		this.entryLevel.addIntFilter();

		this.comboboxEffect.addActionListener(this);
		this.updateTranslations();
	}

	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		this.labelTexture.setImage(this.comboboxEffect.getSelectedObject().texture());
	}

	public Effect generateEffect() throws CommandGenerationException
	{
		int duration, level;
		String d = this.entryDuration.getText(), l = this.entryLevel.getText();

		try
		{
			duration = Integer.parseInt(d);
			if (duration < 0) throw new WrongValueException(this.entryDuration.label.getAbsoluteText(), Lang.translate("error.integer.positive"), d);
		} catch (NumberFormatException e)
		{
			throw new WrongValueException(this.entryDuration.label.getAbsoluteText(), Lang.translate("error.integer.positive"), d);
		}

		try
		{
			level = Integer.parseInt(l);
			if (level < 0 || level > 255) throw new WrongValueException(this.entryLevel.label.getAbsoluteText(), Lang.translate("error.integer.bounds")
					.replace("<min>", "0").replace("<max>", "255"), l);
		} catch (NumberFormatException e)
		{
			throw new WrongValueException(this.entryLevel.label.getAbsoluteText(),
					Lang.translate("error.integer.bounds").replace("<min>", "0").replace("<max>", "255"), l);
		}

		return new Effect(this.comboboxEffect.getSelectedObject(), duration, level, this.checkboxHideParticles.isSelected());
	}

}
