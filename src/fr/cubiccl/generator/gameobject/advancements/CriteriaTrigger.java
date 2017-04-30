package fr.cubiccl.generator.gameobject.advancements;

import java.util.ArrayList;

import org.jdom2.Element;

import fr.cubiccl.generator.gameobject.baseobjects.BaseObject;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateTag;
import fr.cubiccl.generator.utils.Text;

public class CriteriaTrigger extends BaseObject
{
	public static final CriteriaTrigger bred_animals = new CriteriaTrigger("bred_animals", Tags.CRITERIA_ENTITY_CHILD, Tags.CRITERIA_ENTITY_PARENT,
			Tags.CRITERIA_ENTITY_PARTNER);
	public static final CriteriaTrigger brewed_potion = new CriteriaTrigger("brewed_potion", Tags.CRITERIA_POTION);
	public static final CriteriaTrigger changed_dimension = new CriteriaTrigger("changed_dimension", Tags.CRITERIA_DIMENSION_FROM, Tags.CRITERIA_DIMENSION_TO);
	public static final CriteriaTrigger construct_beacon = new CriteriaTrigger("construct_beacon", Tags.CRITERIA_LEVEL);
	public static final CriteriaTrigger cured_zombie_villager = new CriteriaTrigger("cured_zombie_villager", Tags.CRITERIA_ENTITY_VILLAGER,
			Tags.CRITERIA_ENTITY_ZOMBIE);
	public static final CriteriaTrigger enchanted_item = new CriteriaTrigger("enchanted_item", Tags.CRITERIA_ITEM, Tags.CRITERIA_LEVELS);
	public static final CriteriaTrigger enter_block = new CriteriaTrigger("enter_block", Tags.CRITERIA_BLOCK, Tags.CRITERIA_STATE);
	public static final CriteriaTrigger entity_hurt_player = new CriteriaTrigger("entity_hurt_player", Tags.CRITERIA_DAMAGE);
	public static final CriteriaTrigger entity_killed_player = new CriteriaTrigger("entity_killed_player");
	public static final CriteriaTrigger impossible = new CriteriaTrigger("impossible");
	public static final CriteriaTrigger inventory_changed = new CriteriaTrigger("inventory_changed");
	public static final CriteriaTrigger levitation = new CriteriaTrigger("levitation");
	public static final CriteriaTrigger location = new CriteriaTrigger("location");
	public static final CriteriaTrigger player_hurt_entity = new CriteriaTrigger("player_hurt_entity");
	public static final CriteriaTrigger player_killed_entity = new CriteriaTrigger("player_killed_entity");
	public static final CriteriaTrigger recipe_unlocked = new CriteriaTrigger("recipe_unlocked");
	public static final CriteriaTrigger slept_in_bed = new CriteriaTrigger("slept_in_bed");
	public static final CriteriaTrigger summoned_entity = new CriteriaTrigger("summoned_entity");
	private static ArrayList<CriteriaTrigger> triggers;
	public static final CriteriaTrigger used_ender_eye = new CriteriaTrigger("used_ender_eye");
	public static final CriteriaTrigger villager_trade = new CriteriaTrigger("villager_trade");

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
			for (TemplateTag tag : trigger.conditions)
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

	public final ArrayList<TemplateTag> conditions;
	public final String id;

	private CriteriaTrigger(String id, TemplateTag... conditions)
	{
		this.id = id;
		this.conditions = new ArrayList<TemplateTag>();
		for (TemplateTag templateTag : conditions)
			this.conditions.add(templateTag);
		if (triggers == null) triggers = new ArrayList<CriteriaTrigger>();
		triggers.add(this);
	}

	public Text description()
	{
		return new Text("advancement.criteria." + this.id);
	}

	public ArrayList<Tag> findContainedTags(Tag t)
	{
		ArrayList<Tag> tags = new ArrayList<Tag>();
		if (!(t instanceof TagCompound)) return tags;
		for (Tag tag : ((TagCompound) t).value())
			if (this.conditions.contains(tag.template)) tags.add(tag);
		return tags;
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
