package fr.cubiccl.generator.gameobject;

import java.awt.Component;
import java.util.ArrayList;
import java.util.HashMap;

import org.jdom2.Element;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.tags.TagList;
import fr.cubiccl.generator.gameobject.tags.TagString;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound.DefaultCompound;
import fr.cubiccl.generator.gui.Dialogs;
import fr.cubiccl.generator.gui.component.interfaces.IObjectList;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.display.PanelItemDisplay;
import fr.cubiccl.generator.gui.component.panel.recipe.PanelRecipe;
import fr.cubiccl.generator.gui.component.panel.utils.ListProperties;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

/** Represents an editable Recipe for map making. */
public class Recipe extends GameObject<Recipe> implements IObjectList<Recipe>
{
	/** Keys to generate the pattern. */
	private static final char[] KEYS =
	{ 'X', 'Y', 'Z', '#', '$', '*', '1', '2', '3' };
	/** Recipe modes. <br />
	 * <br />
	 * <table border="1">
	 * <tr>
	 * <td>ID</td>
	 * <td>Variable</td>
	 * <td>Mode</td>
	 * </tr>
	 * <tr>
	 * <td>0</td>
	 * <td>SHAPED</td>
	 * <td>Shaped recipe</td>
	 * </tr>
	 * <tr>
	 * <td>1</td>
	 * <td>SHAPELESS</td>
	 * <td>Shapeless recipe</td>
	 * </tr>
	 * </table> */
	public static final byte SHAPED = 0, SHAPELESS = 1;

	/** @param column - The index of the column.
	 * @param pattern - The pattern to analyze.
	 * @return <code>true</code> if the column has no Items. */
	private static boolean isColumnEmpty(int column, String[] pattern)
	{
		for (int c = 0; c < pattern.length; ++c)
			if (column < pattern[c].length() && pattern[c].charAt(column) != ' ') return false;
		if (column >= pattern[0].length() && column >= pattern[1].length() && column >= pattern[2].length()) return false;
		return true;
	}

	/** @param line - The line to analyze.
	 * @return <code>true</code> if the input line is empty. */
	private static boolean isLineEmpty(String line)
	{
		return line.length() > 0 && line.replaceAll(" ", "").length() == 0;
	}

	/** Reduces the input pattern.
	 * 
	 * @param output - The pattern to reduce.
	 * @return The reduced pattern. */
	public static String[] reducePattern(String output)
	{
		String[] pattern = new String[]
		{ output.substring(0, 3), output.substring(3, 6), output.substring(6, 9) };

		boolean improved = true;
		while (improved)
		{
			improved = false;
			for (int i = 0; i < 3; ++i)
			{
				if ((i != 1 || isLineEmpty(pattern[0]) || isLineEmpty(pattern[2])) && isLineEmpty(pattern[i]))
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

	/** Removes a character from the input line. Checks for errors such as OOB.
	 * 
	 * @param index - The index of the character to remove.
	 * @param line - The line to edit.
	 * @return The edited line. */
	private static String removeChar(int index, String line)
	{
		return line.substring(0, index) + (index < line.length() - 1 ? line.substring(index + 1, line.length()) : "");
	}

	/** The Recipe's group. */
	public String group;
	/** The Items forming the Recipe. */
	private ItemStack[] recipe;
	/** The Recipe's type.
	 * 
	 * @see Recipe#SHAPED */
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
		if (properties.isTrue("new"))
		{
			String name = Dialogs.showInputDialog(new Text("objects.name").toString());
			if (name != null) this.setCustomName(name);
			else return null;
			CommandGenerator.window.panelRecipeSelection.list.add(this);
		}
		CommandGenerator.stateManager.clear();
		return new PanelRecipe(this);
	}

	@Override
	public Recipe duplicate(Recipe object)
	{
		this.group = object.group;
		this.type = object.type;
		this.recipe = new ItemStack[object.recipe.length];
		for (int i = 0; i < this.recipe.length; ++i)
			this.recipe[i] = new ItemStack().duplicate(object.recipe[i]);
		return this;
	}

	@Override
	public Recipe fromNBT(TagCompound nbt)
	{
		if (nbt.hasTag(Tags.RECIPE_TYPE) && nbt.getTag(Tags.RECIPE_TYPE).value().equals("crafting_shapeless")) return this.fromNBTShapeless(nbt);
		return this.fromNBTShaped(nbt);
	}

	/** Creates a Shaped Recipe from the input NBT Tag.
	 * 
	 * @param nbt - The NBT Tag describing the Recipe.
	 * @return The created Recipe. */
	private Recipe fromNBTShaped(TagCompound nbt)
	{
		this.type = SHAPED;
		if (nbt.hasTag(Tags.RECIPE_GROUP)) this.group = nbt.getTag(Tags.RECIPE_GROUP).value();
		if (nbt.hasTag(Tags.RECIPE_RESULT))
		{
			this.recipe[9] = new ItemStack().fromNBTForRecipe(nbt.getTag(Tags.RECIPE_RESULT));
			this.recipe[9].slot = 9;
		}

		HashMap<Character, ItemStack> affectations = new HashMap<Character, ItemStack>();
		if (nbt.hasTag(Tags.RECIPE_KEY)) for (Tag t : nbt.getTag(Tags.RECIPE_KEY).value())
			affectations.put(t.id().charAt(0), new ItemStack().fromNBTForRecipe((TagCompound) t));

		String pattern = "";
		if (nbt.hasTag(Tags.RECIPE_KEY))
		{
			for (Tag t : nbt.getTag(Tags.RECIPE_PATTERN).value())
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
				this.recipe[i] = affectations.get(pattern.charAt(i)).clone();
				this.recipe[i].slot = i;
			} catch (CloneNotSupportedException e)
			{
				e.printStackTrace();
			}

		return this;
	}

	/** Creates a Shapeless Recipe from the input NBT Tag.
	 * 
	 * @param nbt - The NBT Tag describing the Recipe.
	 * @return The created Recipe. */
	private Recipe fromNBTShapeless(TagCompound nbt)
	{
		this.type = SHAPELESS;
		if (nbt.hasTag(Tags.RECIPE_GROUP)) this.group = nbt.getTag(Tags.RECIPE_GROUP).value();
		if (nbt.hasTag(Tags.RECIPE_RESULT))
		{
			this.recipe[9] = new ItemStack().fromNBTForRecipe(nbt.getTag(Tags.RECIPE_RESULT));
			this.recipe[9].slot = 9;
		}
		int i = 0;
		if (nbt.hasTag(Tags.RECIPE_INGREDIENTS)) for (Tag t : nbt.getTag(Tags.RECIPE_INGREDIENTS).value())
		{
			if (i > 8) break;
			this.recipe[i] = new ItemStack().fromNBTForRecipe((TagCompound) t);
			this.recipe[i].slot = i;
		}
		return this;
	}

	@Override
	public Recipe fromXML(Element xml)
	{
		this.type = Byte.parseByte(xml.getAttributeValue("type"));
		if (xml.getChild("group") != null) this.group = xml.getChildText("group");
		for (Element item : xml.getChildren("item"))
		{
			int pos = Integer.parseInt(item.getAttributeValue("position"));
			this.recipe[pos] = new ItemStack().fromXML(item);
			this.recipe[pos].slot = pos;
		}
		this.findProperties(xml);
		return this;
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

	/** Getter for {@link Recipe#recipe}. */
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

	/** @return <code>true</code> if this Recipe is valid, i.e. if it has at least one Item. */
	public boolean isValid()
	{
		if (this.recipe[9] == null) return false;
		for (int i = 0; i < 9; ++i)
			if (this.recipe[i] != null) return true;
		return false;
	}

	/** Changes an Item in this Recipe.
	 * 
	 * @param position - The index to put the Item at.
	 * @param item - The Item to put. */
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

	/** Converts this Shaped Recipe to a NBT Tag.
	 * 
	 * @param container - The template for the container Tag.
	 * @return The Compound container tag. */
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

		String[] pattern = reducePattern(output);
		ArrayList<TagString> o = new ArrayList<TagString>();
		for (String string : pattern)
			o.add(Tags.DEFAULT_STRING.create(string));

		ArrayList<Tag> tags = new ArrayList<Tag>();
		tags.add(Tags.RECIPE_TYPE.create("crafting_shaped"));
		if (this.group != null) tags.add(Tags.RECIPE_GROUP.create(this.group));
		tags.add(Tags.RECIPE_KEY.create(key.toArray(new TagCompound[key.size()])));
		tags.add(Tags.RECIPE_PATTERN.create(o.toArray(new TagString[o.size()])));
		tags.add(this.recipe[9].toTagForRecipe(Tags.RECIPE_RESULT));

		return container.create(tags.toArray(new Tag[tags.size()])).setJson(true);
	}

	/** Converts this Shapeless Recipe to a NBT Tag.
	 * 
	 * @param container - The template for the container Tag.
	 * @return The Compound container tag. */
	private TagCompound shapelessToTag(TemplateCompound container)
	{
		ArrayList<TagCompound> items = new ArrayList<TagCompound>();
		for (int i = 0; i < 9; ++i)
			if (this.recipe[i] != null) items.add(this.recipe[i].toTagForRecipe(container));
		TagList ingredients = Tags.RECIPE_INGREDIENTS.create(items.toArray(new TagCompound[items.size()]));
		ArrayList<Tag> tags = new ArrayList<Tag>();
		tags.add(Tags.RECIPE_TYPE.create("crafting_shapeless"));
		if (this.group != null) tags.add(Tags.RECIPE_GROUP.create(this.group));
		tags.add(ingredients);
		tags.add(this.recipe[9].toTagForRecipe(Tags.RECIPE_RESULT));
		return container.create(tags.toArray(new Tag[tags.size()])).setJson(true);
	}

	@Override
	public String toCommand()
	{
		return this.toNBT(Tags.DEFAULT_COMPOUND).valueForCommand(0);
	}

	@Override
	public TagCompound toNBT(TemplateCompound container)
	{
		if (this.type == SHAPELESS) return this.shapelessToTag(container);
		return this.shapedToTag(container);
	}

	@Override
	public String toString()
	{
		return this.customName();
	}

	@Override
	public Element toXML()
	{
		Element root = this.createRoot("recipe").setAttribute("type", Byte.toString(this.type));
		if (this.group != null) root.addContent(new Element("group").setText(this.group));
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
