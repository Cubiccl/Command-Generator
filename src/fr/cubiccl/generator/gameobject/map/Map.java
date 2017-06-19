package fr.cubiccl.generator.gameobject.map;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

import org.jnbt.CompoundTag;
import org.jnbt.NBTInputStream;
import org.jnbt.Tag;

import fr.cubiccl.generator.gameobject.Recipe;
import fr.cubiccl.generator.gameobject.advancements.Advancement;
import fr.cubiccl.generator.gameobject.loottable.LootTable;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.tags.TagString;
import fr.cubiccl.generator.gameobject.tags.TagTransformer;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

/** Represents a Map. */
public class Map
{
	/** Name of the displays slots. */
	public static final String[] slotNames =
	{ "list", "sidebar", "belowName", "sidebar.team.black", "sidebar.team.dark_blue", "sidebar.team.dark_green", "sidebar.team.dark_aqua",
			"sidebar.team.dark_red", "sidebar.team.dark_purple", "sidebar.team.gold", "sidebar.team.gray", "sidebar.team.dark_gray", "sidebar.team.blue",
			"sidebar.team.green", "sidebar.team.aqua", "sidebar.team.red", "sidebar.team.light_purple", "sidebar.team.yellow", "sidebar.team.white" };

	/** Displays the warnings to the user.
	 * 
	 * @param warnings - The warnings to show. */
	private static void displayWarnings(ArrayList<Text> warnings)
	{
		// TODO Map.displayWarnings(warnings)

	}

	/** This Map's Advancements. */
	public final HashMap<String, Advancement> advancements = new HashMap<String, Advancement>();
	/** The Objectives shown in the display slots. <code>null</code> for clear slot. */
	public final String[] displaySlots;
	/** This Map's Functions. */
	public final ArrayList<String> functions = new ArrayList<String>();
	/** This Map's Loot Tables. */
	public final HashMap<String, LootTable> lootTables = new HashMap<String, LootTable>();
	/** This Map's name. */
	public String name;
	/** This Map's Scoreboard Objectives. */
	public final HashMap<String, MapObjective> objectives = new HashMap<String, MapObjective>();
	/** The path to the Map folder. */
	public final String path;
	/** This Map's Player scores. */
	public final HashMap<String, MapScore> playerScores = new HashMap<String, MapScore>();
	/** This Map's Recipes. */
	public final HashMap<String, Recipe> recipes = new HashMap<String, Recipe>();
	/** This Map's Teams. */
	public final HashMap<String, MapTeam> teams = new HashMap<String, MapTeam>();

	public Map(String path)
	{
		this.path = path.substring(0, path.length() - "/level.dat".length());
		this.displaySlots = new String[slotNames.length];
		// TODO Map(path)
	}

	/** Loads the Map from the files.
	 * 
	 * @throws CommandGenerationException if the Map didn't load properly. */
	public void load() throws CommandGenerationException
	{
		this.loadLevel();

		ArrayList<Text> warnings = new ArrayList<Text>();
		warnings.addAll(this.loadAdvancements());
		warnings.addAll(this.loadLootTables());
		warnings.addAll(this.loadRecipes());
		this.loadFunctions();
		this.loadScoreboard();

		displayWarnings(warnings);
	}

	/** Loads the Advancements.
	 * 
	 * @return The list of warnings thrown while loading.
	 * @throws CommandGenerationException if it didn't load properly. */
	private ArrayList<Text> loadAdvancements()
	{
		ArrayList<Text> warnings = new ArrayList<Text>();
		File folder = new File(this.path + "/data/advancements");
		if (!folder.exists()) return warnings;

		// TODO Map.loadAdvancements()
		return warnings;
	}

	/** Loads the Functions.
	 * 
	 * @throws CommandGenerationException if it didn't load properly. */
	private void loadFunctions()
	{
		File folder = new File(this.path + "/data/functions");
		if (!folder.exists()) return;

		for (File function : folder.listFiles())
			if (function.getAbsolutePath().endsWith(".mcfunction")) this.functions.add(function.getAbsolutePath().substring(
					folder.getAbsolutePath().length() + 1, function.getAbsolutePath().length() - ".mcfunction".length()));
	}

	/** Loads the main level properties in <code>level.dat</code>.
	 * 
	 * @throws CommandGenerationException if <code>level.dat</code> doesn't exist or it has invalid format. */
	private void loadLevel() throws CommandGenerationException
	{
		File file = new File(this.path + "/level.dat");

		try
		{
			NBTInputStream is = new NBTInputStream(new BufferedInputStream(new FileInputStream(file)));
			Tag level = is.readTag();
			TagCompound t = TagTransformer.toCG((CompoundTag) level);
			TagCompound data = (TagCompound) t.getTagFromId("Data");
			this.name = ((TagString) data.getTagFromId("LevelName")).value();
			is.close();
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
			throw new CommandGenerationException(new Text("error.map.level"));
		} catch (IOException e)
		{
			e.printStackTrace();
			throw new CommandGenerationException(new Text("error.map.level.read"));
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/** Loads the Loot Tables.
	 * 
	 * @return The list of warnings thrown while loading.
	 * @throws CommandGenerationException if it didn't load properly. */
	private ArrayList<Text> loadLootTables()
	{
		ArrayList<Text> warnings = new ArrayList<Text>();
		File folder = new File(this.path + "/data/loot_tables");
		if (!folder.exists()) return warnings;

		// TODO Map.loadLootTables()
		return warnings;
	}

	/** Loads the Recipes. Currently Recipes are not supported but this method is still ready.
	 * 
	 * @return The list of warnings thrown while loading.
	 * @throws CommandGenerationException if it didn't load properly. */
	private ArrayList<Text> loadRecipes()
	{
		ArrayList<Text> warnings = new ArrayList<Text>();
		return warnings;
	}

	private void loadScoreboard()
	{
		File file = new File(this.path + "/data/scoreboard.dat");
		if (!file.exists()) return;

		// Map.loadScoreboard()

	}

	/** Reloads the Advancements. */
	public void reloadAdvancements()
	{
		displayWarnings(this.loadAdvancements());
	}

	/** Reloads the Functions. */
	public void reloadFunctions()
	{
		this.loadFunctions();
	}

	/** Reloads the Loot Tables. */
	public void reloadLootTables()
	{
		displayWarnings(this.loadLootTables());
	}

	/** Reloads the Recipes. */
	public void reloadRecipes()
	{
		displayWarnings(this.loadRecipes());
	}

	/** Reloads the Scoreboard. */
	public void reloadScoreboard()
	{
		this.loadScoreboard();
	}

	/** Saves the Map to the files.
	 * 
	 * @throws CommandGenerationException if the Map didn't save properly. */
	public void save() throws CommandGenerationException
	{
		// TODO Map.save()
	}

}
