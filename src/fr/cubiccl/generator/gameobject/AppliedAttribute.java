package fr.cubiccl.generator.gameobject;

import java.awt.Component;

import fr.cubiccl.generator.gameobject.baseobjects.Attribute;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gameobject.tags.*;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gui.component.interfaces.IObjectList;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelAttribute;
import fr.cubiccl.generator.gui.component.panel.utils.ListProperties;
import fr.cubiccl.generator.utils.CommandGenerationException;

public class AppliedAttribute extends GameObject implements IObjectList<AppliedAttribute>
{
	public static AppliedAttribute createFrom(TagCompound tag)
	{
		Attribute a = ObjectRegistry.attributes.first();
		double b = 1;
		AttributeModifier[] m = new AttributeModifier[0];

		for (Tag t : tag.value())
		{
			if (t.id().equals(Tags.ATTRIBUTE_ATTRIBUTE_NAME.id())) a = ObjectRegistry.attributes.find(((TagString) t).value());
			if (t.id().equals(Tags.ATTRIBUTE_BASE.id())) b = ((TagBigNumber) t).value();
			if (t.id().equals(Tags.ATTRIBUTE_MODIFIERS.id()))
			{
				TagList list = (TagList) t;
				m = new AttributeModifier[list.size()];
				for (int i = 0; i < m.length; i++)
					m[i] = AttributeModifier.createFrom((TagCompound) list.getTag(i));
			}
		}

		AppliedAttribute at = new AppliedAttribute(a, b, m);
		at.findName(tag);
		return at;
	}

	public Attribute attribute;

	public double base;

	public AttributeModifier[] modifiers;

	public AppliedAttribute()
	{
		this(ObjectRegistry.attributes.find("generic.armor"), 0);
	}

	public AppliedAttribute(Attribute attribute, double base, AttributeModifier... modifiers)
	{
		this.attribute = attribute;
		this.base = base;
		this.modifiers = modifiers;
	}

	@Override
	public CGPanel createPanel(ListProperties properties)
	{
		PanelAttribute p = new PanelAttribute(properties.hasCustomObjects());
		p.setupFrom(this);
		return p;
	}

	@Override
	public Component getDisplayComponent()
	{
		return null;
	}

	@Override
	public String getName(int index)
	{
		return this.customName() != null && !this.customName().equals("") ? this.customName() : this.attribute.name().toString();
	}

	@Override
	public String toCommand()
	{
		return this.toString();
	}

	@Override
	public String toString()
	{
		return this.attribute.name().toString() + ", " + this.modifiers.length + " modifiers";
	}

	@Override
	public TagCompound toTag(TemplateCompound container, boolean includeName)
	{
		TagCompound[] m = new TagCompound[this.modifiers.length];
		for (int i = 0; i < m.length; i++)
			m[i] = this.modifiers[i].toTag(Tags.DEFAULT_COMPOUND, true, includeName);

		if (includeName) return container.create(Tags.ATTRIBUTE_ATTRIBUTE_NAME.create(this.attribute.id), Tags.ATTRIBUTE_BASE.create(this.base),
				this.nameTag(), Tags.ATTRIBUTE_MODIFIERS.create(m));
		return container.create(Tags.ATTRIBUTE_ATTRIBUTE_NAME.create(this.attribute.id), Tags.ATTRIBUTE_BASE.create(this.base),
				Tags.ATTRIBUTE_MODIFIERS.create(m));
	}

	@Override
	public AppliedAttribute update(CGPanel panel) throws CommandGenerationException
	{
		AppliedAttribute a = ((PanelAttribute) panel).generate();
		this.attribute = a.attribute;
		this.base = a.base;
		this.modifiers = a.modifiers;
		return this;
	}

}
