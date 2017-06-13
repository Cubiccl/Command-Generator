package fr.cubiccl.generator.gameobject;

import java.awt.Component;

import org.jdom2.Element;

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

/** Represents an Effect applied to an Entity or stored in a Potion. */
public class Effect extends GameObject<Effect> implements IObjectList<Effect>
{

	/** Creates an Effect from the input XML element.
	 * 
	 * @param trade - The XML element describing the Effect.
	 * @return The created Effect. */
	public static Effect createFrom(TagCompound tag)
	{
		int a = 0, d = 0;
		boolean h = false;
		EffectType e = ObjectRegistry.effects.first();
		for (Tag t : tag.value())
		{
			if (t.id().equals(Tags.EFFECT_ID.id())) e = ObjectRegistry.effects.find(((TagNumber) t).valueInt());
			if (t.id().equals(Tags.EFFECT_DURATION.id())) d = ((TagNumber) t).valueInt();
			if (t.id().equals(Tags.EFFECT_AMPLIFIER.id())) a = ((TagNumber) t).valueInt();
			if (t.id().equals(Tags.EFFECT_PARTICLES.id())) h = ((TagNumber) t).value() == 1;
		}
		Effect effect = new Effect(e, d, a, h);
		effect.findName(tag);
		return effect;
	}

	/** The level of the Effect (0 = Level 1) */
	public int amplifier;
	/** The duration of the Effect. Is usually in ticks, except for /effect where it's in seconds. */
	public int duration;
	/** <code>true</code> if the Effect particles should be hidden. */
	public boolean hideParticles;
	/** The {@link EffectType Effect type}. */
	private EffectType type;

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
	public Effect fromXML(Element xml)
	{
		this.type = ObjectRegistry.effects.find(xml.getChildText("id"));
		this.amplifier = Integer.parseInt(xml.getChildText("amplifier"));
		this.duration = Integer.parseInt(xml.getChildText("duration"));
		this.hideParticles = Boolean.parseBoolean(xml.getChildText("hideparticles"));
		this.findProperties(xml);
		return this;
	}

	@Override
	public Component getDisplayComponent()
	{
		CGPanel p = new CGPanel();
		p.add(new CGLabel(new Text(this.toString(), false)));
		p.add(new ImageLabel(this.type.texture()));
		return p;
	}

	@Override
	public String getName(int index)
	{
		return this.customName() != null && !this.customName().equals("") ? this.customName() : this.type.name().toString() + " " + this.amplifier;
	}

	/** Getter for {@link Effect#type}. */
	public EffectType getType()
	{
		return type;
	}

	/** Setter for {@link Effect#type}. */
	public void setType(EffectType type)
	{
		this.type = type;
		this.onChange();
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
	public TagCompound toTag(TemplateCompound container)
	{
		return container.create(Tags.EFFECT_ID.create(this.type.idInt), Tags.EFFECT_AMPLIFIER.create(this.amplifier),
				Tags.EFFECT_DURATION.create(this.duration), Tags.EFFECT_PARTICLES.create(this.hideParticles ? 0 : 1));
	}

	@Override
	public Element toXML()
	{
		Element root = this.createRoot("effect");
		root.addContent(new Element("id").setText(this.type.id()));
		root.addContent(new Element("amplifier").setText(Integer.toString(this.amplifier)));
		root.addContent(new Element("duration").setText(Integer.toString(this.duration)));
		root.addContent(new Element("hideparticles").setText(Boolean.toString(this.hideParticles)));
		return root;
	}

	@Override
	public Effect update(CGPanel panel) throws CommandGenerationException
	{
		Effect e = ((PanelEffect) panel).generate();
		this.amplifier = e.amplifier;
		this.duration = e.duration;
		this.hideParticles = e.hideParticles;
		this.type = e.type;
		return this;
	}

}
