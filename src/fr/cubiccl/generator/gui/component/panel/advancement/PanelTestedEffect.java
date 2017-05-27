package fr.cubiccl.generator.gui.component.panel.advancement;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gameobject.advancements.TestedEffect;
import fr.cubiccl.generator.gameobject.baseobjects.EffectType;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gui.component.combobox.ObjectCombobox;
import fr.cubiccl.generator.gui.component.panel.tag.PanelRangedValue;
import fr.cubiccl.generator.gui.component.panel.utils.PanelTestValues;
import fr.cubiccl.generator.gui.component.textfield.CGEntry;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

public class PanelTestedEffect extends PanelTestValues
{
	private static final long serialVersionUID = 3035876602300893005L;

	private ObjectCombobox<EffectType> comboboxEffect;
	private PanelRangedValue panelAmplifier, panelDuration;

	public PanelTestedEffect(TestedEffect effect)
	{
		super();

		++this.gbc.gridy;
		this.add(this.comboboxEffect = new ObjectCombobox<EffectType>(ObjectRegistry.effects.list()), this.gbc);
		this.addComponent("effect.amplifier", this.panelAmplifier = new PanelRangedValue("effect.level", null, Text.INTEGER));
		this.addComponent("effect.duration.test", this.panelDuration = new PanelRangedValue("effect.duration", null, Text.INTEGER));

		this.updateFrom(effect);
	}

	public boolean checkInput()
	{
		try
		{
			if (this.isSelected(this.panelAmplifier)) this.panelAmplifier.checkInput(CGEntry.INTEGER);
			if (this.isSelected(this.panelDuration)) this.panelDuration.checkInput(CGEntry.INTEGER);
		} catch (CommandGenerationException e)
		{
			CommandGenerator.report(e);
			return false;
		}
		return true;
	}

	public void update(TestedEffect effect)
	{
		effect.effect = this.comboboxEffect.getSelectedObject();
		if (this.isSelected(this.panelAmplifier)) this.panelAmplifier.generateValue(effect.amplifier);
		if (this.isSelected(this.panelDuration)) this.panelDuration.generateValue(effect.duration);
		effect.amplifierTested = this.isSelected(this.panelAmplifier);
		effect.durationTested = this.isSelected(this.panelDuration);
	}

	private void updateFrom(TestedEffect effect)
	{
		this.comboboxEffect.setSelected(effect.effect);
		this.setSelected(this.panelAmplifier, effect.amplifierTested);
		this.setSelected(this.panelDuration, effect.durationTested);
		if (effect.amplifierTested) this.panelAmplifier.setupFrom(effect.amplifier);
		if (effect.durationTested) this.panelDuration.setupFrom(effect.duration);
	}

}
