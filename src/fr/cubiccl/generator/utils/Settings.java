package fr.cubiccl.generator.utils;

import java.util.ArrayList;
import java.util.HashMap;

import fr.cubiccl.generator.CommandGenerator;

public class Settings
{
	public static enum Language
	{
		ENGLISH("English", "en");

		public static Language get(String codeName)
		{
			for (Language l : Language.values())
				if (l.codeName.equals(codeName)) return l;
			return get(Settings.getDefault(LANG));
		}

		public final String name, codeName;

		private Language(String name, String codeName)
		{
			this.name = name;
			this.codeName = codeName;
		}
	}

	public static enum Version
	{
		v1d10("1.10", -1),
		v1d11("1.11", 0);

		public static Version get(String name)
		{
			for (Version v : Version.values())
				if (v.name.equals(name)) return v;
			return get(Settings.getDefault(MINECRAFT_VERSION));
		}

		public final String name;
		public final int order;

		private Version(String name, int order)
		{
			this.name = name;
			this.order = order;
		}

		public int compare(Version anotherVersion)
		{
			return this.order - anotherVersion.order;
		}

		public boolean isAfter(Version anotherVersion)
		{
			return this.compareTo(anotherVersion) > 0;
		}

		public boolean isBefore(Version anotherVersion)
		{
			return this.compareTo(anotherVersion) < 0;
		}
	}

	public static final boolean CHECK_UPDATES = false;
	public static final String GENERATOR_VERSION = "2.0.1";
	private static Language language;
	private static Version mcversion;
	public static final String MINECRAFT_VERSION = "mcversion", LANG = "lang", SLASH = "slash";
	private static HashMap<String, String> settings = new HashMap<String, String>();

	private static String getDefault(String settingID)
	{
		switch (settingID)
		{
			case MINECRAFT_VERSION:
				return Version.v1d11.name;

			case LANG:
				return Language.ENGLISH.codeName;

			case SLASH:
				return "true";

			default:
				return null;
		}
	}

	public static String getSetting(String id)
	{
		if (!settings.containsKey(id)) setSetting(id, getDefault(id));
		return settings.get(id);
	}

	public static Language language()
	{
		return language;
	}

	public static void loadSettings()
	{
		String[] values = FileUtils.readFileAsArray("settings.txt");
		String version = getDefault(MINECRAFT_VERSION), lang = getDefault(LANG);
		for (String line : values)
		{
			String id = line.split("=")[0], value = line.split("=")[1];
			if (id.equals(LANG)) lang = value;
			else if (id.equals(MINECRAFT_VERSION)) version = value;
			else setSetting(id, value);
		}
		setSetting(LANG, lang);
		setSetting(MINECRAFT_VERSION, version);
	}

	public static void save()
	{
		ArrayList<String> data = new ArrayList<String>();
		for (String id : settings.keySet())
			data.add(id + "=" + getSetting(id));
		FileUtils.writeToFile("settings.txt", data.toArray(new String[data.size()]));
	}

	public static void setLanguage(Language newLanguage)
	{
		language = newLanguage;
		CommandGenerator.updateLanguage();
	}

	public static void setSetting(String id, String value)
	{
		settings.put(id, value);
		if (id.equals(LANG)) setLanguage(Language.get(value));
		if (id.equals(MINECRAFT_VERSION)) setVersion(Version.get(value));
	}

	public static void setVersion(Version newVersion)
	{
		mcversion = newVersion;
		CommandGenerator.updateData();
	}

	public static Version version()
	{
		return mcversion;
	}

	private Settings()
	{}

}
