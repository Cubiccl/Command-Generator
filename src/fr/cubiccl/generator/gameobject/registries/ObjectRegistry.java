package fr.cubiccl.generator.gameobject.registries;

import java.lang.reflect.Array;
import java.util.*;

import org.jdom2.Element;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gameobject.baseobjects.*;
import fr.cubiccl.generator.gameobject.baseobjects.BaseObject.ObjectComparatorID;
import fr.cubiccl.generator.gameobject.baseobjects.BaseObject.ObjectComparatorIDNum;
import fr.cubiccl.generator.gameobject.baseobjects.BaseObject.ObjectComparatorName;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.templatetags.TemplateTag;
import fr.cubiccl.generator.gui.LoadingFrame;
import fr.cubiccl.generator.utils.Settings;
import fr.cubiccl.generator.utils.Textures;

/** Registers all basic Game Objects. */
public class ObjectRegistry<T extends BaseObject<T>>
{
	public static final ObjectRegistry<Achievement> achievements = new ObjectRegistry<Achievement>(false, false, Achievement.class);
	public static final ObjectRegistry<DefaultAdvancement> advancements = new ObjectRegistry<DefaultAdvancement>(false, false, DefaultAdvancement.class);
	public static final ObjectRegistry<Attribute> attributes = new ObjectRegistry<Attribute>(false, false, Attribute.class);
	public static final BlockRegistry blocks = new BlockRegistry();
	public static final ObjectRegistry<TemplateTag> blockTags = new ObjectRegistry<TemplateTag>(false, false, TemplateTag.class);
	public static final ContainerRegistry containers = new ContainerRegistry();
	public static final ObjectRegistry<EffectType> effects = new ObjectRegistry<EffectType>(true, true, EffectType.class);
	public static final ObjectRegistry<EnchantmentType> enchantments = new ObjectRegistry<EnchantmentType>(true, false, EnchantmentType.class);
	public static final EntityRegistry entities = new EntityRegistry();
	public static final ObjectRegistry<TemplateTag> entityTags = new ObjectRegistry<TemplateTag>(false, false, TemplateTag.class);
	public static final ItemRegistry items = new ItemRegistry();
	public static final ObjectRegistry<TemplateTag> itemTags = new ObjectRegistry<TemplateTag>(false, false, TemplateTag.class);
	static final HashMap<String, ArrayList<String>> objectLists = new HashMap<String, ArrayList<String>>();
	public static final ObjectRegistry<Particle> particles = new ObjectRegistry<Particle>(false, false, Particle.class);
	public static final ObjectRegistry<RecipeType> recipes = new ObjectRegistry<RecipeType>(false, false, RecipeType.class);
	/** Identifiers for sorting types.<br />
	 * <br />
	 * <table border="1">
	 * <tr>
	 * <td>ID</td>
	 * <td>Variable</td>
	 * <td>Mode</td>
	 * </tr>
	 * <tr>
	 * <td>0</td>
	 * <td>SORT_ALPHABETICALLY</td>
	 * <td>Sorts by String ID</td>
	 * </tr>
	 * <tr>
	 * <td>1</td>
	 * <td>SORT_NUMERICALLY</td>
	 * <td>Sorts by numerical ID, if any. Else sorts by String ID.</td>
	 * </tr>
	 * <tr>
	 * <td>2</td>
	 * <td>SORT_NAME</td>
	 * <td>Sorts by name.</td>
	 * </tr>
	 * </table> */
	public static final byte SORT_ALPHABETICALLY = 0, SORT_NUMERICALLY = 1, SORT_NAME = 2;
	public static final ObjectRegistry<Sound> sounds = new ObjectRegistry<Sound>(false, false, Sound.class);
	public static final ObjectRegistry<TemplateTag> unavailableTags = new ObjectRegistry<TemplateTag>(false, false, TemplateTag.class);

	/** Adds an Object ID to the input lists.
	 * 
	 * @param objectId - The Object ID to add.
	 * @param lists - The lists to add to. */
	public static void addToLists(String objectId, String... lists)
	{
		for (String list : lists)
		{
			if (!objectLists.containsKey(list)) objectLists.put(list, new ArrayList<String>());
			if (!objectLists.get(list).contains(objectId)) objectLists.get(list).add(objectId);
		}
	}

	/** @return All the registeries in XML format to be saved. */
	public static Element allToXML()
	{
		Element root = new Element("data");
		root.addContent(blocks.toXML("blocks"));
		root.addContent(items.toXML("items"));
		root.addContent(entities.toXML("entities"));
		root.addContent(effects.toXML("effects"));
		root.addContent(enchantments.toXML("enchantments"));
		root.addContent(achievements.toXML("achievements"));
		root.addContent(advancements.toXML("advancements"));
		root.addContent(attributes.toXML("attributes"));
		root.addContent(particles.toXML("particles"));
		root.addContent(recipes.toXML("recipes"));
		root.addContent(sounds.toXML("sounds"));
		root.addContent(containers.toXML("containers"));
		root.addContent(blockTags.toXML("blocktags"));
		root.addContent(itemTags.toXML("itemtags"));
		root.addContent(entityTags.toXML("entitytags"));
		root.addContent(listsToXML());
		return root;
	}

	/** Checks names for all Registries.
	 * 
	 * @see ObjectRegistry#checkNames() */
	static void checkAllNames()
	{
		achievements.checkNames();
		advancements.checkNames();
		attributes.checkNames();
		blocks.checkNames();
		blockTags.checkNames();
		containers.checkNames();
		effects.checkNames();
		enchantments.checkNames();
		entities.checkNames();
		entityTags.checkNames();
		items.checkNames();
		itemTags.checkNames();
		particles.checkNames();
		recipes.checkNames();
		sounds.checkNames();
		unavailableTags.checkNames();
	}

	/** Creates a new List.
	 * 
	 * @param id - The ID of the list. */
	public static void createList(String id)
	{
		if (!objectLists.containsKey(id)) objectLists.put(id, new ArrayList<String>());
	}

	/** @param id - The ID of a List.
	 * @return The List with the input ID. */
	public static String[] getList(String id)
	{
		if (objectLists.containsKey(id))
		{
			ArrayList<String> list = objectLists.get(id);
			list.sort(Comparator.naturalOrder());
			return list.toArray(new String[objectLists.get(id).size()]);
		}
		return new String[0];
	}

	/** @return All lists. */
	public static String[] getLists()
	{
		return objectLists.keySet().toArray(new String[0]);
	}

	/** @param list - A list.
	 * @param object - An Object to test.
	 * @return <code>true</code> if the input List contains the input Object. */
	public static <T> boolean listContains(String list, BaseObject<T> object)
	{
		for (String id : getList(list))
			if (id.replaceAll("minecraft:", "").equals(object.id().replaceAll("minecraft:", ""))) return true;
		return false;
	}

	/** @return The Lists in XML format to be saved. */
	private static Element listsToXML()
	{
		Element lists = new Element("lists");
		for (String listId : objectLists.keySet())
		{
			Element list = new Element("list");
			list.setAttribute("id", listId);
			for (String id : getList(listId))
				list.addContent(new Element("object").setText(id));
			lists.addContent(list);
		}
		return lists;
	}

	/** @param applicationType - An application type.
	 * @return All NBT Tags that can be applied to the input application type.
	 * @see Tag#BLOCK */
	public static TemplateTag[] listTags(int applicationType)
	{
		if (applicationType == Tag.ENTITY) return entityTags.list();
		else if (applicationType == Tag.ITEM) return itemTags.list();
		return blockTags.list();
	}

	/** Loads textures for all registries.
	 * 
	 * @param frame - the Loading Frame to display progress.
	 * @see ObjectRegistry#loadTextures() */
	static void loadAllTextures(LoadingFrame frame)
	{
		CommandGenerator.log("Loading textures...");
		Textures.createTextures();

		frame.setText("loading.textures.block");
		blocks.loadTextures();

		frame.setText("loading.textures.item");
		items.loadTextures();

		frame.setText("loading.textures.other");
		achievements.loadTextures();
		advancements.loadTextures();
		attributes.loadTextures();
		blockTags.loadTextures();
		containers.loadTextures();
		effects.loadTextures();
		enchantments.loadTextures();
		entities.loadTextures();
		entityTags.loadTextures();
		itemTags.loadTextures();
		particles.loadTextures();
		recipes.loadTextures();
		sounds.loadTextures();
		unavailableTags.loadTextures();
	}

	/** Removes an Object ID from the input list.
	 * 
	 * @param objectId - The Object ID to remove.
	 * @param listId - The list to remove from. */
	public static void removeFromList(String objectId, String listId)
	{
		objectLists.get(listId).remove(objectId);
	}

	/** Deletes the input List.
	 * 
	 * @param id - The ID of the List to remove. */
	public static void removeList(String id)
	{
		objectLists.remove(id);
	}

	/** Resets all Registries.
	 * 
	 * @see ObjectRegistry#reset() */
	static void resetAll()
	{
		achievements.reset();
		advancements.reset();
		attributes.reset();
		blocks.reset();
		blockTags.reset();
		containers.reset();
		effects.reset();
		enchantments.reset();
		entities.reset();
		entityTags.reset();
		items.reset();
		itemTags.reset();
		particles.reset();
		recipes.reset();
		sounds.reset();
		objectLists.clear();
		// unavailableTags.reset();
	}

	/** The Class of Objects this Registry stores. */
	protected final Class<T> c;
	/** <code>true</code> if the Objects in this Registry have numerical IDs. */
	protected final boolean hasNumericalIds;
	/** <code>true</code> if the Objects in this Registry have textures. */
	private final boolean hasTexture;
	/** Maps numerical IDs to String IDs. */
	protected final HashMap<Integer, String> ids;
	/** Maps String IDs to Objects. */
	protected final HashMap<String, T> registry;

	ObjectRegistry(boolean hasNumericalIds, boolean hasTexture, Class<T> c)
	{
		this.hasNumericalIds = hasNumericalIds;
		this.hasTexture = hasTexture;
		this.c = c;
		this.registry = new HashMap<String, T>();
		this.ids = this.hasNumericalIds ? new HashMap<Integer, String>() : null;
	}

	/** Checks if the names of all Objects are translated correctly. */
	public void checkNames()
	{
		for (T object : this.registry.values())
			object.name().toString();
	}

	/** @param id - An Object ID.
	 * @return The number of already registered Objects with the same ID as the input ID. */
	private int doubles(String id)
	{
		boolean more = this.registry.containsKey(id);
		int quantity = more ? 0 : -1;
		while (more)
		{
			if (!this.registry.containsKey(id + "_double_" + quantity)) return quantity;
			++quantity;
		}
		return quantity;
	}

	/** @param id - An Object ID.
	 * @return The Object with the input ID. */
	public T find(int id)
	{
		if (!this.hasNumericalIds) return null;
		return this.find(ids.get(id));
	}

	/** @param id - An Object ID.
	 * @return The Object with the input ID. */
	public T find(String id)
	{
		T target = this.registry.get(id);
		if (target == null) target = this.registry.get("minecraft:" + id);
		if (target == null && id != null) target = this.registry.get(id.replaceAll("minecraft:", ""));
		return target;
	}

	/** @param ids - Various Object IDs.
	 * @return The List of Objects with the input IDs. */
	@SuppressWarnings("unchecked")
	public T[] find(String... ids)
	{
		ArrayList<T> objects = new ArrayList<T>();
		for (int i = 0; i < ids.length; ++i)
		{
			if (this.knows(ids[i])) objects.add(this.find(ids[i]));
			for (int j = 0; j < this.doubles(ids[i]); ++j)
				objects.add(this.find(ids[i] + "_double_" + j));
		}
		return objects.toArray((T[]) Array.newInstance(this.c, objects.size()));
	}

	/** @return The first Object in this Registry. */
	public T first()
	{
		if (this.size() == 0) return null;
		return this.list()[0];
	}

	/** @param id - An Object ID.
	 * @return <code>true</code> if this Registry contains an Object with the input ID. */
	public boolean knows(String id)
	{
		return this.registry.containsKey(id) || this.registry.containsKey(id.replaceAll("minecraft:", ""));
	}

	/** @return The list of all Objects in this Registry, sorted as the user requested in the settings. */
	public T[] list()
	{
		return list(Byte.parseByte(Settings.getSetting(Settings.SORT_TYPE)));
	}

	/** @param sortType - How to sort the Objects.
	 * @return The list of all Objects in this Registry.
	 * @see ObjectRegistry#SORT_ALPHABETICALLY */
	@SuppressWarnings("unchecked")
	public T[] list(int sortType)
	{
		Set<T> objects = new HashSet<T>();
		objects.addAll(this.registry.values()); // To prevent duplicate objects (containers)
		ArrayList<T> a = new ArrayList<T>();
		a.addAll(objects);

		if (sortType == SORT_NAME) a.sort(new ObjectComparatorName());
		else if (sortType == SORT_ALPHABETICALLY) a.sort(new ObjectComparatorID());
		else if (sortType == SORT_NUMERICALLY && this.hasNumericalIds) a.sort(new ObjectComparatorIDNum());
		else a.sort(new ObjectComparatorID());

		return a.toArray((T[]) Array.newInstance(this.c, a.size()));
	}

	/** @param listId - The ID of a List.
	 * @return The Objects from this Registry contained in the input List. */
	public T[] list(String listId)
	{
		return this.find(getList(listId));
	}

	/** Loads the Textures of all Objects in this Registry. */
	public void loadTextures()
	{
		if (!this.hasTexture) return;
		for (T object : this.registry.values())
			object.texture();
	}

	/** Adds an Object to this Registry.
	 * 
	 * @param object - The Object to add. */
	public void register(T object)
	{
		if (this.knows(object.id())) this.registry.put(object.id().replaceAll("minecraft:", "") + "_double_" + this.doubles(object.id()), object);
		else this.registry.put(object.id().replaceAll("minecraft:", ""), object);
		if (this.hasNumericalIds) this.ids.put(object.idNum(), object.id().replaceAll("minecraft:", ""));
	}

	/** Removes the input Object from this Registry.
	 * 
	 * @param object - The Object to remove. */
	public void remove(BaseObject<T> object)
	{
		this.registry.remove(object.id().replaceAll("minecraft:", ""), object);
		int quantity = 0;
		while (this.registry.values().contains(object))
		{
			this.registry.remove(object.id().replaceAll("minecraft:", "") + "_double_" + quantity, object);
			++quantity;
		}
	}

	/** Resets this Registry, deleting all Objects. */
	public void reset()
	{
		this.registry.clear();
		if (this.hasNumericalIds) this.ids.clear();
	}

	/** @return The number of Objects in this Registry. */
	public int size()
	{
		return this.registry.size();
	}

	/** @param rootId - The ID of the root XML element.
	 * @return This Registry as an XML element to be saved. */
	public Element toXML(String rootId)
	{
		Element root = new Element(rootId);
		for (T o : this.list(SORT_NUMERICALLY))
			if (o != Entity.PLAYER) root.addContent(o.toXML());
		return root;
	}

	/** Removes the Object with the input ID.
	 * 
	 * @param Id - The ID of the Object to remove. */
	public void unregister(String Id)
	{
		this.unregister(this.find(Id));
	}

	/** Removes the input Object from this Registry.
	 * 
	 * @param object - The Object to remove. */
	public void unregister(T object)
	{
		if (object == null) return;
		if (this.registry.containsValue(object)) this.registry.remove(object);
		if (this.hasNumericalIds && this.ids.containsValue(object.id().replaceAll("minecraft:", ""))) this.ids.remove(object.id().replaceAll("minecraft:", ""));
	}

}
