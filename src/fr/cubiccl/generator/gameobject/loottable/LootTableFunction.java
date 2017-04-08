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
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gameobject.templatetags.TemplateList;
import fr.cubiccl.generator.gui.component.interfaces.IObjectList;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.loottable.PanelFunction;
import fr.cubiccl.generator.gui.component.panel.utils.ListProperties;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Replacement;
import fr.cubiccl.generator.utils.Text;

public class LootTableFunction implements IObjectList<LootTableFunction>
{
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

		public static Function get(String name)
		{
			for (Function f : values())
				if (f.name.equals(name)) return f;
			return null;
		}

		public final String name;
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

		public Text translate()
		{
			return new Text("lt_function." + this.name);
		}
	}

	public static LootTableFunction createFrom(Element function)
	{
		LootTableFunction f = new LootTableFunction();
		f.function = Function.get(function.getChildText("id"));
		f.tags = ((TagCompound) NBTReader.read(function.getChildText("nbt"), true, false, true)).value();

		ArrayList<LootTableCondition> conditions = new ArrayList<LootTableCondition>();
		for (Element condition : function.getChild("conditions").getChildren())
			conditions.add(LootTableCondition.createFrom(condition));
		f.conditions = conditions.toArray(new LootTableCondition[conditions.size()]);

		return f;
	}

	public static LootTableFunction createFrom(TagCompound tag)
	{
		ArrayList<Tag> tags = new ArrayList<Tag>();
		ArrayList<LootTableCondition> conditions = new ArrayList<LootTableCondition>();
		Function f = null;

		for (Tag t : tag.value())
		{
			if (t.id().equals(Tags.LOOTTABLE_FUNCTION_NAME.id())) f = Function.get(((TagString) t).value());
			else if (t.id().equals(Tags.LOOTTABLE_CONDITIONS.id()))
			{
				for (Tag con : ((TagList) t).value())
				{
					LootTableCondition c = LootTableCondition.createFrom((TagCompound) con);
					if (c != null) conditions.add(c);
				}
			} else tags.add(t);
		}

		if (f == null) return null;
		return new LootTableFunction(f, conditions.toArray(new LootTableCondition[conditions.size()]), tags.toArray(new Tag[tags.size()]));
	}

	public LootTableCondition[] conditions;
	public Function function;
	public Tag[] tags;

	public LootTableFunction()
	{
		this(Function.values()[0], new LootTableCondition[0], new Tag[0]);
	}

	public LootTableFunction(Function function, LootTableCondition[] conditions, Tag[] tags)
	{
		this.function = function;
		this.conditions = conditions;
		this.tags = tags;
	}

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
		} else if (this.function == Function.SET_COUNT) for (Tag t : this.tags)
		{// TODO useTestValue
			if (t.template == Tags.LT_FUNCTION_COUNT)
			{
				item.amount = ((TagNumber) t).valueInt();
				break;
			} else if (t.template == Tags.LT_FUNCTION_COUNT_RANGE)
			{
				int min = ((TagNumber) ((TagCompound) t).getTag(Tags.VALUE_MIN)).valueInt();
				int max = ((TagNumber) ((TagCompound) t).getTag(Tags.VALUE_MAX)).valueInt();
				item.amount = new Random().nextInt(max - min) + min;
				break;
			}
		}
		else if (this.function == Function.SET_DAMAGE) for (Tag t : this.tags)
		{// TODO useTestValue
			if (t.template == Tags.LT_FUNCTION_DAMAGE)
			{
				item.setDamage((int) (((TagNumber) t).value() * item.getItem().getDurability()));
				break;
			} else if (t.template == Tags.LT_FUNCTION_DAMAGE_RANGE)
			{
				double min = ((TagNumber) ((TagCompound) t).getTag(Tags.VALUE_MIN_FLOAT)).value();
				double max = ((TagNumber) ((TagCompound) t).getTag(Tags.VALUE_MAX_FLOAT)).value();
				item.setDamage((int) ((new Random().nextDouble() * (max - min) + min) * item.getItem().getDurability()));
				break;
			}
		}
		else if (this.function == Function.SET_DATA) for (Tag t : this.tags)
		{// TODO useTestValue
			if (t.template == Tags.LT_FUNCTION_DATA)
			{
				item.setDamage(((TagNumber) t).valueInt());
				break;
			} else if (t.template == Tags.LT_FUNCTION_DATA_RANGE)
			{
				int min = ((TagNumber) ((TagCompound) t).getTag(Tags.VALUE_MIN)).valueInt();
				int max = ((TagNumber) ((TagCompound) t).getTag(Tags.VALUE_MAX)).valueInt();
				item.setDamage(new Random().nextInt(max - min) + min);
				break;
			}
		}
		else if (this.function == Function.SET_NBT)
		{
			String tag = null;
			for (Tag t : this.tags)
				if (t.template == Tags.LT_FUNCTION_NBT) tag = ((TagString) t).value();
			if (tag == null) return;

			TagCompound t = (TagCompound) NBTReader.read(tag, false, false);
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

	public Element toXML()
	{
		Element conditions = new Element("conditions");
		for (LootTableCondition condition : this.conditions)
			conditions.addContent(condition.toXML());

		Element root = new Element("pool");
		root.addContent(new Element("id").setText(this.function.name));
		root.addContent(conditions);
		root.addContent(new Element("nbt").setText(Tags.DEFAULT_COMPOUND.create(this.tags).valueForCommand()));
		return root;
	}

	@Override
	public LootTableFunction update(CGPanel panel) throws CommandGenerationException
	{
		LootTableFunction f = ((PanelFunction) panel).generate();
		this.conditions = f.conditions;
		this.function = f.function;
		this.tags = f.tags;
		return this;
	}

	public boolean verifyConditions()
	{
		for (LootTableCondition condition : this.conditions)
			if (!condition.verify()) return false;
		return true;
	}
}
