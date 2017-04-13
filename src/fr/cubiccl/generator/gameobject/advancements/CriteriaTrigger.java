package fr.cubiccl.generator.gameobject.advancements;

import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateTag;
import fr.cubiccl.generator.utils.Text;

public enum CriteriaTrigger
{
	bred_animals("bred_animals"),
	brewed_potion("brewed_potion"),
	construct_beacon("construct_beacon", Tags.CRITERIA_BEACON), // TODO Range
	cured_zombie_villager("cured_zombie_villager"),
	enchanted_item("enchanted_item"),
	enter_block("enter_block"),
	entity_killed_player("entity_killed_player"),
	impossible("impossible"),
	inventory_changed("inventory_changed"),
	location("location", Tags.CRITERIA_BIOME),
	player_damaged("player_damaged"),
	player_hurt_entity("player_hurt_entity"),
	player_killed_entity("player_killed_entity"),
	recipe_unlocked("recipe_unlocked"),
	slept_in_bed("slept_in_bed", Tags.CRITERIA_BIOME),
	summoned_entity("summoned_entity"),
	used_ender_eye("used_ender_eye"),
	villager_trade("villager_trade");

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

	public final TemplateTag[] conditions;
	public final String id;

	private CriteriaTrigger(String id, TemplateTag... conditions)
	{
		this.id = id;
		this.conditions = conditions;
	}

	public Text description()
	{
		return new Text("advancement.criteria." + this.id);
	}

}
