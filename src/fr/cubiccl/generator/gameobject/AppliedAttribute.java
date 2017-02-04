package fr.cubiccl.generator.gameobject;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import fr.cubiccl.generator.gameobject.baseobjects.Attribute;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gameobject.tags.*;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gui.component.interfaces.IObjectList;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelAttributeModifier;
import fr.cubiccl.generator.utils.Text;

public class AppliedAttribute extends GameObject
{
	public static class AttributeModifierList implements IObjectList
	{

		public boolean isApplied = false;
		public ArrayList<AttributeModifier> modifiers = new ArrayList<AttributeModifier>();

		public AttributeModifierList(boolean isApplied)
		{
			this.isApplied = isApplied;
		}

		@Override
		public boolean addObject(CGPanel panel, int editIndex)
		{
			AttributeModifier m = ((PanelAttributeModifier) panel).generateModifier();
			if (m == null) return false;
			if (editIndex == -1) this.modifiers.add(m);
			else this.modifiers.set(editIndex, m);
			return true;
		}

		@Override
		public CGPanel createAddPanel(int editIndex)
		{
			PanelAttributeModifier p = new PanelAttributeModifier(this.isApplied);
			if (editIndex != -1) p.setupFrom(this.modifiers.get(editIndex));
			return p;
		}

		@Override
		public Text getName(int index)
		{
			return new Text(this.modifiers.get(index).name, false);
		}

		@Override
		public BufferedImage getTexture(int index)
		{
			return null;
		}

		@Override
		public String[] getValues()
		{
			String[] values = new String[this.modifiers.size()];
			for (int i = 0; i < values.length; i++)
				values[i] = this.getName(i).toString();
			return values;
		}

		@Override
		public void removeObject(int index)
		{
			this.modifiers.remove(index);
		}

	}

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

	public AppliedAttribute(Attribute attribute, double base, AttributeModifier... modifiers)
	{
		this.attribute = attribute;
		this.base = base;
		this.modifiers = modifiers;
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

		if (includeName) return new TagCompound(container, new TagString(Tags.ATTRIBUTE_ATTRIBUTE_NAME, this.attribute.id), new TagBigNumber(
				Tags.ATTRIBUTE_BASE, this.base), this.nameTag(), new TagList(Tags.ATTRIBUTE_MODIFIERS, m));
		return new TagCompound(container, new TagString(Tags.ATTRIBUTE_ATTRIBUTE_NAME, this.attribute.id), new TagBigNumber(Tags.ATTRIBUTE_BASE, this.base),
				new TagList(Tags.ATTRIBUTE_MODIFIERS, m));
	}

}
