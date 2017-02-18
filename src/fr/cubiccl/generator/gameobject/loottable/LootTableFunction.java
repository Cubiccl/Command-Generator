package fr.cubiccl.generator.gameobject.loottable;

import java.awt.Component;
import java.util.ArrayList;

import fr.cubi.cubigui.CTextArea;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.tags.TagList;
import fr.cubiccl.generator.gameobject.tags.TagString;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
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
	public LootTableFunction setupFrom(CGPanel panel) throws CommandGenerationException
	{
		LootTableFunction f = ((PanelFunction) panel).generate();
		this.conditions = f.conditions;
		this.function = f.function;
		this.tags = f.tags;
		return this;
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
		output[0] = new TagString(Tags.LOOTTABLE_FUNCTION_NAME, this.function.name);
		output[1] = new TagList(Tags.LOOTTABLE_CONDITIONS, con);
		for (int i = 0; i < this.tags.length; ++i)
			output[i + 2] = this.tags[i];

		return new TagCompound(container, output);
	}
}
