package fr.cubiccl.generator.gameobject.registries;

import java.util.ArrayList;

import org.jdom2.Element;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gameobject.baseobjects.*;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.target.ArgumentType;
import fr.cubiccl.generator.gameobject.templatetags.*;
import fr.cubiccl.generator.gui.LoadingFrame;
import fr.cubiccl.generator.utils.FileUtils;
import fr.cubiccl.generator.utils.Settings;

/** Utility class to create Basic Game Objects. */
public class ObjectCreator
{

	/** @param applicable - The input XML element.
	 * @return The list of applicable IDs contained in the input XML list. */
	public static String[] createApplicable(Element applicable)
	{
		ArrayList<String> apps = new ArrayList<String>();
		for (Element app : applicable.getChildren())
			apps.add(app.getText());
		return apps.toArray(new String[apps.size()]);
	}

	/** Creates a Block from the input XML element.
	 * 
	 * @param block - The XML element describing the Block.
	 * @return The created Block. */
	private static Block createBlock(Element block)
	{
		Element custom = block.getChild("customblock");
		if (custom != null) try
		{
			return ((Block) Class.forName("fr.cubiccl.generator.gameobject.baseobjects.block.Block" + custom.getText()).getConstructors()[0].newInstance())
					.fromXML(block);
		} catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("Couldn't create custom Block: " + block.getAttributeValue("idstr"));
			return null;
		}
		return new Block().fromXML(block);
	}

	public static int[] createDamage(String damageList)
	{
		String[] values = damageList.split(":");
		int[] damage = new int[values.length];
		for (int i = 0; i < damage.length; ++i)
			damage[i] = Integer.parseInt(values[i]);
		return damage;
	}

	/** Creates an Item from the input XML element.
	 * 
	 * @param item - The XML element describing the Item.
	 * @return The created Item. */
	private static Item createItem(Element item)
	{
		Element custom = item.getChild("customitem");

		if (custom != null) try
		{
			return ((Item) Class.forName("fr.cubiccl.generator.gameobject.baseobjects.item.Item" + custom.getText()).getConstructors()[0].newInstance())
					.fromXML(item);
		} catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("Couldn't create custom Item: " + item.getAttributeValue("idstr"));
			return null;
		}
		return new Item().fromXML(item);
	}

	/** Creates the Object lists.
	 * 
	 * @param lists - The XML element containing the lists. */
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

	/** Creates all Basic Game Objects from the XML data files.
	 * 
	 * @param frame - The Loading Frame to display progress. */
	public static void createObjects(LoadingFrame frame)
	{
		frame.setText("loading.objects");
		ObjectRegistry.resetAll();
		Element data = FileUtils.readXMLFile("data/" + Settings.version().id);

		for (Element block : data.getChild("blocks").getChildren())
			createBlock(block).register();

		for (Element item : data.getChild("items").getChildren())
			createItem(item).register();

		for (Element entity : data.getChild("entities").getChildren())
			new Entity().fromXML(entity).register();

		for (Element effect : data.getChild("effects").getChildren())
			new EffectType().fromXML(effect).register();

		for (Element enchantment : data.getChild("enchantments").getChildren())
			new EnchantmentType().fromXML(enchantment).register();

		for (Element achievement : data.getChild("achievements").getChildren())
			new Achievement().fromXML(achievement).register();

		for (Element advancement : data.getChild("advancements").getChildren())
			new DefaultAdvancement().fromXML(advancement).register();

		for (Element attribute : data.getChild("attributes").getChildren())
			new Attribute().fromXML(attribute).register();

		for (Element particle : data.getChild("particles").getChildren())
			new Particle().fromXML(particle).register();

		for (Element recipe : data.getChild("recipes").getChildren())
			new RecipeType().fromXML(recipe).register();

		for (Element sound : data.getChild("sounds").getChildren())
			new Sound().fromXML(sound).register();

		for (Element container : data.getChild("containers").getChildren())
			new Container().fromXML(container).register();

		for (Element tag : data.getChild("blocktags").getChildren())
			createTag(Tag.BLOCK, tag).register();

		for (Element tag : data.getChild("itemtags").getChildren())
			createTag(Tag.ITEM, tag).register();

		for (Element tag : data.getChild("entitytags").getChildren())
			createTag(Tag.ENTITY, tag).register();

		createLists(data.getChild("lists"));
		TagsMain.create();
		Tags.create();

		CommandGenerator.log("Basic Game Objects created:");
		CommandGenerator.log(ObjectRegistry.blocks.size() + " blocks.");
		CommandGenerator.log(ObjectRegistry.items.size() + " items.");
		CommandGenerator.log(ObjectRegistry.entities.size() + " entities.");
		CommandGenerator.log(ObjectRegistry.effects.size() + " effects.");
		CommandGenerator.log(ObjectRegistry.enchantments.size() + " enchantments.");
		CommandGenerator.log(ObjectRegistry.achievements.size() + " achievements.");
		CommandGenerator.log(ObjectRegistry.advancements.size() + " advancements.");
		CommandGenerator.log(ObjectRegistry.attributes.size() + " attributes.");
		CommandGenerator.log(ObjectRegistry.particles.size() + " particles.");
		CommandGenerator.log(ObjectRegistry.recipes.size() + " recipes.");
		CommandGenerator.log(ObjectRegistry.sounds.size() + " sounds.");
		CommandGenerator.log(ObjectRegistry.containers.size() + " containers.");
		CommandGenerator.log(ObjectRegistry.blockTags.size() + " block NBT Tags.");
		CommandGenerator.log(ObjectRegistry.itemTags.size() + " item NBT Tags.");
		CommandGenerator.log(ObjectRegistry.entityTags.size() + " entity NBT Tags.");
		CommandGenerator.log(ObjectRegistry.objectLists.size() + " object lists.");
		ArgumentType.createArguments();

		ObjectRegistry.checkAllNames();
		ObjectRegistry.loadAllTextures(frame);
	}

	/** Creates an NBT Tag from the input XML element.
	 * 
	 * @param applicationType - The {@link TemplateTag#applicationType application type} of the NBT Tag.
	 * @param xml - The XML element describing the NBT tag.
	 * @return The created tag. */
	private static TemplateTag createTag(byte applicationType, Element xml)
	{
		TemplateTag tag = null;
		if (xml.getChild("customtype") != null)
		{
			String customTagType = xml.getChildText("customtype");
			try
			{
				Class<?> c = Class.forName("fr.cubiccl.generator.gameobject.templatetags.custom.Template" + customTagType);
				tag = ((TemplateTag) c.getConstructor(new Class[0]).newInstance()).fromXML(xml);
			} catch (ClassNotFoundException e)
			{
				CommandGenerator.log("Couldn't find Tag class: " + customTagType);
				return null;
			} catch (Exception e)
			{
				CommandGenerator.log("Error creating Tag: " + xml.getAttributeValue("id"));
				e.printStackTrace();
				return null;
			}
		} else
		{
			byte type = Byte.parseByte(xml.getChildText("type"));
			if (type == Tag.STRING) tag = new TemplateString().fromXML(xml);
			else if (type <= Tag.DOUBLE) tag = new TemplateNumber().fromXML(xml);
			else if (type == Tag.BOOLEAN) tag = new TemplateBoolean().fromXML(xml);
		}

		if (tag == null) CommandGenerator.log("NBT Tag couldn't be identified: " + xml.getAttributeValue("id"));
		else tag.setApplicationType(applicationType);
		return tag;
	}

	private ObjectCreator()
	{}

}
