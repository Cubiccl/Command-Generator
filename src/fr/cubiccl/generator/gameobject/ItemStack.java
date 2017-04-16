package fr.cubiccl.generator.gameobject;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import org.jdom2.Element;

import fr.cubiccl.generator.gameobject.baseobjects.EnchantmentType;
import fr.cubiccl.generator.gameobject.baseobjects.Item;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gameobject.speedrun.ItemStackS;
import fr.cubiccl.generator.gameobject.tags.*;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gameobject.templatetags.TemplateList;
import fr.cubiccl.generator.gui.component.interfaces.IObjectList;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.advancement.PanelTestedItem;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelItem;
import fr.cubiccl.generator.gui.component.panel.gameobject.display.PanelItemDisplay;
import fr.cubiccl.generator.gui.component.panel.utils.ListProperties;
import fr.cubiccl.generator.utils.CommandGenerationException;

public class ItemStack extends GameObject implements IObjectList<ItemStack>
{

	public static ItemStack createForRecipe(TagCompound tag)
	{
		Item i = ObjectRegistry.items.first();
		int a = 1, d = 0;

		for (Tag t : tag.value())
		{
			if (t.id().equals(Tags.RECIPE_ITEM_ID.id())) i = ObjectRegistry.items.find(((TagString) t).value);
			if (t.id().equals(Tags.RECIPE_ITEM_COUNT.id())) a = ((TagNumber) t).valueInt();
			if (t.id().equals(Tags.RECIPE_ITEM_DATA.id())) d = ((TagNumber) t).valueInt();
		}

		ItemStack is = new ItemStack(i, d, a, Tags.DEFAULT_COMPOUND.create());
		is.findName(tag);
		return is;
	}

	public static ItemStack createFrom(Element item)
	{
		ItemStack i = new ItemStack();
		i.item = ObjectRegistry.items.find(item.getChildText("id"));
		i.amount = Integer.parseInt(item.getChildText("amount"));
		i.damage = Integer.parseInt(item.getChildText("damage"));
		i.slot = Integer.parseInt(item.getChildText("slot"));
		i.nbt = (TagCompound) NBTReader.read(item.getChildText("nbt"), true, false, true);
		i.findProperties(item);
		return i;
	}

	public static ItemStack createFrom(TagCompound tag)
	{
		return createFrom(tag, false);
	}

	public static ItemStack createFrom(TagCompound tag, boolean allowNull)
	{
		Item i = allowNull ? null : ObjectRegistry.items.first();
		int a = allowNull ? -1 : 1, d = allowNull ? -1 : 0, s = -1;
		TagCompound nbt = Tags.ITEM_NBT.create();

		for (Tag t : tag.value())
		{
			if (t.id().equals(Tags.ITEM_ID.id()) || t.id().equals(Tags.ITEM_IDITEM.id())) i = ObjectRegistry.items.find(((TagString) t).value);
			if (t.id().equals(Tags.ITEM_COUNT.id()) || t.id().equals(Tags.RECIPE_ITEM_COUNT.id())) a = ((TagNumber) t).valueInt();
			if (t.id().equals(Tags.ITEM_DAMAGE.id()) || t.id().equals(Tags.RECIPE_ITEM_DATA.id())) d = ((TagNumber) t).valueInt();
			if (t.id().equals(Tags.ITEM_SLOT.id())) s = ((TagNumber) t).valueInt();
			if (t.id().equals(Tags.ITEM_NBT.id())) nbt = (TagCompound) t;
			if (t.id().equals(Tags.CRITERIA_POTION.id())) nbt.addTag(t);
			if (t.id().equals(Tags.ITEM_ENCHANTMENTS.id())) nbt.addTag(t);
		}

		ItemStack is = new ItemStack(i, d, a, nbt);
		is.slot = s;
		is.findName(tag);
		return is;
	}

	public int amount;
	private int damage;
	private Item item;
	private TagCompound nbt;
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
		this.slot = -1;
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
		if (properties.isTrue("testing"))
		{
			PanelTestedItem p = new PanelTestedItem();
			if (!properties.isTrue("new")) p.setupFrom(this);
			return p;
		}
		PanelItem p = new PanelItem(null, true, true, properties.hasCustomObjects(), ObjectRegistry.items.list());
		p.setupFrom(this);
		return p;
	}

	public String displayName()
	{
		if (this.nbt.hasTag("display"))
		{
			TagCompound display = (TagCompound) this.nbt.getTagFromId("display");
			if (display.hasTag(Tags.DISPLAY_NAME)) return display.getTag(Tags.DISPLAY_NAME).value();
		}
		return (this.amount == 0 ? "" : this.amount + " ") + this.item.name(this.damage).toString();
	}

	public void enchant(EnchantmentType enchantment)
	{
		TagCompound e = Tags.DEFAULT_COMPOUND.create(Tags.ENCHANTMENT_ID.create(enchantment.idNum()),
				Tags.ENCHANTMENT_LVL.create(new Random().nextInt(enchantment.maxLevel + 1)));
		if (this.nbt.hasTag("ench")) ((TagList) this.nbt.getTagFromId("ench")).addTag(e);
		else this.nbt.addTag(((TemplateList) ObjectRegistry.itemTags.find("ench")).create(e));
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
				TagList lore = display.getTag(Tags.DISPLAY_LORE);
				String[] l = new String[lore.size()];
				for (int i = 0; i < l.length; ++i)
					l[i] = ((TagString) lore.getTag(i)).value();
				return l;
			}
		}
		return new String[0];
	}

	public ItemStackS forSpeedrun(byte importance)
	{
		ItemStackS stack = new ItemStackS(item, this.damage, this.amount, nbt);
		stack.importance = importance;
		return stack;
	}

	public int getDamage()
	{
		return damage;
	}

	@Override
	public PanelItemDisplay getDisplayComponent()
	{
		return new PanelItemDisplay(this);
	}

	public Item getItem()
	{
		return item;
	}

	@Override
	public String getName(int index)
	{
		return this.customName() != null && !this.customName().equals("") ? this.customName() : this.item == null ? Integer.toString(index) : this.item.name(
				this.damage).toString();
	}

	public TagCompound getNbt()
	{
		return nbt;
	}

	public boolean matches(ItemStack item)
	{
		return this.getItem() == item.getItem() && this.getDamage() == item.getDamage();
	}

	public void setDamage(int damage)
	{
		this.damage = damage;
		this.onChange();
	}

	public void setItem(Item item)
	{
		this.item = item;
		this.onChange();
	}

	public void setNbt(TagCompound nbt)
	{
		this.nbt = nbt;
		this.onChange();
	}

	public BufferedImage texture()
	{
		if (this.item == null) return null;
		return this.item.texture(this.damage);
	}

	@Override
	public String toCommand()
	{
		return this.item.id() + " " + this.amount + " " + this.damage + " " + this.nbt.valueForCommand();
	}

	@Override
	public String toString()
	{
		if (this.amount == -1) return this.item.name(this.damage).toString();
		return this.amount + " " + this.item.name(this.damage);
	}

	@Override
	public TagCompound toTag(TemplateCompound container)
	{
		// IF YOU CHANGE THIS CHANGE ALSO BELOW FOR RECIPE AND TEST
		ArrayList<Tag> tags = new ArrayList<Tag>();
		tags.add(Tags.ITEM_ID.create(this.item.id()));
		tags.add(Tags.ITEM_DAMAGE.create(this.damage));
		tags.add(Tags.ITEM_COUNT.create(this.amount));
		if (this.slot != -1) tags.add(Tags.ITEM_SLOT.create(this.slot));
		tags.add(this.nbt);
		return container.create(tags.toArray(new Tag[tags.size()]));
	}

	public TagCompound toTagForRecipe(TemplateCompound container)
	{
		ArrayList<Tag> tags = new ArrayList<Tag>();
		tags.add(Tags.RECIPE_ITEM_ID.create(this.item.id()));
		tags.add(Tags.RECIPE_ITEM_DATA.create(this.damage));
		if (this.amount != 1) tags.add(Tags.RECIPE_ITEM_COUNT.create(this.amount));
		return container.create(tags.toArray(new Tag[tags.size()]));
	}

	public TagCompound toTagForTest(TemplateCompound container)
	{
		ArrayList<Tag> tags = new ArrayList<Tag>();
		if (this.item != null) tags.add(Tags.ITEM_IDITEM.create(this.item.id()));
		if (this.damage != -1) tags.add(Tags.RECIPE_ITEM_DATA.create(this.damage));
		if (this.amount != -1) tags.add(Tags.RECIPE_ITEM_COUNT.create(this.amount));
		if (this.nbt.hasTag(Tags.CRITERIA_POTION)) tags.add(this.nbt.getTag(Tags.CRITERIA_POTION));
		if (this.nbt.hasTag(Tags.ITEM_ENCHANTMENTS)) tags.add(this.nbt.getTag(Tags.ITEM_ENCHANTMENTS));
		return container.create(tags.toArray(new Tag[tags.size()]));
	}

	@Override
	public Element toXML()
	{
		Element root = this.createRoot("item");
		root.addContent(new Element("id").setText(this.item.id()));
		root.addContent(new Element("amount").setText(Integer.toString(this.amount)));
		root.addContent(new Element("damage").setText(Integer.toString(this.damage)));
		root.addContent(new Element("slot").setText(Integer.toString(this.slot)));
		root.addContent(new Element("nbt").setText(this.nbt.valueForCommand()));
		return root;
	}

	@Override
	public ItemStack update(CGPanel panel) throws CommandGenerationException
	{
		ItemStack i;
		if (panel instanceof PanelTestedItem) i = ((PanelTestedItem) panel).generate();
		else i = ((PanelItem) panel).generate();

		this.amount = i.amount;
		this.damage = i.damage;
		this.item = i.item;
		this.nbt = i.nbt;
		this.slot = i.slot;
		return this;
	}

}
