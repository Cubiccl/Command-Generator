package fr.cubiccl.generator.gameobject.baseobjects;

import java.awt.image.BufferedImage;

import org.jdom2.Element;

import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.utils.Lang;
import fr.cubiccl.generator.utils.Text;

public class RecipeType extends BaseObject
{

	private int damage;
	private String id;
	private Item item;

	public RecipeType(String id, Item item)
	{
		this(id, item, -1);
	}

	public RecipeType(String id, Item item, int damage)
	{
		this.id = id;
		this.item = item;
		this.damage = damage;
		if (id != null) ObjectRegistry.recipes.register(this);
		if (item == null) System.out.println("Recipe " + this.id + " has null Item !!");
	}

	@Override
	public String id()
	{
		return "minecraft:" + this.id;
	}

	public Text name()
	{
		if (Lang.keyExists("recipe." + this.id)) return new Text("recipe." + this.id);
		if (this.damage == -1) return this.item.mainName();
		return this.item.name(this.damage);
	}

	public BufferedImage texture()
	{
		if (this.damage == -1) return this.item.texture();
		return this.item.texture(this.damage);
	}

	@Override
	public String toString()
	{
		return this.name().toString();
	}

	@Override
	public Element toXML()
	{
		Element root = new Element("recipe");
		root.setAttribute("id", this.id);
		if (!this.item.id().equals(this.id())) root.addContent(new Element("item").setText(this.item.id().substring("minecraft:".length())));
		if (this.damage != -1) root.addContent(new Element("damage").setText(Integer.toString(this.damage)));
		return root;
	}

}
