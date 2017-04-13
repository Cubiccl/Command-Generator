package fr.cubiccl.generator.gameobject.templatetags;

import static fr.cubiccl.generator.gameobject.tags.Tag.UNAVAILABLE;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound.DefaultCompound;
import fr.cubiccl.generator.gameobject.templatetags.TemplateList.DefaultList;

/** Contains unique and constant NBT Tags. */
public final class Tags
{
	public static final TemplateNumber ABILITIES_FLYING = new TemplateNumber("flying", UNAVAILABLE, Tag.BYTE);
	public static final TemplateNumber ABILITIES_INSTABUILD = new TemplateNumber("instabuild", UNAVAILABLE, Tag.BYTE);
	public static final TemplateNumber ABILITIES_INVULNERABLE = new TemplateNumber("invulnerable", UNAVAILABLE, Tag.BYTE);
	public static final TemplateNumber ABILITIES_MAY_BUILD = new TemplateNumber("mayBuild", UNAVAILABLE, Tag.BYTE);
	public static final TemplateNumber ABILITIES_MAY_FLY = new TemplateNumber("mayFly", UNAVAILABLE, Tag.BYTE);

	public static final TemplateString ADVANCEMENT_BACKGROUND = new TemplateString("background", UNAVAILABLE);
	public static final DefaultCompound ADVANCEMENT_CONDITIONS = new DefaultCompound("conditions");
	public static final DefaultCompound ADVANCEMENT_CRITERIA = new DefaultCompound("criteria");
	public static final TemplateString ADVANCEMENT_DESCRIPTION = new TemplateString("description", UNAVAILABLE);
	public static final DefaultCompound ADVANCEMENT_DISPLAY = new DefaultCompound("display");
	public static final TemplateNumber ADVANCEMENT_EXPERIENCE = new TemplateNumber("experience", UNAVAILABLE, Tag.INT);
	public static final TemplateString ADVANCEMENT_FRAME = new TemplateString("frame", UNAVAILABLE);
	public static final TemplateString ADVANCEMENT_ICON = new TemplateString("icon", UNAVAILABLE);
	public static final DefaultList ADVANCEMENT_LOOT = new DefaultList("requirements");
	public static final TemplateString ADVANCEMENT_PARENT = new TemplateString("parent", UNAVAILABLE);
	public static final DefaultList ADVANCEMENT_RECIPES = new DefaultList("requirements");
	public static final DefaultList ADVANCEMENT_REQUIREMENTS = new DefaultList("requirements");
	public static final DefaultCompound ADVANCEMENT_REWARDS = new DefaultCompound("rewards");
	public static final TemplateString ADVANCEMENT_TITLE = new TemplateString("title", UNAVAILABLE);
	public static final DefaultCompound ADVANCEMENT_TITLE_JSON = new DefaultCompound("conditions");
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

	public static final TemplateNumber COORD_X = new TemplateNumber("X", UNAVAILABLE, Tag.DOUBLE);
	public static final TemplateNumber COORD_X_INT = new TemplateNumber("X", UNAVAILABLE, Tag.INT);
	public static final TemplateNumber COORD_Y = new TemplateNumber("Y", UNAVAILABLE, Tag.DOUBLE);
	public static final TemplateNumber COORD_Y_INT = new TemplateNumber("Y", UNAVAILABLE, Tag.INT);
	public static final TemplateNumber COORD_Z = new TemplateNumber("Z", UNAVAILABLE, Tag.DOUBLE);
	public static final TemplateNumber COORD_Z_INT = new TemplateNumber("Z", UNAVAILABLE, Tag.INT);

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

	public static final TemplateNumber EFFECT_AMPLIFIER = new TemplateNumber("Amplifier", UNAVAILABLE, Tag.BYTE);
	public static final TemplateNumber EFFECT_DURATION = new TemplateNumber("Duration", UNAVAILABLE, Tag.INT);
	public static final TemplateNumber EFFECT_ID = new TemplateNumber("Id", UNAVAILABLE, Tag.BYTE);
	public static final TemplateNumber EFFECT_PARTICLES = new TemplateNumber("ShowParticles", UNAVAILABLE, Tag.BYTE);

	public static final TemplateNumber ENCHANTMENT_ID = new TemplateNumber("id", UNAVAILABLE, Tag.SHORT);
	public static final TemplateNumber ENCHANTMENT_LVL = new TemplateNumber("lvl", UNAVAILABLE, Tag.SHORT);

	public static final DefaultCompound ENTITY = new DefaultCompound("");
	public static final TemplateString ENTITY_ID = new TemplateString("id", UNAVAILABLE);
	public static final DefaultCompound ENTITY_PROPERTIES = new DefaultCompound("Properties");
	public static final TemplateString ENTITY_TYPE = new TemplateString("Type", UNAVAILABLE);
	public static final TemplateNumber ENTITY_WEIGHT = new TemplateNumber("Weight", UNAVAILABLE);

	public static final TemplateString EVENT_ACTION = new TemplateString("action", UNAVAILABLE);
	public static final DefaultCompound EVENT_CLICK = new DefaultCompound("ClickEvent");
	public static final DefaultCompound EVENT_HOVER = new DefaultCompound("HoverEvent");
	public static final TemplateString EVENT_VALUE = new TemplateString("value", UNAVAILABLE);

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
	public static final TemplateString ITEM_ID = new TemplateString("id", UNAVAILABLE);
	public static final DefaultCompound ITEM_NBT = new DefaultCompound("tag");
	public static final TemplateNumber ITEM_SLOT = new TemplateNumber("Slot", UNAVAILABLE, Tag.BYTE);

	public static final DefaultCompound JSON_CONTAINER = new DefaultCompound("json");
	public static final DefaultList JSON_LIST = new DefaultList("json");
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

	public static final TemplateNumber VALUE_MAX = new TemplateNumber("max", UNAVAILABLE, Tag.INT);
	public static final TemplateNumber VALUE_MAX_FLOAT = new TemplateNumber("max", UNAVAILABLE, Tag.FLOAT);
	public static final TemplateNumber VALUE_MIN = new TemplateNumber("min", UNAVAILABLE, Tag.INT);
	public static final TemplateNumber VALUE_MIN_FLOAT = new TemplateNumber("min", UNAVAILABLE, Tag.FLOAT);

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
	}

}
