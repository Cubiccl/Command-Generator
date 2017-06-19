package fr.cubiccl.generator.gameobject.map;

import java.util.ArrayList;
import java.util.HashMap;

import fr.cubiccl.generator.gameobject.Recipe;
import fr.cubiccl.generator.gameobject.advancements.Advancement;
import fr.cubiccl.generator.gameobject.loottable.LootTable;

/** Represents a Map. */
public class Map
{
	/** Name of the displays slots. */
	public static final String[] slotNames =
	{ "list", "sidebar", "belowName", "sidebar.team.black", "sidebar.team.dark_blue", "sidebar.team.dark_green", "sidebar.team.dark_aqua",
			"sidebar.team.dark_red", "sidebar.team.dark_purple", "sidebar.team.gold", "sidebar.team.gray", "sidebar.team.dark_gray", "sidebar.team.blue",
			"sidebar.team.green", "sidebar.team.aqua", "sidebar.team.red", "sidebar.team.light_purple", "sidebar.team.yellow", "sidebar.team.white" };

	/** This Map's Advancements. */
	public final HashMap<String, Advancement> advancements = new HashMap<String, Advancement>();
	public final String[] displaySlots;
	/** This Map's Functions. */
	public final ArrayList<String> functions = new ArrayList<String>();
	/** The last time the Map was loaded. */
	public long lastLoaded;
	/** This Map's Loot Tables. */
	public final HashMap<String, LootTable> lootTables = new HashMap<String, LootTable>();
	/** This Map's name. */
	public String name;
	/** This Map's Scoreboard Objectives. */
	public final HashMap<String, MapObjective> objectives = new HashMap<String, MapObjective>();
	/** The path to the <code>level.dat</code> file of this Map. */
	public final String path;
	/** This Map's Player scores. */
	public final HashMap<String, MapScore> playerScores = new HashMap<String, MapScore>();
	/** This Map's Recipes. */
	public final HashMap<String, Recipe> recipes = new HashMap<String, Recipe>();
	/** This Map's Teams. */
	public final HashMap<String, MapTeam> teams = new HashMap<String, MapTeam>();

	public Map(String path)
	{
		this.path = path;
		this.displaySlots = new String[slotNames.length];
		// TODO Map(path)
	}

	/** Loads the Map from the files.
	 * 
	 * @return <code>false</code> if the Map didn't load properly. */
	public boolean load()
	{
		// TODO Map.load()

		this.lastLoaded = System.currentTimeMillis();
		return false;
	}

	/** Saves the Map to the files.
	 * 
	 * @return <code>false</code> if the Map didn't save properly. */
	public boolean save()
	{
		// TODO Map.save()
		return false;
	}

	/** @return <code>true</code> if the Map files were edited since the Map was last loaded. */
	public boolean wasEdited()
	{
		// TODO Map.wasEdited()
		return false;
	}

}
