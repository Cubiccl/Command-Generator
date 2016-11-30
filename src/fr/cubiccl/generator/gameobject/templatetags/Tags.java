package fr.cubiccl.generator.gameobject.templatetags;

import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagNumber;

/** Contains unique and constant NBT Tags. */
public final class Tags
{
	public static final TemplateCompound BLOCK = new TemplateCompound("block", Tag.ITEM);

	public static final TemplateCompound ENTITY = new TemplateCompound("entity", Tag.ENTITY);
	public static final TemplateString ENTITY_TYPE = new TemplateString("type", Tag.ENTITY);

	public static final TemplateString EVENT_ACTION = new TemplateString("action", Tag.ENTITY);
	public static final TemplateCompound EVENT_CLICK = new TemplateCompound("ClickEvent", Tag.ENTITY);
	public static final TemplateCompound EVENT_HOVER = new TemplateCompound("HoverEvent", Tag.ENTITY);
	public static final TemplateString EVENT_VALUE = new TemplateString("value", Tag.ENTITY);

	public static final TemplateCompound ITEM = new TemplateCompound("item", Tag.ITEM);
	public static final TemplateNumber ITEM_COUNT = new TemplateNumber("Count", Tag.ITEM, TagNumber.BYTE);
	public static final TemplateNumber ITEM_DAMAGE = new TemplateNumber("Damage", Tag.ITEM, TagNumber.SHORT);
	public static final TemplateString ITEM_ID = new TemplateString("id", Tag.ITEM);

	public static final TemplateCompound JSON_CONTAINER = new TemplateCompound("json", Tag.ITEM);
	public static final TemplateList JSON_LIST = new TemplateList("json", Tag.ITEM);
	public static final TemplateCompound JSON_SCORE = new TemplateCompound("score", Tag.ITEM);
	public static final TemplateString JSON_SELECTOR = new TemplateString("selector", Tag.ITEM);
	public static final TemplateString JSON_TEXT = new TemplateString("text", Tag.ITEM);
	public static final TemplateString JSON_TRANSLATE = new TemplateString("translate", Tag.ITEM);

	public static final TemplateString SCORE_OBJECTIVE = new TemplateString("objective", Tag.ITEM);
	public static final TemplateString SCORE_TARGET = new TemplateString("name", Tag.ITEM);

	public static final TemplateString TEXT_BOLD = new TemplateString("bold", Tag.ITEM);
	public static final TemplateString TEXT_COLOR = new TemplateString("color", Tag.ITEM);
	public static final TemplateString TEXT_INSERTION = new TemplateString("insertion", Tag.ITEM);
	public static final TemplateString TEXT_ITALIC = new TemplateString("italic", Tag.ITEM);
	public static final TemplateString TEXT_OBFUSCATED = new TemplateString("obfuscated", Tag.ITEM);
	public static final TemplateString TEXT_STRIKETHROUGH = new TemplateString("strikethrough", Tag.ITEM);
	public static final TemplateString TEXT_UNDERLINED = new TemplateString("underlined", Tag.ITEM);

}
