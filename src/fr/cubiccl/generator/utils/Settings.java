package fr.cubiccl.generator.utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;

public class Settings
{
	public static enum Language
	{
		ENGLISH("English", "en"),
		FRENCH("Français", "fr");

		public static Language get(String codeName)
		{
			for (Language l : Language.values())
				if (l.codeName.equals(codeName)) return l;
			return get(Settings.getDefault(LANG));
		}

		public static Language[] getLanguages()
		{
			ArrayList<Language> langs = new ArrayList<Settings.Language>();
			for (Language language : values())
				langs.add(language);
			langs.sort(new Comparator<Language>()
			{
				@Override
				public int compare(Language o1, Language o2)
				{
					return o1.name.toLowerCase().compareTo(o2.name.toLowerCase());
				}
			});
			return langs.toArray(new Language[langs.size()]);
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
		v1d11("1.11", "1.11", 0),
		v1d12("1.12 - snapshot", "1.12", 1);

		public static Version get(String codeName)
		{
			for (Version v : Version.values())
				if (v.codeName.equals(codeName)) return v;
			return get(Settings.getDefault(MINECRAFT_VERSION));
		}

		public static Version[] getVersions()
		{
			ArrayList<Version> vs = new ArrayList<Version>();
			for (Version version : values())
				vs.add(version);
			vs.sort(new Comparator<Version>()
			{
				@Override
				public int compare(Version o1, Version o2)
				{
					return o1.compare(o2);
				}
			});
			return vs.toArray(new Version[vs.size()]);
		}

		public final String name, codeName;
		public final int order;

		private Version(String name, String codeName, int order)
		{
			this.name = name;
			this.codeName = codeName;
			this.order = order;
		}

		public int compare(Version anotherVersion)
		{
			return this.order - anotherVersion.order;
		}

		/** @return true if this Version is after or equal to another Version. */
		public boolean isAfter(Version anotherVersion)
		{
			return this.compare(anotherVersion) >= 0;
		}

		/** @return true if this Version is before or equal to another Version. */
		public boolean isBefore(Version anotherVersion)
		{
			return this.compare(anotherVersion) <= 0;
		}
	}

	public static final boolean CHECK_UPDATES = false;
	public static final String GENERATOR_VERSION = "2.4.4";
	private static Language language;
	private static Version mcversion;
	public static final String MINECRAFT_VERSION = "mcversion", LANG = "lang", SLASH = "slash", SORT_TYPE = "sort", INDENTATION = "indentation";
	private static HashMap<String, String> settings = new HashMap<String, String>();
	public static boolean testMode = false;

	private static String getDefault(String settingID)
	{
		switch (settingID)
		{
			case MINECRAFT_VERSION:
				return Version.v1d11.codeName;

			case LANG:
				return Language.ENGLISH.codeName;

			case INDENTATION:
			case SLASH:
				return "true";

			case SORT_TYPE:
				return Byte.toString(ObjectRegistry.SORT_ALPHABETICALLY);

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
