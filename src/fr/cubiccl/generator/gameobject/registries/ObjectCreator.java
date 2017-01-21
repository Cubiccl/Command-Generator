package fr.cubiccl.generator.gameobject.registries;

import java.util.ArrayList;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gameobject.Container;
import fr.cubiccl.generator.gameobject.baseobjects.*;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagNumber;
import fr.cubiccl.generator.gameobject.target.TargetArgument;
import fr.cubiccl.generator.gameobject.templatetags.TemplateNumber;
import fr.cubiccl.generator.gameobject.templatetags.TemplateString;
import fr.cubiccl.generator.gameobject.templatetags.custom.TemplateDropChances;
import fr.cubiccl.generator.gameobject.templatetags.custom.TemplateItem;
import fr.cubiccl.generator.gameobject.templatetags.custom.TemplateItemId;
import fr.cubiccl.generator.gameobject.templatetags.custom.TemplateItems;
import fr.cubiccl.generator.gui.LoadingFrame;
import fr.cubiccl.generator.utils.FileUtils;
import fr.cubiccl.generator.utils.Settings;
import fr.cubiccl.generator.utils.Utils;

public class ObjectCreator
{

	public static void createAchievements(String[] data)
	{
		CommandGenerator.log("Creating Achievements...");
		for (String a : data)
		{
			String[] values = a.split(",");
			new Achievement(values[0], ObjectRegistry.items.find(values[1]));
		}
		CommandGenerator.log("Successfully created " + ObjectRegistry.achievements.size() + " achievements.");
	}

	public static void createAttributes(String[] data)
	{
		CommandGenerator.log("Creating Attributes...");
		for (String id : data)
			new Attribute(id);
		CommandGenerator.log("Successfully created " + ObjectRegistry.attributes.size() + " attributes.");
	}

	public static void createBlocks(String[] data)
	{
		CommandGenerator.log("Creating Blocks...");
		for (String block : data)
		{
			String[] values = block.split(",");
			String idString = values[1], damage = null;
			int idInt = Integer.parseInt(values[0]), maxDamage = 0, textureType = 0;
			boolean item = true;
			String custom = null;

			for (String a : values)
			{
				if (a.startsWith("custom=")) custom = a.substring("custom=".length());
				else if (a.startsWith("damage=")) maxDamage = Integer.parseInt(a.substring("damage=".length()));
				else if (a.startsWith("texture=")) textureType = Integer.parseInt(a.substring("texture=".length()));
				else if (a.startsWith("damage_custom=")) damage = a.substring("damage_custom=".length());
				else if (a.equals("blockOnly")) item = false;
				else if (a.startsWith("lists=")) ObjectRegistry.addToLists(idString, a.substring("lists=".length()).split(":"));
			}

			Block b;
			if (custom != null) try
			{
				b = (Block) Class.forName("fr.cubiccl.generator.gameobject.baseobjects.block.Block" + custom).getConstructors()[0].newInstance(idInt, idString);
			} catch (Exception e)
			{
				e.printStackTrace();
				System.out.println("Couldn't create custom Block: " + idString);
				continue;
			}
			else if (damage == null) b = new Block(idInt, idString, maxDamage);
			else b = new Block(idInt, idString, createDamage(damage));
			if (textureType != 0) b.textureType = textureType;

			if (item)
			{
				Item i;
				if (custom != null) try
				{
					i = (Item) Class.forName("fr.cubiccl.generator.gameobject.baseobjects.item.Item" + custom).getConstructors()[0]
							.newInstance(idInt, idString);
				} catch (Exception e)
				{
					e.printStackTrace();
					System.out.println("Couldn't create custom Item: " + idString);
					continue;
				}
				else if (damage == null) i = new Item(idInt, idString, maxDamage);
				else i = new Item(idInt, idString, createDamage(damage));
				if (textureType != 0) i.textureType = textureType;

				ObjectRegistry.addToLists(idString, "placeable");
			}
		}
		CommandGenerator.log("Successfully created " + ObjectRegistry.blocks.size() + " blocks.");
	}

	private static void createContainers(String[] data)
	{
		CommandGenerator.log("Creating Containers...");
		for (String a : data)
		{
			String[] values = a.split(",");
			Slot[] slots;
			int startsAt = 0;
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
				if (slotData.length == 5) startsAt = Integer.parseInt(slotData[4]);
			} else
			{
				slots = new Slot[values.length - 2];
				for (int i = 2; i < values.length; ++i)
				{
					String[] slot = values[i].split(":");
					slots[i - 2] = new Slot(i - 2, Integer.parseInt(slot[0]), Integer.parseInt(slot[1]));
				}
			}
			new Container(values[0], values[1], startsAt, slots);
		}
		CommandGenerator.log("Successfully created " + ObjectRegistry.containers.size() + " containers.");
	}

	private static void createCustomTag(String id, byte tagType, String[] applicable, String customTagType, String[] data)
	{
		if (customTagType.equals("color"))
		{
			TemplateNumber tag = id.equals("Base") ? new TemplateNumber(id, tagType, applicable) : new TemplateNumber(id, tagType, TagNumber.BYTE, applicable);
			tag.setNames("color", Utils.WOOL_COLORS);
		} else if (customTagType.equals("effect"))
		{
			TemplateNumber tag = new TemplateNumber(id, tagType, applicable);
			EffectType[] effects = ObjectRegistry.effects.list(ObjectRegistry.SORT_NUMERICALLY);
			String[] ids = new String[effects.length];
			for (int i = 0; i < ids.length; ++i)
				ids[i] = effects[i].idString;
			tag.setNames("effect", ids);
		} else if (customTagType.equals("item"))
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
		} else if (customTagType.startsWith("DropChances"))
		{
			TemplateDropChances t = new TemplateDropChances(id, tagType, applicable);
			t.setSlotCount(Integer.parseInt(customTagType.substring("DropChances".length())));
		} else try
		{
			Class<?> c = Class.forName("fr.cubiccl.generator.gameobject.templatetags.custom.Template" + customTagType);
			c.getConstructors()[0].newInstance(id, tagType, applicable);
		} catch (ClassNotFoundException e)
		{
			CommandGenerator.log("Couldn't find Tag class: " + customTagType);
		} catch (Exception e)
		{
			CommandGenerator.log("Error creating Tag: " + id);
			e.printStackTrace();
		}

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
		for (String a : data)
		{
			String[] values = a.split(",");
			new EffectType(Integer.parseInt(values[0]), values[1]);
		}
		CommandGenerator.log("Successfully created " + ObjectRegistry.effects.size() + " effects.");
	}

	public static void createEnchantments(String[] data)
	{
		CommandGenerator.log("Creating Enchantments...");
		for (String a : data)
		{
			String[] values = a.split(",");
			new EnchantmentType(Integer.parseInt(values[0]), values[1], Integer.parseInt(values[2]));
		}
		CommandGenerator.log("Successfully created " + ObjectRegistry.enchantments.size() + " enchantments.");
	}

	public static void createEntities(String[] data)
	{
		CommandGenerator.log("Creating Entities...");
		for (String line : data)
		{
			String[] lists = line.split(":").length == 2 ? line.split(":")[1].split(",") : new String[0];
			for (String id : line.split(":")[0].split(","))
			{
				new Entity(id);
				ObjectRegistry.addToLists(id, lists);
			}
		}
		CommandGenerator.log("Successfully created " + ObjectRegistry.entities.size() + " entities.");
	}

	public static void createItems(String[] data)
	{
		CommandGenerator.log("Creating Items...");
		for (String item : data)
		{
			String[] values = item.split(",");
			String idString = values[1], damage = null;
			int idInt = Integer.parseInt(values[0]), maxDamage = 0, textureType = 0, langType = 0;
			boolean durability = false;

			for (String a : values)
			{
				if (a.startsWith("durability="))
				{
					textureType = -1;
					langType = -1;
					maxDamage = Integer.parseInt(a.substring("durability=".length()));
					durability = true;
					ObjectRegistry.addToLists(idString, "durability");
				} else if (a.startsWith("damage=")) maxDamage = Integer.parseInt(a.substring("damage=".length()));
				else if (a.startsWith("texture=")) textureType = Integer.parseInt(a.substring("texture=".length()));
				else if (a.startsWith("damage_custom=")) damage = a.substring("damage_custom=".length());
				else if (a.startsWith("lists=")) ObjectRegistry.addToLists(idString, a.substring("lists=".length()).split(":"));
			}

			Item i;
			if (damage == null) i = new Item(idInt, idString, maxDamage);
			else i = new Item(idInt, idString, createDamage(damage));
			i.textureType = textureType;
			i.hasDurability = durability;
			i.langType = langType;
		}
		CommandGenerator.log("Successfully created " + ObjectRegistry.items.size() + " items.");
	}

	public static void createObjects(LoadingFrame frame)
	{
		frame.setText("loading.objects");
		ObjectRegistry.resetAll();
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
					entities.add(line);
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
					sounds.add(line);
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
		CommandGenerator.log("Successfully created " + ObjectRegistry.objectLists.size() + " object lists.");
		TargetArgument.createArguments();

		ObjectRegistry.checkAllNames();
		ObjectRegistry.loadAllTextures(frame);
	}

	public static void createParticles(String[] data)
	{
		CommandGenerator.log("Creating Particles...");
		for (String id : data)
			new Particle(id);
		CommandGenerator.log("Successfully created " + ObjectRegistry.particles.size() + " particles.");
	}

	public static void createSounds(String[] data)
	{
		CommandGenerator.log("Creating Sounds...");
		for (String category : data)
		{
			String prefix = category.substring(0, category.indexOf(':'));
			for (String id : category.substring(category.indexOf(':') + 1).split(","))
				new Sound(prefix + "." + id);
		}
		CommandGenerator.log("Successfully created " + ObjectRegistry.sounds.size() + " sounds.");
	}

	private static void createTag(byte tagType, String[] data)
	{
		String id = data[0];
		String[] applicable = data[2].split(":");
		if (applicable.length == 1 && applicable[0].startsWith("list=")) applicable = ObjectRegistry.getList(applicable[0].substring("list=".length()));
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
				TemplateItems ti = new TemplateItems(id, tagType, applicable);
				for (String arg : data)
					if (arg.equals("noSlot")) ti.hasSlot = false;
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
		for (String tag : data)
			createTag(tagType, tag.split(","));

		if (tagType == Tag.BLOCK) CommandGenerator.log("Successfully created " + ObjectRegistry.blockTags.size() + " Block NBT Tags.");
		else if (tagType == Tag.ITEM) CommandGenerator.log("Successfully created " + ObjectRegistry.itemTags.size() + " Item NBT Tags.");
		else if (tagType == Tag.ENTITY) CommandGenerator.log("Successfully created " + ObjectRegistry.entityTags.size() + " Entity NBT Tags.");
	}

	private ObjectCreator()
	{}

}
