package fr.cubiccl.generator.gameobject.speedrun;

import java.util.ArrayList;
import java.util.List;

import org.jdom2.Element;

import fr.cubiccl.generator.gameobject.ItemStack;
import fr.cubiccl.generator.gameobject.baseobjects.Item;
import fr.cubiccl.generator.gameobject.tags.TagCompound;

public class ItemStackS extends ItemStack
{
	public static final byte FORCED = 0, RESOURCE = 1, OPTIONNAL = 2;

	public static ItemStackS createFrom(Element item)
	{
		ItemStackS i = new ItemStackS(ItemStack.createFrom(item));
		i.importance = Byte.parseByte(item.getAttributeValue("importance"));
		if (item.getChild("name") != null) i.name = item.getChildText("name");
		return i;
	}

	public byte importance = RESOURCE;
	public String name = null;

	public ItemStackS()
	{
		super();
	}

	public ItemStackS(Item item, int data, int amount)
	{
		super(item, data, amount);
	}

	public ItemStackS(Item item, int data, int amount, TagCompound nbt)
	{
		super(item, data, amount, nbt);
	}

	public ItemStackS(ItemStack item)
	{
		super(item.getItem(), item.getDamage(), item.amount, item.getNbt());
		this.slot = item.slot;
		this.setCustomName(item.customName());
	}

	@Override
	public ItemStackS clone() throws CloneNotSupportedException
	{
		ItemStackS i = new ItemStackS(this.getItem(), this.getDamage(), amount, this.getNbt());
		i.slot = this.slot;
		i.importance = this.importance;
		return i;
	}

	@Override
	public String getName(int index)
	{
		if (this.name != null) return this.name;
		return super.getName(index);
	}

	public boolean isForced()
	{
		return this.importance == FORCED;
	}

	public boolean isOptionnal()
	{
		return this.importance == OPTIONNAL;
	}

	public boolean isResource()
	{
		return this.importance == RESOURCE;
	}

	public List<ItemStackS> splitQuantity()
	{
		ArrayList<ItemStackS> split = new ArrayList<ItemStackS>();
		int quantity = this.amount, remove = 0;
		while (quantity > 0)
		{
			remove = quantity >= this.getItem().maxStackSize ? this.getItem().maxStackSize : quantity;
			quantity -= remove;
			try
			{
				ItemStackS i = this.clone();
				i.amount = remove;
				split.add(i);
			} catch (CloneNotSupportedException e)
			{
				e.printStackTrace();
			}
		}
		return split;
	}

	@Override
	public Element toXML()
	{
		Element root = super.toXML().setAttribute("importance", Byte.toString(this.importance));
		if (this.name != null) root.addContent(new Element("name").setText(this.name));
		return root;
	}

}
