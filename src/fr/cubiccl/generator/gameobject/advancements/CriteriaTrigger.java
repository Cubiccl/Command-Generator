package fr.cubiccl.generator.gameobject.advancements;

import java.util.ArrayList;
import java.util.HashMap;

import org.jdom2.Element;

import fr.cubiccl.generator.gameobject.baseobjects.BaseObject;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gameobject.templatetags.TemplateTag;
import fr.cubiccl.generator.utils.Text;

public class CriteriaTrigger extends BaseObject
{
	public static final CriteriaTrigger bred_animals = new CriteriaTrigger("bred_animals");
	public static final CriteriaTrigger brewed_potion = new CriteriaTrigger("brewed_potion");
	public static final CriteriaTrigger construct_beacon = new CriteriaTrigger("construct_beacon");
	public static final CriteriaTrigger cured_zombie_villager = new CriteriaTrigger("cured_zombie_villager");
	public static final CriteriaTrigger enchanted_item = new CriteriaTrigger("enchanted_item");
	public static final CriteriaTrigger enter_block = new CriteriaTrigger("enter_block");
	public static final CriteriaTrigger entity_killed_player = new CriteriaTrigger("entity_killed_player");
	public static final CriteriaTrigger impossible = new CriteriaTrigger("impossible");
	public static final CriteriaTrigger inventory_changed = new CriteriaTrigger("inventory_changed");
	public static final CriteriaTrigger location = new CriteriaTrigger("location");
	public static final CriteriaTrigger player_damaged = new CriteriaTrigger("player_damaged");
	public static final CriteriaTrigger player_hurt_entity = new CriteriaTrigger("player_hurt_entity");
	public static final CriteriaTrigger player_killed_entity = new CriteriaTrigger("player_killed_entity");
	public static final CriteriaTrigger recipe_unlocked = new CriteriaTrigger("recipe_unlocked");
	public static final CriteriaTrigger slept_in_bed = new CriteriaTrigger("slept_in_bed");
	public static final CriteriaTrigger summoned_entity = new CriteriaTrigger("summoned_entity");
	private static ArrayList<CriteriaTrigger> triggers;
	public static final CriteriaTrigger used_ender_eye = new CriteriaTrigger("used_ender_eye");
	public static final CriteriaTrigger villager_trade = new CriteriaTrigger("villager_trade");

	static
	{
		construct_beacon.addCondition(Tags.CRITERIA_BEACON);

		cured_zombie_villager.addCondition(Tags.CRITERIA_DISTANCE, Tags.CONTAINER_VILLAGER);

		enchanted_item.addCondition(Tags.CRITERIA_LEVELS);

		entity_killed_player.addCondition(Tags.CRITERIA_DISTANCE, Tags.CONTAINER_ENTITY);

		location.addCondition(Tags.CRITERIA_BIOME);

		player_killed_entity.addCondition(Tags.CRITERIA_DISTANCE, Tags.CONTAINER_ENTITY);

		slept_in_bed.addCondition(Tags.CRITERIA_BIOME);

		summoned_entity.addCondition(Tags.CRITERIA_DISTANCE, Tags.CONTAINER_ENTITY);

		used_ender_eye.addCondition(Tags.CRITERIA_DISTANCE);

		villager_trade.addCondition(Tags.CRITERIA_DISTANCE, Tags.CONTAINER_VILLAGER);
	}

	public static CriteriaTrigger find(String id)
	{
		for (CriteriaTrigger trigger : values())
			if (trigger.id.equals(id) || id.equals("minecraft:" + trigger.id)) return trigger;
		return construct_beacon;
	}

	public static TemplateTag[] listTags()
	{
		ArrayList<TemplateTag> tags = new ArrayList<TemplateTag>();
		for (CriteriaTrigger trigger : values())
			for (TemplateTag tag : trigger.conditions.keySet())
				if (!tags.contains(tag)) tags.add(tag);
		tags.sort(new ObjectComparatorID());
		return tags.toArray(new TemplateTag[tags.size()]);
	}

	public static String[] names()
	{
		CriteriaTrigger[] triggers = values();
		String[] names = new String[triggers.length];
		for (int i = 0; i < names.length; ++i)
			names[i] = triggers[i].id;
		return names;
	}

	private static CriteriaTrigger[] values()
	{
		return triggers.toArray(new CriteriaTrigger[triggers.size()]);
	}

	/** Describes the available conditions. Keys are the conditions. Values are the containers for those conditions. */
	public final HashMap<TemplateTag, TemplateCompound> conditions;

	public final String id;

	private CriteriaTrigger(String id)
	{
		this.id = id;
		this.conditions = new HashMap<TemplateTag, TemplateCompound>();
		if (triggers == null) triggers = new ArrayList<CriteriaTrigger>();
		triggers.add(this);
	}

	private void addCondition(TemplateTag condition)
	{
		this.conditions.put(condition, null);
	}

	private void addCondition(TemplateTag condition, TemplateCompound container)
	{
		this.conditions.put(condition, container);
	}

	public Text description()
	{
		return new Text("advancement.criteria." + this.id);
	}

	@Override
	public String id()
	{
		return this.id;
	}

	@Deprecated
	@Override
	public Element toXML()
	{
		return null;
	}

}
