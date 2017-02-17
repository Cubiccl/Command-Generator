package fr.cubiccl.generator.gameobject.loottable;

import java.awt.Component;
import java.util.ArrayList;

import fr.cubiccl.generator.gameobject.tags.*;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gui.component.interfaces.IObjectList;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.loottable.PanelEntry;
import fr.cubiccl.generator.gui.component.panel.utils.ListProperties;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

public class LootTableEntry implements IObjectList<LootTableEntry>
{
	public static final byte ITEM = 0, LOOT_TABLE = 1, EMPTY = 2;
	public static final String[] TYPES =
	{ "item", "loot_table", "empty" };

	public static LootTableEntry createFrom(TagCompound tag)
	{
		String name = null;
		int weight = -1, quality = -1;
		byte type = EMPTY;
		ArrayList<LootTableCondition> conditions = new ArrayList<LootTableCondition>();
		ArrayList<LootTableFunction> functions = new ArrayList<LootTableFunction>();

		if (tag.hasTag(Tags.LOOTTABLE_ENTRY_TYPE))
		{
			String t = ((TagString) tag.getTag(Tags.LOOTTABLE_ENTRY_TYPE)).value();
			if (t.equals("item")) type = ITEM;
			if (t.equals("loot_table")) type = LOOT_TABLE;
			if (t.equals("empty")) type = EMPTY;
		}
		if (tag.hasTag(Tags.LOOTTABLE_ENTRY_NAME)) name = ((TagString) tag.getTag(Tags.LOOTTABLE_ENTRY_NAME)).value();
		if (tag.hasTag(Tags.LOOTTABLE_ENTRY_WEIGHT)) weight = ((TagNumber) tag.getTag(Tags.LOOTTABLE_ENTRY_WEIGHT)).value();
		if (tag.hasTag(Tags.LOOTTABLE_ENTRY_QUALITY)) quality = ((TagNumber) tag.getTag(Tags.LOOTTABLE_ENTRY_QUALITY)).value();
		if (tag.hasTag(Tags.LOOTTABLE_CONDITIONS))
		{
			TagList t = (TagList) tag.getTag(Tags.LOOTTABLE_CONDITIONS);
			for (Tag con : t.value())
			{
				LootTableCondition c = LootTableCondition.createFrom((TagCompound) con);
				if (c != null) conditions.add(c);
			}
		}
		if (tag.hasTag(Tags.LOOTTABLE_FUNCTIONS))
		{
			TagList t = (TagList) tag.getTag(Tags.LOOTTABLE_FUNCTIONS);
			for (Tag fun : t.value())
			{
				LootTableFunction f = LootTableFunction.createFrom((TagCompound) fun);
				if (f != null) functions.add(f);
			}
		}

		if (name == null && type != EMPTY) return null;
		return new LootTableEntry(conditions.toArray(new LootTableCondition[conditions.size()]), type, name, functions.toArray(new LootTableFunction[functions
				.size()]), weight, quality);
	}

	public final LootTableCondition[] conditions;
	public final LootTableFunction[] functions;
	public final String name;
	public final byte type;
	public final int weight, quality;

	public LootTableEntry()
	{
		this(new LootTableCondition[0], (byte) 0, "", new LootTableFunction[0], 0, 0);
	}

	public LootTableEntry(LootTableCondition[] conditions, byte type, String name, LootTableFunction[] functions, int weight, int quality)
	{
		this.conditions = conditions;
		this.type = type;
		this.name = name;
		this.functions = functions;
		this.weight = weight;
		this.quality = quality;
	}

	@Override
	public CGPanel createPanel(ListProperties properties)
	{
		PanelEntry p = new PanelEntry();
		p.setupFrom(this);
		return p;
	}

	@Override
	public Component getDisplayComponent()
	{
		return new CGLabel(new Text(this.name, false));
	}

	@Override
	public String getName(int index)
	{
		return this.name;
	}

	@Override
	public LootTableEntry setupFrom(CGPanel panel) throws CommandGenerationException
	{
		return ((PanelEntry) panel).generate();
	}

	public TagCompound toTag(TemplateCompound container)
	{
		ArrayList<Tag> tags = new ArrayList<Tag>();

		Tag[] con = new Tag[this.conditions.length];
		for (int i = 0; i < con.length; ++i)
			con[i] = this.conditions[i].toTag(Tags.DEFAULT_COMPOUND);

		Tag[] fun = new Tag[this.functions.length];
		for (int i = 0; i < fun.length; ++i)
			fun[i] = this.functions[i].toTag(Tags.DEFAULT_COMPOUND);

		tags.add(new TagList(Tags.LOOTTABLE_CONDITIONS, con));
		tags.add(new TagString(Tags.LOOTTABLE_ENTRY_TYPE, TYPES[this.type]));
		if (this.name != null) tags.add(new TagString(Tags.LOOTTABLE_ENTRY_NAME, this.name));
		tags.add(new TagList(Tags.LOOTTABLE_FUNCTIONS, fun));
		if (this.weight != -1) tags.add(new TagNumber(Tags.LOOTTABLE_ENTRY_WEIGHT, this.weight));
		if (this.quality != -1) tags.add(new TagNumber(Tags.LOOTTABLE_ENTRY_QUALITY, this.quality));

		return new TagCompound(container, tags.toArray(new Tag[tags.size()]));
	}
}
