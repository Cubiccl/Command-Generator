package fr.cubiccl.generator.gameobject.baseobjects;

import java.awt.Component;
import java.awt.image.BufferedImage;

import org.jdom2.Element;

import fr.cubiccl.generator.gameobject.ItemStack;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gui.component.combobox.ObjectCombobox;
import fr.cubiccl.generator.gui.component.interfaces.IObjectList;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.utils.ListProperties;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Lang;
import fr.cubiccl.generator.utils.Text;

/** A default Recipe from Minecraft. */
public class RecipeType extends BaseObject<RecipeType> implements IObjectList<RecipeType>
{

	/** The damage of the resulting Item. */
	private int damage;
	/** The ID of the Recipe. */
	private String id;
	/** The resulting Item. */
	private Item item;

	public RecipeType()
	{
		this.damage = -1;
	}

	@Override
	public CGPanel createPanel(ListProperties properties)
	{
		return new ObjectCombobox<RecipeType>(ObjectRegistry.recipes.list()).container;
	}

	@Override
	public RecipeType duplicate(RecipeType object)
	{
		return this;
	}

	@Override
	public RecipeType fromXML(Element xml)
	{
		this.id = xml.getAttributeValue("id");
		this.item = ObjectRegistry.items.find(xml.getChild("item") == null ? this.id : xml.getChildText("item"));
		if (xml.getChild("damage") != null) this.damage = Integer.parseInt(xml.getChildText("damage"));
		return this;
	}

	@Override
	public Component getDisplayComponent()
	{
		if (this.damage != -1) return new ItemStack(this.item, this.damage, 0).getDisplayComponent();
		return this.item.getDisplayComponent();
	}

	@Override
	public String getName(int index)
	{
		return this.name().toString();
	}

	@Override
	public String id()
	{
		return "minecraft:" + this.id;
	}

	/** Getter for {@link RecipeType#item}. */
	public Item item()
	{
		return this.item;
	}

	@Override
	public Text name()
	{
		if (Lang.keyExists("recipe." + this.id)) return new Text("recipe." + this.id);
		if (this.damage == -1) return this.item.mainName();
		return this.item.name(this.damage);
	}

	@Override
	public RecipeType register()
	{
		ObjectRegistry.recipes.register(this);
		return this;
	}

	@Override
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

	@SuppressWarnings("unchecked")
	@Override
	public RecipeType update(CGPanel panel) throws CommandGenerationException
	{
		return ((ObjectCombobox<RecipeType>) panel.getComponent(1)).getSelectedObject();
	}

}
