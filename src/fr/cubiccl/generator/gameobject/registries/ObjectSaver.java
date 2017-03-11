package fr.cubiccl.generator.gameobject.registries;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

import org.jdom2.Element;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gameobject.*;
import fr.cubiccl.generator.gameobject.loottable.LootTable;
import fr.cubiccl.generator.gameobject.tags.NBTReader;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.target.Target;
import fr.cubiccl.generator.gui.Dialogs;
import fr.cubiccl.generator.gui.component.panel.utils.ListListener;
import fr.cubiccl.generator.utils.FileUtils;
import fr.cubiccl.generator.utils.Lang;
import fr.cubiccl.generator.utils.Text;

public class ObjectSaver<T extends GameObject> implements ListListener<T>
{
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
	private static final int MODIFIERS = 0, ATTRIBUTES = 1, BLOCKS = 2, COORDINATES = 3, EFFECTS = 4, ENCHANTMENTS = 5, ENTITIES = 6, ITEMS = 7, JSONS = 8,
			TRADES = 9, TARGETS = 10, COMMANDS = 11, LOOT_TABLES = 12;
	@SuppressWarnings("rawtypes")
	public static ObjectSaver[] savers, hiddenSavers;
	public static final ObjectSaver<Target> targets = new ObjectSaver<Target>("target", Target.class);
	public static final ObjectSaver<TradeOffer> trades = new ObjectSaver<TradeOffer>("trade", TradeOffer.class);

	public static void load()
	{
		savers = new ObjectSaver[]
		{ attributeModifiers, attributes, blocks, coordinates, effects, enchantments, entities, items, jsonMessages, trades, targets };
		hiddenSavers = new ObjectSaver[]
		{ commands, lootTables };
		resetAll();

		if (!FileUtils.fileExists("savedObjects.xml"))
		{
			loadOld();
			return;
		}

		Element objects = FileUtils.readXMLFile("savedObjects");

		for (Element modifier : objects.getChild("attributemodifiers").getChildren())
			attributeModifiers.addObject(AttributeModifier.createFrom(modifier));

		for (Element attribute : objects.getChild("attributes").getChildren())
			attributes.addObject(AppliedAttribute.createFrom(attribute));

		for (Element block : objects.getChild("blocks").getChildren())
			blocks.addObject(PlacedBlock.createFrom(block));

		for (Element coord : objects.getChild("coords").getChildren())
			coordinates.addObject(Coordinates.createFrom(coord));

		for (Element effect : objects.getChild("effects").getChildren())
			effects.addObject(Effect.createFrom(effect));

		for (Element enchant : objects.getChild("enchantments").getChildren())
			enchantments.addObject(Enchantment.createFrom(enchant));

		for (Element entity : objects.getChild("entities").getChildren())
			entities.addObject(LivingEntity.createFrom(entity));

		for (Element item : objects.getChild("items").getChildren())
			items.addObject(ItemStack.createFrom(item));

		for (Element json : objects.getChild("jsons").getChildren())
			jsonMessages.addObject(JsonMessage.createFrom(json));

		for (Element target : objects.getChild("targets").getChildren())
			targets.addObject(Target.createFrom(target));

		for (Element trade : objects.getChild("trades").getChildren())
			trades.addObject(TradeOffer.createFrom(trade));

		for (Element command : objects.getChild("commands").getChildren())
			commands.addObject(GeneratedCommand.createFrom(command));

		for (Element table : objects.getChild("tables").getChildren())
			lootTables.addObject(LootTable.createFrom(table));
	}

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
					attributeModifiers.addObject(AttributeModifier.createFrom((TagCompound) NBTReader.read(line, true, false)));
					break;

				case ATTRIBUTES:
					attributes.addObject(AppliedAttribute.createFrom((TagCompound) NBTReader.read(line, true, false)));
					break;

				case BLOCKS:
					blocks.addObject(PlacedBlock.createFrom((TagCompound) NBTReader.read(line, true, false)));
					break;

				case COORDINATES:
					coordinates.addObject(Coordinates.createFrom((TagCompound) NBTReader.read(line, true, false)));
					break;

				case EFFECTS:
					effects.addObject(Effect.createFrom((TagCompound) NBTReader.read(line, true, false)));
					break;

				case ENCHANTMENTS:
					enchantments.addObject(Enchantment.createFrom((TagCompound) NBTReader.read(line, true, false)));
					break;

				case ENTITIES:
					entities.addObject(LivingEntity.createFrom((TagCompound) NBTReader.read(line, true, false)));
					break;

				case ITEMS:
					items.addObject(ItemStack.createFrom((TagCompound) NBTReader.read(line, true, false)));
					break;

				case JSONS:
					jsonMessages.addObject(JsonMessage.createFrom((TagCompound) NBTReader.read(line, true, true)));
					break;

				case TRADES:
					trades.addObject(TradeOffer.createFrom((TagCompound) NBTReader.read(line, true, false)));
					break;

				case TARGETS:
					targets.addObject(Target.createFrom((TagCompound) NBTReader.read(line, true, false)));
					break;

				case COMMANDS:
					commands.addObject(GeneratedCommand.createFrom((TagCompound) NBTReader.read(line, true, false)));
					break;

				case LOOT_TABLES:
					lootTables.addObject(LootTable.createFrom((TagCompound) NBTReader.read(line, true, true, true)));
					break;

				default:
					break;
			}
		}
		FileUtils.delete("savedObjects.txt");
		FileUtils.delete("data/1.11.txt");
	}

	public static void resetAll()
	{
		for (@SuppressWarnings("rawtypes")
		ObjectSaver saver : savers)
			saver.reset();
		commands.reset();
		lootTables.reset();
	}

	public static void save()
	{
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

		FileUtils.saveXMLFile(root, "savedObjects");
	}

	public final Class<T> c;

	public final Text name;
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
	}

	public void addObject(T object)
	{
		if (!this.knows(object)) this.savedObjects.put(object.customName(), object);
	}

	public void delete(T object)
	{
		if (this.knows(object)) this.savedObjects.remove(object.customName());
	}

	public T find(String name)
	{
		if (this.savedObjects.containsKey(name)) return this.savedObjects.get(name);
		return null;
	}

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
		this.delete(object);
	}

	private void reset()
	{
		this.savedObjects.clear();
	}

	private Element toXML(String rootID)
	{
		Element root = new Element(rootID);
		for (T object : this.list())
			root.addContent(object.toXML());
		return root;
	}
}
