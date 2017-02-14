package fr.cubiccl.generator.gameobject.loottable;

import java.util.ArrayList;

import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.tags.TagString;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.utils.Text;

public class LootTableCondition
{

	public static enum Condition
	{
		ENTITY_PROPERTIES("entity_properties"),
		ENTITY_SCORES("entity_scores"),
		KILLED_BY_PLAYER("killed_by_player"),
		RANDOM_CHANCE("random_chance"),
		RANDOM_CHANCE_WITH_LOOTING("random_chance_with_looting");

		public static Condition get(String name)
		{
			for (Condition c : values())
				if (c.name.equals(name)) return c;
			return null;
		}

		public final String name;

		private Condition(String name)
		{
			this.name = name;
		}

		public Text translate()
		{
			return new Text("lt_condition." + this.name);
		}
	}

	public static LootTableCondition createFrom(TagCompound tag)
	{
		ArrayList<Tag> tags = new ArrayList<Tag>();
		Condition c = null;

		for (Tag t : tag.value())
		{
			if (t.id().equals(Tags.LOOTTABLE_CONDITION.id())) c = Condition.get(((TagString) t).value());
			else tags.add(t);
		}

		if (c == null) return null;
		return new LootTableCondition(c, tags.toArray(new Tag[tags.size()]));
	}

	public final Condition condition;
	public final Tag[] tags;

	public LootTableCondition(Condition condition, Tag[] tags)
	{
		this.condition = condition;
		this.tags = tags;
	}

	@Override
	public String toString()
	{
		String display = this.condition.translate().toString();
		for (Tag tag : this.tags)
			display += ",\n" + tag.toCommand();
		return display;
	}

	public TagCompound toTag(TemplateCompound container)
	{
		Tag[] output = new Tag[this.tags.length + 1];
		output[0] = new TagString(Tags.LOOTTABLE_CONDITION, this.condition.name);

		for (int i = 0; i < this.tags.length; ++i)
			output[i + 1] = this.tags[i];

		return new TagCompound(container, output);
	}

}
