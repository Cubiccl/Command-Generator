package fr.cubiccl.generator.gameobject;

import java.awt.Component;

import org.jdom2.Element;

import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gui.component.interfaces.IObjectList;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.display.PanelItemDisplay;
import fr.cubiccl.generator.gui.component.panel.utils.ListProperties;
import fr.cubiccl.generator.utils.CommandGenerationException;

public class Recipe extends GameObject implements IObjectList<Recipe>
{
	public static final byte SHAPED = 0, SHAPELESS = 1;

	public static Recipe createFrom(Element recipe)
	{
		Recipe r = new Recipe(Byte.parseByte(recipe.getAttributeValue("type")));
		for (Element item : recipe.getChildren("item"))
			r.recipe[Integer.parseInt(recipe.getAttributeValue("position"))] = ItemStack.createFrom(item);
		return r;
	}

	public static Recipe createFrom(TagCompound tag)
	{
		// TODO Recipe.createFrom(tag)
		return null;
	}

	private ItemStack[] recipe;
	public byte type;

	public Recipe()
	{
		this(SHAPED);
	}

	public Recipe(byte type, ItemStack... recipe)
	{
		this.type = type;
		this.recipe = new ItemStack[10];
		for (int i = 0; i < recipe.length && i < this.recipe.length; ++i)
			try
			{
				this.recipe[i] = recipe[i].clone();
			} catch (CloneNotSupportedException e)
			{
				e.printStackTrace();
			}
	}

	@Override
	public CGPanel createPanel(ListProperties properties)
	{
		// TODO Recipe.createPanel(properties)
		return null;
	}

	@Override
	public Component getDisplayComponent()
	{
		return new PanelItemDisplay(this.recipe[9]);
	}

	@Override
	public String getName(int index)
	{
		return this.customName();
	}

	public ItemStack[] getRecipe()
	{
		ItemStack[] toreturn = new ItemStack[this.recipe.length];
		for (int i = 0; i < toreturn.length; ++i)
			try
			{
				toreturn[i] = this.recipe[i].clone();
			} catch (CloneNotSupportedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return toreturn;
	}

	@Override
	public String toCommand()
	{
		return this.toTag(Tags.DEFAULT_COMPOUND).valueForCommand();
	}

	@Override
	public String toString()
	{
		return this.customName();
	}

	@Override
	public TagCompound toTag(TemplateCompound container)
	{
		// TODO Recipe.toTag(container)
		return null;
	}

	@Override
	public Element toXML()
	{
		Element root = new Element("recipe").setAttribute("type", Byte.toString(this.type));
		for (int i = 0; i < this.recipe.length; ++i)
			if (this.recipe[i] != null) root.addContent(this.recipe[i].toXML().setAttribute("position", Integer.toString(i)));
		return root;
	}

	@Override
	public Recipe update(CGPanel panel) throws CommandGenerationException
	{
		// TODO Recipe.update(panel)
		return this;
	}

}
