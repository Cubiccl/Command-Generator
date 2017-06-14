package fr.cubiccl.generator.gameobject.registries;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

import org.jdom2.Element;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gameobject.*;
import fr.cubiccl.generator.gameobject.advancements.Advancement;
import fr.cubiccl.generator.gameobject.loottable.LootTable;
import fr.cubiccl.generator.gameobject.tags.NBTParser;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.target.Target;
import fr.cubiccl.generator.gui.Dialogs;
import fr.cubiccl.generator.gui.component.panel.utils.ListListener;
import fr.cubiccl.generator.utils.*;
import fr.cubiccl.generator.utils.Settings.Version;

/** Utility class to save custom {@link GameObject Game Objects}. */
public class ObjectSaver<T extends GameObject<T>> implements ListListener<T>
{
	public static final ObjectSaver<Advancement> advancements = new ObjectSaver<Advancement>("advancements", Advancement.class);
	public static final ObjectSaver<AttributeModifier> attributeModifiers = new ObjectSaver<AttributeModifier>("modifier", AttributeModifier.class);
	public static final ObjectSaver<AppliedAttribute> attributes = new ObjectSaver<AppliedAttribute>("attribute", AppliedAttribute.class);
	public static final ObjectSaver<PlacedBlock> blocks = new ObjectSaver<PlacedBlock>("block", PlacedBlock.class);
	public static final ObjectSaver<GeneratedCommand> commands = new ObjectSaver<GeneratedCommand>(new Text("commands", false), GeneratedCommand.class);
	public static final ObjectSaver<Coordinates> coordinates = new ObjectSaver<Coordinates>("coordinates", Coordinates.class);
	public static final ObjectSaver<Effect> effects = new ObjectSaver<Effect>("effect", Effect.class);
	public static final ObjectSaver<Enchantment> enchantments = new ObjectSaver<Enchantment>("enchantment", Enchantment.class);
	public static final ObjectSaver<LivingEntity> entities = new ObjectSaver<LivingEntity>("entity", LivingEntity.class);
	public static final ObjectSaver<ItemStack> items = new ObjectSaver<ItemStack>("item", ItemStack.class);
	public static final ObjectSaver<JsonMessage> jsonMessages = new ObjectSaver<JsonMessage>("json", JsonMessage.class);
	public static final ObjectSaver<LootTable> lootTables = new ObjectSaver<LootTable>(new Text("loot_tables", false), LootTable.class);
	@Deprecated
	private static final int MODIFIERS = 0, ATTRIBUTES = 1, BLOCKS = 2, COORDINATES = 3, EFFECTS = 4, ENCHANTMENTS = 5, ENTITIES = 6, ITEMS = 7, JSONS = 8,
			TRADES = 9, TARGETS = 10, COMMANDS = 11, LOOT_TABLES = 12;
	public static final ObjectSaver<Recipe> recipes = new ObjectSaver<Recipe>(new Text("recipes", false), Recipe.class);
	/** Stores all Object savers. */
	@SuppressWarnings("rawtypes")
	public static ObjectSaver[] savers, hiddenSavers;
	public static final ObjectSaver<Target> targets = new ObjectSaver<Target>("target", Target.class);
	public static final ObjectSaver<TradeOffer> trades = new ObjectSaver<TradeOffer>("trade", TradeOffer.class);
	/** True if this has been loaded. */
	private static boolean wasLoaded = false;

	/** Loads the Object Savers. */
	public static void load()
	{
		wasLoaded = true;
		savers = new ObjectSaver[]
		{ attributeModifiers, attributes, blocks, coordinates, effects, enchantments, entities, items, jsonMessages, trades, targets };
		hiddenSavers = new ObjectSaver[]
		{ commands, lootTables, recipes, advancements };
		resetAll();

		if (!FileUtils.fileExists("savedObjects.xml"))
		{
			loadOld();
			return;
		}

		Element objects = FileUtils.readXMLFile("savedObjects");

		for (Element modifier : objects.getChild("attributemodifiers").getChildren())
			if (shouldLoad(modifier)) attributeModifiers.addObject(new AttributeModifier().fromXML(modifier));
			else attributeModifiers.recentObjects.add(modifier);

		for (Element attribute : objects.getChild("attributes").getChildren())
			if (shouldLoad(attribute)) attributes.addObject(new AppliedAttribute().fromXML(attribute));
			else attributes.recentObjects.add(attribute);

		for (Element block : objects.getChild("blocks").getChildren())
			if (shouldLoad(block)) blocks.addObject(new PlacedBlock().fromXML(block));
			else blocks.recentObjects.add(block);

		for (Element coord : objects.getChild("coords").getChildren())
			if (shouldLoad(coord)) coordinates.addObject(new Coordinates().fromXML(coord));
			else coordinates.recentObjects.add(coord);

		for (Element effect : objects.getChild("effects").getChildren())
			if (shouldLoad(effect)) effects.addObject(new Effect().fromXML(effect));
			else effects.recentObjects.add(effect);

		for (Element enchant : objects.getChild("enchantments").getChildren())
			if (shouldLoad(enchant)) enchantments.addObject(new Enchantment().fromXML(enchant));
			else enchantments.recentObjects.add(enchant);

		for (Element entity : objects.getChild("entities").getChildren())
			if (shouldLoad(entity)) entities.addObject(new LivingEntity().fromXML(entity));
			else entities.recentObjects.add(entity);

		for (Element item : objects.getChild("items").getChildren())
			if (shouldLoad(item)) items.addObject(new ItemStack().fromXML(item));
			else items.recentObjects.add(item);

		for (Element json : objects.getChild("jsons").getChildren())
			if (shouldLoad(json)) jsonMessages.addObject(new JsonMessage().fromXML(json));
			else jsonMessages.recentObjects.add(json);

		for (Element target : objects.getChild("targets").getChildren())
			if (shouldLoad(target)) targets.addObject(new Target().fromXML(target));
			else targets.recentObjects.add(target);

		for (Element trade : objects.getChild("trades").getChildren())
			if (shouldLoad(trade)) trades.addObject(new TradeOffer().fromXML(trade));
			else trades.recentObjects.add(trade);

		for (Element command : objects.getChild("commands").getChildren())
			if (shouldLoad(command)) commands.addObject(new GeneratedCommand().fromXML(command));
			else commands.recentObjects.add(command);

		for (Element table : objects.getChild("tables").getChildren())
			if (shouldLoad(table)) lootTables.addObject(new LootTable().fromXML(table));
			else lootTables.recentObjects.add(table);

		if (objects.getChild("recipes") != null) for (Element recipe : objects.getChild("recipes").getChildren())
			if (shouldLoad(recipe)) recipes.addObject(new Recipe().fromXML(recipe));
			else recipes.recentObjects.add(recipe);

		if (objects.getChild("advancements") != null) for (Element recipe : objects.getChild("advancements").getChildren())
			if (shouldLoad(recipe)) advancements.addObject(new Advancement().fromXML(recipe));
			else advancements.recentObjects.add(recipe);
	}

	/** Loads if saved files were before v2.3.2. */
	private static void loadOld()
	{
		CommandGenerator.log("Old files detected from v2.3.2, updating.");
		String[] data = FileUtils.readFileAsArray("savedObjects.txt");
		int current = -1;
		for (String line : data)
		{
			if (line.startsWith("[") && line.endsWith("]"))
			{
				++current;
				continue;
			}
			switch (current)
			{
				case MODIFIERS:
					attributeModifiers.addObject(new AttributeModifier().fromNBT((TagCompound) NBTParser.parse(line, true, false)));
					break;

				case ATTRIBUTES:
					attributes.addObject(new AppliedAttribute().fromNBT((TagCompound) NBTParser.parse(line, true, false)));
					break;

				case BLOCKS:
					blocks.addObject(new PlacedBlock().fromNBT((TagCompound) NBTParser.parse(line, true, false)));
					break;

				case COORDINATES:
					coordinates.addObject(new Coordinates().fromNBT((TagCompound) NBTParser.parse(line, true, false)));
					break;

				case EFFECTS:
					effects.addObject(new Effect().fromNBT((TagCompound) NBTParser.parse(line, true, false)));
					break;

				case ENCHANTMENTS:
					enchantments.addObject(new Enchantment().fromNBT((TagCompound) NBTParser.parse(line, true, false)));
					break;

				case ENTITIES:
					entities.addObject(new LivingEntity().fromNBT((TagCompound) NBTParser.parse(line, true, false)));
					break;

				case ITEMS:
					items.addObject(new ItemStack().fromNBT((TagCompound) NBTParser.parse(line, true, false)));
					break;

				case JSONS:
					jsonMessages.addObject(new JsonMessage().fromNBT((TagCompound) NBTParser.parse(line, true, true)));
					break;

				case TRADES:
					trades.addObject(new TradeOffer().fromNBT((TagCompound) NBTParser.parse(line, true, false)));
					break;

				case TARGETS:
					targets.addObject(new Target().fromNBT((TagCompound) NBTParser.parse(line, true, false)));
					break;

				case COMMANDS:
					commands.addObject(new GeneratedCommand().fromNBT((TagCompound) NBTParser.parse(line, true, false)));
					break;

				case LOOT_TABLES:
					lootTables.addObject(new LootTable().fromNBT((TagCompound) NBTParser.parse(line, true, true, true)));
					break;

				default:
					break;
			}
		}
		FileUtils.delete("savedObjects.txt");
		FileUtils.delete("data/1.11.txt");
	}

	/** Resets the savers. */
	@SuppressWarnings("rawtypes")
	public static void resetAll()
	{
		for (ObjectSaver saver : savers)
			saver.reset();
		for (ObjectSaver saver : hiddenSavers)
			saver.reset();
	}

	/** Saves the Objects. */
	public static void save()
	{
		if (!wasLoaded) return;
		wasLoaded = false; // Prevent save before loading

		Element root = new Element("objects");
		root.addContent(attributeModifiers.toXML("attributemodifiers"));
		root.addContent(attributes.toXML("attributes"));
		root.addContent(blocks.toXML("blocks"));
		root.addContent(coordinates.toXML("coords"));
		root.addContent(effects.toXML("effects"));
		root.addContent(enchantments.toXML("enchantments"));
		root.addContent(entities.toXML("entities"));
		root.addContent(items.toXML("items"));
		root.addContent(jsonMessages.toXML("jsons"));
		root.addContent(targets.toXML("targets"));
		root.addContent(trades.toXML("trades"));
		root.addContent(commands.toXML("commands"));
		root.addContent(lootTables.toXML("tables"));
		root.addContent(recipes.toXML("recipes"));
		root.addContent(advancements.toXML("advancements"));

		FileUtils.saveXMLFile(root, "savedObjects");
	}

	/** @param object - An Object to load.
	 * @return <code>true</code> if the input Object should be loaded. */
	private static boolean shouldLoad(Element object)
	{
		if (object.getAttribute("version") == null) return false;
		return Settings.version().isAfter(Version.get(object.getAttributeValue("version")));
	}

	/** The Class of the Objects stored in this Saver. */
	public final Class<T> c;
	/** This Saver's namer. */
	public final Text name;
	/** Objects created after the current version, thus not loaded. */
	private ArrayList<Element> recentObjects;
	/** Objects saved. */
	private HashMap<String, T> savedObjects;

	protected ObjectSaver(String nameID, Class<T> c)
	{
		this(new Text("objects." + nameID), c);
	}

	protected ObjectSaver(Text name, Class<T> c)
	{
		this.name = name;
		this.c = c;
		this.savedObjects = new HashMap<String, T>();
		this.recentObjects = new ArrayList<Element>();
	}

	/** Adds an Object to this Saver.
	 * 
	 * @param object - The Object to add. */
	public void addObject(T object)
	{
		if (!this.knows(object)) this.savedObjects.put(object.customName(), object);
	}

	/** @param name - The name of an Object.
	 * @return The Object with the input name. */
	public T find(String name)
	{
		if (this.savedObjects.containsKey(name)) return this.savedObjects.get(name);
		return null;
	}

	/** @param object - An Object to test.
	 * @return <code>true</code> if this Saver knows the input Object. */
	public boolean knows(T object)
	{
		return this.savedObjects.containsKey(object.customName());
	}

	@SuppressWarnings("unchecked")
	public T[] list()
	{
		ArrayList<T> values = new ArrayList<T>();
		values.addAll(this.savedObjects.values());
		values.sort(new Comparator<T>()
		{
			@Override
			public int compare(T o1, T o2)
			{
				return o1.customName().toLowerCase().compareTo(o2.customName().toLowerCase());
			}
		});
		return values.toArray((T[]) Array.newInstance(this.c, values.size()));
	}

	@Override
	public void onAddition(int index, T object)
	{
		object.setCustomName(Dialogs.showInputDialog(new Text("objects.name").toString(), Lang.translate("general.confirm"), null));
		this.addObject(object);
	}

	@Override
	public void onChange(int index, T object)
	{}

	@Override
	public void onDeletion(int index, T object)
	{
		this.remove(object);
	}

	/** Removes an Object from this Saver.
	 * 
	 * @param object - The Object to remove. */
	public void remove(T object)
	{
		if (this.knows(object)) this.savedObjects.remove(object.customName());
	}

	/** Deletes all Objects from this saver. */
	private void reset()
	{
		this.savedObjects.clear();
	}

	/** @param rootID - The ID of the root XML element.
	 * @return This Saver as an XML element to be saved. */
	private Element toXML(String rootID)
	{
		Element root = new Element(rootID);
		for (T object : this.list())
			root.addContent(object.toXML());
		for (Element e : this.recentObjects)
		{
			e.detach();
			root.addContent(e);
		}
		return root;
	}
}
