package fr.cubiccl.generator.gameobject.registries;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

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

public class ObjectRegistry<T extends BaseObject>
{
	public static final ObjectRegistry<Achievement> achievements = new ObjectRegistry<Achievement>(false, false, Achievement.class);
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
	public static final byte SORT_ALPHABETICALLY = 0, SORT_NUMERICALLY = 1, SORT_NAME = 2;
	public static final ObjectRegistry<Sound> sounds = new ObjectRegistry<Sound>(false, false, Sound.class);
	public static final ObjectRegistry<TemplateTag> unavailableTags = new ObjectRegistry<TemplateTag>(false, false, TemplateTag.class);

	static void addToLists(String objectId, String... lists)
	{
		for (String list : lists)
		{
			if (!objectLists.containsKey(list)) objectLists.put(list, new ArrayList<String>());
			if (!objectLists.get(list).contains(objectId)) objectLists.get(list).add(objectId);
		}
	}

	public static Element allToXML()
	{
		Element root = new Element("data");
		root.addContent(blocks.toXML("blocks"));
		root.addContent(items.toXML("items"));
		root.addContent(entities.toXML("entities"));
		root.addContent(effects.toXML("effects"));
		root.addContent(enchantments.toXML("enchantments"));
		root.addContent(achievements.toXML("achievements"));
		root.addContent(attributes.toXML("attributes"));
		root.addContent(particles.toXML("particles"));
		root.addContent(sounds.toXML("sounds"));
		root.addContent(containers.toXML("containers"));
		root.addContent(blockTags.toXML("blocktags"));
		root.addContent(itemTags.toXML("itemtags"));
		root.addContent(entityTags.toXML("entitytags"));
		root.addContent(listsToXML());
		return root;
	}

	static void checkAllNames()
	{
		achievements.checkNames();
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
		sounds.checkNames();
		unavailableTags.checkNames();
	}

	public static String[] getList(String listId)
	{
		if (objectLists.containsKey(listId)) return objectLists.get(listId).toArray(new String[objectLists.get(listId).size()]);
		return new String[0];
	}

	private static Element listsToXML()
	{
		Element lists = new Element("lists");
		for (String listId : objectLists.keySet())
		{
			Element list = new Element("list");
			list.setAttribute("id", listId);
			for (String id : objectLists.get(listId))
				list.addContent(new Element("object").setText(id));
			lists.addContent(list);
		}
		return lists;
	}

	public static TemplateTag[] listTags(int applicationType)
	{
		if (applicationType == Tag.ENTITY) return entityTags.list();
		else if (applicationType == Tag.ITEM) return itemTags.list();
		return blockTags.list();
	}

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
		attributes.loadTextures();
		blockTags.loadTextures();
		containers.loadTextures();
		effects.loadTextures();
		enchantments.loadTextures();
		entities.loadTextures();
		entityTags.loadTextures();
		itemTags.loadTextures();
		particles.loadTextures();
		sounds.loadTextures();
		unavailableTags.loadTextures();
	}

	static void resetAll()
	{
		achievements.reset();
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
		sounds.reset();
		objectLists.clear();
		unavailableTags.reset();
	}

	protected final Class<T> c;
	protected final boolean hasNumericalIds;
	private final boolean hasTexture;
	protected final HashMap<Integer, String> ids;
	protected final HashMap<String, T> registry;

	ObjectRegistry(boolean hasNumericalIds, boolean hasTexture, Class<T> c)
	{
		this.hasNumericalIds = hasNumericalIds;
		this.hasTexture = hasTexture;
		this.c = c;
		this.registry = new HashMap<String, T>();
		this.ids = this.hasNumericalIds ? new HashMap<Integer, String>() : null;
	}

	public void checkNames()
	{
		for (T object : this.registry.values())
			object.name().toString();
	}

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

	public T find(int id)
	{
		if (!this.hasNumericalIds) return null;
		return this.find(ids.get(id));
	}

	public T find(String id)
	{
		T target = this.registry.get(id);
		if (target == null) target = this.registry.get("minecraft:" + id);
		if (target == null && id != null) target = this.registry.get(id.replaceAll("minecraft:", ""));
		return target;
	}

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

	public T first()
	{
		return this.list()[0];
	}

	public boolean knows(String id)
	{
		return this.registry.containsKey(id) || this.registry.containsKey(id.replaceAll("minecraft:", ""));
	}

	public T[] list()
	{
		return list(Byte.parseByte(Settings.getSetting(Settings.SORT_TYPE)));
	}

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

	public T[] list(String listId)
	{
		return this.find(getList(listId));
	}

	public void loadTextures()
	{
		if (!this.hasTexture) return;
		for (T object : this.registry.values())
			object.texture();
	}

	public void register(T object)
	{
		if (this.knows(object.id())) this.registry.put(object.id().replaceAll("minecraft:", "") + "_double_" + this.doubles(object.id()), object);
		else this.registry.put(object.id().replaceAll("minecraft:", ""), object);
		if (this.hasNumericalIds) this.ids.put(object.idNum(), object.id().replaceAll("minecraft:", ""));
	}

	public void reset()
	{
		this.registry.clear();
		if (this.hasNumericalIds) this.ids.clear();
	}

	public int size()
	{
		return this.registry.size();
	}

	public Element toXML(String name)
	{
		Element root = new Element(name);
		for (T o : this.list(SORT_NUMERICALLY))
			root.addContent(o.toXML());
		return root;
	}

	public void unregister(String ID)
	{
		this.unregister(this.find(ID));
	}

	public void unregister(T object)
	{
		if (object == null) return;
		if (this.registry.containsValue(object)) this.registry.remove(object);
		if (this.hasNumericalIds && this.ids.containsValue(object.id().replaceAll("minecraft:", ""))) this.ids.remove(object.id().replaceAll("minecraft:", ""));
	}

}
