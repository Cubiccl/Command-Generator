package fr.cubiccl.generator.gameobject.templatetags;

import static fr.cubiccl.generator.gameobject.tags.Tag.UNAVAILABLE;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagNumber;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound.DefaultCompound;
import fr.cubiccl.generator.gameobject.templatetags.TemplateList.DefaultList;

/** Contains unique and constant NBT Tags. */
public final class Tags
{
	public static final TemplateNumber ABILITIES_FLYING = new TemplateNumber("flying", UNAVAILABLE, TagNumber.BYTE_BOOLEAN);
	public static final TemplateNumber ABILITIES_INSTABUILD = new TemplateNumber("instabuild", UNAVAILABLE, TagNumber.BYTE_BOOLEAN);
	public static final TemplateNumber ABILITIES_INVULNERABLE = new TemplateNumber("invulnerable", UNAVAILABLE, TagNumber.BYTE_BOOLEAN);
	public static final TemplateNumber ABILITIES_MAY_BUILD = new TemplateNumber("mayBuild", UNAVAILABLE, TagNumber.BYTE_BOOLEAN);
	public static final TemplateNumber ABILITIES_MAY_FLY = new TemplateNumber("mayFly", UNAVAILABLE, TagNumber.BYTE_BOOLEAN);

	public static final TemplateNumber ATTRIBUTE_amount = new TemplateNumber("amount", UNAVAILABLE, TagNumber.DOUBLE);
	public static final TemplateNumber ATTRIBUTE_AMOUNT = new TemplateNumber("Amount", UNAVAILABLE, TagNumber.DOUBLE);
	public static final DefaultCompound ATTRIBUTE_amount_range = new DefaultCompound("amount");
	public static final TemplateString ATTRIBUTE_attribute_name = new TemplateString("attribute", UNAVAILABLE);
	public static final TemplateString ATTRIBUTE_ATTRIBUTE_NAME = new TemplateString("AttributeName", UNAVAILABLE);
	public static final TemplateNumber ATTRIBUTE_base = new TemplateNumber("base", UNAVAILABLE, TagNumber.DOUBLE);
	public static final TemplateNumber ATTRIBUTE_BASE = new TemplateNumber("Base", UNAVAILABLE, TagNumber.DOUBLE);
	public static final TemplateString ATTRIBUTE_modifier_name = new TemplateString("name", UNAVAILABLE);
	public static final TemplateString ATTRIBUTE_MODIFIER_NAME = new TemplateString("Name", UNAVAILABLE);
	public static final DefaultList ATTRIBUTE_modifiers = new DefaultList("modifiers");
	public static final DefaultList ATTRIBUTE_MODIFIERS = new DefaultList("Modifiers");
	public static final TemplateNumber ATTRIBUTE_operation = new TemplateNumber("operation", UNAVAILABLE, TagNumber.INTEGER);
	public static final TemplateNumber ATTRIBUTE_OPERATION = new TemplateNumber("Operation", UNAVAILABLE, TagNumber.INTEGER);
	public static final TemplateString ATTRIBUTE_SLOT = new TemplateString("Slot", UNAVAILABLE);
	public static final DefaultList ATTRIBUTE_slots = new DefaultList("slot");
	public static final TemplateNumber ATTRIBUTE_UUIDLEAST = new TemplateNumber("UUIDLeast", UNAVAILABLE, TagNumber.LONG);
	public static final TemplateNumber ATTRIBUTE_UUIDMOST = new TemplateNumber("UUIDMost", UNAVAILABLE, TagNumber.LONG);

	public static final TemplateNumber BLOCK_DATA = new TemplateNumber("data", UNAVAILABLE, TagNumber.BYTE);
	public static final TemplateString BLOCK_ID = new TemplateString("id", UNAVAILABLE);
	public static final DefaultCompound BLOCK_NBT = new DefaultCompound("tag");

	public static final TemplateString COMMAND = new TemplateString("Command", UNAVAILABLE);

	public static final TemplateNumber COORD_X = new TemplateNumber("X", UNAVAILABLE, TagNumber.DOUBLE);
	public static final TemplateNumber COORD_X_INT = new TemplateNumber("X", UNAVAILABLE, TagNumber.INTEGER);
	public static final TemplateNumber COORD_Y = new TemplateNumber("Y", UNAVAILABLE, TagNumber.DOUBLE);
	public static final TemplateNumber COORD_Y_INT = new TemplateNumber("Y", UNAVAILABLE, TagNumber.INTEGER);
	public static final TemplateNumber COORD_Z = new TemplateNumber("Z", UNAVAILABLE, TagNumber.DOUBLE);
	public static final TemplateNumber COORD_Z_INT = new TemplateNumber("Z", UNAVAILABLE, TagNumber.INTEGER);

	public static final TemplateBoolean DEFAULT_BOOLEAN = new TemplateBoolean("", UNAVAILABLE);
	public static final TemplateNumber DEFAULT_BYTE = new TemplateNumber("", UNAVAILABLE, TagNumber.BYTE);
	public static final TemplateCompound DEFAULT_COMPOUND = new DefaultCompound("");
	public static final TemplateNumber DEFAULT_DOUBLE = new TemplateNumber("", UNAVAILABLE, TagNumber.DOUBLE);
	public static final TemplateNumber DEFAULT_FLOAT = new TemplateNumber("", UNAVAILABLE, TagNumber.FLOAT);
	public static final TemplateNumber DEFAULT_INTEGER = new TemplateNumber("", UNAVAILABLE, TagNumber.INTEGER);
	public static final TemplateList DEFAULT_LIST = new DefaultList("");
	public static final TemplateNumber DEFAULT_LONG = new TemplateNumber("", UNAVAILABLE, TagNumber.LONG);
	public static final TemplateNumber DEFAULT_SHORT = new TemplateNumber("", UNAVAILABLE, TagNumber.SHORT);
	public static final TemplateString DEFAULT_STRING = new TemplateString("", UNAVAILABLE);

	public static final TemplateNumber DISPLAY_COLOR = new TemplateNumber("color", UNAVAILABLE, TagNumber.INTEGER);
	public static final TemplateString DISPLAY_LOCAL = new TemplateString("LocName", UNAVAILABLE);
	public static final DefaultList DISPLAY_LORE = new DefaultList("Lore");
	public static final TemplateString DISPLAY_NAME = new TemplateString("Name", UNAVAILABLE);

	public static final TemplateNumber EFFECT_AMPLIFIER = new TemplateNumber("Amplifier", UNAVAILABLE, TagNumber.BYTE);
	public static final TemplateNumber EFFECT_DURATION = new TemplateNumber("Duration", UNAVAILABLE, TagNumber.BYTE);
	public static final TemplateNumber EFFECT_ID = new TemplateNumber("Id", UNAVAILABLE, TagNumber.BYTE);
	public static final TemplateNumber EFFECT_PARTICLES = new TemplateNumber("ShowParticles", UNAVAILABLE, TagNumber.BYTE);

	public static final TemplateNumber ENCHANTMENT_ID = new TemplateNumber("id", UNAVAILABLE, TagNumber.SHORT);
	public static final TemplateNumber ENCHANTMENT_LVL = new TemplateNumber("lvl", UNAVAILABLE, TagNumber.SHORT);

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
	public static final TemplateNumber FIREWORK_FLICKER = new TemplateNumber("Flicker", UNAVAILABLE, TagNumber.BYTE_BOOLEAN);
	public static final TemplateNumber FIREWORK_FLIGHT = new TemplateNumber("Flight", UNAVAILABLE, TagNumber.BYTE);
	public static final TemplateNumber FIREWORK_TRAIL = new TemplateNumber("Trail", UNAVAILABLE, TagNumber.BYTE_BOOLEAN);
	public static final TemplateNumber FIREWORK_TYPE = new TemplateNumber("Type", UNAVAILABLE, TagNumber.BYTE);

	public static final DefaultCompound ITEM = new DefaultCompound("item");
	public static final TemplateNumber ITEM_COUNT = new TemplateNumber("Count", UNAVAILABLE, TagNumber.BYTE);
	public static final TemplateNumber ITEM_DAMAGE = new TemplateNumber("Damage", UNAVAILABLE, TagNumber.SHORT);
	public static final TemplateString ITEM_ID = new TemplateString("id", Tag.ITEM);
	public static final DefaultCompound ITEM_NBT = new DefaultCompound("tag");
	public static final TemplateNumber ITEM_SLOT = new TemplateNumber("Slot", UNAVAILABLE, TagNumber.SHORT);

	public static final DefaultCompound JSON_CONTAINER = new DefaultCompound("json");
	public static final DefaultList JSON_LIST = new DefaultList("json");
	public static final DefaultCompound JSON_SCORE = new DefaultCompound("score");
	public static final TemplateString JSON_SELECTOR = new TemplateString("selector", UNAVAILABLE);
	public static final TemplateString JSON_TEXT = new TemplateString("text", UNAVAILABLE);
	public static final TemplateString JSON_TRANSLATE = new TemplateString("translate", UNAVAILABLE);

	public static final TemplateNumber LOOTTABLE_BONUS_ROLLS = new TemplateNumber("bonus_rolls", UNAVAILABLE, TagNumber.FLOAT);
	public static final TemplateNumber LOOTTABLE_BONUS_ROLLS_MAX = new TemplateNumber("max", UNAVAILABLE, TagNumber.FLOAT);
	public static final TemplateNumber LOOTTABLE_BONUS_ROLLS_MIN = new TemplateNumber("min", UNAVAILABLE, TagNumber.FLOAT);
	public static final DefaultCompound LOOTTABLE_BONUS_ROLLS_RANGE = new DefaultCompound("bonus_rolls");
	public static final TemplateString LOOTTABLE_CONDITION = new TemplateString("condition", UNAVAILABLE);
	public static final DefaultList LOOTTABLE_CONDITIONS = new DefaultList("conditions");
	public static final DefaultList LOOTTABLE_ENTRIES = new DefaultList("entries");
	public static final TemplateString LOOTTABLE_ENTRY_NAME = new TemplateString("name", UNAVAILABLE);
	public static final TemplateNumber LOOTTABLE_ENTRY_QUALITY = new TemplateNumber("quality", UNAVAILABLE, TagNumber.INTEGER);
	public static final TemplateString LOOTTABLE_ENTRY_TYPE = new TemplateString("type", UNAVAILABLE);
	public static final TemplateNumber LOOTTABLE_ENTRY_WEIGHT = new TemplateNumber("weight", UNAVAILABLE, TagNumber.INTEGER);
	public static final TemplateString LOOTTABLE_FUNCTION_NAME = new TemplateString("name", UNAVAILABLE);
	public static final DefaultList LOOTTABLE_FUNCTIONS = new DefaultList("functions");
	public static final DefaultList LOOTTABLE_POOLS = new DefaultList("pools");
	public static final TemplateNumber LOOTTABLE_ROLLS = new TemplateNumber("rolls", UNAVAILABLE, TagNumber.INTEGER);
	public static final TemplateNumber LOOTTABLE_ROLLS_MAX = new TemplateNumber("max", UNAVAILABLE, TagNumber.INTEGER);
	public static final TemplateNumber LOOTTABLE_ROLLS_MIN = new TemplateNumber("min", UNAVAILABLE, TagNumber.INTEGER);
	public static final DefaultCompound LOOTTABLE_ROLLS_RANGE = new DefaultCompound("rolls");

	public static final TemplateNumber LT_CONDITION_CHANCE = new TemplateNumber("chance", UNAVAILABLE, TagNumber.FLOAT);
	public static final TemplateString LT_CONDITION_ENTITY = new TemplateString("entity", UNAVAILABLE);
	public static final TemplateBoolean LT_CONDITION_ENTITY_ONFIRE = new TemplateBoolean("on_fire", UNAVAILABLE);
	public static final TemplateBoolean LT_CONDITION_KILLED = new TemplateBoolean("inverse", UNAVAILABLE);
	public static final TemplateNumber LT_CONDITION_LOOTING = new TemplateNumber("looting_multiplier", UNAVAILABLE, TagNumber.FLOAT);
	public static final DefaultCompound LT_CONDITION_PROPERTIES = new DefaultCompound("properties");
	public static final DefaultCompound LT_CONDITION_SCORES = new DefaultCompound("scores");
	public static final TemplateNumber LT_CONDITION_SCORES_MAX = new TemplateNumber("max", UNAVAILABLE, TagNumber.INTEGER);
	public static final TemplateNumber LT_CONDITION_SCORES_MIN = new TemplateNumber("min", UNAVAILABLE, TagNumber.INTEGER);

	public static final TemplateNumber LT_FUNCTION_COUNT = new TemplateNumber("count", UNAVAILABLE, TagNumber.INTEGER);
	public static final DefaultCompound LT_FUNCTION_COUNT_RANGE = new DefaultCompound("count");
	public static final TemplateNumber LT_FUNCTION_DAMAGE = new TemplateNumber("damage", UNAVAILABLE, TagNumber.INTEGER);
	public static final DefaultCompound LT_FUNCTION_DAMAGE_RANGE = new DefaultCompound("damage");
	public static final TemplateNumber LT_FUNCTION_DATA = new TemplateNumber("data", UNAVAILABLE, TagNumber.INTEGER);
	public static final DefaultCompound LT_FUNCTION_DATA_RANGE = new DefaultCompound("data");
	public static final DefaultList LT_FUNCTION_ENCHANTMENTS = new DefaultList("enchantments");
	public static final TemplateNumber LT_FUNCTION_LEVELS = new TemplateNumber("levels", UNAVAILABLE, TagNumber.INTEGER);
	public static final DefaultCompound LT_FUNCTION_LEVELS_RANGE = new DefaultCompound("levels");
	public static final TemplateNumber LT_FUNCTION_LOOTING_LIMIT = new TemplateNumber("limit", UNAVAILABLE, TagNumber.INTEGER);
	public static final TemplateNumber LT_FUNCTION_MAX = new TemplateNumber("max", UNAVAILABLE, TagNumber.INTEGER);
	public static final TemplateNumber LT_FUNCTION_MAX_FLOAT = new TemplateNumber("max", UNAVAILABLE, TagNumber.FLOAT);
	public static final TemplateNumber LT_FUNCTION_MIN = new TemplateNumber("min", UNAVAILABLE, TagNumber.INTEGER);
	public static final TemplateNumber LT_FUNCTION_MIN_FLOAT = new TemplateNumber("min", UNAVAILABLE, TagNumber.FLOAT);
	public static final DefaultList LT_FUNCTION_MODIFIERS = new DefaultList("modifiers");
	public static final TemplateString LT_FUNCTION_NBT = new TemplateString("nbt", UNAVAILABLE);
	public static final TemplateBoolean LT_FUNCTION_TREASURE = new TemplateBoolean("treasure", UNAVAILABLE);

	public static final TemplateString OBJECT_NAME = new TemplateString("ObjectName", UNAVAILABLE);

	public static final DefaultCompound OFFER_BUY = new DefaultCompound("buy");
	public static final DefaultCompound OFFER_BUYB = new DefaultCompound("buyB");
	public static final TemplateNumber OFFER_EXP = new TemplateNumber("rewardExp", UNAVAILABLE, TagNumber.BYTE_BOOLEAN);
	public static final TemplateNumber OFFER_MAX_USES = new TemplateNumber("maxUses", UNAVAILABLE, TagNumber.INTEGER);
	public static final DefaultList OFFER_RECIPES = new DefaultList("Recipes");
	public static final DefaultCompound OFFER_SELL = new DefaultCompound("sell");
	public static final TemplateNumber OFFER_USES = new TemplateNumber("uses", UNAVAILABLE, TagNumber.INTEGER);

	public static final DefaultCompound PATTERN = new DefaultCompound("Pattern");
	public static final TemplateNumber PATTERN_COLOR = new TemplateNumber("Color", UNAVAILABLE, TagNumber.INTEGER);
	public static final TemplateString PATTERN_SHAPE = new TemplateString("Pattern", UNAVAILABLE);

	public static final DefaultList POSE_ARM_LEFT = new DefaultList("LeftArm");
	public static final DefaultList POSE_ARM_RIGHT = new DefaultList("RightArm");
	public static final DefaultList POSE_BODY = new DefaultList("Body");
	public static final DefaultList POSE_HEAD = new DefaultList("Head");
	public static final DefaultList POSE_LEG_LEFT = new DefaultList("LeftLeg");
	public static final DefaultList POSE_LEG_RIGHT = new DefaultList("RightLeg");

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
	{}

}
