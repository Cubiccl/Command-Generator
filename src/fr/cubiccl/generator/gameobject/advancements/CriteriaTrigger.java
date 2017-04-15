package fr.cubiccl.generator.gameobject.advancements;

import java.util.HashMap;

import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gameobject.templatetags.TemplateTag;
import fr.cubiccl.generator.utils.Text;

public enum CriteriaTrigger
{
	bred_animals("bred_animals"),
	brewed_potion("brewed_potion"),
	construct_beacon("construct_beacon"),
	cured_zombie_villager("cured_zombie_villager"),
	enchanted_item("enchanted_item"),
	enter_block("enter_block"),
	entity_killed_player("entity_killed_player"),
	impossible("impossible"),
	inventory_changed("inventory_changed"),
	location("location"),
	player_damaged("player_damaged"),
	player_hurt_entity("player_hurt_entity"),
	player_killed_entity("player_killed_entity"),
	recipe_unlocked("recipe_unlocked"),
	slept_in_bed("slept_in_bed"),
	summoned_entity("summoned_entity"),
	used_ender_eye("used_ender_eye"),
	villager_trade("villager_trade");

	static
	{
		construct_beacon.addCondition(Tags.CRITERIA_BEACON);

		enchanted_item.addCondition(Tags.CRITERIA_LEVELS);

		entity_killed_player.addCondition(Tags.CRITERIA_DISTANCE, Tags.CRITERIA_ENTITY);

		location.addCondition(Tags.CRITERIA_BIOME);

		player_killed_entity.addCondition(Tags.CRITERIA_DISTANCE, Tags.CRITERIA_ENTITY);

		slept_in_bed.addCondition(Tags.CRITERIA_BIOME);

		summoned_entity.addCondition(Tags.CRITERIA_DISTANCE, Tags.CRITERIA_ENTITY);

		used_ender_eye.addCondition(Tags.CRITERIA_DISTANCE);
	}

	public static CriteriaTrigger find(String id)
	{
		for (CriteriaTrigger trigger : values())
			if (trigger.id.equals(id) || id.equals("minecraft:" + trigger.id)) return trigger;
		return construct_beacon;
	}

	public static String[] names()
	{
		CriteriaTrigger[] triggers = values();
		String[] names = new String[triggers.length];
		for (int i = 0; i < names.length; ++i)
			names[i] = triggers[i].id;
		return names;
	}

	/** Describes the available conditions. Keys are the conditions. Values are the containers for those conditions. */
	public final HashMap<TemplateTag, TemplateCompound> conditions;
	public final String id;

	private CriteriaTrigger(String id)
	{
		this.id = id;
		this.conditions = new HashMap<TemplateTag, TemplateCompound>();
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

}
