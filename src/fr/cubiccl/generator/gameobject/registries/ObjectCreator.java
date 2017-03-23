package fr.cubiccl.generator.gameobject.registries;

import java.util.ArrayList;

import org.jdom2.Element;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gameobject.baseobjects.*;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagNumber;
import fr.cubiccl.generator.gameobject.target.TargetArgument;
import fr.cubiccl.generator.gameobject.templatetags.*;
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

	public static void createAchievements(Element achievements)
	{
		for (Element achievement : achievements.getChildren())
			new Achievement(achievement.getAttributeValue("id"), ObjectRegistry.items.find(achievement.getAttributeValue("item")));
		CommandGenerator.log("Successfully created " + ObjectRegistry.achievements.size() + " achievements.");
	}

	private static String[] createApplicable(Element applicable)
	{
		ArrayList<String> apps = new ArrayList<String>();
		for (Element app : applicable.getChildren())
			apps.add(app.getText());
		return apps.toArray(new String[apps.size()]);
	}

	public static void createAttributes(Element attributes)
	{
		for (Element attribute : attributes.getChildren())
			new Attribute(attribute.getAttributeValue("id"));
		CommandGenerator.log("Successfully created " + ObjectRegistry.attributes.size() + " attributes.");
	}

	public static void createBlocks(Element blocks)
	{
		for (Element block : blocks.getChildren())
		{
			String idString = block.getAttributeValue("idstr");
			int idInt = Integer.parseInt(block.getAttributeValue("idint"));
			Element custom = block.getChild("customblock");

			if (custom != null) try
			{
				Block b = (Block) Class.forName("fr.cubiccl.generator.gameobject.baseobjects.block.Block" + custom.getText()).getConstructors()[0].newInstance(
						idInt, idString);
				b.customObjectName = custom.getText();
			} catch (Exception e)
			{
				e.printStackTrace();
				System.out.println("Couldn't create custom Block: " + idString);
				continue;
			}
			else
			{
				Block b;
				if (block.getChild("customdamage") != null) b = new Block(idInt, idString, createDamage(block.getChildText("customdamage")));
				else if (block.getChild("maxdamage") != null) b = new Block(idInt, idString, Integer.parseInt(block.getChildText("maxdamage")));
				else b = new Block(idInt, idString);
				if (block.getChild("texture") != null) b.textureType = Integer.parseInt(block.getChildText("texture"));
			}
		}
		CommandGenerator.log("Successfully created " + ObjectRegistry.blocks.size() + " blocks.");
	}

	private static void createContainers(Element containers)
	{
		for (Element container : containers.getChildren())
		{
			ArrayList<Slot> slots = new ArrayList<Slot>();
			for (Element s : container.getChild("slots").getChildren())
				slots.add(new Slot(Integer.parseInt(s.getAttributeValue("id")), Integer.parseInt(s.getAttributeValue("x")), Integer.parseInt(s
						.getAttributeValue("y"))));

			new Container(container.getAttributeValue("id"), createApplicable(container.getChild("applicable")), slots.toArray(new Slot[slots.size()]));
		}
		CommandGenerator.log("Successfully created " + ObjectRegistry.containers.size() + " containers.");
	}

	public static void createCustomTag(String id, byte applicationType, String[] applicable, String customTagType, Element tag)
	{
		if (customTagType.equals("color"))
		{
			TemplateNumber t = id.equals("Base") ? new TemplateNumber(id, applicationType, applicable) : new TemplateNumber(id, applicationType,
					TagNumber.BYTE, applicable);
			t.setNames("color", Utils.WOOL_COLORS);
			t.customTagName = customTagType;
		} else if (customTagType.equals("effect"))
		{
			TemplateNumber t = new TemplateNumber(id, applicationType, applicable);
			EffectType[] effects = ObjectRegistry.effects.list(ObjectRegistry.SORT_NUMERICALLY);
			String[] ids = new String[effects.length];
			for (int i = 0; i < ids.length; ++i)
				ids[i] = effects[i].idString.substring("minecraft:".length());
			t.setNames("effect", ids);
			t.customTagName = customTagType;
		} else if (customTagType.equals("item"))
		{
			TemplateItem t = new TemplateItem(id, applicationType, applicable);
			if (tag.getChild("limited") != null)
			{
				ArrayList<String> values = new ArrayList<String>();
				for (Element v : tag.getChild("limited").getChildren())
					values.add(v.getText());
				t.setLimited(values.toArray(new String[values.size()]));
			}
			if (tag.getChild("itemautoselect") != null) t.setAutoselect(tag.getChildText("itemautoselect"));
			t.customTagName = customTagType;
		} else if (customTagType.equals("item_id"))
		{
			TemplateItemId t = new TemplateItemId(id, applicationType, applicable);
			if (tag.getChild("limited") != null)
			{
				ArrayList<String> values = new ArrayList<String>();
				for (Element v : tag.getChild("limited").getChildren())
					values.add(v.getText());
				t.setLimited(values.toArray(new String[values.size()]));
			}
			t.customTagName = customTagType;
		} else if (customTagType.startsWith("DropChances"))
		{
			TemplateDropChances t = new TemplateDropChances(id, applicationType, applicable);
			t.setSlotCount(Integer.parseInt(customTagType.substring("DropChances".length())));
			t.customTagName = "DropChances";
		} else if (customTagType.equals("Items"))
		{
			TemplateItems ti = new TemplateItems(id, applicationType, applicable);
			if (tag.getChild("noslot") != null) ti.hasSlot = !Boolean.parseBoolean(tag.getChildText("noslot"));
			ti.customTagName = customTagType;
		} else try
		{
			Class<?> c = Class.forName("fr.cubiccl.generator.gameobject.templatetags.custom.Template" + customTagType);
			((TemplateTag) c.getConstructors()[0].newInstance(id, applicationType, applicable)).customTagName = customTagType;
		} catch (ClassNotFoundException e)
		{
			CommandGenerator.log("Couldn't find Tag class: " + customTagType);
		} catch (Exception e)
		{
			CommandGenerator.log("Error creating Tag: " + id);
			e.printStackTrace();
		}

	}

	public static int[] createDamage(String damageList)
	{
		String[] values = damageList.split(":");
		int[] damage = new int[values.length];
		for (int i = 0; i < damage.length; ++i)
			damage[i] = Integer.parseInt(values[i]);
		return damage;
	}

	public static void createEffects(Element effects)
	{
		for (Element effect : effects.getChildren())
			new EffectType(Integer.parseInt(effect.getAttributeValue("idint")), effect.getAttributeValue("idstr"));
		CommandGenerator.log("Successfully created " + ObjectRegistry.effects.size() + " effects.");
	}

	public static void createEnchantments(Element enchants)
	{
		for (Element enchant : enchants.getChildren())
		{
			new EnchantmentType(Integer.parseInt(enchant.getAttributeValue("idint")), enchant.getAttributeValue("idstr"), Integer.parseInt(enchant
					.getAttributeValue("maxlevel")));
		}
		CommandGenerator.log("Successfully created " + ObjectRegistry.enchantments.size() + " enchantments.");
	}

	public static void createEntities(Element entities)
	{
		for (Element entity : entities.getChildren())
			new Entity(entity.getAttributeValue("id"));
		CommandGenerator.log("Successfully created " + ObjectRegistry.entities.size() + " entities.");
	}

	public static void createItems(Element items)
	{
		for (Element item : items.getChildren())
		{
			String idString = item.getAttributeValue("idstr");
			int idInt = Integer.parseInt(item.getAttributeValue("idint"));
			Element custom = item.getChild("customitem");

			if (custom != null) try
			{
				Item i = (Item) Class.forName("fr.cubiccl.generator.gameobject.baseobjects.item.Item" + custom.getText()).getConstructors()[0].newInstance(
						idInt, idString);
				i.customObjectName = custom.getText();
			} catch (Exception e)
			{
				e.printStackTrace();
				System.out.println("Couldn't create custom Item: " + idString);
				continue;
			}
			else
			{
				Item i;
				if (item.getChild("customdamage") != null) i = new Item(idInt, idString, createDamage(item.getChildText("customdamage")));
				else if (item.getChild("maxdamage") != null) i = new Item(idInt, idString, Integer.parseInt(item.getChildText("maxdamage")));
				else if (item.getChild("durability") != null)
				{
					i = new Item(idInt, idString, Integer.parseInt(item.getChildText("durability")));
					i.hasDurability = true;
					i.maxStackSize = 1;
				} else i = new Item(idInt, idString);
				if (item.getChild("stacksize") != null) i.maxStackSize = Integer.parseInt(item.getChildText("stacksize"));
				if (item.getChild("texture") != null) i.textureType = Integer.parseInt(item.getChildText("texture"));
				if (item.getChild("cooksto") != null) i.cooksTo = item.getChildText("cooksto");
			}

		}
		CommandGenerator.log("Successfully created " + ObjectRegistry.items.size() + " items.");
	}

	private static void createLists(Element lists)
	{
		for (Element list : lists.getChildren())
		{
			String id = list.getAttributeValue("id");
			ObjectRegistry.createList(id);
			for (Element object : list.getChildren())
				ObjectRegistry.addToLists(object.getText(), id);
		}
	}

	public static void createObjects(LoadingFrame frame)
	{
		frame.setText("loading.objects");
		ObjectRegistry.resetAll();
		Element data = FileUtils.readXMLFile("data/" + Settings.version().codeName);

		createBlocks(data.getChild("blocks"));
		createItems(data.getChild("items"));
		createEntities(data.getChild("entities"));
		createEffects(data.getChild("effects"));
		createEnchantments(data.getChild("enchantments"));
		createAchievements(data.getChild("achievements"));
		createAttributes(data.getChild("attributes"));
		createParticles(data.getChild("particles"));
		createSounds(data.getChild("sounds"));
		createContainers(data.getChild("containers"));
		createTags(data.getChild("blocktags"), Tag.BLOCK);
		createTags(data.getChild("itemtags"), Tag.ITEM);
		createTags(data.getChild("entitytags"), Tag.ENTITY);
		createLists(data.getChild("lists"));
		Tags.create();
		CommandGenerator.log("Successfully created " + ObjectRegistry.objectLists.size() + " object lists.");
		TargetArgument.createArguments();

		ObjectRegistry.checkAllNames();
		ObjectRegistry.loadAllTextures(frame);
	}

	public static void createParticles(Element particles)
	{
		for (Element particle : particles.getChildren())
			new Particle(particle.getAttributeValue("id"));
		CommandGenerator.log("Successfully created " + ObjectRegistry.particles.size() + " particles.");
	}

	public static void createSounds(Element sounds)
	{
		for (Element sound : sounds.getChildren())
			new Sound(sound.getAttributeValue("id"));
		CommandGenerator.log("Successfully created " + ObjectRegistry.sounds.size() + " sounds.");
	}

	private static void createTag(byte applicationType, Element tag)
	{
		String id = tag.getAttributeValue("id");
		String[] applicable = createApplicable(tag.getChild("applicable"));
		if (tag.getChild("customtype") != null) createCustomTag(id, applicationType, applicable, tag.getChildText("customtype"), tag);
		else
		{
			byte type = Byte.parseByte(tag.getChildText("type"));
			if (type == Tag.STRING)
			{
				TemplateString t = new TemplateString(id, applicationType, applicable);
				if (tag.getChild("strvalues") != null)
				{
					ArrayList<String> values = new ArrayList<String>();
					for (Element v : tag.getChild("strvalues").getChildren())
						values.add(v.getText());
					t.setValues(values.toArray(new String[values.size()]));
				}
			} else if (type <= Tag.DOUBLE)
			{
				TemplateNumber number = new TemplateNumber(id, applicationType, type, applicable);
				if (tag.getChild("intnamed") != null)
				{
					if (tag.getChild("intnamed") != null)
					{
						ArrayList<String> values = new ArrayList<String>();
						for (Element v : tag.getChild("intnamed").getChildren())
							values.add(v.getText());
						number.setNames(tag.getChild("intnamed").getAttributeValue("prefix"), values.toArray(new String[values.size()]));
					}
				}
				if (tag.getChild("byteboolean") != null) number.isByteBoolean = Boolean.parseBoolean(tag.getChildText("byteboolean"));
			} else if (type == Tag.BOOLEAN) new TemplateBoolean(id, applicationType, applicable);
		}
	}

	public static void createTags(Element tags, byte applicationType)
	{
		for (Element tag : tags.getChildren())
			createTag(applicationType, tag);

		if (applicationType == Tag.BLOCK) CommandGenerator.log("Successfully created " + ObjectRegistry.blockTags.size() + " Block NBT Tags.");
		else if (applicationType == Tag.ITEM) CommandGenerator.log("Successfully created " + ObjectRegistry.itemTags.size() + " Item NBT Tags.");
		else if (applicationType == Tag.ENTITY) CommandGenerator.log("Successfully created " + ObjectRegistry.entityTags.size() + " Entity NBT Tags.");
	}

	@SuppressWarnings("unused")
	private static void removePrevious(String[] values)
	{
		for (String line : values)
		{
			String type = line.split(" ")[0], id = line.split(" ")[1];
			switch (type)
			{
				case "block":
					ObjectRegistry.blocks.unregister(id);
					break;

				case "item":
					ObjectRegistry.items.unregister(id);
					break;

				case "entity":
					ObjectRegistry.entities.unregister(id);
					break;

				case "effect":
					ObjectRegistry.effects.unregister(id);
					break;

				case "enchantment":
					ObjectRegistry.enchantments.unregister(id);
					break;

				case "achievement":
					ObjectRegistry.achievements.unregister(id);
					break;

				case "attribute":
					ObjectRegistry.attributes.unregister(id);
					break;

				case "particle":
					ObjectRegistry.particles.unregister(id);
					break;

				case "sound":
					ObjectRegistry.sounds.unregister(id);
					break;

				case "container":
					ObjectRegistry.containers.unregister(id);
					break;

				case "block_tag":
					ObjectRegistry.blockTags.unregister(id);
					break;

				case "item_tag":
					ObjectRegistry.itemTags.unregister(id);
					break;

				case "entity_tag":
					ObjectRegistry.entityTags.unregister(id);
					break;

				default:
					break;
			}
		}
	}

	private ObjectCreator()
	{}

}
