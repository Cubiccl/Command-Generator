package fr.cubiccl.generator.gameobject.loottable;

import java.awt.Component;
import java.util.ArrayList;

import fr.cubiccl.generator.gameobject.tags.*;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gui.component.interfaces.IObjectList;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.loottable.PanelPool;
import fr.cubiccl.generator.gui.component.panel.utils.ListProperties;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Replacement;
import fr.cubiccl.generator.utils.Text;

public class LootTablePool implements IObjectList<LootTablePool>
{

	public static LootTablePool createFrom(TagCompound tag)
	{
		float bonusMin = 0, bonusMax = -1;
		int min = 0, max = -1;
		ArrayList<LootTableCondition> conditions = new ArrayList<LootTableCondition>();
		ArrayList<LootTableEntry> entries = new ArrayList<LootTableEntry>();

		if (tag.hasTag(Tags.LOOTTABLE_ROLLS)) min = ((TagNumber) tag.getTag(Tags.LOOTTABLE_ROLLS)).value();
		if (tag.hasTag(Tags.LOOTTABLE_ROLLS_RANGE))
		{
			TagCompound t = (TagCompound) tag.getTag(Tags.LOOTTABLE_ROLLS_RANGE);
			if (t.hasTag(Tags.LOOTTABLE_ROLLS_MIN)) min = ((TagNumber) t.getTag(Tags.LOOTTABLE_ROLLS_MIN)).value();
			if (t.hasTag(Tags.LOOTTABLE_ROLLS_MAX)) max = ((TagNumber) t.getTag(Tags.LOOTTABLE_ROLLS_MAX)).value();
		}
		if (tag.hasTag(Tags.LOOTTABLE_BONUS_ROLLS)) bonusMin = (float) (double) ((TagBigNumber) tag.getTag(Tags.LOOTTABLE_BONUS_ROLLS)).value();
		if (tag.hasTag(Tags.LOOTTABLE_BONUS_ROLLS_RANGE))
		{
			TagCompound t = (TagCompound) tag.getTag(Tags.LOOTTABLE_BONUS_ROLLS_RANGE);
			if (t.hasTag(Tags.LOOTTABLE_BONUS_ROLLS_MIN)) bonusMin = (float) (double) ((TagBigNumber) t.getTag(Tags.LOOTTABLE_BONUS_ROLLS_MIN)).value();
			if (t.hasTag(Tags.LOOTTABLE_BONUS_ROLLS_MAX)) bonusMax = (float) (double) ((TagBigNumber) t.getTag(Tags.LOOTTABLE_BONUS_ROLLS_MAX)).value();
		}
		if (tag.hasTag(Tags.LOOTTABLE_CONDITIONS))
		{
			TagList t = (TagList) tag.getTag(Tags.LOOTTABLE_CONDITIONS);
			for (Tag con : t.value())
			{
				LootTableCondition c = LootTableCondition.createFrom((TagCompound) con);
				if (c != null) conditions.add(c);
			}
		}
		if (tag.hasTag(Tags.LOOTTABLE_ENTRIES))
		{
			TagList t = (TagList) tag.getTag(Tags.LOOTTABLE_ENTRIES);
			for (Tag ent : t.value())
			{
				LootTableEntry e = LootTableEntry.createFrom((TagCompound) ent);
				if (e != null) entries.add(e);
			}
		}

		return new LootTablePool(conditions.toArray(new LootTableCondition[conditions.size()]), min, max, bonusMin, bonusMax,
				entries.toArray(new LootTableEntry[entries.size()]));
	}

	public double bonusRollsMin, bonusRollsMax;
	public LootTableCondition[] conditions;
	public LootTableEntry[] entries;
	public int rollsMin, rollsMax;

	public LootTablePool()
	{
		this(new LootTableCondition[0], 0, 0, new LootTableEntry[0]);
	}

	public LootTablePool(LootTableCondition[] conditions, int rolls, double bonusRolls, LootTableEntry[] entries)
	{
		this(conditions, rolls, -1, bonusRolls, -1, entries);
	}

	public LootTablePool(LootTableCondition[] conditions, int rollsMin, int rollsMax, double bonusRollsMin, double bonusRollsMax, LootTableEntry[] entries)
	{
		this.conditions = conditions;
		this.rollsMin = rollsMin;
		this.rollsMax = rollsMax;
		this.bonusRollsMin = bonusRollsMin;
		this.bonusRollsMax = bonusRollsMax;
		this.entries = entries;
	}

	@Override
	public CGPanel createPanel(ListProperties properties)
	{
		PanelPool p = new PanelPool();
		p.setupFrom(this);
		return p;
	}

	@Override
	public Component getDisplayComponent()
	{
		return new CGLabel(new Text(this.toString(), false));
	}

	@Override
	public String getName(int index)
	{
		return new Text("loottable.pool", new Replacement("<index>", Integer.toString(index + 1))).toString();
	}

	@Override
	public LootTablePool setupFrom(CGPanel panel) throws CommandGenerationException
	{
		return ((PanelPool) panel).generatePool();
	}

	@Override
	public String toString()
	{
		String rolls = Integer.toString(this.rollsMin);
		if (this.rollsMax != -1) rolls = this.rollsMin + "-" + this.rollsMax;
		return new Text("loottable.pool.tostring", new Replacement("<items>", Integer.toString(this.entries.length)), new Replacement("<conditions>",
				Integer.toString(this.conditions.length)), new Replacement("<rolls>", rolls)).toString();
	}

	public TagCompound toTag(TemplateCompound container)
	{
		ArrayList<Tag> tags = new ArrayList<Tag>();

		if (this.rollsMax == -1) tags.add(new TagNumber(Tags.LOOTTABLE_ROLLS, this.rollsMin));
		else tags.add(new TagCompound(Tags.LOOTTABLE_ROLLS_RANGE, new TagNumber(Tags.LOOTTABLE_ROLLS_MIN, this.rollsMin), new TagNumber(
				Tags.LOOTTABLE_ROLLS_MAX, this.rollsMax)));

		if (this.bonusRollsMax == -1) tags.add(new TagBigNumber(Tags.LOOTTABLE_BONUS_ROLLS, this.bonusRollsMin));
		else tags.add(new TagCompound(Tags.LOOTTABLE_BONUS_ROLLS_RANGE, new TagBigNumber(Tags.LOOTTABLE_BONUS_ROLLS_MIN, this.bonusRollsMin), new TagBigNumber(
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
