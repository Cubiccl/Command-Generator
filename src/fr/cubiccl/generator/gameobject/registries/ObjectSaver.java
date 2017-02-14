package fr.cubiccl.generator.gameobject.registries;

import java.awt.Component;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

import javax.swing.JOptionPane;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gameobject.*;
import fr.cubiccl.generator.gameobject.loottable.LootTable;
import fr.cubiccl.generator.gameobject.registries.ListInterface.AttributeList;
import fr.cubiccl.generator.gameobject.registries.ListInterface.BlockList;
import fr.cubiccl.generator.gameobject.registries.ListInterface.CoordinatesList;
import fr.cubiccl.generator.gameobject.registries.ListInterface.EffectList;
import fr.cubiccl.generator.gameobject.registries.ListInterface.EnchantmentList;
import fr.cubiccl.generator.gameobject.registries.ListInterface.EntityList;
import fr.cubiccl.generator.gameobject.registries.ListInterface.ItemList;
import fr.cubiccl.generator.gameobject.registries.ListInterface.JsonList;
import fr.cubiccl.generator.gameobject.registries.ListInterface.ModifierList;
import fr.cubiccl.generator.gameobject.registries.ListInterface.TargetList;
import fr.cubiccl.generator.gameobject.registries.ListInterface.TradeList;
import fr.cubiccl.generator.gameobject.tags.NBTReader;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.target.Target;
import fr.cubiccl.generator.gui.component.interfaces.IObjectList;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.FileUtils;
import fr.cubiccl.generator.utils.Text;

public class ObjectSaver<T extends GameObject> implements IObjectList
{
	public static final ObjectSaver<AttributeModifier> attributeModifiers = new ObjectSaver<AttributeModifier>("modifier", new ModifierList(),
			AttributeModifier.class);
	public static final ObjectSaver<AppliedAttribute> attributes = new ObjectSaver<AppliedAttribute>("attribute", new AttributeList(), AppliedAttribute.class);
	public static final ObjectSaver<PlacedBlock> blocks = new ObjectSaver<PlacedBlock>("block", new BlockList(), PlacedBlock.class);
	public static final ObjectSaver<GeneratedCommand> commands = new ObjectSaver<GeneratedCommand>(new Text("commands", false), null, GeneratedCommand.class);
	public static final ObjectSaver<Coordinates> coordinates = new ObjectSaver<Coordinates>("coordinates", new CoordinatesList(), Coordinates.class);
	public static final ObjectSaver<Effect> effects = new ObjectSaver<Effect>("effect", new EffectList(), Effect.class);
	public static final ObjectSaver<Enchantment> enchantments = new ObjectSaver<Enchantment>("enchantment", new EnchantmentList(), Enchantment.class);
	public static final ObjectSaver<LivingEntity> entities = new ObjectSaver<LivingEntity>("entity", new EntityList(), LivingEntity.class);
	public static final ObjectSaver<ItemStack> items = new ObjectSaver<ItemStack>("item", new ItemList(), ItemStack.class);
	public static final ObjectSaver<JsonMessage> jsonMessages = new ObjectSaver<JsonMessage>("json", new JsonList(), JsonMessage.class);
	public static final ObjectSaver<LootTable> lootTables = new ObjectSaver<LootTable>(new Text("loot_tables", false), null, LootTable.class);
	private static final int MODIFIERS = 0, ATTRIBUTES = 1, BLOCKS = 2, COORDINATES = 3, EFFECTS = 4, ENCHANTMENTS = 5, ENTITIES = 6, ITEMS = 7, JSONS = 8,
			TRADES = 9, TARGETS = 10, COMMANDS = 11, LOOT_TABLES = 12;
	@SuppressWarnings("rawtypes")
	public static ObjectSaver[] savers, hiddenSavers;
	public static final ObjectSaver<Target> targets = new ObjectSaver<Target>("target", new TargetList(), Target.class);
	public static final ObjectSaver<TradeOffer> trades = new ObjectSaver<TradeOffer>("trade", new TradeList(), TradeOffer.class);

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
	private ListInterface<T> list;
	public final Text name;
	private HashMap<String, T> savedObjects;

	protected ObjectSaver(String nameID, ListInterface<T> list, Class<T> c)
	{
		this(new Text("objects." + nameID), list, c);
	}

	protected ObjectSaver(Text name, ListInterface<T> list, Class<T> c)
	{
		this.name = name;
		this.list = list;
		this.c = c;
		this.savedObjects = new HashMap<String, T>();
	}

	@Override
	public boolean addObject(CGPanel panel, int editIndex)
	{
		try
		{
			T object = this.list.createObject(panel);

			if (editIndex != -1)
			{
				T o = this.get(editIndex);
				this.delete(o);
				object.setCustomName(o.customName());
			} else
			{
				object.setCustomName(JOptionPane.showInputDialog(new Text("objects.name")));
				if (object.customName() == null) return false;
			}

			this.addObject(object);
			return true;
		} catch (CommandGenerationException e)
		{
			CommandGenerator.report(e);
			return false;
		}
	}

	public void addObject(T object)
	{
		if (!this.knows(object)) this.savedObjects.put(object.customName(), object);
	}

	@Override
	public CGPanel createAddPanel(int editIndex)
	{
		return this.list.createEditionPanel(editIndex == -1 ? null : this.list()[editIndex]);
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

	private T get(int index)
	{
		return this.list()[index];
	}

	@Override
	public Component getDisplayComponent(int index)
	{
		return this.list.getDisplayComponent(this.list()[index]);
	}

	@Override
	public String[] getValues()
	{
		T[] list = this.list();
		String[] names = new String[list.length];
		for (int i = 0; i < names.length; ++i)
			names[i] = list[i].customName();
		return names;
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
	public void removeObject(int index)
	{
		this.delete(this.list()[index]);
	}

	private void reset()
	{
		this.savedObjects.clear();
	}
}
