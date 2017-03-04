package fr.cubiccl.generator.gameobject.baseobjects;

import org.jdom2.Element;

import fr.cubiccl.generator.utils.Text;

public class Item extends BlockItem
{
	public String cooksTo = null;
	public boolean hasDurability = false;

	public Item(int idInt, String idString)
	{
		super(ITEM, idInt, idString);
	}

	public Item(int idInt, String idString, int maxDamage)
	{
		super(ITEM, idInt, idString, maxDamage);
	}

	public Item(int idInt, String idString, int... damage)
	{
		super(ITEM, idInt, idString, damage);
	}

	@Override
	public int[] getDamageValues()
	{
		if (this.hasDurability) return new int[]
		{ 0 };
		return super.getDamageValues();
	}

	public int getDurability()
	{
		return this.getMaxDamage();
	}

	public Text name(int damage)
	{
		if (this.hasDurability) return this.name(this.id());
		return super.name(damage);
	}

	public void setDurability(int durability)
	{
		this.hasDurability = durability != -1;
		this.setMaxDamage(durability);
	}

	@Override
	public Element toXML()
	{
		Element root = super.toXML();
		if (this.hasDurability) root.getChild("maxdamage").setName("durability");
		if (this.cooksTo != null) root.addContent(new Element("cooksto").setText(this.cooksTo));
		return root;
	}

}
