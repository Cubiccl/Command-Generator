package fr.cubiccl.generator.gameobject;

import java.awt.Component;
import java.util.ArrayList;

import fr.cubiccl.generator.gameobject.baseobjects.Item;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.tags.TagNumber;
import fr.cubiccl.generator.gameobject.tags.TagString;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gui.component.interfaces.IObjectList;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.label.ImageLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelItem;
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

	public final int amount, damage;
	public final Item item;
	public final TagCompound nbt;
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

	@Override
	public Component getDisplayComponent()
	{
		CGPanel p = new CGPanel();
		p.add(new CGLabel(this.item.name(this.damage)));
		p.add(new ImageLabel(this.item.texture(this.damage)));
		return p;
	}

	@Override
	public String getName(int index)
	{
		return this.customName() != null && !this.customName().equals("") ? this.customName() : this.item.name(this.damage).toString();
	}

	@Override
	public ItemStack setupFrom(CGPanel panel) throws CommandGenerationException
	{
		return ((PanelItem) panel).generate();
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

}
