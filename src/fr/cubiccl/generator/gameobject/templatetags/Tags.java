package fr.cubiccl.generator.gameobject.templatetags;

import static fr.cubiccl.generator.gameobject.tags.Tag.UNAVAILABLE;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound.DefaultCompound;
import fr.cubiccl.generator.gameobject.templatetags.TemplateList.DefaultList;
import fr.cubiccl.generator.gameobject.templatetags.custom.*;
import fr.cubiccl.generator.gameobject.templatetags.custom.advancements.*;
import fr.cubiccl.generator.utils.Utils;

/** Contains unique and constant NBT Tags. */
public final class Tags
{
	public static final TemplateNumber ABILITIES_FLYING = new TemplateNumber("flying", UNAVAILABLE, Tag.BYTE);
	public static final TemplateNumber ABILITIES_INSTABUILD = new TemplateNumber("instabuild", UNAVAILABLE, Tag.BYTE);
	public static final TemplateNumber ABILITIES_INVULNERABLE = new TemplateNumber("invulnerable", UNAVAILABLE, Tag.BYTE);
	public static final TemplateNumber ABILITIES_MAY_BUILD = new TemplateNumber("mayBuild", UNAVAILABLE, Tag.BYTE);
	public static final TemplateNumber ABILITIES_MAY_FLY = new TemplateNumber("mayFly", UNAVAILABLE, Tag.BYTE);

	public static final TemplateBoolean ADVANCEMENT_ANNOUNCE = new TemplateBoolean("announce_to_chat", UNAVAILABLE);
	public static final TemplateString ADVANCEMENT_BACKGROUND = new TemplateString("background", UNAVAILABLE);
	public static final DefaultCompound ADVANCEMENT_CONDITIONS = new DefaultCompound("conditions");
	public static final DefaultCompound ADVANCEMENT_CRITERIA = new DefaultCompound("criteria");
	public static final TemplateString ADVANCEMENT_DESCRIPTION = new TemplateString("description", UNAVAILABLE);
	public static final DefaultCompound ADVANCEMENT_DISPLAY = new DefaultCompound("display");
	public static final TemplateNumber ADVANCEMENT_EXPERIENCE = new TemplateNumber("experience", UNAVAILABLE, Tag.INT);
	public static final TemplateString ADVANCEMENT_FRAME = new TemplateString("frame", UNAVAILABLE);
	public static final TemplateString ADVANCEMENT_FUNCTION = new TemplateString("function", UNAVAILABLE);
	public static final TemplateBoolean ADVANCEMENT_HIDDEN = new TemplateBoolean("hidden", UNAVAILABLE);
	public static final DefaultCompound ADVANCEMENT_ICON = new DefaultCompound("icon");
	public static final DefaultList ADVANCEMENT_LOOT = new DefaultList("loot");
	public static final TemplateString ADVANCEMENT_PARENT = new TemplateString("parent", UNAVAILABLE);
	public static final DefaultList ADVANCEMENT_RECIPES = new DefaultList("recipes");
	public static final DefaultList ADVANCEMENT_REQUIREMENTS = new DefaultList("requirements");
	public static final DefaultCompound ADVANCEMENT_REWARDS = new DefaultCompound("rewards");
	public static final TemplateString ADVANCEMENT_TITLE = new TemplateString("title", UNAVAILABLE);
	public static final DefaultCompound ADVANCEMENT_TITLE_JSON = new DefaultCompound("conditions");
	public static final TemplateBoolean ADVANCEMENT_TOAST = new TemplateBoolean("show_toast", UNAVAILABLE);
	public static final TemplateString ADVANCEMENT_TRIGGER = new TemplateString("trigger", UNAVAILABLE);

	public static final TemplateNumber ATTRIBUTE_amount = new TemplateNumber("amount", UNAVAILABLE, Tag.DOUBLE);
	public static final TemplateNumber ATTRIBUTE_AMOUNT = new TemplateNumber("Amount", UNAVAILABLE, Tag.DOUBLE);
	public static final DefaultCompound ATTRIBUTE_amount_range = new DefaultCompound("amount");
	public static final TemplateString ATTRIBUTE_attribute_name = new TemplateString("attribute", UNAVAILABLE);
	public static final TemplateString ATTRIBUTE_ATTRIBUTE_NAME = new TemplateString("AttributeName", UNAVAILABLE);
	public static final TemplateNumber ATTRIBUTE_base = new TemplateNumber("base", UNAVAILABLE, Tag.DOUBLE);
	public static final TemplateNumber ATTRIBUTE_BASE = new TemplateNumber("Base", UNAVAILABLE, Tag.DOUBLE);
	public static final TemplateString ATTRIBUTE_modifier_name = new TemplateString("name", UNAVAILABLE);
	public static final TemplateString ATTRIBUTE_MODIFIER_NAME = new TemplateString("Name", UNAVAILABLE);
	public static final DefaultList ATTRIBUTE_modifiers = new DefaultList("modifiers");
	public static final DefaultList ATTRIBUTE_MODIFIERS = new DefaultList("Modifiers");
	public static final TemplateNumber ATTRIBUTE_operation = new TemplateNumber("operation", UNAVAILABLE, Tag.INT);
	public static final TemplateNumber ATTRIBUTE_OPERATION = new TemplateNumber("Operation", UNAVAILABLE, Tag.INT);
	public static final TemplateString ATTRIBUTE_SLOT = new TemplateString("Slot", UNAVAILABLE);
	public static final DefaultList ATTRIBUTE_slots = new DefaultList("slot");
	public static final TemplateNumber ATTRIBUTE_UUIDLEAST = new TemplateNumber("UUIDLeast", UNAVAILABLE, Tag.LONG);
	public static final TemplateNumber ATTRIBUTE_UUIDMOST = new TemplateNumber("UUIDMost", UNAVAILABLE, Tag.LONG);

	public static final TemplateNumber BLOCK_DATA = new TemplateNumber("data", UNAVAILABLE, Tag.BYTE);
	public static final TemplateString BLOCK_ID = new TemplateString("id", UNAVAILABLE);
	public static final DefaultCompound BLOCK_NBT = new DefaultCompound("tag");

	public static final TemplateString COMMAND = new TemplateString("Command", UNAVAILABLE);

	public static final TemplateCompound CONTAINER_CHILD = new DefaultCompound("child");
	public static final TemplateCompound CONTAINER_DAMAGE = new DefaultCompound("damage");
	public static final TemplateCompound CONTAINER_ENTITY = new DefaultCompound("entity");
	public static final TemplateCompound CONTAINER_PARENT = new DefaultCompound("parent");
	public static final TemplateCompound CONTAINER_PARTNER = new DefaultCompound("partner");
	public static final TemplateCompound CONTAINER_POSITION = new DefaultCompound("position");
	public static final TemplateCompound CONTAINER_SLOTS = new DefaultCompound("slots");
	public static final TemplateCompound CONTAINER_VILLAGER = new DefaultCompound("villager");
	public static final TemplateCompound CONTAINER_ZOMBIE = new DefaultCompound("zombie");

	public static final TemplateNumber COORD_X = new TemplateNumber("X", UNAVAILABLE, Tag.DOUBLE);
	public static final TemplateNumber COORD_X_INT = new TemplateNumber("X", UNAVAILABLE, Tag.INT);
	public static final TemplateNumber COORD_Y = new TemplateNumber("Y", UNAVAILABLE, Tag.DOUBLE);
	public static final TemplateNumber COORD_Y_INT = new TemplateNumber("Y", UNAVAILABLE, Tag.INT);
	public static final TemplateNumber COORD_Z = new TemplateNumber("Z", UNAVAILABLE, Tag.DOUBLE);
	public static final TemplateNumber COORD_Z_INT = new TemplateNumber("Z", UNAVAILABLE, Tag.INT);

	public static final TemplateNumber CRITERIA_ABSOLUTE = new TemplateNumber("absolute", UNAVAILABLE, Tag.DOUBLE);
	public static final TemplateCompound CRITERIA_ABSOLUTE_ = new DefaultCompound("absolute");
	public static final TemplateString CRITERIA_BIOME = new TemplateString("biome", UNAVAILABLE, "location", "slept_in_bed");
	public static final TemplateBlockIdString CRITERIA_BLOCK = new TemplateBlockIdString("block", UNAVAILABLE, "enter_block", "placed_block");
	public static final TemplateBoolean CRITERIA_BLOCKED = new TemplateBoolean("blocked", UNAVAILABLE);
	public static final TemplateBoolean CRITERIA_BYPASSARMOR = new TemplateBoolean("bypasses_armor", UNAVAILABLE);
	public static final TemplateBoolean CRITERIA_BYPASSINVUL = new TemplateBoolean("bypasses_invulnerability", UNAVAILABLE);
	public static final TemplateBoolean CRITERIA_BYPASSMAGIC = new TemplateBoolean("bypasses_magic", UNAVAILABLE);
	public static final TemplateCriteriaDamage CRITERIA_DAMAGE = new TemplateCriteriaDamage("damage", "entity_hurt_player", "player_hurt_entity");
	public static final TemplateCompound CRITERIA_DAMAGE_FLAGS = new DefaultCompound("type");
	public static final TemplateNumber CRITERIA_DEALT = new TemplateNumber("dealt", UNAVAILABLE, Tag.DOUBLE);
	public static final TemplateCompound CRITERIA_DEALT_ = new DefaultCompound("dealt");
	public static final TemplateRange CRITERIA_DELTA = new TemplateRange("delta", UNAVAILABLE, Tag.INT, "item_durabillity_changed");
	public static final TemplateString CRITERIA_DIMENSION = new TemplateString("dimension", UNAVAILABLE, "location", "slept_in_bed");
	public static final TemplateString CRITERIA_DIMENSION_FROM = new TemplateString("from", UNAVAILABLE, "changed_dimension");
	public static final TemplateString CRITERIA_DIMENSION_TO = new TemplateString("to", UNAVAILABLE, "changed_dimension");
	public static final TemplateCriteriaEntity CRITERIA_DIRECT_ENTITY = new TemplateCriteriaEntity("direct_entity");
	public static final TemplateCriteriaDistance CRITERIA_DISTANCE = new TemplateCriteriaDistance("distance", "levitation", "nether_travel");
	public static final TemplateRange CRITERIA_DURABILITY = new TemplateRange("durability", UNAVAILABLE, Tag.INT, "item_durabillity_changed");
	public static final TemplateCriteriaEffects CRITERIA_EFFECTS = new TemplateCriteriaEffects("effects", "effects_changed");
	public static final TemplateNumber CRITERIA_EMPTY = new TemplateNumber("empty", UNAVAILABLE, Tag.INT);
	public static final TemplateCompound CRITERIA_EMPTY_ = new DefaultCompound("empty");
	public static final TemplateRange CRITERIA_ENDEREYE_DISTANCE = new TemplateRange("distance", UNAVAILABLE, Tag.INT, "used_ender_eye");
	public static final TemplateCriteriaEntity CRITERIA_ENTITY = new TemplateCriteriaEntity("entity", "entity_killed_player", "player_hurt_entity",
			"player_killed_entity", "summoned_entity", "tame_animal");
	public static final TemplateCriteriaEntity CRITERIA_ENTITY_CHILD = new TemplateCriteriaEntity("child", "bred_animals");
	public static final TemplateString CRITERIA_ENTITY_ID = new TemplateString("type", UNAVAILABLE);
	public static final TemplateCriteriaEntity CRITERIA_ENTITY_PARENT = new TemplateCriteriaEntity("parent", "bred_animals");
	public static final TemplateCriteriaEntity CRITERIA_ENTITY_PARTNER = new TemplateCriteriaEntity("partner", "bred_animals");
	public static final TemplateCriteriaEntity CRITERIA_ENTITY_VILLAGER = new TemplateCriteriaEntity("villager", "cured_zombie_villager", "villager_trade");
	public static final TemplateCriteriaEntity CRITERIA_ENTITY_ZOMBIE = new TemplateCriteriaEntity("zombie", "cured_zombie_villager");
	public static final TemplateString CRITERIA_FEATURE = new TemplateString("feature", UNAVAILABLE, "location", "slept_in_bed");
	public static final TemplateNumber CRITERIA_FULL = new TemplateNumber("full", UNAVAILABLE, Tag.INT);
	public static final TemplateCompound CRITERIA_FULL_ = new DefaultCompound("full");
	public static final TemplateNumber CRITERIA_HORIZONTAL = new TemplateNumber("horizontal", UNAVAILABLE, Tag.DOUBLE);
	public static final TemplateCompound CRITERIA_HORIZONTAL_ = new DefaultCompound("horizontal");
	public static final TemplateBoolean CRITERIA_ISEXPLOSION = new TemplateBoolean("is_explosion", UNAVAILABLE);
	public static final TemplateBoolean CRITERIA_ISFIRE = new TemplateBoolean("is_fire", UNAVAILABLE);
	public static final TemplateBoolean CRITERIA_ISMAGIC = new TemplateBoolean("is_magic", UNAVAILABLE);
	public static final TemplateBoolean CRITERIA_ISPROJECTILE = new TemplateBoolean("is_projectile", UNAVAILABLE);
	public static final TemplateTestedItem CRITERIA_ITEM = new TemplateTestedItem("item", UNAVAILABLE, "consume_item", "enchanted_item",
			"item_durabillity_changed", "placed_block", "used_totem", "villager_trade");
	public static final TemplateTestedItemList CRITERIA_ITEMS = new TemplateTestedItemList("items", UNAVAILABLE, "inventory_changed");
	public static final TemplateCriteriaDamageFlags CRITERIA_KILLINGBLOW = new TemplateCriteriaDamageFlags("killing_blow", "entity_killed_player",
			"player_killed_entity");
	public static final TemplateRange CRITERIA_LEVEL = new TemplateRange("level", UNAVAILABLE, Tag.INT, "construct_beacon");
	public static final TemplateRange CRITERIA_LEVELS = new TemplateRange("levels", UNAVAILABLE, Tag.INT, "enchanted_item");
	public static final TemplateRange CRITERIA_LEVITATION_DURATION = new TemplateRange("duration", UNAVAILABLE, Tag.INT, "levitation");
	public static final TemplateCriteriaLocation CRITERIA_LOCATION = new TemplateCriteriaLocation("location", "placed_block");
	public static final TemplateString CRITERIA_NBT = new TemplateString("nbt", UNAVAILABLE);
	public static final TemplateNumber CRITERIA_OCCUPIED = new TemplateNumber("occupied", UNAVAILABLE, Tag.INT);
	public static final TemplateCompound CRITERIA_OCCUPIED_ = new DefaultCompound("occupied");
	public static final TemplateCriteriaPosition CRITERIA_POSITION = new TemplateCriteriaPosition("position", "location", "slept_in_bed");
	public static final TemplatePotion CRITERIA_POTION = new TemplatePotion("potion", UNAVAILABLE, "brewed_potion");
	public static final TemplateRecipeId CRITERIA_RECIPE = new TemplateRecipeId("recipe", UNAVAILABLE, "recipe_unlocked");
	public static final TemplateCriteriaSlots CRITERIA_SLOTS = new TemplateCriteriaSlots("slots", "inventory_changed");
	public static final TemplateCriteriaEntity CRITERIA_SOURCE_ENTITY = new TemplateCriteriaEntity("source_entity");
	public static final TemplateBlockState CRITERIA_STATE = new TemplateBlockState("state", UNAVAILABLE, "enter_block", "placed_block");
	public static final TemplateNumber CRITERIA_TAKEN = new TemplateNumber("taken", UNAVAILABLE, Tag.DOUBLE);
	public static final TemplateCompound CRITERIA_TAKEN_ = new DefaultCompound("taken");
	public static final TemplateNumber CRITERIA_X = new TemplateNumber("x", UNAVAILABLE, Tag.DOUBLE);
	public static final TemplateCompound CRITERIA_X_ = new DefaultCompound("x");
	public static final TemplateNumber CRITERIA_Y = new TemplateNumber("y", UNAVAILABLE, Tag.DOUBLE);
	public static final TemplateCompound CRITERIA_Y_ = new DefaultCompound("y");
	public static final TemplateNumber CRITERIA_Z = new TemplateNumber("z", UNAVAILABLE, Tag.DOUBLE);
	public static final TemplateCompound CRITERIA_Z_ = new DefaultCompound("z");

	public static final TemplateBoolean DEFAULT_BOOLEAN = new TemplateBoolean("", UNAVAILABLE);
	public static final TemplateNumber DEFAULT_BYTE = new TemplateNumber("", UNAVAILABLE, Tag.BYTE);
	public static final TemplateCompound DEFAULT_COMPOUND = new DefaultCompound("");
	public static final TemplateNumber DEFAULT_DOUBLE = new TemplateNumber("", UNAVAILABLE, Tag.DOUBLE);
	public static final TemplateNumber DEFAULT_FLOAT = new TemplateNumber("", UNAVAILABLE, Tag.FLOAT);
	public static final TemplateNumber DEFAULT_INTEGER = new TemplateNumber("", UNAVAILABLE, Tag.INT);
	public static final TemplateList DEFAULT_LIST = new DefaultList("");
	public static final TemplateNumber DEFAULT_LONG = new TemplateNumber("", UNAVAILABLE, Tag.LONG);
	public static final TemplateNumber DEFAULT_SHORT = new TemplateNumber("", UNAVAILABLE, Tag.SHORT);
	public static final TemplateString DEFAULT_STRING = new TemplateString("", UNAVAILABLE);

	public static final TemplateNumber DISPLAY_COLOR = new TemplateNumber("color", UNAVAILABLE, Tag.INT);
	public static final TemplateString DISPLAY_LOCAL = new TemplateString("LocName", UNAVAILABLE);
	public static final DefaultList DISPLAY_LORE = new DefaultList("Lore");
	public static final TemplateString DISPLAY_NAME = new TemplateString("Name", UNAVAILABLE);

	public static final TemplateNumber EFFECT_amplifier = new TemplateNumber("amplifier", UNAVAILABLE, Tag.INT);
	public static final TemplateNumber EFFECT_AMPLIFIER = new TemplateNumber("Amplifier", UNAVAILABLE, Tag.BYTE);
	public static final DefaultCompound EFFECT_amplifier_ = new DefaultCompound("amplifier");
	public static final TemplateNumber EFFECT_duration = new TemplateNumber("duration", UNAVAILABLE, Tag.INT);
	public static final TemplateNumber EFFECT_DURATION = new TemplateNumber("Duration", UNAVAILABLE, Tag.INT);
	public static final DefaultCompound EFFECT_duration_ = new DefaultCompound("duration");
	public static final TemplateNumber EFFECT_ID = new TemplateNumber("Id", UNAVAILABLE, Tag.BYTE);
	public static final TemplateNumber EFFECT_PARTICLES = new TemplateNumber("ShowParticles", UNAVAILABLE, Tag.BYTE);

	public static final TemplateNumber ENCHANTMENT_ID = new TemplateNumber("id", UNAVAILABLE, Tag.SHORT);
	public static final TemplateString ENCHANTMENT_IDSTRING = new TemplateString("enchantment", UNAVAILABLE);
	public static final TemplateNumber ENCHANTMENT_LVL = new TemplateNumber("lvl", UNAVAILABLE, Tag.SHORT);
	public static final TemplateNumber ENCHANTMENT_LVLINT = new TemplateNumber("levels", UNAVAILABLE, Tag.INT);
	public static final DefaultCompound ENCHANTMENT_LVLRANGE = new DefaultCompound("levels");

	public static final DefaultCompound ENTITY = new DefaultCompound("");
	public static final TemplateString ENTITY_ID = new TemplateString("id", UNAVAILABLE);
	public static final DefaultCompound ENTITY_PROPERTIES = new DefaultCompound("Properties");
	public static final TemplateString ENTITY_TYPE = new TemplateString("Type", UNAVAILABLE);
	public static final TemplateNumber ENTITY_WEIGHT = new TemplateNumber("Weight", UNAVAILABLE);

	public static final TemplateString EVENT_ACTION = new TemplateString("action", UNAVAILABLE);
	public static final DefaultCompound EVENT_CLICK = new DefaultCompound("clickEvent");
	public static final DefaultCompound EVENT_HOVER = new DefaultCompound("hoverEvent");
	public static final TemplateString EVENT_VALUE = new TemplateString("value", UNAVAILABLE);
	public static final DefaultCompound EVENT_VALUE_JSON = new DefaultCompound("value");

	public static final DefaultList FIREWORK_COLORS = new DefaultList("Colors");
	public static final DefaultList FIREWORK_EXPLOSIONS = new DefaultList("Explosions");
	public static final DefaultList FIREWORK_FADE_COLORS = new DefaultList("FadeColors");
	public static final TemplateNumber FIREWORK_FLICKER = new TemplateNumber("Flicker", UNAVAILABLE, Tag.BYTE);
	public static final TemplateNumber FIREWORK_FLIGHT = new TemplateNumber("Flight", UNAVAILABLE, Tag.BYTE);
	public static final TemplateNumber FIREWORK_TRAIL = new TemplateNumber("Trail", UNAVAILABLE, Tag.BYTE);
	public static final TemplateNumber FIREWORK_TYPE = new TemplateNumber("Type", UNAVAILABLE, Tag.BYTE);

	public static final DefaultCompound ITEM = new DefaultCompound("item");
	public static final TemplateNumber ITEM_COUNT = new TemplateNumber("Count", UNAVAILABLE, Tag.BYTE);
	public static final TemplateNumber ITEM_DAMAGE = new TemplateNumber("Damage", UNAVAILABLE, Tag.SHORT);
	public static final DefaultList ITEM_ENCHANTMENTS = new DefaultList("enchantments");
	public static final TemplateString ITEM_ID = new TemplateString("id", UNAVAILABLE);
	public static final TemplateString ITEM_IDITEM = new TemplateString("item", UNAVAILABLE);
	public static final DefaultCompound ITEM_NBT = new DefaultCompound("tag");
	public static final TemplateNumber ITEM_SLOT = new TemplateNumber("Slot", UNAVAILABLE, Tag.BYTE);

	// public static final DefaultList JSON_LIST = new DefaultList("json");
	public static final DefaultCompound JSON_SCORE = new DefaultCompound("score");
	public static final TemplateString JSON_SELECTOR = new TemplateString("selector", UNAVAILABLE);
	public static final TemplateString JSON_TEXT = new TemplateString("text", UNAVAILABLE);
	public static final TemplateString JSON_TRANSLATE = new TemplateString("translate", UNAVAILABLE);

	public static final TemplateNumber LOOTTABLE_BONUS_ROLLS = new TemplateNumber("bonus_rolls", UNAVAILABLE, Tag.FLOAT);
	public static final DefaultCompound LOOTTABLE_BONUS_ROLLS_RANGE = new DefaultCompound("bonus_rolls");
	public static final TemplateString LOOTTABLE_CONDITION = new TemplateString("condition", UNAVAILABLE);
	public static final DefaultList LOOTTABLE_CONDITIONS = new DefaultList("conditions");
	public static final DefaultList LOOTTABLE_ENTRIES = new DefaultList("entries");
	public static final TemplateString LOOTTABLE_ENTRY_NAME = new TemplateString("name", UNAVAILABLE);
	public static final TemplateNumber LOOTTABLE_ENTRY_QUALITY = new TemplateNumber("quality", UNAVAILABLE, Tag.INT);
	public static final TemplateString LOOTTABLE_ENTRY_TYPE = new TemplateString("type", UNAVAILABLE);
	public static final TemplateNumber LOOTTABLE_ENTRY_WEIGHT = new TemplateNumber("weight", UNAVAILABLE, Tag.INT);
	public static final TemplateString LOOTTABLE_FUNCTION_NAME = new TemplateString("name", UNAVAILABLE);
	public static final DefaultList LOOTTABLE_FUNCTIONS = new DefaultList("functions");
	public static final DefaultList LOOTTABLE_POOLS = new DefaultList("pools");
	public static final TemplateNumber LOOTTABLE_ROLLS = new TemplateNumber("rolls", UNAVAILABLE, Tag.INT);
	public static final DefaultCompound LOOTTABLE_ROLLS_RANGE = new DefaultCompound("rolls");

	public static final TemplateNumber LT_CONDITION_CHANCE = new TemplateNumber("chance", UNAVAILABLE, Tag.FLOAT);
	public static final TemplateString LT_CONDITION_ENTITY = new TemplateString("entity", UNAVAILABLE);
	public static final TemplateBoolean LT_CONDITION_ENTITY_ONFIRE = new TemplateBoolean("on_fire", UNAVAILABLE);
	public static final TemplateBoolean LT_CONDITION_KILLED = new TemplateBoolean("inverse", UNAVAILABLE);
	public static final TemplateNumber LT_CONDITION_LOOTING = new TemplateNumber("looting_multiplier", UNAVAILABLE, Tag.FLOAT);
	public static final DefaultCompound LT_CONDITION_PROPERTIES = new DefaultCompound("properties");
	public static final DefaultCompound LT_CONDITION_SCORES = new DefaultCompound("scores");

	public static final TemplateNumber LT_FUNCTION_COUNT = new TemplateNumber("count", UNAVAILABLE, Tag.INT);
	public static final DefaultCompound LT_FUNCTION_COUNT_RANGE = new DefaultCompound("count");
	public static final TemplateNumber LT_FUNCTION_DAMAGE = new TemplateNumber("damage", UNAVAILABLE, Tag.INT);
	public static final DefaultCompound LT_FUNCTION_DAMAGE_RANGE = new DefaultCompound("damage");
	public static final TemplateNumber LT_FUNCTION_DATA = new TemplateNumber("data", UNAVAILABLE, Tag.INT);
	public static final DefaultCompound LT_FUNCTION_DATA_RANGE = new DefaultCompound("data");
	public static final DefaultList LT_FUNCTION_ENCHANTMENTS = new DefaultList("enchantments");
	public static final TemplateNumber LT_FUNCTION_LEVELS = new TemplateNumber("levels", UNAVAILABLE, Tag.INT);
	public static final DefaultCompound LT_FUNCTION_LEVELS_RANGE = new DefaultCompound("levels");
	public static final TemplateNumber LT_FUNCTION_LOOTING_LIMIT = new TemplateNumber("limit", UNAVAILABLE, Tag.INT);
	public static final DefaultList LT_FUNCTION_MODIFIERS = new DefaultList("modifiers");
	public static final TemplateString LT_FUNCTION_NBT = new TemplateString("nbt", UNAVAILABLE);
	public static final TemplateBoolean LT_FUNCTION_TREASURE = new TemplateBoolean("treasure", UNAVAILABLE);

	public static final TemplateString OBJECT_NAME = new TemplateString("ObjectName", UNAVAILABLE);

	public static final DefaultCompound OFFER_BUY = new DefaultCompound("buy");
	public static final DefaultCompound OFFER_BUYB = new DefaultCompound("buyB");
	public static final TemplateNumber OFFER_EXP = new TemplateNumber("rewardExp", UNAVAILABLE, Tag.BYTE);
	public static final TemplateNumber OFFER_MAX_USES = new TemplateNumber("maxUses", UNAVAILABLE, Tag.INT);
	public static final DefaultList OFFER_RECIPES = new DefaultList("Recipes");
	public static final DefaultCompound OFFER_SELL = new DefaultCompound("sell");
	public static final TemplateNumber OFFER_USES = new TemplateNumber("uses", UNAVAILABLE, Tag.INT);

	public static final DefaultCompound PATTERN = new DefaultCompound("Pattern");
	public static final TemplateNumber PATTERN_COLOR = new TemplateNumber("Color", UNAVAILABLE, Tag.INT);
	public static final TemplateString PATTERN_SHAPE = new TemplateString("Pattern", UNAVAILABLE);

	public static final DefaultList POSE_ARM_LEFT = new DefaultList("LeftArm");
	public static final DefaultList POSE_ARM_RIGHT = new DefaultList("RightArm");
	public static final DefaultList POSE_BODY = new DefaultList("Body");
	public static final DefaultList POSE_HEAD = new DefaultList("Head");
	public static final DefaultList POSE_LEG_LEFT = new DefaultList("LeftLeg");
	public static final DefaultList POSE_LEG_RIGHT = new DefaultList("RightLeg");

	public static final TemplateBoolean RECIPE_DISPLAYED = new TemplateBoolean("displayed", UNAVAILABLE);
	public static final TemplateString RECIPE_GROUP = new TemplateString("group", UNAVAILABLE);
	public static final DefaultList RECIPE_INGREDIENTS = new DefaultList("ingredients");
	public static final TemplateNumber RECIPE_ITEM_COUNT = new TemplateNumber("count", UNAVAILABLE, Tag.INT);
	public static final TemplateNumber RECIPE_ITEM_DATA = new TemplateNumber("data", UNAVAILABLE, Tag.INT);
	public static final TemplateString RECIPE_ITEM_ID = new TemplateString("item", UNAVAILABLE);
	public static final DefaultCompound RECIPE_KEY = new DefaultCompound("key");
	public static final DefaultList RECIPE_PATTERN = new DefaultList("pattern");
	public static final DefaultCompound RECIPE_RESULT = new DefaultCompound("result");
	public static final TemplateString RECIPE_TYPE = new TemplateString("type", UNAVAILABLE);
	public static final TemplateBoolean RECIPE_UNLOCKED = new TemplateBoolean("unlocked", UNAVAILABLE);

	public static final TemplateNumber RECIPEBOOK_ISFILTERING = new TemplateNumber("isFilteringCraftable", UNAVAILABLE, Tag.BYTE);
	public static final TemplateNumber RECIPEBOOK_ISGUIOPEN = new TemplateNumber("isGuiOpen", UNAVAILABLE, Tag.BYTE);
	public static final DefaultList RECIPEBOOK_TOBEDISPLAYED = new DefaultList("toBeDisplayed");

	public static final TemplateString SCORE_OBJECTIVE = new TemplateString("objective", UNAVAILABLE);
	public static final TemplateString SCORE_TARGET = new TemplateString("name", UNAVAILABLE);

	public static final TemplateString STATS_BLOCKS_NAME = new TemplateString("AffectedBlocksName", UNAVAILABLE);
	public static final TemplateString STATS_BLOCKS_OBJECTIVE = new TemplateString("AffectedBlocksObjective", UNAVAILABLE);
	public static final TemplateString STATS_ENTITIES_NAME = new TemplateString("AffectedItemsName", UNAVAILABLE);
	public static final TemplateString STATS_ENTITIES_OBJECTIVE = new TemplateString("AffectedEntitiesObjective", UNAVAILABLE);
	public static final TemplateString STATS_ITEMS_NAME = new TemplateString("AffectedEntitiesName", UNAVAILABLE);
	public static final TemplateString STATS_ITEMS_OBJECTIVE = new TemplateString("AffectedItemsObjective", UNAVAILABLE);
	public static final TemplateString STATS_QUERY_NAME = new TemplateString("QueryResultName", UNAVAILABLE);
	public static final TemplateString STATS_QUERY_OBJECTIVE = new TemplateString("QueryResultObjective", UNAVAILABLE);
	public static final TemplateString STATS_SUCCESS_NAME = new TemplateString("SuccessCountName", UNAVAILABLE);
	public static final TemplateString STATS_SUCCESS_OBJECTIVE = new TemplateString("SuccessCountObjective", UNAVAILABLE);

	public static final TemplateString TARGET = new TemplateString("target", UNAVAILABLE);

	public static final TemplateString TEXT_BOLD = new TemplateString("bold", UNAVAILABLE);
	public static final TemplateString TEXT_COLOR = new TemplateString("color", UNAVAILABLE);
	public static final TemplateString TEXT_INSERTION = new TemplateString("insertion", UNAVAILABLE);
	public static final TemplateString TEXT_ITALIC = new TemplateString("italic", UNAVAILABLE);
	public static final TemplateString TEXT_OBFUSCATED = new TemplateString("obfuscated", UNAVAILABLE);
	public static final TemplateString TEXT_STRIKETHROUGH = new TemplateString("strikethrough", UNAVAILABLE);
	public static final TemplateString TEXT_UNDERLINED = new TemplateString("underlined", UNAVAILABLE);

	public static void create()
	{
		ABILITIES_FLYING.isByteBoolean = true;
		ABILITIES_INSTABUILD.isByteBoolean = true;
		ABILITIES_INVULNERABLE.isByteBoolean = true;
		ABILITIES_MAY_BUILD.isByteBoolean = true;
		ABILITIES_MAY_FLY.isByteBoolean = true;
		FIREWORK_FLICKER.isByteBoolean = true;
		FIREWORK_TRAIL.isByteBoolean = true;
		OFFER_EXP.isByteBoolean = true;

		CRITERIA_BIOME.setValues(Utils.BIOMES);
		CRITERIA_BIOME.minecraftPrefix = true;
		CRITERIA_DIMENSION.setValues(Utils.DIMENSIONS);
		CRITERIA_DIMENSION_FROM.setValues(Utils.DIMENSIONS);
		CRITERIA_DIMENSION_TO.setValues(Utils.DIMENSIONS);
		CRITERIA_FEATURE.setValues(Utils.STRUCTURES);
	}

	private Tags()
	{}

}
