package fr.cubiccl.generator.utils;

import java.util.HashMap;

import fr.cubiccl.generator.CommandGenerator;

public class Lang
{

	private static final HashMap<String, String> dictionnary = new HashMap<String, String>();

	public static boolean keyExists(String key)
	{
		return dictionnary.containsKey(key.replaceAll("minecraft:", ""));
	}

	/** @param textID - A text to translate.
	 * @return The translation if found. If not, returns <code>textID</code>. */
	public static String translate(String textID)
	{
		textID = textID.replaceAll("minecraft:", "");
		if (keyExists(textID)) return dictionnary.get(textID);
		CommandGenerator.log("Couldn't find translation for : " + textID);
		return textID;
	}

	public static void updateLang()
	{
		dictionnary.clear();
		String[] translations = FileUtils.readFileAsArray("lang/" + Settings.getLanguage().codeName + ".txt");
		for (String translation : translations)
		{
			if (translation.contains("=")) dictionnary.put(translation.substring(0, translation.indexOf('=')),
					translation.substring(translation.indexOf('=') + 1));
		}
	}

	private Lang()
	{}
}
