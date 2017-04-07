package fr.cubiccl.generator.gameobject.advancements;

import java.util.ArrayList;

import org.jdom2.Element;

import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.utils.Text;

public class AdvancementCriteria
{

	public static enum CriteriaTrigger
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

		public final String id;

		private CriteriaTrigger(String id)
		{
			this.id = id;
		}

		public Text description()
		{
			return new Text("advancement.criteria." + this.id);
		}
	}

	private ArrayList<Tag> conditions;
	public final CriteriaTrigger trigger;

	public AdvancementCriteria(CriteriaTrigger trigger)
	{
		this.trigger = trigger;
		this.conditions = new ArrayList<Tag>();
	}

	public void addCondition(Tag condition)
	{
		this.conditions.add(condition);
	}

	public Tag[] getConditions()
	{
		return this.conditions.toArray(new Tag[this.conditions.size()]);
	}

	public void removeCondition(Tag condition)
	{
		this.conditions.remove(condition);
	}

	public TagCompound toTag(TemplateCompound container)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Element toXML()
	{
		// TODO Auto-generated method stub
		return null;
	}

}
