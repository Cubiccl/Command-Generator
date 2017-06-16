package fr.cubiccl.generator.gameobject.tags;

import java.util.ArrayList;

import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gameobject.templatetags.*;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound.DefaultCompound;
import fr.cubiccl.generator.gameobject.templatetags.TemplateList.DefaultList;

/** Utility class to parse NBT Tags. */
public class NBTParser
{

	/** @param value - The Tag's value.
	 * @return The Tag type.
	 * @see Tag#STRING */
	private static byte determineType(String value)
	{
		if (value.equals("true") || value.equals("false")) return Tag.BOOLEAN;
		if (value.startsWith("\"") && value.endsWith("\"")) return Tag.STRING;
		if (value.startsWith("{") && value.endsWith("}")) return Tag.COMPOUND;
		if (value.startsWith("[") && value.endsWith("]")) return Tag.LIST;
		if (value.endsWith("b")) return Tag.BYTE;
		if (value.endsWith("s")) return Tag.SHORT;
		if (value.endsWith("l")) return Tag.LONG;
		if (value.endsWith("f")) return Tag.FLOAT;
		if (value.endsWith("d")) return Tag.DOUBLE;
		if (value.contains(".")) return Tag.FLOAT;
		return Tag.INT;
	}

	/** @param tag - The Tag to parse.
	 * @return The ID of the Tag. */
	private static String findId(String tag)
	{
		if (tag.startsWith("\"")) return tag.substring(1, tag.substring(1).indexOf("\"") + 1);
		return tag.substring(0, tag.indexOf(":"));
	}

	/** @param id - The ID of the Tag to parse.
	 * @param type - The type of the Tag to parse.
	 * @return The Template matching the Tag to parse. <code>null</code> if no matching Template found. */
	private static TemplateTag findMatchingTag(String id, byte type)
	{
		TemplateTag[] tags = ObjectRegistry.blockTags.find(new String[]
		{ id });
		for (TemplateTag t : tags)
			if (t.tagType() == type) return t;
		tags = ObjectRegistry.itemTags.find(new String[]
		{ id });
		for (TemplateTag t : tags)
			if (t.tagType() == type) return t;
		tags = ObjectRegistry.entityTags.find(new String[]
		{ id });
		for (TemplateTag t : tags)
			if (t.tagType() == type) return t;
		tags = ObjectRegistry.unavailableTags.find(new String[]
		{ id });
		for (TemplateTag t : tags)
			if (t.tagType() == type || (t.tagType() == Tag.RANGE && (type == Tag.COMPOUND || ((TemplateRange) t).numberType == type))) return t;
		return null;
	}

	/** @param tag - The Tag to parse.
	 * @return The value of the Tag. */
	private static String findValue(String tag)
	{
		if (tag.startsWith("\"")) return tag.substring(tag.substring(1).indexOf("\"") + 3);
		return tag.substring(tag.indexOf(":") + 1);
	}

	/** Splits the input Tag into elementary parts.
	 * 
	 * @param value - The Tag to parse.
	 * @return An array containing each part of the Tag. */
	public static String[] multisplit(String value)
	{
		String current = "";
		ArrayList<String> split = new ArrayList<String>();
		ArrayList<Character> chars = new ArrayList<Character>();
		chars.add(',');
		chars.add('"');
		chars.add('[');
		chars.add(']');
		chars.add('{');
		chars.add('}');
		chars.add('\t');
		chars.add('\n');
		chars.add(' ');

		for (char c : value.toCharArray())
		{
			if (c == '\t' || c == '\n' || c == ' ')
			{
				split.add(current);
				current = "";
			}
			current += c;
			if (chars.contains(c))
			{
				if (c == '"' && current.endsWith("\\\"")) continue;
				split.add(current);
				current = "";
			}
		}

		if (!current.equals("")) split.add(current);
		return split.toArray(new String[split.size()]);
	}

	/** Parses a String NBT Tag and returns its NBT Tag form.
	 * 
	 * @param tag - The tag to read.
	 * @param isInList - True if it is in a TagList, and thus its ID is not present.
	 * @param isJson - True if it is in a json NBT structure, and thus its ID is surrounded with quotes "". */
	public static Tag parse(String tag, boolean isInList, boolean isJson)
	{
		return parse(tag, isInList, isJson, false);
	}

	/** Parses a String NBT Tag and returns its NBT Tag form.
	 * 
	 * @param tag - The tag to read.
	 * @param isInList - True if it is in a TagList, and thus its ID is not present.
	 * @param isJson - True if it is in a json NBT structure, and thus its ID is surrounded with quotes "".
	 * @param readUnknown - True if unknown Tags should still be read. */
	public static Tag parse(String tag, boolean isInList, boolean isJson, boolean readUnknown)
	{
		if (isInList) return parseNamelessTag(determineType(tag), tag, isJson, readUnknown);
		String id = findId(tag), value = findValue(tag);
		if (isJson && id.startsWith("\"") && id.endsWith("\"")) id = id.substring(1, id.length() - 1);
		byte type = determineType(value);
		TemplateTag matching = findMatchingTag(id, type);
		if (matching == null) return readUnknown ? parseUnknownTag(id, type, value, isJson) : null;
		return matching.parseTag(value, isJson, readUnknown).setJson(isJson);
	}

	/** Parses a nameless Tag (in a List).
	 * 
	 * @param type - The Tag type.
	 * @param tag - The Tag to parse.
	 * @param isJson - <code>true</code> if the Tag is in Json format.
	 * @param readUnknown - <code>true</code> if Tags with no matching Template should be created as {@link Tag#UNKNOWN unknown} Tags.
	 * @return The parsed NBT Tag. */
	private static Tag parseNamelessTag(byte type, String tag, boolean isJson, boolean readUnknown)
	{
		switch (type)
		{
			case Tag.COMPOUND:
				return Tags.DEFAULT_COMPOUND.parseTag(tag, isJson, readUnknown);
			case Tag.LIST:
				return Tags.DEFAULT_LIST.parseTag(tag, isJson, readUnknown);
			case Tag.STRING:
				return Tags.DEFAULT_STRING.parseTag(tag, isJson, readUnknown);
			case Tag.BYTE:
				return Tags.DEFAULT_BYTE.parseTag(tag, isJson, readUnknown);
			case Tag.SHORT:
				return Tags.DEFAULT_SHORT.parseTag(tag, isJson, readUnknown);
			case Tag.INT:
				return Tags.DEFAULT_INTEGER.parseTag(tag, isJson, readUnknown);
			case Tag.LONG:
				return Tags.DEFAULT_LONG.parseTag(tag, isJson, readUnknown);
			case Tag.FLOAT:
				return Tags.DEFAULT_FLOAT.parseTag(tag, isJson, readUnknown);
			case Tag.DOUBLE:
				return Tags.DEFAULT_DOUBLE.parseTag(tag, isJson, readUnknown);
			case Tag.BOOLEAN:
				return Tags.DEFAULT_BOOLEAN.parseTag(tag, isJson, readUnknown);
			default:
				return Tags.DEFAULT_STRING.parseTag(tag, isJson, readUnknown);
		}
	}

	/** Parses an unknown Tag (no matching Template).
	 * 
	 * @param id - The Tag ID.
	 * @param type - The Tag type.
	 * @param tag - The Tag to parse.
	 * @param isJson - <code>true</code> if the Tag is in Json format.
	 * @return The parsed NBT Tag. */
	private static Tag parseUnknownTag(String id, byte type, String tag, boolean isJson)
	{
		switch (type)
		{
			case Tag.COMPOUND:
				return new DefaultCompound(id, Tag.UNKNOWN).parseTag(tag, isJson, true);
			case Tag.LIST:
				return new DefaultList(id, Tag.UNKNOWN).parseTag(tag, isJson, true);
			case Tag.STRING:
				return new TemplateString(id, Tag.UNKNOWN).parseTag(tag, isJson, true);
			case Tag.BYTE:
			case Tag.SHORT:
			case Tag.INT:
			case Tag.LONG:
			case Tag.FLOAT:
			case Tag.DOUBLE:
				return new TemplateNumber(id, Tag.UNKNOWN, type).parseTag(tag, isJson, true);

			default:
				return new TemplateString(id, Tag.UNKNOWN).parseTag(tag, isJson, true);
		}
	}

	/** Splits the value of the input List or Compound Tag into each different sub-Tags.
	 * 
	 * @param value - The value to split.
	 * @return An array containing each sub-Tag. */
	public static String[] splitTagValues(String value)
	{
		String[] split = multisplit(value);
		int curly = 0, square = 0;
		boolean inString = false;

		ArrayList<String> values = new ArrayList<String>();
		String current = "";
		for (String s : split)
		{
			if (s.endsWith("[")) ++square;
			if (s.endsWith("{")) ++curly;
			if (s.endsWith("]")) --square;
			if (s.endsWith("}")) --curly;
			if (s.endsWith("\"")) inString = !inString;
			if (!(curly == 0 && square == 0 && !inString && (s.equals("\t") || s.equals(" ") || s.equals("\n"))))
			{
				current += s;
			}
			if (s.endsWith(",") && curly == 0 && square == 0 && !inString)
			{
				current = current.substring(0, current.length() - 1);
				values.add(current);
				current = "";
			}
		}
		if (curly == 0 && square == 0 && !inString) values.add(current);

		return values.toArray(new String[values.size()]);
	}
}
