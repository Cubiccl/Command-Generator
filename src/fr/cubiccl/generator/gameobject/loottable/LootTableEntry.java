package fr.cubiccl.generator.gameobject.loottable;

import fr.cubiccl.generator.gameobject.tags.*;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;

public class LootTableEntry
{
	public static final byte ITEM = 0, LOOT_TABLE = 1, EMPTY = 2;
	public static final String[] TYPES =
	{ "item", "loot_table", "empty" };

	protected final LootTableCondition[] conditions;
	protected final LootTableFunction[] functions;
	public final String name;
	public final byte type;
	public final int weight, quality;

	public LootTableEntry(LootTableCondition[] conditions, byte type, String name, LootTableFunction[] functions, int weight, int quality)
	{
		this.conditions = conditions;
		this.type = type;
		this.name = name;
		this.functions = functions;
		this.weight = weight;
		this.quality = quality;
	}

	public TagCompound toTag(TemplateCompound container)
	{
		Tag[] con = new Tag[this.conditions.length];
		for (int i = 0; i < con.length; ++i)
			con[i] = this.conditions[i].toTag(Tags.DEFAULT_COMPOUND);

		Tag[] fun = new Tag[this.functions.length];
		for (int i = 0; i < fun.length; ++i)
			fun[i] = this.functions[i].toTag(Tags.DEFAULT_COMPOUND);

		return new TagCompound(container, new TagList(Tags.LOOTTABLE_CONDITIONS, con), new TagString(Tags.LOOTTABLE_ENTRY_TYPE, TYPES[this.type]),
				new TagString(Tags.LOOTTABLE_ENTRY_NAME, this.name), new TagList(Tags.LOOTTABLE_FUNCTIONS, fun), new TagNumber(Tags.LOOTTABLE_ENTRY_WEIGHT,
						this.weight), new TagNumber(Tags.LOOTTABLE_ENTRY_QUALITY, this.quality));
	}
}
