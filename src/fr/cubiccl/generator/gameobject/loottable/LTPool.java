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
import fr.cubiccl.generator.gameobject.utils.XMLSaveable;
import fr.cubiccl.generator.gui.component.interfaces.IObjectList;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.loottable.PanelPool;
import fr.cubiccl.generator.gui.component.panel.utils.ListProperties;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Replacement;
import fr.cubiccl.generator.utils.Text;

/** Represents a Pool in a Loot Table. */
public class LTPool implements IObjectList<LTPool>, XMLSaveable<LTPool>
{

	/** Creates a Loot Table Pool from the input NBT Tag.
	 * 
	 * @param tag - The NBT Tag describing the Loot Table Pool.
	 * @return The created Loot Table Pool. */
	public static LTPool createFrom(TagCompound tag)
	{
		float bonusMin = 0, bonusMax = -1;
		int min = 0, max = -1;
		ArrayList<LTCondition> conditions = new ArrayList<LTCondition>();
		ArrayList<LTEntry> entries = new ArrayList<LTEntry>();

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
				LTCondition c = LTCondition.createFrom((TagCompound) con);
				if (c != null) conditions.add(c);
			}
		}
		if (tag.hasTag(Tags.LOOTTABLE_ENTRIES))
		{
			TagList t = tag.getTag(Tags.LOOTTABLE_ENTRIES);
			for (Tag ent : t.value())
			{
				LTEntry e = LTEntry.createFrom((TagCompound) ent);
				if (e != null) entries.add(e);
			}
		}

		return new LTPool(conditions.toArray(new LTCondition[conditions.size()]), min, max, bonusMin, bonusMax, entries.toArray(new LTEntry[entries.size()]));
	}

	/** The maximum number of bonus rolls for this Pool. If -1, the value is exact and is equal to {@link LTPool#bonusRollsMin}. */
	public double bonusRollsMax;
	/** The minimum number of bonus rolls for this Pool. The exact number if {@link LTPool#bonusRollsMax} is <code>-1</code>. */
	public double bonusRollsMin;
	/** This Pool's conditions. */
	public LTCondition[] conditions;
	/** This Pool's entries. */
	public LTEntry[] entries;
	/** The maximum number of rolls for this Pool. If -1, the value is exact and is equal to {@link LTPool#rollsMin}. */
	public int rollsMax;
	/** The minimum number of rolls for this Pool. The exact number if {@link LTPool#rollsMax} is <code>-1</code>. */
	public int rollsMin;

	public LTPool()
	{
		this(new LTCondition[0], 0, 0, new LTEntry[0]);
	}

	public LTPool(LTCondition[] conditions, int rolls, double bonusRolls, LTEntry[] entries)
	{
		this(conditions, rolls, -1, bonusRolls, -1, entries);
	}

	public LTPool(LTCondition[] conditions, int rollsMin, int rollsMax, double bonusRollsMin, double bonusRollsMax, LTEntry[] entries)
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
	public LTPool duplicate(LTPool object)
	{
		this.bonusRollsMax = object.bonusRollsMax;
		this.bonusRollsMin = object.bonusRollsMin;
		this.rollsMax = object.rollsMax;
		this.rollsMin = object.rollsMin;

		this.conditions = new LTCondition[object.conditions.length];
		for (int i = 0; i < this.conditions.length; ++i)
			this.conditions[i] = new LTCondition().duplicate(object.conditions[i]);

		this.entries = new LTEntry[object.entries.length];
		for (int i = 0; i < this.entries.length; ++i)
			this.entries[i] = new LTEntry().duplicate(object.entries[i]);

		return this;
	}

	@Override
	public LTPool fromXML(Element xml)
	{
		ArrayList<LTCondition> conditions = new ArrayList<LTCondition>();
		for (Element condition : xml.getChild("conditions").getChildren())
			conditions.add(new LTCondition().fromXML(condition));
		this.conditions = conditions.toArray(new LTCondition[conditions.size()]);

		ArrayList<LTEntry> entries = new ArrayList<LTEntry>();
		for (Element entry : xml.getChild("entries").getChildren())
			entries.add(new LTEntry().fromXML(entry));
		this.entries = entries.toArray(new LTEntry[entries.size()]);

		if (xml.getChild("rolls") != null) this.rollsMin = Integer.parseInt(xml.getChildText("rolls"));
		else
		{
			this.rollsMin = Integer.parseInt(xml.getChildText("minrolls"));
			this.rollsMax = Integer.parseInt(xml.getChildText("maxrolls"));
		}
		if (xml.getChild("bonusrolls") != null) this.bonusRollsMin = Double.parseDouble(xml.getChildText("bonusrolls"));
		else
		{
			this.bonusRollsMin = Double.parseDouble(xml.getChildText("minbonusrolls"));
			this.bonusRollsMax = Double.parseDouble(xml.getChildText("maxbonusrolls"));
		}

		return this;
	}

	/** @return A list of Items generated by this Pool. */
	public Set<ItemStack> generateItems()
	{
		HashSet<ItemStack> items = new HashSet<ItemStack>();

		int rolls = this.rollsMin;
		if (this.rollsMax != -1) rolls = new Random().nextInt(this.rollsMax - this.rollsMin + 1) + this.rollsMin;

		ArrayList<LTEntry> entries = new ArrayList<LTEntry>(); // Entries sorted by weight
		for (LTEntry e : this.entries)
			entries.add(e);
		entries.sort(new Comparator<LTEntry>()
		{
			@Override
			public int compare(LTEntry o1, LTEntry o2)
			{
				return o1.weight - o2.weight;
			}
		});

		for (int i = 0; i < rolls; ++i)
		{
			LTEntry entry = null;

			for (LTEntry e : entries)
				if (e.type != LTEntry.LOOT_TABLE && e.verifyConditions())
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

	/** @param container - The Compound to store this Pool in.
	 * @return This Pool as an NBT Tag to be generated. */
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

	@Override
	public Element toXML()
	{
		Element conditions = new Element("conditions");
		for (LTCondition condition : this.conditions)
			conditions.addContent(condition.toXML());

		Element entries = new Element("entries");
		for (LTEntry entry : this.entries)
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
	public LTPool update(CGPanel panel) throws CommandGenerationException
	{
		LTPool pool = ((PanelPool) panel).generatePool();
		this.bonusRollsMin = pool.bonusRollsMin;
		this.bonusRollsMax = pool.bonusRollsMax;
		this.conditions = pool.conditions;
		this.entries = pool.entries;
		this.rollsMin = pool.rollsMin;
		this.rollsMax = pool.rollsMax;
		return this;
	}

	/** @return Verifies this Pool's {@link LTPool#conditions conditions} for Item generation. */
	public boolean verifyConditions()
	{
		for (LTCondition condition : this.conditions)
			if (!condition.verify()) return false;
		return true;
	}
}
