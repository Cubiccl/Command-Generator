package fr.cubiccl.generator.gameobject.loottable;

import java.awt.Component;
import java.util.*;

import org.jdom2.Element;

import fr.cubiccl.generator.gameobject.ItemStack;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.tags.TagList;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TagsMain;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gameobject.utils.TestValue;
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

	public static LootTablePool createFrom(Element pool)
	{
		LootTablePool p = new LootTablePool();

		ArrayList<LootTableCondition> conditions = new ArrayList<LootTableCondition>();
		for (Element condition : pool.getChild("conditions").getChildren())
			conditions.add(LootTableCondition.createFrom(condition));
		p.conditions = conditions.toArray(new LootTableCondition[conditions.size()]);

		ArrayList<LootTableEntry> entries = new ArrayList<LootTableEntry>();
		for (Element entry : pool.getChild("entries").getChildren())
			entries.add(LootTableEntry.createFrom(entry));
		p.entries = entries.toArray(new LootTableEntry[entries.size()]);

		if (pool.getChild("rolls") != null) p.rollsMin = Integer.parseInt(pool.getChildText("rolls"));
		else
		{
			p.rollsMin = Integer.parseInt(pool.getChildText("minrolls"));
			p.rollsMax = Integer.parseInt(pool.getChildText("maxrolls"));
		}
		if (pool.getChild("bonusrolls") != null) p.bonusRollsMin = Double.parseDouble(pool.getChildText("bonusrolls"));
		else
		{
			p.bonusRollsMin = Double.parseDouble(pool.getChildText("minbonusrolls"));
			p.bonusRollsMax = Double.parseDouble(pool.getChildText("maxbonusrolls"));
		}

		return p;
	}

	public static LootTablePool createFrom(TagCompound tag)
	{
		float bonusMin = 0, bonusMax = -1;
		int min = 0, max = -1;
		ArrayList<LootTableCondition> conditions = new ArrayList<LootTableCondition>();
		ArrayList<LootTableEntry> entries = new ArrayList<LootTableEntry>();

		TestValue v = new TestValue(Tags.LOOTTABLE_ROLLS, Tags.LOOTTABLE_ROLLS_RANGE);
		v.findValue(tag);
		min = (int) v.valueMin;
		if (v.isRanged) max = (int) v.valueMax;

		v = new TestValue(Tags.LOOTTABLE_BONUS_ROLLS, Tags.LOOTTABLE_BONUS_ROLLS_RANGE, TagsMain.VALUE_MIN_FLOAT, TagsMain.VALUE_MAX_FLOAT);
		v.findValue(tag);
		bonusMin = (float) v.valueMin;
		if (v.isRanged) bonusMax = (float) v.valueMax;

		if (tag.hasTag(Tags.LOOTTABLE_CONDITIONS))
		{
			TagList t = tag.getTag(Tags.LOOTTABLE_CONDITIONS);
			for (Tag con : t.value())
			{
				LootTableCondition c = LootTableCondition.createFrom((TagCompound) con);
				if (c != null) conditions.add(c);
			}
		}
		if (tag.hasTag(Tags.LOOTTABLE_ENTRIES))
		{
			TagList t = tag.getTag(Tags.LOOTTABLE_ENTRIES);
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

	public Set<ItemStack> generateItems()
	{
		HashSet<ItemStack> items = new HashSet<ItemStack>();

		int rolls = this.rollsMin;
		if (this.rollsMax != -1) rolls = new Random().nextInt(this.rollsMax - this.rollsMin + 1) + this.rollsMin;

		ArrayList<LootTableEntry> entries = new ArrayList<LootTableEntry>(); // Entries sorted by weight
		for (LootTableEntry e : this.entries)
			entries.add(e);
		entries.sort(new Comparator<LootTableEntry>()
		{
			@Override
			public int compare(LootTableEntry o1, LootTableEntry o2)
			{
				return o1.weight - o2.weight;
			}
		});

		for (int i = 0; i < rolls; ++i)
		{
			LootTableEntry entry = null;

			for (LootTableEntry e : entries)
				if (e.type != LootTableEntry.LOOT_TABLE && e.verifyConditions())
				{
					entry = e;
					break;
				}

			if (entry != null) items.add(entry.generateItem());
		}

		return items;
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

		TestValue v = new TestValue(Tags.LOOTTABLE_ROLLS, Tags.LOOTTABLE_ROLLS_RANGE);
		v.valueMin = this.rollsMin;
		v.valueMax = this.rollsMax;
		v.isRanged = this.rollsMax != -1;
		tags.add(v.toTag());

		v = new TestValue(Tags.LOOTTABLE_BONUS_ROLLS, Tags.LOOTTABLE_BONUS_ROLLS_RANGE, TagsMain.VALUE_MIN_FLOAT, TagsMain.VALUE_MAX_FLOAT);
		v.valueMin = this.bonusRollsMin;
		v.valueMax = this.bonusRollsMax;
		v.isRanged = this.bonusRollsMax != -1;
		tags.add(v.toTag());

		Tag[] con = new Tag[this.conditions.length];
		for (int i = 0; i < con.length; ++i)
			con[i] = this.conditions[i].toTag(Tags.DEFAULT_COMPOUND);
		tags.add(Tags.LOOTTABLE_CONDITIONS.create(con));

		Tag[] ent = new Tag[this.entries.length];
		for (int i = 0; i < ent.length; ++i)
			ent[i] = this.entries[i].toTag(Tags.DEFAULT_COMPOUND);
		tags.add(Tags.LOOTTABLE_ENTRIES.create(ent));

		return container.create(tags.toArray(new Tag[tags.size()]));
	}

	public Element toXML()
	{
		Element conditions = new Element("conditions");
		for (LootTableCondition condition : this.conditions)
			conditions.addContent(condition.toXML());

		Element entries = new Element("entries");
		for (LootTableEntry entry : this.entries)
			entries.addContent(entry.toXML());

		Element root = new Element("pool");
		root.addContent(conditions);
		root.addContent(entries);
		if (this.rollsMax == -1) root.addContent(new Element("rolls").setText(Integer.toString(this.rollsMin)));
		else
		{
			root.addContent(new Element("minrolls").setText(Integer.toString(this.rollsMin)));
			root.addContent(new Element("maxrolls").setText(Integer.toString(this.rollsMax)));
		}
		if (this.bonusRollsMax == -1) root.addContent(new Element("bonusrolls").setText(Double.toString(this.bonusRollsMin)));
		else
		{
			root.addContent(new Element("minbonusrolls").setText(Double.toString(this.bonusRollsMin)));
			root.addContent(new Element("maxbonusrolls").setText(Double.toString(this.bonusRollsMax)));
		}

		return root;
	}

	@Override
	public LootTablePool update(CGPanel panel) throws CommandGenerationException
	{
		LootTablePool pool = ((PanelPool) panel).generatePool();
		this.bonusRollsMin = pool.bonusRollsMin;
		this.bonusRollsMax = pool.bonusRollsMax;
		this.conditions = pool.conditions;
		this.entries = pool.entries;
		this.rollsMin = pool.rollsMin;
		this.rollsMax = pool.rollsMax;
		return this;
	}

	public boolean verifyConditions()
	{
		for (LootTableCondition condition : this.conditions)
			if (!condition.verify()) return false;
		return true;
	}
}
