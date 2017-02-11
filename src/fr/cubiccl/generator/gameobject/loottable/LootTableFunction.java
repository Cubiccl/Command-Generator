package fr.cubiccl.generator.gameobject.loottable;

import java.util.ArrayList;

import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.tags.TagList;
import fr.cubiccl.generator.gameobject.tags.TagString;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;

public class LootTableFunction
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

	protected final LootTableCondition[] conditions;
	public final Function function;
	protected final Tag[] tags;

	public LootTableFunction(Function function, LootTableCondition[] conditions, Tag[] tags)
	{
		this.function = function;
		this.conditions = conditions;
		this.tags = tags;
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