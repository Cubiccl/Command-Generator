package fr.cubiccl.generator.gameobject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gameobject.baseobjects.Achievement;
import fr.cubiccl.generator.gameobject.baseobjects.Attribute;
import fr.cubiccl.generator.gameobject.baseobjects.Block;
import fr.cubiccl.generator.gameobject.baseobjects.EffectType;
import fr.cubiccl.generator.gameobject.baseobjects.EnchantmentType;
import fr.cubiccl.generator.gameobject.baseobjects.Entity;
import fr.cubiccl.generator.gameobject.baseobjects.Item;
import fr.cubiccl.generator.gameobject.baseobjects.Slot;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.target.TargetArgument;
import fr.cubiccl.generator.gameobject.templatetags.TemplateNumber;
import fr.cubiccl.generator.gameobject.templatetags.TemplateString;
import fr.cubiccl.generator.gameobject.templatetags.TemplateTag;
import fr.cubiccl.generator.gameobject.templatetags.custom.TemplateBlockEntity;
import fr.cubiccl.generator.gameobject.templatetags.custom.TemplateBlockList;
import fr.cubiccl.generator.gameobject.templatetags.custom.TemplateColor;
import fr.cubiccl.generator.gameobject.templatetags.custom.TemplateCommandStats;
import fr.cubiccl.generator.gameobject.templatetags.custom.TemplateCoordinates;
import fr.cubiccl.generator.gameobject.templatetags.custom.TemplateDisplay;
import fr.cubiccl.generator.gameobject.templatetags.custom.TemplateEffectList;
import fr.cubiccl.generator.gameobject.templatetags.custom.TemplateEnchantmentList;
import fr.cubiccl.generator.gameobject.templatetags.custom.TemplateHideFlags;
import fr.cubiccl.generator.gameobject.templatetags.custom.TemplateItem;
import fr.cubiccl.generator.gameobject.templatetags.custom.TemplateItemId;
import fr.cubiccl.generator.gameobject.templatetags.custom.TemplateItems;
import fr.cubiccl.generator.gameobject.templatetags.custom.TemplateJson;
import fr.cubiccl.generator.gameobject.templatetags.custom.TemplatePatterns;
import fr.cubiccl.generator.gameobject.templatetags.custom.TemplateText;
import fr.cubiccl.generator.gui.LoadingFrame;
import fr.cubiccl.generator.utils.FileUtils;
import fr.cubiccl.generator.utils.Settings;
import fr.cubiccl.generator.utils.Textures;
import fr.cubiccl.generator.utils.Utils;
import fr.cubiccl.generator.utils.Utils.BlockComparator;

public class ObjectRegistry
{
	private static final HashMap<String, Achievement> achievements = new HashMap<String, Achievement>();
	private static final HashMap<String, Attribute> attributes = new HashMap<String, Attribute>();
	private static final HashMap<String, Block> blocks = new HashMap<String, Block>();
	private static final HashMap<String, TemplateTag> blockTags = new HashMap<String, TemplateTag>(), entityTags = new HashMap<String, TemplateTag>(),
			itemTags = new HashMap<String, TemplateTag>();
	private static final HashMap<String, Container> containers = new HashMap<String, Container>();
	private static final HashMap<String, EffectType> effects = new HashMap<String, EffectType>();
	private static final HashMap<String, EnchantmentType> enchantments = new HashMap<String, EnchantmentType>();
	private static final HashMap<String, Entity> entities = new HashMap<String, Entity>();
	private static final HashMap<Integer, String> itemIDs = new HashMap<Integer, String>(), blockIDs = new HashMap<Integer, String>(),
			effectIDs = new HashMap<Integer, String>(), enchantmentIDs = new HashMap<Integer, String>();
	private static final HashMap<String, Item> items = new HashMap<String, Item>();
	private static final HashMap<String, ArrayList<String>> objectLists = new HashMap<String, ArrayList<String>>();
	private static final HashMap<String, Particle> particles = new HashMap<String, Particle>();
	public static final byte SORT_ALPHABETICALLY = 0, SORT_NUMERICALLY = 1;
	private static final HashMap<String, Sound> sounds = new HashMap<String, Sound>();

	private static void addToLists(String objectId, String... lists)
	{
		for (String list : lists)
		{
			if (!objectLists.containsKey(list)) objectLists.put(list, new ArrayList<String>());
			if (!objectLists.get(list).contains(objectId)) objectLists.get(list).add(objectId);
		}
	}

	public static void createAchievements(String[] data)
	{
		CommandGenerator.log("Creating Achievements...");
		achievements.clear();
		for (String a : data)
		{
			String[] values = a.split(",");
			new Achievement(values[0], getItemFromID(values[1]));
		}
		CommandGenerator.log("Successfully created " + achievements.size() + " achievements.");
	}

	public static void createAttributes(String[] data)
	{
		CommandGenerator.log("Creating Attributes...");
		attributes.clear();
		for (String id : data)
			new Attribute(id);
		CommandGenerator.log("Successfully created " + attributes.size() + " attributes.");
	}

	public static void createBlocks(String[] data)
	{
		CommandGenerator.log("Creating Blocks...");
		blocks.clear();
		blockIDs.clear();
		items.clear();
		itemIDs.clear();
		for (String block : data)
		{
			String[] values = block.split(",");
			String idString = values[1], damage = null;
			int idInt = Integer.parseInt(values[0]), maxDamage = 0, langType = 0, textureType = 0;
			boolean item = true;

			for (String a : values)
			{
				if (a.startsWith("damage=")) maxDamage = Integer.parseInt(a.substring("damage=".length()));
				else if (a.startsWith("lang=")) langType = Integer.parseInt(a.substring("lang=".length()));
				else if (a.startsWith("texture=")) textureType = Integer.parseInt(a.substring("texture=".length()));
				else if (a.startsWith("damage_custom=")) damage = a.substring("damage_custom=".length());
				else if (a.equals("blockOnly")) item = false;
				else if (a.startsWith("lists=")) addToLists(idString, a.substring("lists=".length()).split(":"));
			}

			Block b;
			if (damage == null) b = new Block(idInt, idString, maxDamage);
			else b = new Block(idInt, idString, createDamage(damage));
			b.langType = langType;
			b.textureType = textureType;

			if (item)
			{
				Item i;
				if (damage == null) i = new Item(idInt, idString, maxDamage);
				else i = new Item(idInt, idString, createDamage(damage));
				i.langType = langType;
				i.textureType = textureType;
				addToLists(idString, "placeable");
			}
		}
		CommandGenerator.log("Successfully created " + blocks.size() + " blocks.");
	}

	private static void createContainers(String[] data)
	{
		CommandGenerator.log("Creating Containers...");
		containers.clear();
		for (String a : data)
		{
			String[] values = a.split(",");
			Slot[] slots;
			if (values.length == 3)
			{
				String[] slotData = values[2].split(":");
				slots = new Slot[Integer.parseInt(slotData[0])];

				int slotsPerLine = Integer.parseInt(slotData[1]), startX = Integer.parseInt(slotData[2]), y = Integer.parseInt(slotData[3]), x = startX;
				for (int i = 0; i < slots.length; ++i)
				{
					slots[i] = new Slot(i, x, y);
					x += Slot.SIZE + 2;
					if ((i + 1) % slotsPerLine == 0)
					{
						x = startX;
						y += Slot.SIZE + 2;
					}

				}
			} else
			{
				slots = new Slot[values.length - 2];
				for (int i = 2; i < values.length; ++i)
				{
					String[] slot = values[i].split(":");
					slots[i - 2] = new Slot(i - 2, Integer.parseInt(slot[0]), Integer.parseInt(slot[1]));
				}
			}
			new Container(values[0], values[1], slots);
		}
		CommandGenerator.log("Successfully created " + containers.size() + " containers.");
	}

	private static void createCustomTag(String id, byte tagType, String[] applicable, String customTagType, String[] data)
	{
		if (customTagType.equals("armorColor")) new TemplateColor(id, tagType, applicable);
		else if (customTagType.equals("command_stats")) new TemplateCommandStats(id, tagType, applicable);
		else if (customTagType.equals("coordinates")) new TemplateCoordinates(id, tagType, applicable);
		else if (customTagType.equals("block_entity")) new TemplateBlockEntity(id, tagType, applicable);
		else if (customTagType.equals("blockList")) new TemplateBlockList(id, tagType, applicable);
		else if (customTagType.equals("color"))
		{
			TemplateNumber tag = new TemplateNumber(id, tagType, applicable);
			tag.setNames("color", Utils.WOOL_COLORS);
		} else if (customTagType.equals("display")) new TemplateDisplay(id, tagType, applicable);
		else if (customTagType.equals("enchantmentList")) new TemplateEnchantmentList(id, tagType, applicable);
		else if (customTagType.equals("effect"))
		{
			TemplateNumber tag = new TemplateNumber(id, tagType, applicable);
			EffectType[] effects = getEffectTypes(SORT_NUMERICALLY);
			String[] ids = new String[effects.length];
			for (int i = 0; i < ids.length; ++i)
				ids[i] = effects[i].idString;
			tag.setNames("effect", ids);
		} else if (customTagType.equals("effectList")) new TemplateEffectList(id, tagType, applicable);
		else if (customTagType.equals("hideFlags")) new TemplateHideFlags(id, tagType, applicable);
		else if (customTagType.equals("item"))
		{
			TemplateItem t = new TemplateItem(id, tagType, applicable);
			for (int i = 3; i < data.length; ++i)
			{
				if (data[i].startsWith("limited=")) t.setLimited(data[i].substring("limited=".length()).split(":"));
				if (data[i].startsWith("autoselect=")) t.setAutoselect(data[i].substring("autoselect=".length()));
			}
		} else if (customTagType.equals("item_id"))
		{
			TemplateItemId t = new TemplateItemId(id, tagType, applicable);
			if (data.length == 4) t.setLimited(data[3].substring("limited=".length()).split(":"));
		} else if (customTagType.equals("json")) new TemplateJson(id, tagType, applicable);
		else if (customTagType.equals("patterns")) new TemplatePatterns(id, tagType, applicable);
		else if (customTagType.equals("text")) new TemplateText(id, tagType, applicable);
	}

	private static int[] createDamage(String damageList)
	{
		String[] values = damageList.split(":");
		int[] damage = new int[values.length];
		for (int i = 0; i < damage.length; ++i)
			damage[i] = Integer.parseInt(values[i]);
		return damage;
	}

	public static void createEffects(String[] data)
	{
		CommandGenerator.log("Creating Effects...");
		effects.clear();
		effectIDs.clear();
		for (String a : data)
		{
			String[] values = a.split(",");
			new EffectType(Integer.parseInt(values[0]), values[1]);
		}
		CommandGenerator.log("Successfully created " + effects.size() + " effects.");
	}

	public static void createEnchantments(String[] data)
	{
		CommandGenerator.log("Creating Enchantments...");
		enchantments.clear();
		enchantmentIDs.clear();
		for (String a : data)
		{
			String[] values = a.split(",");
			new EnchantmentType(Integer.parseInt(values[0]), values[1], Integer.parseInt(values[2]));
		}
		CommandGenerator.log("Successfully created " + enchantments.size() + " enchantments.");
	}

	public static void createEntities(String[] data)
	{
		CommandGenerator.log("Creating Entities...");
		entities.clear();
		for (String id : data)
			new Entity(id);
		CommandGenerator.log("Successfully created " + entities.size() + " entities.");
	}

	public static void createItems(String[] data)
	{
		CommandGenerator.log("Creating Items...");
		for (String item : data)
		{
			String[] values = item.split(",");
			String idString = values[1], damage = null;
			int idInt = Integer.parseInt(values[0]), maxDamage = 0, langType = 0, textureType = 0;
			boolean durability = false;

			for (String a : values)
			{
				if (a.startsWith("durability="))
				{
					langType = -1;
					textureType = -1;
					maxDamage = Integer.parseInt(a.substring("durability=".length()));
					durability = true;
					addToLists(idString, "durability");
				} else if (a.startsWith("damage=")) maxDamage = Integer.parseInt(a.substring("damage=".length()));
				else if (a.startsWith("lang=")) langType = Integer.parseInt(a.substring("lang=".length()));
				else if (a.startsWith("texture=")) textureType = Integer.parseInt(a.substring("texture=".length()));
				else if (a.startsWith("damage_custom=")) damage = a.substring("damage_custom=".length());
				else if (a.startsWith("lists=")) addToLists(idString, a.substring("lists=".length()).split(":"));
			}

			Item i;
			if (damage == null) i = new Item(idInt, idString, maxDamage);
			else i = new Item(idInt, idString, createDamage(damage));
			i.langType = langType;
			i.textureType = textureType;
			i.hasDurability = durability;
		}
		CommandGenerator.log("Successfully created " + items.size() + " items.");
	}

	public static void createObjects(LoadingFrame frame)
	{
		frame.setText("loading.objects");
		String[] data = FileUtils.readFileAsArray("data/" + Settings.getVersion().name + ".txt");
		ArrayList<String> blocks = new ArrayList<String>(), items = new ArrayList<String>(), entities = new ArrayList<String>(), effects = new ArrayList<String>(), enchantments = new ArrayList<String>(), achievements = new ArrayList<String>(), attributes = new ArrayList<String>(), particles = new ArrayList<String>(), sounds = new ArrayList<String>(), containers = new ArrayList<String>(), blocktags = new ArrayList<String>(), itemtags = new ArrayList<String>(), entitytags = new ArrayList<String>();

		int current = -1;
		for (String line : data)
		{
			if (line.startsWith("["))
			{
				++current;
				continue;
			}
			switch (current)
			{
				case 0:
					blocks.add(line);
					break;

				case 1:
					items.add(line);
					break;

				case 2:
					String[] e = line.split(",");
					for (String entity : e)
						entities.add(entity);
					break;

				case 3:
					effects.add(line);
					break;

				case 4:
					enchantments.add(line);
					break;

				case 5:
					achievements.add(line);
					break;

				case 6:
					String[] a = line.split(",");
					for (String attribute : a)
						attributes.add(attribute);
					break;

				case 7:
					String[] p = line.split(",");
					for (String particle : p)
						particles.add(particle);
					break;

				case 8:
					String[] s = line.split(",");
					for (String sound : s)
						sounds.add(sound);
					break;

				case 9:
					containers.add(line);
					break;

				case 10:
					blocktags.add(line);
					break;

				case 11:
					itemtags.add(line);
					break;

				case 12:
					entitytags.add(line);
					break;
			}
		}

		createBlocks(blocks.toArray(new String[blocks.size()]));
		createItems(items.toArray(new String[items.size()]));
		createEntities(entities.toArray(new String[entities.size()]));
		createEffects(effects.toArray(new String[effects.size()]));
		createEnchantments(enchantments.toArray(new String[enchantments.size()]));
		createAchievements(achievements.toArray(new String[achievements.size()]));
		createAttributes(attributes.toArray(new String[attributes.size()]));
		createParticles(particles.toArray(new String[particles.size()]));
		createSounds(sounds.toArray(new String[sounds.size()]));
		createContainers(containers.toArray(new String[containers.size()]));
		createTags(blocktags.toArray(new String[blocktags.size()]), Tag.BLOCK);
		createTags(itemtags.toArray(new String[itemtags.size()]), Tag.ITEM);
		createTags(entitytags.toArray(new String[entitytags.size()]), Tag.ENTITY);
		CommandGenerator.log("Successfully created " + objectLists.size() + " object lists.");
		TargetArgument.createArguments();
	}

	public static void createParticles(String[] data)
	{
		CommandGenerator.log("Creating Particles...");
		particles.clear();
		for (String id : data)
			new Particle(id);
		CommandGenerator.log("Successfully created " + particles.size() + " particles.");
	}

	public static void createSounds(String[] data)
	{
		CommandGenerator.log("Creating Sounds...");
		sounds.clear();
		for (String id : data)
			new Sound(id);
		CommandGenerator.log("Successfully created " + sounds.size() + " sounds.");
	}

	private static void createTag(byte tagType, String[] data)
	{
		String id = data[0];
		String[] applicable = data[2].split(":");
		if (applicable.length == 1 && applicable[0].startsWith("list=")) applicable = getList(applicable[0].substring("list=".length()));
		switch (data[1].substring(0, 3))
		{
			case "num":
				TemplateNumber number = new TemplateNumber(id, tagType, Byte.parseByte(data[1].substring(3, 4)), applicable);
				for (int i = 3; i < data.length; ++i)
				{
					if (data[i].startsWith("bounds="))
					{
						String[] bounds = data[i].substring("bounds=".length()).split(":");
						number.setBounds(Double.parseDouble(bounds[0]), Double.parseDouble(bounds[1]));
					} else if (data[i].startsWith("values="))
					{
						String[] v = data[i].substring("values=".length()).split(":");
						int[] values = new int[v.length];
						for (int j = 0; j < values.length; ++j)
							values[j] = Integer.parseInt(v[j]);
						number.setValues(values);
					} else if (data[i].startsWith("named=")) number.setNames(data[i].substring("named=".length(), data[i].indexOf('^')),
							data[i].substring(data[i].indexOf('^') + 1).split(":"));

				}
				break;

			case "ite":
				new TemplateItems(id, tagType, applicable);
				break;

			case "cus":
				createCustomTag(id, tagType, applicable, data[1].split(":")[1], data);
				break;

			case "str":
			default:
				TemplateString t = new TemplateString(id, tagType, applicable);
				for (int i = 3; i < data.length; ++i)
				{
					if (data[i].startsWith("custom=")) t.setValues(data[i].substring("custom=".length()).split(":"));
				}
				break;
		}
	}

	public static void createTags(String[] data, byte tagType)
	{
		CommandGenerator.log("Creating NBT Tags...");
		if (tagType == Tag.BLOCK) blockTags.clear();
		else if (tagType == Tag.ITEM) itemTags.clear();
		else if (tagType == Tag.ENTITY) entityTags.clear();
		for (String tag : data)
			createTag(tagType, tag.split(","));

		if (tagType == Tag.BLOCK) CommandGenerator.log("Successfully created " + blockTags.size() + " Block NBT Tags.");
		else if (tagType == Tag.ITEM) CommandGenerator.log("Successfully created " + itemTags.size() + " Item NBT Tags.");
		else if (tagType == Tag.ENTITY) CommandGenerator.log("Successfully created " + entityTags.size() + " Entity NBT Tags.");
	}

	public static Achievement getAchievementFromID(String id)
	{
		return achievements.get(id);
	}

	public static Achievement[] getAchievements()
	{
		ArrayList<Achievement> a = new ArrayList<Achievement>();
		a.addAll(achievements.values());
		a.sort(new Comparator<Achievement>()
		{
			@Override
			public int compare(Achievement o1, Achievement o2)
			{
				return o1.id.compareTo(o2.id);
			}
		});
		return a.toArray(new Achievement[a.size()]);
	}

	public static Attribute getAttributeFromID(String id)
	{
		return attributes.get(id);
	}

	public static Attribute[] getAttributes()
	{
		ArrayList<Attribute> a = new ArrayList<Attribute>();
		a.addAll(attributes.values());
		a.sort(new Comparator<Attribute>()
		{
			@Override
			public int compare(Attribute o1, Attribute o2)
			{
				return o1.id.compareTo(o2.id);
			}
		});
		return a.toArray(new Attribute[a.size()]);
	}

	public static Block getBlockFromID(int id)
	{
		return getBlockFromID(blockIDs.get(id));
	}

	public static Block getBlockFromID(String id)
	{
		return blocks.get(id);
	}

	public static Block[] getBlocks()
	{
		return getBlocks(SORT_ALPHABETICALLY);
	}

	public static Block[] getBlocks(int sortType)
	{
		ArrayList<Block> a = new ArrayList<Block>();
		a.addAll(blocks.values());

		if (sortType == SORT_ALPHABETICALLY) a.sort(new Comparator<Block>()
		{
			@Override
			public int compare(Block o1, Block o2)
			{
				return o1.idString.compareTo(o2.idString);
			}
		});
		else a.sort(new BlockComparator());

		return a.toArray(new Block[a.size()]);
	}

	public static Block[] getBlocks(String... ids)
	{
		Block[] blocks = new Block[ids.length];
		for (int i = 0; i < blocks.length; ++i)
			blocks[i] = getBlockFromID(ids[i]);
		return blocks;
	}

	public static Container getContainerFromID(String id)
	{
		return containers.get(id);
	}

	public static EffectType getEffectFromID(int id)
	{
		return getEffectFromID(effectIDs.get(id));
	}

	public static EffectType getEffectFromID(String id)
	{
		return effects.get(id);
	}

	public static EffectType[] getEffectTypes()
	{
		return getEffectTypes(SORT_ALPHABETICALLY);
	}

	public static EffectType[] getEffectTypes(int sortType)
	{
		ArrayList<EffectType> a = new ArrayList<EffectType>();
		a.addAll(effects.values());

		if (sortType == SORT_ALPHABETICALLY) a.sort(new Comparator<EffectType>()
		{
			@Override
			public int compare(EffectType o1, EffectType o2)
			{
				return o1.idString.compareTo(o2.idString);
			}
		});
		else a.sort(new Comparator<EffectType>()
		{
			@Override
			public int compare(EffectType o1, EffectType o2)
			{
				return o1.idInt - o2.idInt;
			}
		});

		return a.toArray(new EffectType[a.size()]);
	}

	public static EnchantmentType getEnchantmentFromID(int id)
	{
		return getEnchantmentFromID(enchantmentIDs.get(id));
	}

	public static EnchantmentType getEnchantmentFromID(String id)
	{
		return enchantments.get(id);
	}

	public static EnchantmentType[] getEnchantmentTypes()
	{
		ArrayList<EnchantmentType> a = new ArrayList<EnchantmentType>();
		a.addAll(enchantments.values());
		a.sort(new Comparator<EnchantmentType>()
		{
			@Override
			public int compare(EnchantmentType o1, EnchantmentType o2)
			{
				return o1.idString.compareTo(o2.idString);
			}
		});
		return a.toArray(new EnchantmentType[a.size()]);
	}

	public static Entity[] getEntities()
	{
		ArrayList<Entity> a = new ArrayList<Entity>();
		a.addAll(entities.values());
		a.sort(new Comparator<Entity>()
		{
			@Override
			public int compare(Entity o1, Entity o2)
			{
				return o1.id.compareTo(o2.id);
			}
		});
		return a.toArray(new Entity[a.size()]);
	}

	public static Entity getEntityFromID(String id)
	{
		return entities.get(id);
	}

	public static Item getItemFromID(int id)
	{
		return getItemFromID(itemIDs.get(id));
	}

	public static Item getItemFromID(String id)
	{
		return items.get(id);
	}

	public static Item[] getItemList(String listId)
	{
		String[] ids = getList(listId);
		Item[] items = new Item[ids.length];
		for (int i = 0; i < items.length; ++i)
			items[i] = getItemFromID(ids[i]);
		return items;
	}

	public static Item[] getItems()
	{
		return getItems(SORT_ALPHABETICALLY);
	}

	public static Item[] getItems(int sortType)
	{
		ArrayList<Item> a = new ArrayList<Item>();
		a.addAll(items.values());

		if (sortType == SORT_ALPHABETICALLY) a.sort(new Comparator<Item>()
		{
			@Override
			public int compare(Item o1, Item o2)
			{
				return o1.idString.compareTo(o2.idString);
			}
		});
		else a.sort(new Comparator<Item>()
		{
			@Override
			public int compare(Item o1, Item o2)
			{
				return o1.idInt - o2.idInt;
			}
		});

		return a.toArray(new Item[a.size()]);
	}

	public static Item[] getItems(String... ids)
	{
		Item[] items = new Item[ids.length];
		for (int i = 0; i < items.length; ++i)
			items[i] = getItemFromID(ids[i]);
		return items;
	}

	public static String[] getList(String listId)
	{
		if (!objectLists.containsKey(listId)) return new String[0];
		return objectLists.get(listId).toArray(new String[objectLists.get(listId).size()]);
	}

	public static Particle getParticleFromID(String id)
	{
		return particles.get(id);
	}

	public static Particle[] getParticles()
	{
		ArrayList<Particle> a = new ArrayList<Particle>();
		a.addAll(particles.values());
		a.sort(new Comparator<Particle>()
		{
			@Override
			public int compare(Particle o1, Particle o2)
			{
				return o1.id.compareTo(o2.id);
			}
		});
		return a.toArray(new Particle[a.size()]);
	}

	public static Sound getSoundFromID(String id)
	{
		return sounds.get(id);
	}

	public static Sound[] getSounds()
	{
		ArrayList<Sound> a = new ArrayList<Sound>();
		a.addAll(sounds.values());
		a.sort(new Comparator<Sound>()
		{
			@Override
			public int compare(Sound o1, Sound o2)
			{
				return o1.id.compareTo(o2.id);
			}
		});
		return a.toArray(new Sound[a.size()]);
	}

	public static TemplateTag[] getTags(int tagType)
	{
		ArrayList<TemplateTag> a = new ArrayList<TemplateTag>();

		if (tagType == Tag.BLOCK) a.addAll(blockTags.values());
		if (tagType == Tag.ITEM) a.addAll(itemTags.values());
		if (tagType == Tag.ENTITY) a.addAll(entityTags.values());

		a.sort(new Comparator<TemplateTag>()
		{
			@Override
			public int compare(TemplateTag o1, TemplateTag o2)
			{
				return o1.id.toLowerCase().compareTo(o2.id.toLowerCase());
			}
		});

		return a.toArray(new TemplateTag[a.size()]);
	}

	public static void loadTextures(LoadingFrame frame)
	{
		CommandGenerator.log("Loading textures...");
		Textures.createTextures();

		frame.setText("loading.textures.block");
		for (Block block : blocks.values())
			for (int d : block.damage)
				block.texture(d);

		frame.setText("loading.textures.item");
		for (Item item : items.values())
			for (int d : item.damage)
				item.texture(d);

		frame.setText("loading.textures.other");
		for (EffectType effect : effects.values())
			effect.texture();

		for (Container container : containers.values())
			container.texture();
	}

	public static void registerAchievement(Achievement achievement)
	{
		achievements.put(achievement.id, achievement);
	}

	public static void registerAttribute(Attribute attribute)
	{
		attributes.put(attribute.id, attribute);
	}

	public static void registerBlock(Block block)
	{
		blocks.put(block.idString, block);
		blockIDs.put(block.idInt, block.idString);
	}

	public static void registerContainer(Container container)
	{
		containers.put(container.id, container);
	}

	public static void registerEffect(EffectType effect)
	{
		effects.put(effect.idString, effect);
		effectIDs.put(effect.idInt, effect.idString);
	}

	public static void registerEnchantment(EnchantmentType enchantment)
	{
		enchantments.put(enchantment.idString, enchantment);
		enchantmentIDs.put(enchantment.idInt, enchantment.idString);
	}

	public static void registerEntity(Entity entity)
	{
		entities.put(entity.id, entity);
	}

	public static void registerItem(Item item)
	{
		items.put(item.idString, item);
		itemIDs.put(item.idInt, item.idString);
	}

	public static void registerParticle(Particle particle)
	{
		particles.put(particle.id, particle);
	}

	public static void registerSound(Sound sound)
	{
		sounds.put(sound.id, sound);
	}

	public static void registerTag(TemplateTag tag, int tagType)
	{
		if (tagType == Tag.BLOCK) blockTags.put(tag.id, tag);
		else if (tagType == Tag.ITEM) itemTags.put(tag.id, tag);
		else if (tagType == Tag.ENTITY) entityTags.put(tag.id, tag);
	}

	private ObjectRegistry()
	{}

}
