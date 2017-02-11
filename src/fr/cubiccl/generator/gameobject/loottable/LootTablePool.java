package fr.cubiccl.generator.gameobject.loottable;

import java.util.ArrayList;

import fr.cubiccl.generator.gameobject.tags.*;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;

public class LootTablePool
{

	public final float bonusRollsMin, bonusRollsMax;
	protected final LootTableCondition[] conditions;
	protected final LootTableEntry[] entries;
	public final int rollsMin, rollsMax;

	public LootTablePool(LootTableCondition[] conditions, int rolls, float bonusRolls, LootTableEntry[] entries)
	{
		this(conditions, rolls, -1, bonusRolls, -1, entries);
	}

	public LootTablePool(LootTableCondition[] conditions, int rollsMin, int rollsMax, float bonusRollsMin, float bonusRollsMax, LootTableEntry[] entries)
	{
		this.conditions = conditions;
		this.rollsMin = rollsMin;
		this.rollsMax = rollsMax;
		this.bonusRollsMin = bonusRollsMin;
		this.bonusRollsMax = bonusRollsMax;
		this.entries = entries;
	}

	public TagCompound toTag(TemplateCompound container)
	{
		ArrayList<Tag> tags = new ArrayList<Tag>();

		if (this.rollsMax == -1) tags.add(new TagNumber(Tags.LOOTTABLE_ROLLS, this.rollsMin));
		else tags.add(new TagCompound(Tags.LOOTTABLE_BONUS_ROLLS_RANGE, new TagNumber(Tags.LOOTTABLE_ROLLS_MIN, this.rollsMin), new TagNumber(
				Tags.LOOTTABLE_ROLLS_MAX, this.rollsMax)));

		if (this.bonusRollsMax == -1) tags.add(new TagBigNumber(Tags.LOOTTABLE_BONUS_ROLLS, this.bonusRollsMin));
		else tags.add(new TagCompound(Tags.LOOTTABLE_ROLLS_RANGE, new TagBigNumber(Tags.LOOTTABLE_BONUS_ROLLS_MIN, this.bonusRollsMin), new TagBigNumber(
				Tags.LOOTTABLE_BONUS_ROLLS_MAX, this.bonusRollsMax)));

		Tag[] con = new Tag[this.conditions.length];
		for (int i = 0; i < con.length; ++i)
			con[i] = this.conditions[i].toTag(Tags.DEFAULT_COMPOUND);
		tags.add(new TagList(Tags.LOOTTABLE_CONDITIONS, con));

		Tag[] ent = new Tag[this.entries.length];
		for (int i = 0; i < ent.length; ++i)
			ent[i] = this.entries[i].toTag(Tags.DEFAULT_COMPOUND);
		tags.add(new TagList(Tags.LOOTTABLE_ENTRIES, ent));

		return new TagCompound(container, tags.toArray(new Tag[tags.size()]));
	}
}
