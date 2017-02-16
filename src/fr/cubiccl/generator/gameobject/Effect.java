package fr.cubiccl.generator.gameobject;

import java.awt.Component;

import fr.cubiccl.generator.gameobject.baseobjects.EffectType;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.tags.TagNumber;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gui.component.interfaces.IObjectList;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.label.ImageLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelEffect;
import fr.cubiccl.generator.gui.component.panel.utils.ListProperties;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

public class Effect extends GameObject implements IObjectList<Effect>
{

	public static Effect createFrom(TagCompound tag)
	{
		int a = 0, d = 0;
		boolean h = false;
		EffectType e = ObjectRegistry.effects.first();
		for (Tag t : tag.value())
		{
			if (t.id().equals(Tags.EFFECT_ID.id())) e = ObjectRegistry.effects.find(((TagNumber) t).value());
			if (t.id().equals(Tags.EFFECT_DURATION.id())) d = ((TagNumber) t).value();
			if (t.id().equals(Tags.EFFECT_AMPLIFIER.id())) a = ((TagNumber) t).value();
			if (t.id().equals(Tags.EFFECT_PARTICLES.id())) h = ((TagNumber) t).value() == 1;
		}
		Effect ef = new Effect(e, d, a, h);
		ef.findName(tag);
		return ef;
	}

	/** Level of Effect (0 = Level 1) */
	public int amplifier;
	/** Duration in seconds */
	public int duration;
	public boolean hideParticles;
	public EffectType type;

	public Effect()
	{
		this(ObjectRegistry.effects.find("speed"), 0, 0, false);
	}

	public Effect(EffectType type, int duration, int amplifier, boolean hideParticles)
	{
		this.type = type;
		this.duration = duration;
		this.amplifier = amplifier;
		this.hideParticles = hideParticles;
	}

	@Override
	public CGPanel createPanel(ListProperties properties)
	{
		PanelEffect e = new PanelEffect(properties.hasCustomObjects());
		e.setupFrom(this);
		return e;
	}

	@Override
	public Component getDisplayComponent()
	{
		CGPanel p = new CGPanel();
		p.add(new CGLabel(new Text(this.toString())));
		p.add(new ImageLabel(this.type.texture()));
		return p;
	}

	@Override
	public String getName(int index)
	{
		return this.customName() != null && !this.customName().equals("") ? this.customName() : this.type.name().toString() + " " + this.amplifier;
	}

	@Override
	public Effect setupFrom(CGPanel panel) throws CommandGenerationException
	{
		return ((PanelEffect) panel).generate();
	}

	@Override
	public String toCommand()
	{
		return this.type.idString + " " + this.duration + " " + this.amplifier + " " + this.hideParticles;
	}

	@Override
	public String toString()
	{
		return this.type.name().toString() + " " + this.amplifier + " (" + this.duration + "s" + (this.hideParticles ? ", particles hidden)" : ")");
	}

	@Override
	public TagCompound toTag(TemplateCompound container, boolean includeName)
	{
		if (includeName) return new TagCompound(container, new TagNumber(Tags.EFFECT_ID, this.type.idInt),
				new TagNumber(Tags.EFFECT_AMPLIFIER, this.amplifier), new TagNumber(Tags.EFFECT_DURATION, this.duration), new TagNumber(Tags.EFFECT_PARTICLES,
						this.hideParticles ? 0 : 1), this.nameTag());
		return new TagCompound(container, new TagNumber(Tags.EFFECT_ID, this.type.idInt), new TagNumber(Tags.EFFECT_AMPLIFIER, this.amplifier), new TagNumber(
				Tags.EFFECT_DURATION, this.duration), new TagNumber(Tags.EFFECT_PARTICLES, this.hideParticles ? 0 : 1));
	}

}
