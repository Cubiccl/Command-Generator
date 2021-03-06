package fr.cubiccl.generator.gameobject.map;

import static fr.cubiccl.generator.gameobject.map.MapTags.*;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

import org.jnbt.CompoundTag;
import org.jnbt.NBTInputStream;
import org.jnbt.NBTOutputStream;

import fr.cubiccl.generator.gameobject.Recipe;
import fr.cubiccl.generator.gameobject.advancements.Advancement;
import fr.cubiccl.generator.gameobject.loottable.LootTable;
import fr.cubiccl.generator.gameobject.tags.*;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateString;
import fr.cubiccl.generator.gui.Dialogs;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.FileUtils;
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
		String message = "";
		for (Text text : warnings)
			message += text.toString() + "\n";
		Dialogs.showMessage(message);
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
		this.advancements.clear();
		ArrayList<Text> warnings = new ArrayList<Text>();
		File folder = new File(this.path + "/data/advancements");
		if (!folder.exists() || folder.listFiles() == null) return warnings;

		for (File file : folder.listFiles())
			if (file.listFiles() != null) this.loadAdvancements(file, file.getName() + ":", warnings);

		return warnings;
	}

	/** Loads all Advancements contained in the input folder.
	 * 
	 * @param folder - The folder containing the Advancements to load.
	 * @param superpath - The path to the folder.
	 * @param warnings - An array to put raised warnings in. */
	private void loadAdvancements(File folder, String superpath, ArrayList<Text> warnings)
	{
		for (File file : folder.listFiles())
			if (file.getAbsolutePath().endsWith(".json")) try
			{
				Advancement advancement = new Advancement().fromNBT((TagCompound) NBTParser.parse(FileUtils.readFile(file), true, true, true));
				advancement.setCustomName(superpath + FileUtils.fileName(file, ".json"));
				this.advancements.put(advancement.customName(), advancement);
			} catch (Exception e)
			{
				warnings.add(new Text("error.map.advancement").addReplacement("<adv>", FileUtils.fileName(file, ".json")));
			}
			else if (file.listFiles() == null) this.loadAdvancements(file, superpath + file.getName() + "/", warnings);
	}

	/** Loads the Functions.
	 * 
	 * @throws CommandGenerationException if it didn't load properly. */
	private void loadFunctions()
	{
		this.functions.clear();
		File folder = new File(this.path + "/data/functions");
		if (!folder.exists() || folder.listFiles() == null) return;

		for (File file : folder.listFiles())
			if (file.listFiles() != null) this.loadFunctions(file, file.getName() + ":");
	}

	/** Loads functions in the input folder.
	 * 
	 * @param folder - The folder containing the functions.
	 * @param superpath - The path to the folder. */
	private void loadFunctions(File folder, String superpath)
	{
		for (File file : folder.listFiles())
			if (file.getAbsolutePath().endsWith(".mcfunction")) this.functions.add(superpath + FileUtils.fileName(file, ".mcfunction"));
			else if (file.listFiles() != null) this.loadFunctions(file, superpath + file.getName() + "/");
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
			TagCompound t = TagTransformer.toCG((CompoundTag) is.readTag());
			TagCompound data = t.getTag(Data);
			this.name = data.getTag(LevelName).value();
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
		this.lootTables.clear();
		ArrayList<Text> warnings = new ArrayList<Text>();
		File folder = new File(this.path + "/data/loot_tables");
		if (!folder.exists() || folder.listFiles() == null) return warnings;

		for (File file : folder.listFiles())
			if (file.listFiles() != null) this.loadLootTables(file, file.getName() + ":", warnings);

		return warnings;
	}

	/** Loads all Loot Tables contained in the input folder.
	 * 
	 * @param folder - The folder containing the Loot Tables to load.
	 * @param superpath - The path to the folder.
	 * @param warnings - An array to put raised warnings in. */
	private void loadLootTables(File folder, String superpath, ArrayList<Text> warnings)
	{
		for (File file : folder.listFiles())
			if (file.getAbsolutePath().endsWith(".json")) try
			{
				LootTable table = new LootTable().fromNBT((TagCompound) NBTParser.parse(FileUtils.readFile(file), true, true, true));
				table.setCustomName(superpath + FileUtils.fileName(file, ".json"));
				this.lootTables.put(table.customName(), table);
			} catch (Exception e)
			{
				warnings.add(new Text("error.map.loottable").addReplacement("<lt>", FileUtils.fileName(file, ".json")));
			}
			else if (file.listFiles() == null) this.loadLootTables(file, superpath + file.getName() + "/", warnings);
	}

	/** Loads the Recipes. Currently Recipes are not supported but this method is still ready.
	 * 
	 * @return The list of warnings thrown while loading.
	 * @throws CommandGenerationException if it didn't load properly. */
	private ArrayList<Text> loadRecipes()
	{
		this.recipes.clear();
		ArrayList<Text> warnings = new ArrayList<Text>();
		return warnings;
	}

	private void loadScoreboard()
	{
		File file = new File(this.path + "/data/scoreboard.dat");
		if (!file.exists()) return;

		try
		{
			NBTInputStream is = new NBTInputStream(new BufferedInputStream(new FileInputStream(file)));
			TagCompound t = TagTransformer.toCG((CompoundTag) is.readTag());
			is.close();

			TagCompound data = t.getTag(MapTags.Data);
			this.loadScoreboardObjectives(data.getTag(Objectives));
			this.loadScoreboardTeams(data.getTag(Teams));
			this.loadScoreboardScores(data.getTag(PlayerScores));
			this.loadScoreboardSlots(data.getTag(DisplaySlots));

		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
			// throw new CommandGenerationException(new Text("error.map.scoreboard.read"));
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/** Reads the Objectives from the input List.
	 * 
	 * @param objectives - The List containing the Objectives. */
	private void loadScoreboardObjectives(TagList objectives)
	{
		this.objectives.clear();
		for (Tag objective : objectives.value())
		{
			MapObjective o = new MapObjective().fromNBT((TagCompound) objective);
			this.objectives.put(o.id, o);
		}
	}

	/** Reads the Scores from the input List.
	 * 
	 * @param objectives - The List containing the Scores. */
	private void loadScoreboardScores(TagList scores)
	{
		this.playerScores.clear();
		for (Tag score : scores.value())
		{
			MapScore s = new MapScore().fromNBT((TagCompound) score);
			this.playerScores.put(s.name + " " + s.objective, s);
		}
	}

	/** Reads the Display slots from the input List.
	 * 
	 * @param objectives - The List containing the Display slots. */
	private void loadScoreboardSlots(TagCompound slots)
	{
		for (int i = 0; i < 19; ++i)
		{
			this.displaySlots[i] = null;
			if (slots.hasTag("slot_" + i)) this.displaySlots[i] = ((TagString) slots.getTagFromId("slot_" + i)).value();
		}
	}

	/** Reads the Teams from the input List.
	 * 
	 * @param objectives - The List containing the Teams. */
	private void loadScoreboardTeams(TagList teams)
	{
		this.teams.clear();
		for (Tag team : teams.value())
		{
			MapTeam t = new MapTeam().fromNBT((TagCompound) team);
			this.teams.put(t.id, t);
		}
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
		this.saveAdvancements();
		this.saveFunctions();
		this.saveLevel();
		this.saveLootTables();
		this.saveRecipes();
		this.saveScoreboard();
	}

	public void saveAdvancements()
	{
		for (Advancement advancement : this.advancements.values())
			FileUtils.writeToFile(advancement.toCommand(), new File(this.path + "/" + advancement.customName().replaceAll(":", "/") + ".json"));
	}

	public void saveFunctions()
	{}

	public void saveLevel()
	{
		File file = new File(this.path + "/level.dat");

		try
		{
			NBTInputStream is = new NBTInputStream(new BufferedInputStream(new FileInputStream(file)));
			TagCompound t = TagTransformer.toCG((CompoundTag) is.readTag());
			is.close();

			TagCompound data = t.getTag(Data);
			data.removeTag(LevelName);
			data.addTag(LevelName.create(this.name));

			NBTOutputStream os = new NBTOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
			os.writeTag(TagTransformer.toJNBT(t));
			os.close();
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
			// throw new CommandGenerationException(new Text("error.map.level"));
		} catch (IOException e)
		{
			e.printStackTrace();
			// throw new CommandGenerationException(new Text("error.map.level.read"));
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void saveLootTables()
	{
		for (LootTable lootTable : this.lootTables.values())
			FileUtils.writeToFile(lootTable.toCommand(), new File(this.path + "/" + lootTable.customName().replaceAll(":", "/") + ".json"));
	}

	public void saveRecipes()
	{}

	public void saveScoreboard()
	{
		File file = new File(this.path + "/data/scoreboard.dat");
		if (!file.exists()) return;

		try
		{

			TagCompound data = MapTags.Data.create();

			TagList o = Objectives.create();
			for (MapObjective objective : this.objectives.values())
				o.addTag(objective.toNBT(Tags.DEFAULT_COMPOUND));
			data.addTag(o);

			TagList t = Teams.create();
			for (MapTeam team : this.teams.values())
				t.addTag(team.toNBT(Tags.DEFAULT_COMPOUND));
			data.addTag(t);

			TagList s = PlayerScores.create();
			for (MapScore score : this.playerScores.values())
				s.addTag(score.toNBT(Tags.DEFAULT_COMPOUND));
			data.addTag(s);

			TagList d = DisplaySlots.create();
			for (int i = 0; i < this.displaySlots.length; ++i)
				if (this.displaySlots[i] != null) d.addTag(new TemplateString("slot_" + i, Tag.UNKNOWN).create(this.displaySlots[i]));
			data.addTag(d);

			NBTOutputStream os = new NBTOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
			os.writeTag(TagTransformer.toJNBT(Tags.DEFAULT_COMPOUND.create(data)));
			os.close();

		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
			// throw new CommandGenerationException(new Text("error.map.scoreboard.read"));
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
