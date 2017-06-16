package fr.cubiccl.generator.gameobject.advancements;

import java.util.ArrayList;

import org.jdom2.Element;

import fr.cubiccl.generator.gameobject.baseobjects.BaseObject;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateTag;
import fr.cubiccl.generator.utils.Text;

/** The available Triggers for Advancements. */
public class CriterionTrigger extends BaseObject<CriterionTrigger>
{
	// public static final CriteriaTrigger arbitrary_player_tick = new CriteriaTrigger("arbitrary_player_tick"); Deprecated during snapshots.
	public static final CriterionTrigger bred_animals = new CriterionTrigger("bred_animals", Tags.CRITERIA_ENTITY_CHILD, Tags.CRITERIA_ENTITY_PARENT,
			Tags.CRITERIA_ENTITY_PARTNER);
	public static final CriterionTrigger brewed_potion = new CriterionTrigger("brewed_potion", Tags.CRITERIA_POTION);
	public static final CriterionTrigger changed_dimension = new CriterionTrigger("changed_dimension", Tags.CRITERIA_DIMENSION_FROM, Tags.CRITERIA_DIMENSION_TO);
	public static final CriterionTrigger construct_beacon = new CriterionTrigger("construct_beacon", Tags.CRITERIA_LEVEL);
	public static final CriterionTrigger consume_item = new CriterionTrigger("consume_item", Tags.CRITERIA_ITEM);
	public static final CriterionTrigger cured_zombie_villager = new CriterionTrigger("cured_zombie_villager", Tags.CRITERIA_ENTITY_VILLAGER,
			Tags.CRITERIA_ENTITY_ZOMBIE);
	public static final CriterionTrigger effects_changed = new CriterionTrigger("effects_changed", Tags.CRITERIA_EFFECTS);
	public static final CriterionTrigger enchanted_item = new CriterionTrigger("enchanted_item", Tags.CRITERIA_ITEM, Tags.CRITERIA_LEVELS);
	public static final CriterionTrigger enter_block = new CriterionTrigger("enter_block", Tags.CRITERIA_BLOCK, Tags.CRITERIA_STATE);
	public static final CriterionTrigger entity_hurt_player = new CriterionTrigger("entity_hurt_player", Tags.CRITERIA_DAMAGE);
	public static final CriterionTrigger entity_killed_player = new CriterionTrigger("entity_killed_player", Tags.CRITERIA_ENTITY, Tags.CRITERIA_KILLINGBLOW);
	public static final CriterionTrigger impossible = new CriterionTrigger("impossible");
	public static final CriterionTrigger inventory_changed = new CriterionTrigger("inventory_changed", Tags.CRITERIA_ITEMS, Tags.CRITERIA_SLOTS);
	public static final CriterionTrigger item_durabillity_changed = new CriterionTrigger("item_durabillity_changed", Tags.CRITERIA_ITEM,
			Tags.CRITERIA_DURABILITY, Tags.CRITERIA_DELTA);
	public static final CriterionTrigger levitation = new CriterionTrigger("levitation", Tags.CRITERIA_DISTANCE, Tags.CRITERIA_LEVITATION_DURATION);
	public static final CriterionTrigger location = new CriterionTrigger("location", Tags.CRITERIA_BIOME, Tags.CRITERIA_DIMENSION, Tags.CRITERIA_FEATURE,
			Tags.CRITERIA_POSITION);
	public static final CriterionTrigger nether_travel = new CriterionTrigger("nether_travel", Tags.CRITERIA_DISTANCE);
	public static final CriterionTrigger placed_block = new CriterionTrigger("placed_block", Tags.CRITERIA_BLOCK, Tags.CRITERIA_ITEM, Tags.CRITERIA_LOCATION,
			Tags.CRITERIA_STATE);
	public static final CriterionTrigger player_hurt_entity = new CriterionTrigger("player_hurt_entity", Tags.CRITERIA_DAMAGE, Tags.CRITERIA_ENTITY);
	public static final CriterionTrigger player_killed_entity = new CriterionTrigger("player_killed_entity", Tags.CRITERIA_ENTITY, Tags.CRITERIA_KILLINGBLOW);
	public static final CriterionTrigger recipe_unlocked = new CriterionTrigger("recipe_unlocked", Tags.CRITERIA_RECIPE);
	public static final CriterionTrigger slept_in_bed = new CriterionTrigger("slept_in_bed", Tags.CRITERIA_BIOME, Tags.CRITERIA_DIMENSION,
			Tags.CRITERIA_FEATURE, Tags.CRITERIA_POSITION);
	public static final CriterionTrigger summoned_entity = new CriterionTrigger("summoned_entity", Tags.CRITERIA_ENTITY);
	public static final CriterionTrigger tame_animal = new CriterionTrigger("tame_animal", Tags.CRITERIA_ENTITY);
	public static final CriterionTrigger tick = new CriterionTrigger("tick");
	private static ArrayList<CriterionTrigger> triggers;
	public static final CriterionTrigger used_ender_eye = new CriterionTrigger("used_ender_eye", Tags.CRITERIA_ENDEREYE_DISTANCE);
	public static final CriterionTrigger used_totem = new CriterionTrigger("used_totem", Tags.CRITERIA_ITEM);
	public static final CriterionTrigger villager_trade = new CriterionTrigger("villager_trade", Tags.CRITERIA_ITEM, Tags.CRITERIA_ENTITY_VILLAGER);

	/** @param id - The Trigger ID.
	 * @return The Trigger with the input ID. */
	public static CriterionTrigger find(String id)
	{
		for (CriterionTrigger trigger : values())
			if (trigger.id.equals(id) || id.equals("minecraft:" + trigger.id)) return trigger;
		return construct_beacon;
	}

	/** @return All the NBT Tags that can be a condition for a Trigger. */
	public static TemplateTag[] listTags()
	{
		ArrayList<TemplateTag> tags = new ArrayList<TemplateTag>();
		for (CriterionTrigger trigger : values())
			for (TemplateTag tag : trigger.conditions)
				if (!tags.contains(tag)) tags.add(tag);
		tags.sort(new ObjectComparatorID());
		return tags.toArray(new TemplateTag[tags.size()]);
	}

	/** @return The names of the Triggers. */
	public static String[] names()
	{
		CriterionTrigger[] triggers = values();
		String[] names = new String[triggers.length];
		for (int i = 0; i < names.length; ++i)
			names[i] = triggers[i].id;
		return names;
	}

	/** @return The list of Triggers. */
	private static CriterionTrigger[] values()
	{
		return triggers.toArray(new CriterionTrigger[triggers.size()]);
	}

	/** The NBT Tags that can be a condition for this Trigger. */
	public final ArrayList<TemplateTag> conditions;
	/** This Trigger's ID. */
	public final String id;

	private CriterionTrigger(String id, TemplateTag... conditions)
	{
		this.id = id;
		this.conditions = new ArrayList<TemplateTag>();
		for (TemplateTag templateTag : conditions)
			this.conditions.add(templateTag);
		if (triggers == null) triggers = new ArrayList<CriterionTrigger>();
		triggers.add(this);
	}

	/** @return This Trigger's description. */
	public Text description()
	{
		return new Text("advancement.criteria." + this.id);
	}

	@Deprecated
	public ArrayList<Tag> findContainedTags(Tag t)
	{
		ArrayList<Tag> tags = new ArrayList<Tag>();
		if (!(t instanceof TagCompound)) return tags;
		for (Tag tag : ((TagCompound) t).value())
			if (this.conditions.contains(tag.template)) tags.add(tag);
		return tags;
	}

	@Deprecated
	@Override
	public CriterionTrigger fromXML(Element xml)
	{
		return this;
	}

	@Override
	public String id()
	{
		return this.id;
	}

	@Deprecated
	@Override
	public CriterionTrigger register()
	{
		return this;
	}

	@Deprecated
	@Override
	public Element toXML()
	{
		return null;
	}

}
