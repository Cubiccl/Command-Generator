package fr.cubiccl.generator.gameobject;

import java.awt.Component;
import java.util.ArrayList;
import java.util.HashMap;

import org.jdom2.Element;

import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.tags.TagList;
import fr.cubiccl.generator.gameobject.tags.TagString;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound.DefaultCompound;
import fr.cubiccl.generator.gui.component.interfaces.IObjectList;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.display.PanelItemDisplay;
import fr.cubiccl.generator.gui.component.panel.recipe.PanelRecipe;
import fr.cubiccl.generator.gui.component.panel.utils.ListProperties;
import fr.cubiccl.generator.utils.CommandGenerationException;

public class Recipe extends GameObject implements IObjectList<Recipe>
{
	private static final char[] KEYS =
	{ 'X', 'Y', 'Z', '#', '$', '*', '1', '2', '3' };
	public static final byte SHAPED = 0, SHAPELESS = 1;

	public static Recipe createFrom(Element recipe)
	{
		Recipe r = new Recipe(Byte.parseByte(recipe.getAttributeValue("type")));
		for (Element item : recipe.getChildren("item"))
			r.recipe[Integer.parseInt(item.getAttributeValue("position"))] = ItemStack.createFrom(item);
		r.findProperties(recipe);
		return r;
	}

	public static Recipe createFrom(TagCompound tag)
	{
		if (tag.hasTag(Tags.RECIPE_TYPE) && tag.getTag(Tags.RECIPE_TYPE).value().equals("crafting_shapeless")) return createShapelessFrom(tag);
		return createShapedFrom(tag);
	}

	public static String[] createPattern(String output)
	{
		String[] pattern = new String[]
		{ output.substring(0, 3), output.substring(3, 6), output.substring(6, 9) };

		boolean improved = true;
		while (improved)
		{
			improved = false;
			for (int i = 0; i < 3; ++i)
			{
				if ((i != 1 || isEmpty(pattern[0]) || isEmpty(pattern[2])) && isEmpty(pattern[i]))
				{
					pattern[i] = "";
					improved = true;
					break;
				} else if ((i != 1 || pattern[0].length() < 2) && isColumnEmpty(i, pattern))
				{
					if (i < pattern[0].length()) pattern[0] = removeChar(i, pattern[0]);
					if (i < pattern[1].length()) pattern[1] = removeChar(i, pattern[1]);
					if (i < pattern[2].length()) pattern[2] = removeChar(i, pattern[2]);
					improved = true;
					break;
				}
			}
		}

		ArrayList<String> purged = new ArrayList<String>();
		for (int i = 0; i < pattern.length; ++i)
			if (!pattern[i].equals("")) purged.add(pattern[i]);

		return purged.toArray(new String[purged.size()]);
	}

	private static Recipe createShapedFrom(TagCompound tag)
	{
		Recipe r = new Recipe(SHAPED);
		if (tag.hasTag(Tags.RECIPE_RESULT)) r.recipe[9] = ItemStack.createFrom((TagCompound) tag.getTag(Tags.RECIPE_RESULT));

		HashMap<Character, ItemStack> affectations = new HashMap<Character, ItemStack>();
		if (tag.hasTag(Tags.RECIPE_KEY)) for (Tag t : ((TagCompound) tag.getTag(Tags.RECIPE_KEY)).value())
			affectations.put(t.id().charAt(0), ItemStack.createFrom((TagCompound) t));

		String pattern = "";
		if (tag.hasTag(Tags.RECIPE_KEY))
		{
			for (Tag t : ((TagList) tag.getTag(Tags.RECIPE_PATTERN)).value())
			{
				pattern += ((TagString) t).value();
				while (pattern.length() % 3 != 0)
					pattern += " ";
			}
			while (pattern.length() < 9)
				pattern += " ";
		} else pattern = "         ";

		for (int i = 0; i < pattern.length(); ++i)
			if (affectations.containsKey(pattern.charAt(i))) try
			{
				r.recipe[i] = affectations.get(pattern.charAt(i)).clone();
			} catch (CloneNotSupportedException e)
			{
				e.printStackTrace();
			}

		return r;
	}

	private static Recipe createShapelessFrom(TagCompound tag)
	{
		Recipe r = new Recipe(SHAPELESS);
		if (tag.hasTag(Tags.RECIPE_RESULT)) r.recipe[9] = ItemStack.createFrom((TagCompound) tag.getTag(Tags.RECIPE_RESULT));
		int i = 0;
		if (tag.hasTag(Tags.RECIPE_INGREDIENTS)) for (Tag t : ((TagList) tag.getTag(Tags.RECIPE_INGREDIENTS)).value())
		{
			if (i > 8) break;
			r.recipe[i] = ItemStack.createFrom((TagCompound) t);
		}
		return r;
	}

	private static boolean isColumnEmpty(int i, String[] pattern)
	{
		for (int c = 0; c < pattern.length; ++c)
			if (i < pattern[c].length() && pattern[c].charAt(i) != ' ') return false;
		if (i >= pattern[0].length() && i >= pattern[1].length() && i >= pattern[2].length()) return false;
		return true;
	}

	private static boolean isEmpty(String string)
	{
		return string.length() > 0 && string.replaceAll(" ", "").length() == 0;
	}

	private static String removeChar(int i, String string)
	{
		return string.substring(0, i) + (i < string.length() - 1 ? string.substring(i + 1, string.length()) : "");
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
		return new PanelRecipe(this);
	}

	@Override
	public Component getDisplayComponent()
	{
		if (!this.isValid()) return new CGLabel("recipe.invalid");
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
				toreturn[i] = this.recipe[i] == null ? null : this.recipe[i].clone();
			} catch (CloneNotSupportedException e)
			{
				e.printStackTrace();
			}
		return toreturn;
	}

	public boolean isValid()
	{
		if (this.recipe[9] == null) return false;
		for (int i = 0; i < 9; ++i)
			if (this.recipe[i] != null) return true;
		return false;
	}

	public void setItemAt(int position, ItemStack item)
	{
		try
		{
			this.recipe[position] = item.clone();
			this.onChange();
		} catch (CloneNotSupportedException e)
		{
			e.printStackTrace();
		}
	}

	private TagCompound shapedToTag(TemplateCompound container)
	{
		ItemStack[] affectations = new ItemStack[9];
		ArrayList<TagCompound> key = new ArrayList<TagCompound>();

		String output = "";
		for (int i = 0; i < 9; ++i)
		{
			if (this.recipe[i] == null) output += " ";
			else for (int j = 0; j < affectations.length; ++j)
			{
				if (affectations[j] == null)
				{
					affectations[j] = this.recipe[i];
					key.add(this.recipe[i].toTagForRecipe(new DefaultCompound(Character.toString(KEYS[j]), Tag.UNKNOWN)));
				} else if (!this.recipe[i].matches(affectations[j])) continue;
				output += KEYS[j];
				break;
			}
		}

		String[] pattern = createPattern(output);
		ArrayList<TagString> o = new ArrayList<TagString>();
		for (String string : pattern)
			o.add(Tags.DEFAULT_STRING.create(string));

		return container.create(Tags.RECIPE_TYPE.create("crafting_shaped"), Tags.RECIPE_KEY.create(key.toArray(new TagCompound[key.size()])),
				Tags.RECIPE_PATTERN.create(o.toArray(new TagString[o.size()])), this.recipe[9].toTag(Tags.RECIPE_RESULT));
	}

	private TagCompound shapelessToTag(TemplateCompound container)
	{
		ArrayList<TagCompound> items = new ArrayList<TagCompound>();
		for (int i = 0; i < 9; ++i)
			if (this.recipe[i] != null) items.add(this.recipe[i].toTag(container));
		TagList ingredients = Tags.RECIPE_INGREDIENTS.create(items.toArray(new TagCompound[items.size()]));
		return container.create(Tags.RECIPE_TYPE.create("crafting_shapeless"), ingredients, this.recipe[9].toTag(Tags.RECIPE_RESULT));
	}

	@Override
	public String toCommand()
	{
		return this.toTag(Tags.DEFAULT_COMPOUND).valueForCommand(0);
	}

	@Override
	public String toString()
	{
		return this.customName();
	}

	@Override
	public TagCompound toTag(TemplateCompound container)
	{
		if (this.type == SHAPELESS) return this.shapelessToTag(container);
		return this.shapedToTag(container);
	}

	@Override
	public Element toXML()
	{
		Element root = this.createRoot("recipe").setAttribute("type", Byte.toString(this.type));
		for (int i = 0; i < this.recipe.length; ++i)
			if (this.recipe[i] != null) root.addContent(this.recipe[i].toXML().setAttribute("position", Integer.toString(i)));
		return root;
	}

	@Deprecated
	@Override
	public Recipe update(CGPanel panel) throws CommandGenerationException
	{
		return this;
	}

}
