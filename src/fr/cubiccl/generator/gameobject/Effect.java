package fr.cubiccl.generator.gameobject;

import fr.cubiccl.generator.gameobject.baseobjects.EffectType;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.tags.TagNumber;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;

public class Effect
{

	/** Level of Effect (0 = Level 1) */
	public final int amplifier;
	/** Duration in seconds */
	public final int duration;
	public final boolean hideParticles;
	public final EffectType type;

	public Effect(EffectType type, int duration, int amplifier, boolean hideParticles)
	{
		this.type = type;
		this.duration = duration;
		this.amplifier = amplifier;
		this.hideParticles = hideParticles;
	}

	public String toCommand()
	{
		return this.type.idString + " " + this.duration + " " + this.amplifier + " " + this.hideParticles;
	}

	public TagCompound toTag(TemplateCompound container)
	{
		return new TagCompound(container, new TagNumber(Tags.EFFECT_ID, this.type.idInt), new TagNumber(Tags.EFFECT_AMPLIFIER, this.amplifier), new TagNumber(
				Tags.EFFECT_DURATION, this.duration), new TagNumber(Tags.EFFECT_PARTICLES, this.hideParticles ? 0 : 1));
	}

}
