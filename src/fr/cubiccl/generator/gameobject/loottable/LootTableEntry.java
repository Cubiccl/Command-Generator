package fr.cubiccl.generator.gameobject.loottable;

import java.awt.Component;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Comparator;

import org.jdom2.Element;

import fr.cubiccl.generator.gameobject.ItemStack;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
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

	public static LootTableEntry createFrom(Element entry)
	{
		LootTableEntry e = new LootTableEntry();
		e.name = entry.getChildText("name");
		e.type = Byte.parseByte(entry.getChildText("type"));
		e.weight = Integer.parseInt(entry.getChildText("weight"));
		e.quality = Integer.parseInt(entry.getChildText("quality"));

		ArrayList<LootTableCondition> conditions = new ArrayList<LootTableCondition>();
		for (Element condition : entry.getChild("conditions").getChildren())
			conditions.add(LootTableCondition.createFrom(condition));
		e.conditions = conditions.toArray(new LootTableCondition[conditions.size()]);

		ArrayList<LootTableFunction> functions = new ArrayList<LootTableFunction>();
		for (Element function : entry.getChild("functions").getChildren())
			functions.add(LootTableFunction.createFrom(function));
		e.functions = functions.toArray(new LootTableFunction[functions.size()]);

		return e;
	}

	public static LootTableEntry createFrom(TagCompound tag)
	{
		String name = null;
		int weight = -1, quality = -1;
		byte type = EMPTY;
		ArrayList<LootTableCondition> conditions = new ArrayList<LootTableCondition>();
		ArrayList<LootTableFunction> functions = new ArrayList<LootTableFunction>();

		if (tag.hasTag(Tags.LOOTTABLE_ENTRY_TYPE))
		{
			String t = tag.getTag(Tags.LOOTTABLE_ENTRY_TYPE).value();
			if (t.equals("item")) type = ITEM;
			if (t.equals("loot_table")) type = LOOT_TABLE;
			if (t.equals("empty")) type = EMPTY;
		}
		if (tag.hasTag(Tags.LOOTTABLE_ENTRY_NAME)) name = tag.getTag(Tags.LOOTTABLE_ENTRY_NAME).value();
		if (tag.hasTag(Tags.LOOTTABLE_ENTRY_WEIGHT)) weight = tag.getTag(Tags.LOOTTABLE_ENTRY_WEIGHT).valueInt();
		if (tag.hasTag(Tags.LOOTTABLE_ENTRY_QUALITY)) quality = tag.getTag(Tags.LOOTTABLE_ENTRY_QUALITY).valueInt();
		if (tag.hasTag(Tags.LOOTTABLE_CONDITIONS))
		{
			TagList t = tag.getTag(Tags.LOOTTABLE_CONDITIONS);
			for (Tag con : t.value())
			{
				LootTableCondition c = LootTableCondition.createFrom((TagCompound) con);
				if (c != null) conditions.add(c);
			}
		}
		if (tag.hasTag(Tags.LOOTTABLE_FUNCTIONS))
		{
			TagList t = tag.getTag(Tags.LOOTTABLE_FUNCTIONS);
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

	public LootTableCondition[] conditions;
	private LootTableFunction[] functions;
	public String name;
	public byte type;
	public int weight, quality;

	public LootTableEntry()
	{
		this(new LootTableCondition[0], (byte) 0, "", new LootTableFunction[0], 0, 0);
	}

	public LootTableEntry(LootTableCondition[] conditions, byte type, String name, LootTableFunction[] functions, int weight, int quality)
	{
		this.conditions = conditions;
		this.type = type;
		this.name = name;
		this.setFunctions(functions);
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

	public ItemStack generateItem()
	{
		if (this.type == EMPTY) return null;

		ItemStack item = new ItemStack();
		item.setItem(ObjectRegistry.items.find(this.name));

		for (LootTableFunction function : this.functions)
			if (function.verifyConditions()) function.applyTo(item);

		return item;
	}

	@Override
	public Component getDisplayComponent()
	{
		return new CGLabel(new Text(this.name, false));
	}

	public LootTableFunction[] getFunctions()
	{
		return this.functions;
	}

	public BufferedImage getIcon()
	{
		if (this.type != ITEM) return null;
		return ObjectRegistry.items.find(this.name).texture();
	}

	@Override
	public String getName(int index)
	{
		return this.name;
	}

	public String name()
	{
		if (this.type != ITEM) return this.name;
		return ObjectRegistry.items.find(this.name).name().toString();
	}

	public void setFunctions(LootTableFunction[] functions)
	{
		ArrayList<LootTableFunction> f = new ArrayList<LootTableFunction>();
		for (LootTableFunction lootTableFunction : functions)
			f.add(lootTableFunction);
		f.sort(new Comparator<LootTableFunction>()
		{

			@Override
			public int compare(LootTableFunction o1, LootTableFunction o2)
			{
				return o1.function.compareToFunction(o2.function);
			}
		});
		this.functions = f.toArray(new LootTableFunction[f.size()]);
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

		tags.add(Tags.LOOTTABLE_CONDITIONS.create(con));
		tags.add(Tags.LOOTTABLE_ENTRY_TYPE.create(TYPES[this.type]));
		if (this.name != null) tags.add(Tags.LOOTTABLE_ENTRY_NAME.create(this.name));
		tags.add(Tags.LOOTTABLE_FUNCTIONS.create(fun));
		if (this.weight != -1) tags.add(Tags.LOOTTABLE_ENTRY_WEIGHT.create(this.weight));
		if (this.quality != -1) tags.add(Tags.LOOTTABLE_ENTRY_QUALITY.create(this.quality));

		return container.create(tags.toArray(new Tag[tags.size()]));
	}

	public Element toXML()
	{
		Element conditions = new Element("conditions");
		for (LootTableCondition condition : this.conditions)
			conditions.addContent(condition.toXML());

		Element functions = new Element("functions");
		for (LootTableFunction function : this.functions)
			functions.addContent(function.toXML());

		Element root = new Element("pool");
		root.addContent(new Element("name").setText(this.name));
		root.addContent(new Element("type").setText(Byte.toString(this.type)));
		root.addContent(new Element("weight").setText(Integer.toString(this.weight)));
		root.addContent(new Element("quality").setText(Integer.toString(this.quality)));
		root.addContent(conditions);
		root.addContent(functions);
		return root;
	}

	@Override
	public LootTableEntry update(CGPanel panel) throws CommandGenerationException
	{
		LootTableEntry e = ((PanelEntry) panel).generate();
		this.conditions = e.conditions;
		this.functions = e.functions;
		this.name = e.name;
		this.type = e.type;
		this.weight = e.weight;
		this.quality = e.quality;
		return this;
	}

	public boolean verifyConditions()
	{
		for (LootTableCondition condition : this.conditions)
			if (!condition.verify()) return false;
		return true;
	}
}
