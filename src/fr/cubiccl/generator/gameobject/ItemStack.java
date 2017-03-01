package fr.cubiccl.generator.gameobject;

import java.util.ArrayList;

import fr.cubiccl.generator.gameobject.baseobjects.Item;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gameobject.tags.*;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gui.component.interfaces.IObjectList;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelItem;
import fr.cubiccl.generator.gui.component.panel.gameobject.display.PanelItemDisplay;
import fr.cubiccl.generator.gui.component.panel.utils.ListProperties;
import fr.cubiccl.generator.utils.CommandGenerationException;

public class ItemStack extends GameObject implements IObjectList<ItemStack>
{

	public static ItemStack createFrom(TagCompound tag)
	{
		Item i = ObjectRegistry.items.first();
		int a = 1, d = 0, s = -1;
		TagCompound nbt = Tags.ITEM_NBT.create();

		for (Tag t : tag.value())
		{
			if (t.id().equals(Tags.ITEM_ID.id())) i = ObjectRegistry.items.find(((TagString) t).value);
			if (t.id().equals(Tags.ITEM_COUNT.id())) a = ((TagNumber) t).value;
			if (t.id().equals(Tags.ITEM_DAMAGE.id())) d = ((TagNumber) t).value;
			if (t.id().equals(Tags.ITEM_SLOT.id())) s = ((TagNumber) t).value;
			if (t.id().equals(Tags.ITEM_NBT.id())) nbt = (TagCompound) t;
		}

		ItemStack is = new ItemStack(i, d, a, nbt);
		is.slot = s;
		is.findName(tag);
		return is;
	}

	public int amount, damage;
	public Item item;
	public TagCompound nbt;
	public int slot;

	public ItemStack()
	{
		this(ObjectRegistry.items.find("stone"), 0, 1);
	}

	public ItemStack(Item item, int data, int amount)
	{
		this(item, data, amount, Tags.ITEM_NBT.create());
	}

	public ItemStack(Item item, int data, int amount, TagCompound nbt)
	{
		super();
		this.item = item;
		this.damage = data;
		this.amount = amount;
		this.slot = 0;
		this.nbt = nbt;
	}

	@Override
	public ItemStack clone() throws CloneNotSupportedException
	{
		ItemStack is = new ItemStack(item, damage, amount, nbt);
		is.slot = this.slot;
		return is;
	}

	@Override
	public CGPanel createPanel(ListProperties properties)
	{
		PanelItem p = new PanelItem(null, true, true, properties.hasCustomObjects(), ObjectRegistry.items.list());
		p.setupFrom(this);
		return p;
	}

	public String displayName()
	{
		if (this.nbt.hasTag("display"))
		{
			TagCompound display = (TagCompound) this.nbt.getTagFromId("display");
			if (display.hasTag(Tags.DISPLAY_NAME)) return ((TagString) display.getTag(Tags.DISPLAY_NAME)).value();
		}
		return this.amount + " " + this.item.name(this.damage).toString();
	}

	public Enchantment[] findEnchantments()
	{
		if (this.nbt.hasTag("ench"))
		{
			TagList ench = (TagList) this.nbt.getTagFromId("ench");
			Enchantment[] e = new Enchantment[ench.size()];
			for (int i = 0; i < e.length; ++i)
				e[i] = Enchantment.createFrom((TagCompound) ench.getTag(i));
			return e;
		}
		return new Enchantment[0];
	}

	public String[] findLore()
	{
		if (this.nbt.hasTag("display"))
		{
			TagCompound display = (TagCompound) this.nbt.getTagFromId("display");
			if (display.hasTag(Tags.DISPLAY_LORE))
			{
				TagList lore = (TagList) display.getTag(Tags.DISPLAY_LORE);
				String[] l = new String[lore.size()];
				for (int i = 0; i < l.length; ++i)
					l[i] = ((TagString) lore.getTag(i)).value();
				return l;
			}
		}
		return new String[0];
	}

	@Override
	public PanelItemDisplay getDisplayComponent()
	{
		return new PanelItemDisplay(this);
	}

	@Override
	public String getName(int index)
	{
		return this.customName() != null && !this.customName().equals("") ? this.customName() : this.item.name(this.damage).toString();
	}

	@Override
	public String toCommand()
	{
		return this.item.id() + " " + this.amount + " " + this.damage + " " + this.nbt.valueForCommand();
	}

	@Override
	public String toString()
	{
		return this.amount + " " + this.item.name(this.damage);
	}

	@Override
	public TagCompound toTag(TemplateCompound container, boolean includeName)
	{
		ArrayList<Tag> tags = new ArrayList<Tag>();
		tags.add(Tags.ITEM_ID.create(this.item.id()));
		tags.add(Tags.ITEM_DAMAGE.create(this.damage));
		tags.add(Tags.ITEM_COUNT.create(this.amount));
		if (this.slot != -1) tags.add(Tags.ITEM_SLOT.create(this.slot));
		tags.add(this.nbt);
		if (includeName) tags.add(this.nameTag());
		return container.create(tags.toArray(new Tag[tags.size()]));
	}

	@Override
	public ItemStack update(CGPanel panel) throws CommandGenerationException
	{
		ItemStack i = ((PanelItem) panel).generate();
		this.amount = i.amount;
		this.damage = i.damage;
		this.item = i.item;
		this.nbt = i.nbt;
		this.slot = i.slot;
		return this;
	}

}
