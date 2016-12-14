package fr.cubiccl.generator.gameobject.templatetags;

import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagNumber;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound.DefaultCompound;
import fr.cubiccl.generator.gameobject.templatetags.TemplateList.DefaultList;

/** Contains unique and constant NBT Tags. */
public final class Tags
{
	public static final DefaultCompound BLOCK = new DefaultCompound("block");

	public static final TemplateNumber COORD_X = new TemplateNumber("X", Tag.UNAVAILABLE, TagNumber.INTEGER);
	public static final TemplateNumber COORD_Y = new TemplateNumber("Y", Tag.UNAVAILABLE, TagNumber.INTEGER);
	public static final TemplateNumber COORD_Z = new TemplateNumber("Z", Tag.UNAVAILABLE, TagNumber.INTEGER);

	public static final DefaultCompound ENTITY = new DefaultCompound("entity");
	public static final TemplateString ENTITY_TYPE = new TemplateString("type", Tag.UNAVAILABLE);

	public static final TemplateString EVENT_ACTION = new TemplateString("action", Tag.UNAVAILABLE);
	public static final DefaultCompound EVENT_CLICK = new DefaultCompound("ClickEvent");
	public static final DefaultCompound EVENT_HOVER = new DefaultCompound("HoverEvent");
	public static final TemplateString EVENT_VALUE = new TemplateString("value", Tag.UNAVAILABLE);

	public static final DefaultCompound ITEM = new DefaultCompound("item");
	public static final TemplateNumber ITEM_COUNT = new TemplateNumber("Count", Tag.UNAVAILABLE, TagNumber.BYTE);
	public static final TemplateNumber ITEM_DAMAGE = new TemplateNumber("Damage", Tag.UNAVAILABLE, TagNumber.SHORT);
	public static final TemplateString ITEM_ID = new TemplateString("id", Tag.ITEM);
	public static final DefaultCompound ITEM_NBT = new DefaultCompound("tag");
	public static final TemplateNumber ITEM_SLOT = new TemplateNumber("Slot", Tag.UNAVAILABLE, TagNumber.SHORT);

	public static final DefaultCompound JSON_CONTAINER = new DefaultCompound("json");
	public static final DefaultList JSON_LIST = new DefaultList("json");
	public static final DefaultCompound JSON_SCORE = new DefaultCompound("score");
	public static final TemplateString JSON_SELECTOR = new TemplateString("selector", Tag.UNAVAILABLE);
	public static final TemplateString JSON_TEXT = new TemplateString("text", Tag.UNAVAILABLE);
	public static final TemplateString JSON_TRANSLATE = new TemplateString("translate", Tag.UNAVAILABLE);

	public static final DefaultCompound PATTERN = new DefaultCompound("Pattern");
	public static final TemplateNumber PATTERN_COLOR = new TemplateNumber("Color", Tag.UNAVAILABLE, TagNumber.INTEGER);
	public static final TemplateString PATTERN_SHAPE = new TemplateString("Pattern", Tag.UNAVAILABLE);

	public static final TemplateString SCORE_OBJECTIVE = new TemplateString("objective", Tag.UNAVAILABLE);
	public static final TemplateString SCORE_TARGET = new TemplateString("name", Tag.UNAVAILABLE);

	public static final TemplateString STATS_BLOCKS_NAME = new TemplateString("AffectedBlocksName", Tag.UNAVAILABLE);
	public static final TemplateString STATS_BLOCKS_OBJECTIVE = new TemplateString("AffectedBlocksObjective", Tag.UNAVAILABLE);
	public static final TemplateString STATS_ENTITIES_NAME = new TemplateString("AffectedItemsName", Tag.UNAVAILABLE);
	public static final TemplateString STATS_ENTITIES_OBJECTIVE = new TemplateString("AffectedEntitiesObjective", Tag.UNAVAILABLE);
	public static final TemplateString STATS_ITEMS_NAME = new TemplateString("AffectedEntitiesName", Tag.UNAVAILABLE);
	public static final TemplateString STATS_ITEMS_OBJECTIVE = new TemplateString("AffectedItemsObjective", Tag.UNAVAILABLE);
	public static final TemplateString STATS_QUERY_NAME = new TemplateString("QueryResultName", Tag.UNAVAILABLE);
	public static final TemplateString STATS_QUERY_OBJECTIVE = new TemplateString("QueryResultObjective", Tag.UNAVAILABLE);
	public static final TemplateString STATS_SUCCESS_NAME = new TemplateString("SuccessCountName", Tag.UNAVAILABLE);
	public static final TemplateString STATS_SUCCESS_OBJECTIVE = new TemplateString("SuccessCountObjective", Tag.UNAVAILABLE);

	public static final TemplateString TEXT_BOLD = new TemplateString("bold", Tag.UNAVAILABLE);
	public static final TemplateString TEXT_COLOR = new TemplateString("color", Tag.UNAVAILABLE);
	public static final TemplateString TEXT_INSERTION = new TemplateString("insertion", Tag.UNAVAILABLE);
	public static final TemplateString TEXT_ITALIC = new TemplateString("italic", Tag.UNAVAILABLE);
	public static final TemplateString TEXT_OBFUSCATED = new TemplateString("obfuscated", Tag.UNAVAILABLE);
	public static final TemplateString TEXT_STRIKETHROUGH = new TemplateString("strikethrough", Tag.UNAVAILABLE);
	public static final TemplateString TEXT_UNDERLINED = new TemplateString("underlined", Tag.UNAVAILABLE);

}
