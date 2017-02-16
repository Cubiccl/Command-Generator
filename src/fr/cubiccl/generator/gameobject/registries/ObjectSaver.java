package fr.cubiccl.generator.gameobject.registries;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

import fr.cubiccl.generator.gameobject.*;
import fr.cubiccl.generator.gameobject.loottable.LootTable;
import fr.cubiccl.generator.gameobject.tags.NBTReader;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.target.Target;
import fr.cubiccl.generator.gui.component.panel.utils.ListListener;
import fr.cubiccl.generator.utils.FileUtils;
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
	}

	public static void resetAll()
	{
		for (@SuppressWarnings("rawtypes")
		ObjectSaver saver : savers)
			saver.reset();
		commands.reset();
	}

	public static void save()
	{
		ArrayList<String> data = new ArrayList<String>();
		data.add("// This file saves Custom Game Objects.");

		for (@SuppressWarnings("rawtypes")
		ObjectSaver saver : savers)
		{
			data.add("");
			data.add("[" + saver.name.toString().toUpperCase() + "]");
			for (GameObject object : saver.list())
				data.add(object.save());
		}
		for (@SuppressWarnings("rawtypes")
		ObjectSaver saver : hiddenSavers)
		{
			data.add("");
			data.add("[" + saver.name.toString().toUpperCase() + "]");
			for (GameObject object : saver.list())
				data.add(object.save());
		}

		FileUtils.writeToFile("savedObjects.txt", data.toArray(new String[data.size()]));
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
		this.addObject(object);
	}

	@Override
	public void onChange(int index, T object)
	{
		this.savedObjects.put(object.customName(), object);
	}

	@Override
	public void onDeletion(int index, T object)
	{
		this.delete(object);
	}

	private void reset()
	{
		this.savedObjects.clear();
	}
}
