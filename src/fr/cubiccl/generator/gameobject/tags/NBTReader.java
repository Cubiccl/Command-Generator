package fr.cubiccl.generator.gameobject.tags;

import java.util.ArrayList;

import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gameobject.templatetags.*;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound.DefaultCompound;
import fr.cubiccl.generator.gameobject.templatetags.TemplateList.DefaultList;

public class NBTReader
{

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

	private static TemplateTag findMatchingTag(String id, byte type)
	{
		TemplateTag[] tags = ObjectRegistry.blockTags.find(new String[]
		{ id });
		for (TemplateTag t : tags)
			if (t.tagType == type) return t;
		tags = ObjectRegistry.itemTags.find(new String[]
		{ id });
		for (TemplateTag t : tags)
			if (t.tagType == type) return t;
		tags = ObjectRegistry.entityTags.find(new String[]
		{ id });
		for (TemplateTag t : tags)
			if (t.tagType == type) return t;
		tags = ObjectRegistry.unavailableTags.find(new String[]
		{ id });
		for (TemplateTag t : tags)
			if (t.tagType == type) return t;
		return null;
	}

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

	/** Reads a String NBT Tag and returns its NBT Tag form.
	 * 
	 * @param tag - The tag to read.
	 * @param isInList - True if it is in a TagList, and thus its ID is not present.
	 * @param isJson - True if it is in a json NBT structure, and thus its ID is surrounded with quotes "". */
	public static Tag read(String tag, boolean isInList, boolean isJson)
	{
		return read(tag, isInList, isJson, false);
	}

	/** Reads a String NBT Tag and returns its NBT Tag form.
	 * 
	 * @param tag - The tag to read.
	 * @param isInList - True if it is in a TagList, and thus its ID is not present.
	 * @param isJson - True if it is in a json NBT structure, and thus its ID is surrounded with quotes "".
	 * @param readUnknown - True if unknown Tags should still be read. */
	public static Tag read(String tag, boolean isInList, boolean isJson, boolean readUnknown)
	{
		if (isInList) return readNamelessTag(determineType(tag), tag, isJson, readUnknown);
		String id = tag.substring(0, tag.indexOf(":")), value = tag.substring(tag.indexOf(":") + 1);
		System.out.println("reading: " + id);
		if (isJson && id.startsWith("\"") && id.endsWith("\"")) id = id.substring(1, id.length() - 1);
		byte type = determineType(value);
		TemplateTag matching = findMatchingTag(id, type);
		if (matching == null) return readUnknown ? readUnknownTag(id, type, value, isJson) : null;
		return matching.readTag(value, isJson, readUnknown);
	}

	private static Tag readNamelessTag(byte type, String tag, boolean isJson, boolean readUnknown)
	{
		switch (type)
		{
			case Tag.COMPOUND:
				return Tags.DEFAULT_COMPOUND.readTag(tag, isJson, readUnknown);
			case Tag.LIST:
				return Tags.DEFAULT_LIST.readTag(tag, isJson, readUnknown);
			case Tag.STRING:
				return Tags.DEFAULT_STRING.readTag(tag, isJson, readUnknown);
			case Tag.BYTE:
				return Tags.DEFAULT_BYTE.readTag(tag, isJson, readUnknown);
			case Tag.SHORT:
				return Tags.DEFAULT_SHORT.readTag(tag, isJson, readUnknown);
			case Tag.INT:
				return Tags.DEFAULT_INTEGER.readTag(tag, isJson, readUnknown);
			case Tag.LONG:
				return Tags.DEFAULT_LONG.readTag(tag, isJson, readUnknown);
			case Tag.FLOAT:
				return Tags.DEFAULT_FLOAT.readTag(tag, isJson, readUnknown);
			case Tag.DOUBLE:
				return Tags.DEFAULT_DOUBLE.readTag(tag, isJson, readUnknown);
			case Tag.BOOLEAN:
				return Tags.DEFAULT_BOOLEAN.readTag(tag, isJson, readUnknown);
			default:
				return Tags.DEFAULT_STRING.readTag(tag, isJson, readUnknown);
		}
	}

	private static Tag readUnknownTag(String id, byte type, String value, boolean isJson)
	{
		switch (type)
		{
			case Tag.COMPOUND:
				return new DefaultCompound(id, Tag.UNKNOWN).readTag(value, isJson, true);
			case Tag.LIST:
				return new DefaultList(id, Tag.UNKNOWN).readTag(value, isJson, true);
			case Tag.STRING:
				return new TemplateString(id, Tag.UNKNOWN).readTag(value, isJson, true);
			case Tag.BYTE:
			case Tag.SHORT:
			case Tag.INT:
			case Tag.LONG:
			case Tag.FLOAT:
			case Tag.DOUBLE:
				return new TemplateNumber(id, Tag.UNKNOWN, TemplateNumber.numberTypeFor(type)).readTag(value, isJson, true);

			default:
				return new TemplateString(id, Tag.UNKNOWN).readTag(value, isJson, true);
		}
	}

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

		for (String string : values)
		{
			System.out.println("value: " + string);
		}

		return values.toArray(new String[values.size()]);
	}
}
