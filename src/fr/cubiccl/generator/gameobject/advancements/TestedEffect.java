package fr.cubiccl.generator.gameobject.advancements;

import java.awt.Component;

import fr.cubiccl.generator.gameobject.baseobjects.EffectType;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound.DefaultCompound;
import fr.cubiccl.generator.gameobject.utils.TestValue;
import fr.cubiccl.generator.gui.component.interfaces.IObjectList;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.advancement.PanelTestedEffect;
import fr.cubiccl.generator.gui.component.panel.utils.ListProperties;
import fr.cubiccl.generator.utils.CommandGenerationException;

public class TestedEffect implements IObjectList<TestedEffect>
{

	public static TestedEffect createFrom(TagCompound tag)
	{
		TestedEffect e = new TestedEffect();
		e.effect = ObjectRegistry.effects.find(tag.id());
		e.amplifierTested = e.amplifier.findValue(tag);
		e.durationTested = e.duration.findValue(tag);
		return e;
	}

	public TestValue amplifier, duration;
	public boolean amplifierTested, durationTested;
	public EffectType effect;

	public TestedEffect()
	{
		this.effect = ObjectRegistry.effects.first();
		this.amplifier = new TestValue(Tags.EFFECT_amplifier, Tags.EFFECT_amplifier_);
		this.duration = new TestValue(Tags.EFFECT_duration, Tags.EFFECT_duration_);
		this.amplifierTested = this.durationTested = false;
	}

	@Override
	public CGPanel createPanel(ListProperties properties)
	{
		return new PanelTestedEffect(this);
	}

	@Override
	public Component getDisplayComponent()
	{
		return null;
	}

	@Override
	public String getName(int index)
	{
		return this.effect.name().toString();
	}

	public TagCompound toTag()
	{
		TagCompound tag = new DefaultCompound(this.effect.id()).create();
		if (this.amplifierTested) tag.addTag(this.amplifier.toTag());
		if (this.durationTested) tag.addTag(this.duration.toTag());
		return tag;
	}

	@Override
	public TestedEffect update(CGPanel panel) throws CommandGenerationException
	{
		((PanelTestedEffect) panel).update(this);
		return this;
	}

}
