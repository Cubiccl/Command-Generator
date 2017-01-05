package fr.cubiccl.generator.gameobject;

import fr.cubiccl.generator.gameobject.baseobjects.EffectType;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.tags.TagNumber;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;

public class Effect
{

	public static Effect createFrom(TagCompound tag)
	{
		int a = 0, d = 0;
		boolean h = false;
		EffectType e = ObjectRegistry.getEffectTypes()[0];
		for (Tag t : tag.value())
		{
			if (t.id().equals(Tags.EFFECT_ID.id)) e = ObjectRegistry.getEffectFromID(((TagNumber) t).value());
			if (t.id().equals(Tags.EFFECT_DURATION.id)) d = ((TagNumber) t).value();
			if (t.id().equals(Tags.EFFECT_AMPLIFIER.id)) a = ((TagNumber) t).value();
			if (t.id().equals(Tags.EFFECT_PARTICLES.id)) h = ((TagNumber) t).value() == 1;
		}
		return new Effect(e, d, a, h);
	}

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
