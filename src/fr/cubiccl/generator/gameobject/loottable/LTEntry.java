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
import fr.cubiccl.generator.gameobject.utils.XMLSaveable;
import fr.cubiccl.generator.gui.component.interfaces.IObjectList;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.loottable.PanelEntry;
import fr.cubiccl.generator.gui.component.panel.utils.ListProperties;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

/** Represents an Entry for Loot Tables. */
public class LTEntry implements IObjectList<LTEntry>, XMLSaveable<LTEntry>
{
	/** Identifiers for Entry types.<br />
	 * <br />
	 * <table border="1">
	 * <tr>
	 * <td>ID</td>
	 * <td>Variable</td>
	 * <td>Mode</td>
	 * </tr>
	 * <tr>
	 * <td>0</td>
	 * <td>ITEM</td>
	 * <td>Item</td>
	 * </tr>
	 * <tr>
	 * <td>1</td>
	 * <td>LOOT_TABLE</td>
	 * <td>Path to another Loot Table</td>
	 * </tr>
	 * <tr>
	 * <td>2</td>
	 * <td>EMPTY</td>
	 * <td>Nothing is generated</td>
	 * </tr>
	 * </table> */
	public static final byte ITEM = 0, LOOT_TABLE = 1, EMPTY = 2;
	/** Names for {@link LTEntry#ITEM Entry types}. */
	public static final String[] TYPES =
	{ "item", "loot_table", "empty" };

	/** Creates a Loot Table Entry from the input NBT Tag.
	 * 
	 * @param tag - The NBT Tag describing the Loot Table Entry.
	 * @return The created Loot Table Entry. */
	public static LTEntry createFrom(TagCompound tag)
	{
		String name = null;
		int weight = -1, quality = -1;
		byte type = EMPTY;
		ArrayList<LTCondition> conditions = new ArrayList<LTCondition>();
		ArrayList<LTFunction> functions = new ArrayList<LTFunction>();

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
				LTCondition c = LTCondition.createFrom((TagCompound) con);
				if (c != null) conditions.add(c);
			}
		}
		if (tag.hasTag(Tags.LOOTTABLE_FUNCTIONS))
		{
			TagList t = tag.getTag(Tags.LOOTTABLE_FUNCTIONS);
			for (Tag fun : t.value())
			{
				LTFunction f = LTFunction.createFrom((TagCompound) fun);
				if (f != null) functions.add(f);
			}
		}

		if (name == null && type != EMPTY) return null;
		return new LTEntry(conditions.toArray(new LTCondition[conditions.size()]), type, name, functions.toArray(new LTFunction[functions.size()]), weight,
				quality);
	}

	/** This Entry's Conditions. */
	public LTCondition[] conditions;
	/** This Entry's Functions. */
	private LTFunction[] functions;
	/** This Entry's value. */
	public String name;
	/** This Entry's quality. */
	public int quality;
	/** This Entry's type.
	 * 
	 * @see LTEntry#ITEM */
	public byte type;
	/** This Entry's weight. */
	public int weight;

	public LTEntry()
	{
		this(new LTCondition[0], (byte) 0, "", new LTFunction[0], 0, 0);
	}

	public LTEntry(LTCondition[] conditions, byte type, String name, LTFunction[] functions, int weight, int quality)
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

	@Override
	public LTEntry duplicate(LTEntry object)
	{
		this.name = object.name;
		this.quality = object.quality;
		this.type = object.type;
		this.weight = object.weight;

		this.conditions = new LTCondition[object.conditions.length];
		for (int i = 0; i < this.conditions.length; ++i)
			this.conditions[i] = new LTCondition().duplicate(object.conditions[i]);

		this.functions = new LTFunction[object.functions.length];
		for (int i = 0; i < this.functions.length; ++i)
			this.functions[i] = new LTFunction().duplicate(object.functions[i]);

		return this;
	}

	@Override
	public LTEntry fromXML(Element xml)
	{
		this.name = xml.getChildText("name");
		this.type = Byte.parseByte(xml.getChildText("type"));
		this.weight = Integer.parseInt(xml.getChildText("weight"));
		this.quality = Integer.parseInt(xml.getChildText("quality"));

		ArrayList<LTCondition> conditions = new ArrayList<LTCondition>();
		for (Element condition : xml.getChild("conditions").getChildren())
			conditions.add(new LTCondition().fromXML(condition));
		this.conditions = conditions.toArray(new LTCondition[conditions.size()]);

		ArrayList<LTFunction> functions = new ArrayList<LTFunction>();
		for (Element function : xml.getChild("functions").getChildren())
			functions.add(new LTFunction().fromXML(function));
		this.functions = functions.toArray(new LTFunction[functions.size()]);

		return this;
	}

	/** @return The Item generated by this Entry. */
	public ItemStack generateItem()
	{
		if (this.type == EMPTY) return null;

		ItemStack item = new ItemStack();
		item.setItem(ObjectRegistry.items.find(this.name));

		for (LTFunction function : this.functions)
			if (function.verifyConditions()) function.applyTo(item);

		return item;
	}

	@Override
	public Component getDisplayComponent()
	{
		return new CGLabel(new Text(this.name, false));
	}

	/** Getter for {@link LTEntry#functions}. */
	public LTFunction[] getFunctions()
	{
		return this.functions;
	}

	/** @return This Entry's Item's texture. If not an Item, returns <code>null</code>. */
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

	/** @return This Entry's Item's name. If not an Item, returns this Entry's name. */
	public String name()
	{
		if (this.type != ITEM) return this.name;
		return ObjectRegistry.items.find(this.name).name().toString();
	}

	/** Setter for {@link LTEntry#functions}. */
	public void setFunctions(LTFunction[] functions)
	{
		ArrayList<LTFunction> f = new ArrayList<LTFunction>();
		for (LTFunction lootTableFunction : functions)
			f.add(lootTableFunction);
		f.sort(new Comparator<LTFunction>()
		{

			@Override
			public int compare(LTFunction o1, LTFunction o2)
			{
				return o1.function.compareToFunction(o2.function);
			}
		});
		this.functions = f.toArray(new LTFunction[f.size()]);
	}

	/** @param container - The Compound to store this Entry in.
	 * @return This Entry as an NBT Tag to be generated. */
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

	@Override
	public Element toXML()
	{
		Element conditions = new Element("conditions");
		for (LTCondition condition : this.conditions)
			conditions.addContent(condition.toXML());

		Element functions = new Element("functions");
		for (LTFunction function : this.functions)
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
	public LTEntry update(CGPanel panel) throws CommandGenerationException
	{
		LTEntry e = ((PanelEntry) panel).generate();
		this.conditions = e.conditions;
		this.functions = e.functions;
		this.name = e.name;
		this.type = e.type;
		this.weight = e.weight;
		this.quality = e.quality;
		return this;
	}

	/** @return <code>true</code> if this Entry's {@link LTEntry#conditions Conditions} are verified. */
	public boolean verifyConditions()
	{
		for (LTCondition condition : this.conditions)
			if (!condition.verify()) return false;
		return true;
	}
}
