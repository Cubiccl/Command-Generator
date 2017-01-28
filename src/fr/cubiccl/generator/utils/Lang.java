package fr.cubiccl.generator.utils;

import java.util.HashMap;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.utils.Settings.Language;

public class Lang
{

	private static final HashMap<String, String> dictionnary = new HashMap<String, String>();
	private static final HashMap<String, String> english = new HashMap<String, String>();

	private static String doTranslate(String textID)
	{
		if (Settings.language() != Language.ENGLISH)
		{
			if (dictionnary.containsKey(textID)) return dictionnary.get(textID);
			CommandGenerator.log("Not translated in " + Settings.language().name + " : " + textID);
		}
		if (english.containsKey(textID)) return english.get(textID);
		return textID;
	}

	/** @return True if the input textID exists in the language files. */
	public static boolean keyExists(String textID)
	{
		return english.containsKey(textID.replaceAll("minecraft:", ""));
	}

	private static void loadEnglish()
	{
		english.clear();
		String[] translations = FileUtils.readFileAsArray("lang/" + Language.ENGLISH.codeName + ".txt");
		for (String translation : translations)
			if (translation.contains("=")) english.put(translation.substring(0, translation.indexOf('=')), translation.substring(translation.indexOf('=') + 1));
	}

	/** @param textID - A text to translate.
	 * @return The translation if found. If not, returns <code>textID</code>. */
	public static String translate(String textID)
	{
		textID = textID.replaceAll("minecraft:", "");
		if (!keyExists(textID))
		{
			CommandGenerator.log("Couldn't find translation for : " + textID);
			new Exception().printStackTrace();
		}
		return doTranslate(textID);
	}

	public static void updateLang()
	{
		if (english.size() == 0) loadEnglish();
		dictionnary.clear();
		if (Settings.language() == Language.ENGLISH) return;
		String[] translations = FileUtils.readFileAsArray("lang/" + Settings.language().codeName + ".txt");
		for (String translation : translations)
		{
			if (translation.contains("=")) dictionnary.put(translation.substring(0, translation.indexOf('=')),
					translation.substring(translation.indexOf('=') + 1));
		}
	}

	private Lang()
	{}
}
