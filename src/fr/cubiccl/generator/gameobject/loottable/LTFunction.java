package fr.cubiccl.generator.gameobject.loottable;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Random;

import org.jdom2.Element;

import fr.cubi.cubigui.CTextArea;
import fr.cubiccl.generator.gameobject.ItemStack;
import fr.cubiccl.generator.gameobject.baseobjects.EnchantmentType;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gameobject.tags.*;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TagsMain;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gameobject.templatetags.TemplateList;
import fr.cubiccl.generator.gameobject.utils.TestValue;
import fr.cubiccl.generator.gui.component.interfaces.IObjectList;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.loottable.PanelFunction;
import fr.cubiccl.generator.gui.component.panel.utils.ListProperties;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Replacement;
import fr.cubiccl.generator.utils.Text;

/** Represents a Function for Loot Tables. */
public class LTFunction implements IObjectList<LTFunction>
{
	/** Function types. */
	public static enum Function
	{
		ENCHANT_RANDOMLY("enchant_randomly", 7),
		ENCHANT_WITH_LEVELS("enchant_with_levels", 6),
		FURNACE_SMELT("furnace_smelt", 8),
		LOOTING_ENCHANT("looting_enchant", 3),
		SET_ATTRIBUTES("set_attributes", 5),
		SET_COUNT("set_count", 4),
		SET_DAMAGE("set_damage", 2),
		SET_DATA("set_data", 1),
		SET_NBT("set_nbt", 0);

		/** @param name - Name of the Function.
		 * @return The Function with the input name. */
		public static Function find(String name)
		{
			for (Function f : values())
				if (f.name.equals(name)) return f;
			return null;
		}

		/** This Function's name. */
		public final String name;
		/** This Function's priority compared to others. */
		public final int priority;

		private Function(String name, int priority)
		{
			this.name = name;
			this.priority = priority;
		}

		public int compareToFunction(Function anotherFunction)
		{
			return this.priority - anotherFunction.priority;
		}

		/** @return This Function's translated name. */
		public Text translate()
		{
			return new Text("lt_function." + this.name);
		}
	}

	/** Creates a Loot Table Function from the input XML element.
	 * 
	 * @param function - The XML element describing the Loot Table Function.
	 * @return The created Loot Table Function. */
	public static LTFunction createFrom(Element function)
	{
		LTFunction f = new LTFunction();
		f.function = Function.find(function.getChildText("id"));
		f.tags = ((TagCompound) NBTParser.parse(function.getChildText("nbt"), true, false, true)).value();

		ArrayList<LTCondition> conditions = new ArrayList<LTCondition>();
		for (Element condition : function.getChild("conditions").getChildren())
			conditions.add(LTCondition.createFrom(condition));
		f.conditions = conditions.toArray(new LTCondition[conditions.size()]);

		return f;
	}

	/** Creates a Loot Table Function from the input NBT Tag.
	 * 
	 * @param tag - The NBT Tag describing the Loot Table Function.
	 * @return The created Loot Table Function. */
	public static LTFunction createFrom(TagCompound tag)
	{
		ArrayList<Tag> tags = new ArrayList<Tag>();
		ArrayList<LTCondition> conditions = new ArrayList<LTCondition>();
		Function f = null;

		for (Tag t : tag.value())
		{
			if (t.id().equals(Tags.LOOTTABLE_FUNCTION_NAME.id())) f = Function.find(((TagString) t).value());
			else if (t.id().equals(Tags.LOOTTABLE_CONDITIONS.id()))
			{
				for (Tag con : ((TagList) t).value())
				{
					LTCondition c = LTCondition.createFrom((TagCompound) con);
					if (c != null) conditions.add(c);
				}
			} else tags.add(t);
		}

		if (f == null) return null;
		return new LTFunction(f, conditions.toArray(new LTCondition[conditions.size()]), tags.toArray(new Tag[tags.size()]));
	}

	/** This Function's Conditions. */
	public LTCondition[] conditions;
	/** This Function's {@link Function type}. */
	public Function function;
	/** The NBT Tags describing this Function. */
	public Tag[] tags;

	public LTFunction()
	{
		this(Function.values()[0], new LTCondition[0], new Tag[0]);
	}

	public LTFunction(Function function, LTCondition[] conditions, Tag[] tags)
	{
		this.function = function;
		this.conditions = conditions;
		this.tags = tags;
	}

	/** Applies this Function to the input Item.
	 * 
	 * @param item - The Item to apply this Function to. */
	public void applyTo(ItemStack item)
	{
		if (this.function == Function.ENCHANT_RANDOMLY)
		{
			EnchantmentType[] enchants = ObjectRegistry.enchantments.list();
			for (Tag t : this.tags)
				if (t.template == Tags.LT_FUNCTION_ENCHANTMENTS)
				{
					enchants = new EnchantmentType[((TagList) t).size()];
					for (int i = 0; i < ((TagList) t).size(); ++i)
						enchants[i] = ObjectRegistry.enchantments.find(((TagString) ((TagList) t).getTag(i)).value());
					break;
				}
			item.enchant(enchants[new Random().nextInt(enchants.length)]);
		} else if (this.function == Function.FURNACE_SMELT && item.getItem().cooksTo != null && ObjectRegistry.items.find(item.getItem().cooksTo) != null) item
				.setItem(ObjectRegistry.items.find(item.getItem().cooksTo));
		else if (this.function == Function.SET_ATTRIBUTES)
		{
			Tag[] modifiers = null;
			for (Tag t : this.tags)
				if (t.id().equals("modifiers")) modifiers = ((TagList) t).value();

			if (modifiers == null) return;
			if (item.getNbt().hasTag("AttributeModifiers"))
			{
				TagList list = (TagList) item.getNbt().getTagFromId("AttributeModifiers");
				for (Tag tag : modifiers)
					list.addTag(tag);
			} else item.getNbt().addTag(((TemplateList) ObjectRegistry.itemTags.find("AttributeModifiers")).create(modifiers));
		} else if (this.function == Function.SET_COUNT)
		{
			TestValue v = new TestValue(Tags.LT_FUNCTION_COUNT, Tags.LT_FUNCTION_COUNT_RANGE);
			v.findValue(this.tags);
			item.amount = (int) v.generateValue();
		} else if (this.function == Function.SET_DAMAGE)
		{
			TestValue v = new TestValue(Tags.LT_FUNCTION_DAMAGE, Tags.LT_FUNCTION_DAMAGE_RANGE, TagsMain.VALUE_MIN_FLOAT, TagsMain.VALUE_MAX_FLOAT);
			v.findValue(this.tags);
			item.setDamage((int) v.generateValue());
		} else if (this.function == Function.SET_DATA)
		{
			TestValue v = new TestValue(Tags.LT_FUNCTION_DATA, Tags.LT_FUNCTION_DATA_RANGE);
			v.findValue(this.tags);
			item.setDamage((int) v.generateValue());
		} else if (this.function == Function.SET_NBT)
		{
			String tag = null;
			for (Tag t : this.tags)
				if (t.template == Tags.LT_FUNCTION_NBT) tag = ((TagString) t).value();
			if (tag == null) return;

			TagCompound t = (TagCompound) NBTParser.parse(tag, false, false);
			for (Tag nbt : t.value())
				item.getNbt().addTag(nbt);
		}
	}

	@Override
	public CGPanel createPanel(ListProperties properties)
	{
		PanelFunction p = new PanelFunction();
		p.setupFrom(this);
		return p;
	}

	@Override
	public Component getDisplayComponent()
	{
		return new CTextArea(this.toString());
	}

	@Override
	public String getName(int index)
	{
		return new Text("loottable.function", new Replacement("<index>", Integer.toString(index + 1))).toString();
	}

	@Override
	public String toString()
	{
		String display = this.function.translate().toString();
		for (Tag tag : this.tags)
			if (!tag.id().equals(Tags.LT_FUNCTION_MODIFIERS.id())) display += ",\n" + tag.toCommand(-1);
		return display;
	}

	/** @param container - The Compound to store this Function in.
	 * @return This Function as an NBT Tag to be generated. */
	public TagCompound toTag(TemplateCompound container)
	{
		Tag[] con = new Tag[this.conditions.length];
		for (int i = 0; i < con.length; ++i)
			con[i] = this.conditions[i].toTag(Tags.DEFAULT_COMPOUND);

		Tag[] output = new Tag[this.tags.length + 2];
		output[0] = Tags.LOOTTABLE_FUNCTION_NAME.create(this.function.name);
		output[1] = Tags.LOOTTABLE_CONDITIONS.create(con);
		for (int i = 0; i < this.tags.length; ++i)
			output[i + 2] = this.tags[i];

		return container.create(output);
	}

	/** @return This Function as an XML element to be stored. */
	public Element toXML()
	{
		Element conditions = new Element("conditions");
		for (LTCondition condition : this.conditions)
			conditions.addContent(condition.toXML());

		Element root = new Element("pool");
		root.addContent(new Element("id").setText(this.function.name));
		root.addContent(conditions);
		root.addContent(new Element("nbt").setText(Tags.DEFAULT_COMPOUND.create(this.tags).valueForCommand()));
		return root;
	}

	@Override
	public LTFunction update(CGPanel panel) throws CommandGenerationException
	{
		LTFunction f = ((PanelFunction) panel).generate();
		this.conditions = f.conditions;
		this.function = f.function;
		this.tags = f.tags;
		return this;
	}

	/** @return <code>true</code> if this Function's Conditions are verified. */
	public boolean verifyConditions()
	{
		for (LTCondition condition : this.conditions)
			if (!condition.verify()) return false;
		return true;
	}
}
