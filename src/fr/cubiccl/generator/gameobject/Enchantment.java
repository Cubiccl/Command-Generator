package fr.cubiccl.generator.gameobject;

import java.awt.Component;

import fr.cubiccl.generator.gameobject.baseobjects.EnchantmentType;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.tags.TagNumber;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gui.component.interfaces.IObjectList;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelEnchantment;
import fr.cubiccl.generator.gui.component.panel.utils.ListProperties;
import fr.cubiccl.generator.utils.CommandGenerationException;

public class Enchantment extends GameObject implements IObjectList<Enchantment>
{

	public static Enchantment createFrom(TagCompound tag)
	{
		EnchantmentType type = ObjectRegistry.enchantments.first();
		int level = 1;
		for (Tag t : tag.value())
		{
			if (t.id().equals(Tags.ENCHANTMENT_ID.id())) type = ObjectRegistry.enchantments.find(((TagNumber) t).value());
			if (t.id().equals(Tags.ENCHANTMENT_LVL.id())) level = ((TagNumber) t).value();
		}
		Enchantment e = new Enchantment(type, level);
		e.findName(tag);
		return e;
	}

	public int level;
	public EnchantmentType type;

	public Enchantment()
	{
		this(ObjectRegistry.enchantments.find("protection"), 1);
	}

	public Enchantment(EnchantmentType type, int level)
	{
		this.type = type;
		this.level = level;
	}

	@Override
	public CGPanel createPanel(ListProperties properties)
	{
		PanelEnchantment p = new PanelEnchantment(false, properties.hasCustomObjects());
		p.setupFrom(this);
		return p;
	}

	@Override
	public Component getDisplayComponent()
	{
		return new CGLabel(this.toString());
	}

	@Override
	public String getName(int index)
	{
		return this.customName() != null && !this.customName().equals("") ? this.customName() : this.toString();
	}

	@Override
	public Enchantment setupFrom(CGPanel panel) throws CommandGenerationException
	{
		return ((PanelEnchantment) panel).generate();
	}

	@Override
	public String toCommand()
	{
		return this.type.idString + " " + this.level;
	}

	@Override
	public String toString()
	{
		return this.type.name().toString() + " " + this.level;
	}

	@Override
	public TagCompound toTag(TemplateCompound container, boolean includeName)
	{
		if (includeName) return new TagCompound(container, new TagNumber(Tags.ENCHANTMENT_ID, this.type.idInt),
				new TagNumber(Tags.ENCHANTMENT_LVL, this.level), this.nameTag());
		return new TagCompound(container, new TagNumber(Tags.ENCHANTMENT_ID, this.type.idInt), new TagNumber(Tags.ENCHANTMENT_LVL, this.level));
	}

}
