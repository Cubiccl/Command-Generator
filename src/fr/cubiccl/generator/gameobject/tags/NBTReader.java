package fr.cubiccl.generator.gameobject.tags;

import java.util.ArrayList;

import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateTag;

public class NBTReader
{

	private static byte determineType(String value)
	{
		if (value.startsWith("\"") && value.endsWith("\"")) return Tag.STRING;
		if (value.startsWith("{") && value.endsWith("}")) return Tag.COMPOUND;
		if (value.startsWith("[") && value.endsWith("]")) return Tag.LIST;
		if (value.endsWith("b")) return Tag.BYTE;
		if (value.endsWith("s")) return Tag.SHORT;
		if (value.endsWith("l")) return Tag.LONG;
		if (value.endsWith("f")) return Tag.FLOAT;
		if (value.endsWith("d")) return Tag.DOUBLE;
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

		for (char c : value.toCharArray())
		{
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

	public static Tag read(String tag, boolean isInList, boolean isJson)
	{
		if (isInList) return readNamelessTag(determineType(tag), tag, isJson);
		String id = tag.substring(0, tag.indexOf(":")), value = tag.substring(tag.indexOf(":") + 1);
		if (isJson && id.startsWith("\"") && id.endsWith("\"")) id = id.substring(1, id.length() - 1);
		byte type = determineType(value);
		TemplateTag matching = findMatchingTag(id, type);
		if (matching == null) return null;
		return matching.readTag(value, isJson);
	}

	private static Tag readNamelessTag(byte type, String tag, boolean isJson)
	{
		switch (type)
		{
			case Tag.COMPOUND:
				return Tags.DEFAULT_COMPOUND.readTag(tag, isJson);
			case Tag.LIST:
				return Tags.DEFAULT_LIST.readTag(tag, isJson);
			case Tag.STRING:
				return Tags.DEFAULT_STRING.readTag(tag, isJson);
			case Tag.BYTE:
			case Tag.SHORT:
			case Tag.INT:
			case Tag.LONG:
			case Tag.FLOAT:
			case Tag.DOUBLE:
				return Tags.DEFAULT_INTEGER.readTag(tag, isJson);

			default:
				return Tags.DEFAULT_STRING.readTag(tag, isJson);
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
			if (s.endsWith("]")) --square;
			if (s.endsWith("{")) ++curly;
			if (s.endsWith("}")) --curly;
			if (s.endsWith("\"")) inString = !inString;
			current += s;
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
