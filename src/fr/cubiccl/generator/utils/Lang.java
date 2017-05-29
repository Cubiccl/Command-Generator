package fr.cubiccl.generator.utils;

import java.util.ArrayList;
import java.util.HashMap;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gameobject.baseobjects.*;
import fr.cubiccl.generator.utils.Settings.Language;

/** Manages translations. */
public class Lang
{

	/** Contains all translations for the current language. */
	private static final HashMap<String, String> dictionnary = new HashMap<String, String>();
	/** Contains all translations for the default language (English). */
	private static final HashMap<String, String> english = new HashMap<String, String>();
	/** Contains the mapping changes. */
	private static final HashMap<String, String> remapping = new HashMap<String, String>();
	/** Contains all untranslated IDs. */
	public static ArrayList<String> untranslated = new ArrayList<String>();
	/** Contains the link to all grammar changes. */
	private static final HashMap<String, String> variations = new HashMap<String, String>();

	/** When loading a language, checks if every ID is translated (uses English as reference). */
	public static void checkTranslations()
	{
		for (String id : FileUtils.readFileAsArray("untranslated.txt"))
			if (!english.containsKey(id) && !untranslated.contains(id)) untranslated.add(id);
	}

	/** Translates the input ID directly. Does not check for remapping or grammar.
	 * 
	 * @param textID - The ID to translate.
	 * @return The translation of the input ID. Returns the ID itself if it's not translated. */
	private static String doTranslate(String textID)
	{
		if (Settings.language() != Language.ENGLISH && dictionnary.containsKey(textID)) return dictionnary.get(textID);
		if (english.containsKey(textID)) return english.get(textID);
		return textID;
	}

	/** @return True if the input <code>textID</code> exists in the language files. */
	public static boolean keyExists(String textID)
	{
		return english.containsKey(textID.replaceAll("minecraft:", "")) || dictionnary.containsKey(textID.replaceAll("minecraft:", ""));
	}

	/** Loads the English translations. */
	private static void loadEnglish()
	{
		english.clear();
		String[] translations = FileUtils.readFileAsArray("lang/" + Language.ENGLISH.id + ".txt");
		for (String translation : translations)
			if (translation.contains("=")) english.put(translation.substring(0, translation.indexOf('=')), translation.substring(translation.indexOf('=') + 1));
	}

	/** Loads the remapping. */
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

	/** Translates a text ID. Checks for remapping and logs if it's not translated.
	 * 
	 * @param textID - A text to translate.
	 * @return The translation if found. If not, returns <code>textID</code>. */
	public static String translate(String textID)
	{
		textID = textID.replaceAll("minecraft:", "");
		while (remapping.containsKey(textID))
			textID = remapping.get(textID);
		if (!keyExists(textID) && !untranslated.contains(textID))
		{
			untranslated.add(textID);
			CommandGenerator.log("Couldn't find translation for : " + textID);
			// new Exception().printStackTrace();
		}
		return doTranslate(textID);
	}

	/** Finds the correct Text for the input object. Checks for grammar.
	 * 
	 * @param object - The object to translate its name.
	 * @return The Text. */
	public static Text translateObject(BaseObject object)
	{
		return translateObject(object, -1);
	}

	/** Finds the correct Text for the input object. Checks for grammar.
	 * 
	 * @param object - The object to translate its name.
	 * @param damage - A damage value to translate the object with.
	 * @return The Text. */
	public static Text translateObject(BaseObject object, int damage)
	{
		return translateObject(object, damage, true);
	}

	/** Finds the correct Text for the input object. Checks for grammar.
	 * 
	 * @param object - The object to translate its name.
	 * @param damage - A damage value to translate the object with.
	 * @param undetermined - <code>true</code> if the object is undetermined.
	 * @return The Text. */
	public static Text translateObject(BaseObject object, int damage, boolean undetermined)
	{
		if (object == null)
		{
			if (object instanceof Block) return new Text("general.block");
			if (object instanceof Entity) return new Text("general.entity");
			if (object instanceof Item) return new Text("general.item");
			return new Text("general.object");
		}
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

	/** Updates the languages. Checks for the current languages and reads the files. */
	public static void updateLang()
	{
		if (english.size() == 0) loadEnglish();
		if (remapping.size() == 0) loadRemapping();
		dictionnary.clear();
		variations.clear();

		String[] vars = FileUtils.readFileAsArray("lang/" + Settings.language().id + "_variations.txt");
		for (String line : vars)
		{
			String id = line.split("=")[0];
			for (String object : line.split("=")[1].split(","))
				variations.put(object, id);
		}

		if (Settings.language() == Language.ENGLISH) return;
		String[] translations = FileUtils.readFileAsArray("lang/" + Settings.language().id + ".txt");
		for (String translation : translations)
		{
			if (translation.contains("=")) dictionnary.put(translation.substring(0, translation.indexOf('=')),
					translation.substring(translation.indexOf('=') + 1));
		}

		for (String textID : english.keySet())
			if (!dictionnary.containsKey(textID))
			{
				CommandGenerator.log("Not translated in " + Settings.language().name + " : " + textID);
				if (Settings.testMode && !untranslated.contains(textID)) untranslated.add(textID);
			}
	}

	private Lang()
	{}
}
