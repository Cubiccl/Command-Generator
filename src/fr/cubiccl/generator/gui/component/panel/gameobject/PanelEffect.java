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
		if (customObjects) this.add(new PanelCustomObject<Effect, Effect>(this, ObjectSaver.effects), gbc);
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
		this.setDefault();
		this.updateTranslations();
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		this.labelTexture.setImage(this.selectedEffect().texture());
	}

	public void addActionListener(ActionListener listener)
	{
		this.comboboxEffect.addActionListener(listener);
	}

	@Override
	public Effect generate() throws CommandGenerationException
	{
		this.entryDuration.checkValueSuperior(CGEntry.INTEGER, 0);
		this.entryLevel.checkValueInBounds(CGEntry.INTEGER, 0, 255);

		return new Effect(this.selectedEffect(), Integer.parseInt(this.entryDuration.getText()), Integer.parseInt(this.entryLevel.getText()),
				this.checkboxHideParticles.isSelected());
	}

	public EffectType selectedEffect()
	{
		return this.comboboxEffect.getSelectedObject();
	}

	public void setAmplifier(int amplifier)
	{
		this.entryLevel.setText(Integer.toString(amplifier));
	}

	public void setDefault()
	{
		this.setDuration(30);
		this.setAmplifier(0);
		this.setHideParticles(false);
	}

	public void setDuration(int duration)
	{
		this.entryDuration.setText(Integer.toString(duration));
	}

	public void setDurationAsSeconds(boolean asSeconds)
	{
		if (asSeconds) this.entryDuration.label.setTextID("effect.duration.seconds");
		else this.entryDuration.label.setTextID("effect.duration");
	}

	public void setEffect(EffectType effect)
	{
		if (effect != null) this.comboboxEffect.setSelected(effect);
	}

	public void setHideParticles(boolean hideParticles)
	{
		this.checkboxHideParticles.setSelected(hideParticles);
	}

	@Override
	public void setupFrom(Effect effect)
	{
		this.setHideParticles(effect.hideParticles);
		this.comboboxEffect.setSelected(effect.getType());
		this.labelTexture.setImage(this.selectedEffect().texture());
		this.setDuration(effect.duration);
		this.setAmplifier(effect.amplifier);
	}
}
