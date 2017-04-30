package fr.cubiccl.generator.utils;

import java.util.HashMap;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gameobject.baseobjects.*;
import fr.cubiccl.generator.utils.Settings.Language;

public class Lang
{

	private static final HashMap<String, String> dictionnary = new HashMap<String, String>();
	private static final HashMap<String, String> english = new HashMap<String, String>();
	private static final HashMap<String, String> remapping = new HashMap<String, String>();
	private static final HashMap<String, String> variations = new HashMap<String, String>();

	public static void checkTranslations()
	{
		for (String id : FileUtils.readFileAsArray("untranslated.txt"))
			if (!english.containsKey(id) && !CommandGenerator.untranslated.contains(id)) CommandGenerator.untranslated.add(id);
	}

	private static String doTranslate(String textID)
	{
		if (Settings.language() != Language.ENGLISH && dictionnary.containsKey(textID)) return dictionnary.get(textID);
		if (english.containsKey(textID)) return english.get(textID);
		return textID;
	}

	/** @return True if the input textID exists in the language files. */
	public static boolean keyExists(String textID)
	{
		return english.containsKey(textID.replaceAll("minecraft:", "")) || dictionnary.containsKey(textID.replaceAll("minecraft:", ""));
	}

	private static void loadEnglish()
	{
		english.clear();
		String[] translations = FileUtils.readFileAsArray("lang/" + Language.ENGLISH.codeName + ".txt");
		for (String translation : translations)
			if (translation.contains("=")) english.put(translation.substring(0, translation.indexOf('=')), translation.substring(translation.indexOf('=') + 1));
	}

	private static void loadRemapping()
	{
		remapping.clear();
		String[] translations = FileUtils.readFileAsArray("lang/remapping.txt");
		for (String path : translations)
		{
			String[] data = path.split("=");
			for (int i = 0; i < data.length - 1; ++i)
				remapping.put(data[i], data[data.length - 1]);
		}
	}

	/** @param textID - A text to translate.
	 * @return The translation if found. If not, returns <code>textID</code>. */
	public static String translate(String textID)
	{
		textID = textID.replaceAll("minecraft:", "");
		while (remapping.containsKey(textID))
			textID = remapping.get(textID);
		if (!keyExists(textID) && !CommandGenerator.untranslated.contains(textID))
		{
			CommandGenerator.untranslated.add(textID);
			CommandGenerator.log("Couldn't find translation for : " + textID);
			// new Exception().printStackTrace();
		}
		return doTranslate(textID);
	}

	public static Text translateObject(BaseObject object)
	{
		return translateObject(object, -1);
	}

	public static Text translateObject(BaseObject object, int damage)
	{
		return translateObject(object, damage, true);
	}

	public static Text translateObject(BaseObject object, int damage, boolean undetermined)
	{
		String id = object.id().replaceAll("minecraft:", "");
		String t = variations.get(id);
		if (t == null)
		{
			if (object instanceof Block) t = variations.get("block." + id);
			else if (object instanceof Entity) t = variations.get("entity." + id);
			else if (object instanceof Item) t = variations.get("item." + id);
			if (t == null) t = "tag.object";
		}

		if (undetermined) t += ".undetermined";
		if (damage != -1 && object instanceof BlockItem) return new Text(t).addReplacement("<name>", ((BlockItem) object).name(damage));
		return new Text(t).addReplacement("<name>", object.name());
	}

	public static void updateLang()
	{
		if (english.size() == 0) loadEnglish();
		if (remapping.size() == 0) loadRemapping();
		dictionnary.clear();
		variations.clear();

		String[] vars = FileUtils.readFileAsArray("lang/" + Settings.language().codeName + "_variations.txt");
		for (String line : vars)
		{
			String id = line.split("=")[0];
			for (String object : line.split("=")[1].split(","))
				variations.put(object, id);
		}

		if (Settings.language() == Language.ENGLISH) return;
		String[] translations = FileUtils.readFileAsArray("lang/" + Settings.language().codeName + ".txt");
		for (String translation : translations)
		{
			if (translation.contains("=")) dictionnary.put(translation.substring(0, translation.indexOf('=')),
					translation.substring(translation.indexOf('=') + 1));
		}

		for (String textID : english.keySet())
			if (!dictionnary.containsKey(textID))
			{
				CommandGenerator.log("Not translated in " + Settings.language().name + " : " + textID);
				if (Settings.testMode && !CommandGenerator.untranslated.contains(textID)) CommandGenerator.untranslated.add(textID);
			}
	}

	private Lang()
	{}
}
