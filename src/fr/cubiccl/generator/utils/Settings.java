package fr.cubiccl.generator.utils;

import fr.cubiccl.generator.CommandGenerator;

public class Settings
{
	public enum Language
	{
		ENGLISH("English", "en"),
		FRENCH("Français", "fr");

		public final String name, codeName;

		private Language(String name, String codeName)
		{
			this.name = name;
			this.codeName = codeName;
		}
	}

	public enum Version
	{
		v1d10("1.10"),
		v1d11("1.11"),
		V1d8("1.8"),
		V1d9("1.9");

		public final String name;

		private Version(String name)
		{
			this.name = name;
		}
	}

	private static Language language;
	private static Version version;

	public static void createSettings()
	{
		setLanguage(Language.ENGLISH);
		setVersion(Version.v1d11);
	}

	public static Language getLanguage()
	{
		return language;
	}

	public static Version getVersion()
	{
		return version;
	}

	public static void setLanguage(Language newLanguage)
	{
		language = newLanguage;
		CommandGenerator.updateLanguage();
	}

	public static void setVersion(Version newVersion)
	{
		version = newVersion;
		CommandGenerator.updateData();
	}

	private Settings()
	{}

}
