package fr.cubiccl.generator.gameobject;

import fr.cubiccl.generator.gameobject.baseobjects.EffectType;

public class Effect
{

	/** Duration in seconds */
	public final int duration;
	/** Level of Effect (0 = Level 1) */
	public final int amplifier;
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

}
