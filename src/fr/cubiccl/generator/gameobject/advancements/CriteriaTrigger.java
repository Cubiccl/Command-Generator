package fr.cubiccl.generator.gameobject.advancements;

import fr.cubiccl.generator.gameobject.templatetags.TemplateTag;
import fr.cubiccl.generator.utils.Text;

public enum CriteriaTrigger
{
	beacon("construct_beacon"),
	block("enter_block"),
	breed("bred_animals"),
	brew("cured_zombie_villager"),
	damage("player_damaged"),
	dealDamage("player_hurt_entity"),
	death("entity_killed_player"),
	enchant("enchanted_item"),
	enderEye("used_ender_eye"),
	impossible("impossible"),
	inventory("inventory_changed"),
	kill("player_killed_entity"),
	location("location"),
	recipe("recipe_unlocked"),
	sleep("slept_in_bed"),
	summon("summoned_entity"),
	villager("villager_trade");

	public static CriteriaTrigger find(String id)
	{
		for (CriteriaTrigger trigger : values())
			if (trigger.id.equals(id) || id.equals("minecraft:" + trigger.id)) return trigger;
		return beacon;
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
