package fr.cubiccl.generator.gameobject;

import java.awt.Component;
import java.util.ArrayList;

import org.jdom2.Element;

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

/** Represents an Attribute applied to an Entity. */
public class AppliedAttribute extends GameObject<AppliedAttribute> implements IObjectList<AppliedAttribute>
{

	/** The applied {@link Attribute}. */
	private Attribute attribute;
	/** The base value of the Attribute. */
	public double base;
	/** The modifiers of this Attribute. */
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
	public AppliedAttribute fromNBT(TagCompound nbt)
	{

		for (Tag t : nbt.value())
		{
			if (t.id().equals(Tags.ATTRIBUTE_ATTRIBUTE_NAME.id())) this.attribute = ObjectRegistry.attributes.find(((TagString) t).value());
			if (t.id().equals(Tags.ATTRIBUTE_BASE.id())) this.base = ((TagNumber) t).value();
			if (t.id().equals(Tags.ATTRIBUTE_MODIFIERS.id()))
			{
				TagList list = (TagList) t;
				this.modifiers = new AttributeModifier[list.size()];
				for (int i = 0; i < this.modifiers.length; i++)
					this.modifiers[i] = new AttributeModifier().fromNBT((TagCompound) list.getTag(i));
			}
		}

		this.findName(nbt);
		return this;
	}

	@Override
	public AppliedAttribute fromXML(Element xml)
	{
		ArrayList<AttributeModifier> modifiers = new ArrayList<AttributeModifier>();
		for (Element m : xml.getChild("modifiers").getChildren())
			modifiers.add(new AttributeModifier().fromXML(m));

		this.attribute = ObjectRegistry.attributes.find(xml.getChildText("attribute"));
		this.base = Double.parseDouble(xml.getChildText("base"));
		this.modifiers = modifiers.toArray(new AttributeModifier[modifiers.size()]);
		this.findProperties(xml);
		return this;
	}

	/** Getter for {@link AppliedAttribute#attribute}. */
	public Attribute getAttribute()
	{
		return attribute;
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

	/** Setter for {@link AppliedAttribute#attribute}. */
	public void setAttribute(Attribute attribute)
	{
		this.attribute = attribute;
		this.onChange();
	}

	@Override
	public String toCommand()
	{
		return this.toString();
	}

	@Override
	public TagCompound toNBT(TemplateCompound container)
	{
		TagCompound[] m = new TagCompound[this.modifiers.length];
		for (int i = 0; i < m.length; i++)
			m[i] = this.modifiers[i].toTag(Tags.DEFAULT_COMPOUND, true);

		return container.create(Tags.ATTRIBUTE_ATTRIBUTE_NAME.create(this.attribute.id), Tags.ATTRIBUTE_BASE.create(this.base),
				Tags.ATTRIBUTE_MODIFIERS.create(m));
	}

	@Override
	public String toString()
	{
		return this.attribute.name().toString() + ", " + this.modifiers.length + " modifiers";
	}

	@Override
	public Element toXML()
	{
		Element root = this.createRoot("attribute");
		root.addContent(new Element("id").setText(this.attribute.id));
		root.addContent(new Element("base").setText(Double.toString(this.base)));

		Element modifiers = new Element("modifiers");
		for (AttributeModifier modifier : this.modifiers)
			modifiers.addContent(modifier.toXML());

		return root.addContent(modifiers);
	}

	@Override
	public AppliedAttribute update(CGPanel panel) throws CommandGenerationException
	{
		AppliedAttribute a = ((PanelAttribute) panel).generate();
		this.attribute = a.attribute;
		this.base = a.base;
		this.modifiers = a.modifiers;
		this.onChange();
		return this;
	}

}
