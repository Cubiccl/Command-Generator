package fr.cubiccl.generator.gameobject;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import org.jdom2.Element;

import fr.cubiccl.generator.gameobject.baseobjects.EnchantmentType;
import fr.cubiccl.generator.gameobject.baseobjects.Item;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
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

/** Represents an Item in an Inventory. */
public class ItemStack extends GameObject<ItemStack> implements IObjectList<ItemStack>
{

	/** The number of Items in this Stack. */
	public int amount;
	/** The damage value of the Item. */
	private int damage;
	/** The {@link Item}. */
	private Item item;
	/** The NBT Tags of this Item. */
	private TagCompound nbt;
	/** The slot this Item is in. */
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

	/** @return This Item Stack's display name. Looks into the NBT Tags to find a custom one; if it doesn't, returns the default name. */
	public String displayName()
	{
		if (this.nbt.hasTag("display"))
		{
			TagCompound display = (TagCompound) this.nbt.getTagFromId("display");
			if (display.hasTag(Tags.DISPLAY_NAME)) return display.getTag(Tags.DISPLAY_NAME).value();
		}
		return (this.amount == 0 ? "" : this.amount + " ") + this.item.name(this.damage).toString();
	}

	@Override
	public ItemStack duplicate(ItemStack object)
	{
		this.amount = object.amount;
		this.damage = object.damage;
		this.item = object.item;
		this.nbt = object.nbt.duplicate();
		this.slot = object.slot;
		return this;
	}

	/** Adds an Enchantment to this Item, with a random level.
	 * 
	 * @param enchantment - The Enchantment to add. */
	public void enchant(EnchantmentType enchantment)
	{
		TagCompound e = Tags.DEFAULT_COMPOUND.create(Tags.ENCHANTMENT_ID.create(enchantment.idNum()),
				Tags.ENCHANTMENT_LVL.create(new Random().nextInt(enchantment.maxLevel() + 1)));
		if (this.nbt.hasTag("ench")) ((TagList) this.nbt.getTagFromId("ench")).addTag(e);
		else this.nbt.addTag(((TemplateList) ObjectRegistry.itemTags.find("ench")).create(e));
	}

	/** @return The Enchantments applied to this Item. */
	public Enchantment[] findEnchantments()
	{
		if (this.nbt.hasTag("ench"))
		{
			TagList ench = (TagList) this.nbt.getTagFromId("ench");
			Enchantment[] e = new Enchantment[ench.size()];
			for (int i = 0; i < e.length; ++i)
				e[i] = new Enchantment().fromNBT((TagCompound) ench.getTag(i));
			return e;
		}
		return new Enchantment[0];
	}

	/** @return The Lore describing this Item. */
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

	@Override
	public ItemStack fromNBT(TagCompound tag)
	{
		return this.fromNBT(tag, false);
	}

	/** Creates an Item Stack from the input NBT Tag.
	 * 
	 * @param tag - The NBT Tag describing the Item Stack.
	 * @param allowNull - <code>true</code> if some elements such as Item ID or count can be omitted.
	 * @return The created Item Stack. */
	public ItemStack fromNBT(TagCompound tag, boolean allowNull)
	{
		this.item = allowNull ? null : ObjectRegistry.items.first();
		this.amount = allowNull ? -1 : 1;
		this.damage = allowNull ? -1 : 0;
		this.slot = -1;
		this.nbt = Tags.ITEM_NBT.create();

		for (Tag t : tag.value())
		{
			if (t.id().equals(Tags.ITEM_ID.id()) || t.id().equals(Tags.ITEM_IDITEM.id())) this.item = ObjectRegistry.items.find(((TagString) t).value);
			if (t.id().equals(Tags.ITEM_COUNT.id()) || t.id().equals(Tags.RECIPE_ITEM_COUNT.id())) this.amount = ((TagNumber) t).valueInt();
			if (t.id().equals(Tags.ITEM_DAMAGE.id()) || t.id().equals(Tags.RECIPE_ITEM_DATA.id())) this.damage = ((TagNumber) t).valueInt();
			if (t.id().equals(Tags.ITEM_SLOT.id())) this.slot = ((TagNumber) t).valueInt();
			if (t.id().equals(Tags.ITEM_NBT.id())) this.nbt = (TagCompound) t;
			if (t.id().equals(Tags.CRITERIA_POTION.id())) this.nbt.addTag(t);
			if (t.id().equals(Tags.ITEM_ENCHANTMENTS.id())) this.nbt.addTag(t);
			if (t.id().equals(Tags.CRITERIA_NBT.id())) this.nbt.addTag(t);
		}

		this.findName(tag);
		return this;
	}

	/** Creates an Item Stack from the input NBT Tag, for a Recipe (uses different NBT Tags).
	 * 
	 * @param tag - The NBT Tag describing the Item Stack.
	 * @return The created Item Stack. */
	public ItemStack fromNBTForRecipe(TagCompound tag)
	{
		for (Tag t : tag.value())
		{
			if (t.id().equals(Tags.RECIPE_ITEM_ID.id())) this.item = ObjectRegistry.items.find(((TagString) t).value);
			if (t.id().equals(Tags.RECIPE_ITEM_COUNT.id())) this.amount = ((TagNumber) t).valueInt();
			if (t.id().equals(Tags.RECIPE_ITEM_DATA.id())) this.damage = ((TagNumber) t).valueInt();
		}

		this.findName(tag);
		return this;
	}

	@Override
	public ItemStack fromXML(Element xml)
	{
		this.item = ObjectRegistry.items.find(xml.getChildText("id"));
		this.amount = Integer.parseInt(xml.getChildText("amount"));
		this.damage = Integer.parseInt(xml.getChildText("damage"));
		this.slot = Integer.parseInt(xml.getChildText("slot"));
		this.nbt = (TagCompound) NBTParser.parse(xml.getChildText("nbt"), true, false, true);
		this.findProperties(xml);
		return this;
	}

	/** Getter for {@link ItemStack#damage}. */
	public int getDamage()
	{
		return damage;
	}

	@Override
	public PanelItemDisplay getDisplayComponent()
	{
		return new PanelItemDisplay(this);
	}

	/** Getter for {@link ItemStack#item}. */
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

	/** Getter for {@link ItemStack#nbt}. */
	public TagCompound getNbt()
	{
		return nbt;
	}

	/** @param item - An Item Stack to compare with.
	 * @return <code>true</code> if this Item Stack matches the input Item Stack, i.e. their ID and damage are equal. */
	public boolean matches(ItemStack item)
	{
		return this.getItem() == item.getItem() && this.getDamage() == item.getDamage();
	}

	/** Setter for {@link ItemStack#damage}. */
	public void setDamage(int damage)
	{
		this.damage = damage;
		this.onChange();
	}

	/** Setter for {@link ItemStack#item}. */
	public void setItem(Item item)
	{
		this.item = item;
		this.onChange();
	}

	/** Setter for {@link ItemStack#nbt}. */
	public void setNbt(TagCompound nbt)
	{
		this.nbt = nbt;
		this.onChange();
	}

	/** @return This Item Stack's texture. */
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
	public TagCompound toNBT(TemplateCompound container)
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

	@Override
	public String toString()
	{
		if (this.amount == -1) return this.item.name(this.damage).toString();
		return this.amount + " " + this.item.name(this.damage);
	}

	/** Converts this Object to a NBT Tag, for a Recipe.
	 * 
	 * @param container - The template for the container Tag.
	 * @return The Compound container tag. */
	public TagCompound toTagForRecipe(TemplateCompound container)
	{
		ArrayList<Tag> tags = new ArrayList<Tag>();
		tags.add(Tags.RECIPE_ITEM_ID.create(this.item.id()));
		tags.add(Tags.RECIPE_ITEM_DATA.create(this.damage));
		if (this.amount != 1) tags.add(Tags.RECIPE_ITEM_COUNT.create(this.amount));
		return container.create(tags.toArray(new Tag[tags.size()]));
	}

	/** Converts this Object to a NBT Tag, for a testing object (loot table or advancement).
	 * 
	 * @param container - The template for the container Tag.
	 * @return The Compound container tag. */
	public TagCompound toTagForTest(TemplateCompound container)
	{
		ArrayList<Tag> tags = new ArrayList<Tag>();
		if (this.item != null) tags.add(Tags.ITEM_IDITEM.create(this.item.id()));
		if (this.damage != -1) tags.add(Tags.RECIPE_ITEM_DATA.create(this.damage));
		if (this.amount != -1) tags.add(Tags.RECIPE_ITEM_COUNT.create(this.amount));
		if (this.nbt.hasTag(Tags.CRITERIA_POTION)) tags.add(this.nbt.getTag(Tags.CRITERIA_POTION));
		if (this.nbt.hasTag(Tags.ITEM_ENCHANTMENTS)) tags.add(this.nbt.getTag(Tags.ITEM_ENCHANTMENTS));
		if (this.nbt.hasTag(Tags.CRITERIA_NBT)) tags.add(this.nbt.getTag(Tags.CRITERIA_NBT));
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
