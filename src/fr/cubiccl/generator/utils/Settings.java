package fr.cubiccl.generator.utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;

/** Settings. */
public class Settings
{
	/** The available Languages. */
	public static enum Language
	{
		ENGLISH("en", "English"),
		FRENCH("fr", "Français");

		/** @return The Language matching the input <code>id</code>. */
		public static Language get(String id)
		{
			for (Language l : Language.values())
				if (l.id.equals(id)) return l;
			return get(Settings.getDefault(LANG));
		}

		/** @return An array containing all Languages. */
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

		/** This Language's id. */
		public final String id;
		/** This Language's name. */
		public final String name;

		private Language(String id, String name)
		{
			this.id = id;
			this.name = name;
		}
	}

	/** The available Minecraft Versions. */
	public static enum Version
	{
		v1d11("1.11.2", "1.11", 0),
		v1d12("1.12", "1.12", 1);

		public static Version get(String id)
		{
			for (Version v : Version.values())
				if (v.id.equals(id)) return v;
			return get(Settings.getDefault(MINECRAFT_VERSION));
		}

		/** @return An array containing all Versions. */
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

		/** This Version's ID. */
		public final String id;
		/** This Version's name. */
		public final String name;
		/** The position of the Version. */
		public final int order;

		private Version(String name, String id, int order)
		{
			this.id = id;
			this.name = name;
			this.order = order;
		}

		public int compare(Version anotherVersion)
		{
			return Integer.compare(this.order, anotherVersion.order);
		}

		/** @return <code>true</code> if this Version is after or equal to another Version. */
		public boolean isAfter(Version anotherVersion)
		{
			return this.compare(anotherVersion) >= 0;
		}

		/** @return <code>true</code> if this Version is before or equal to another Version. */
		public boolean isBefore(Version anotherVersion)
		{
			return this.compare(anotherVersion) <= 0;
		}
	}

	/** The Generator's version. */
	public static final String GENERATOR_VERSION = "2.6.3.1";
	/** The selected Language. */
	private static Language language;
	/** The selected Minecraft version. */
	private static Version mcversion;
	/** Setting IDs. */
	public static final String MINECRAFT_VERSION = "mcversion", LANG = "lang", SLASH = "slash", SORT_TYPE = "sort", INDENTATION = "indentation",
			LAST_VERSION = "lastversion", LAST_FOLDER = "folder";
	/** Stores the Settings. */
	private static HashMap<String, String> settings = new HashMap<String, String>();
	/** <code>true</code> if the Generator is in debug mode. */
	public static boolean testMode = false;

	/** @param id - The Setting's ID.
	 * @return The default value for the input Setting. */
	private static String getDefault(String id)
	{
		switch (id)
		{
			case MINECRAFT_VERSION:
				return Version.v1d12.id;

			case LANG:
				return Language.ENGLISH.id;

			case LAST_VERSION:
				return " ";

			case LAST_FOLDER:
				return "";

			case INDENTATION:
			case SLASH:
				return "true";

			case SORT_TYPE:
				return Byte.toString(ObjectRegistry.SORT_ALPHABETICALLY);

			default:
				return null;
		}
	}

	/** @param id - The Setting's ID.
	 * @return The value of the input Setting. */
	public static String getSetting(String id)
	{
		if (!settings.containsKey(id)) setSetting(id, getDefault(id));
		return settings.get(id);
	}

	/** @return The selected {@link Language}. */
	public static Language language()
	{
		return language;
	}

	/** Loads the Settings by reading the settings file. */
	public static void loadSettings()
	{
		String[] values = FileUtils.readFileAsArray("settings.txt");
		String version = getDefault(MINECRAFT_VERSION), lang = getDefault(LANG);
		for (String line : values)
		{
			String id = line.split("=")[0];
			String value = line.substring(line.indexOf('=') + 1);
			if (id.equals(LANG)) lang = value;
			else if (id.equals(MINECRAFT_VERSION)) version = value;
			else setSetting(id, value);
		}
		setSetting(LANG, lang);
		setSetting(MINECRAFT_VERSION, version);
	}

	/** Saves the Settings to the file. */
	public static void save()
	{
		ArrayList<String> data = new ArrayList<String>();
		for (String id : settings.keySet())
			data.add(id + "=" + getSetting(id));
		FileUtils.writeToFile("settings.txt", data.toArray(new String[data.size()]));
	}

	/** Changes the selected Language.
	 * 
	 * @param newLanguage - The new Language. */
	public static void setLanguage(Language newLanguage)
	{
		language = newLanguage;
		CommandGenerator.updateLanguage();
	}

	/** Changes the value of a setting.
	 * 
	 * @param id - The Setting's ID.
	 * @param value - The value of the Setting. */
	public static void setSetting(String id, String value)
	{
		settings.put(id, value);
		if (id.equals(LANG)) setLanguage(Language.get(value));
		if (id.equals(MINECRAFT_VERSION)) setVersion(Version.get(value));
	}

	/** Changes the selected Minecraft Version.
	 * 
	 * @param newLanguage - The new Version. */
	public static void setVersion(Version newVersion)
	{
		mcversion = newVersion;
		CommandGenerator.updateData();
	}

	/** @return The selected {@link Version Minecraft Version}. */
	public static Version version()
	{
		return mcversion;
	}

	private Settings()
	{}

}
