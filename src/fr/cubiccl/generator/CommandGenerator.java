package fr.cubiccl.generator;

import java.util.ArrayList;
import java.util.Comparator;

import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;

import fr.cubiccl.generator.command.Command;
import fr.cubiccl.generator.command.Commands;
import fr.cubiccl.generator.gameobject.Recipe;
import fr.cubiccl.generator.gameobject.advancements.Advancement;
import fr.cubiccl.generator.gameobject.loottable.LootTable;
import fr.cubiccl.generator.gameobject.registries.ObjectCreator;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gameobject.registries.ObjectSaver;
import fr.cubiccl.generator.gameobject.tags.NBTParser;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gui.Dialogs;
import fr.cubiccl.generator.gui.LoadingFrame;
import fr.cubiccl.generator.gui.Window;
import fr.cubiccl.generator.utils.*;

/** Main class. */
public class CommandGenerator
{
	/** Stores each command that was generated. */
	private static ArrayList<String> commandHistory = new ArrayList<String>();
	/** Identifiers for the Generator mode. <br />
	 * <br />
	 * <table border="1">
	 * <tr>
	 * <td>ID</td>
	 * <td>Variable</td>
	 * <td>Mode</td>
	 * </tr>
	 * <tr>
	 * <td>0</td>
	 * <td>COMMANDS</td>
	 * <td>Command Generator</td>
	 * </tr>
	 * <tr>
	 * <td>1</td>
	 * <td>LOOT_TABLES</td>
	 * <td>Loot Table Generator</td>
	 * </tr>
	 * <tr>
	 * <td>4</td>
	 * <td>RECIPES</td>
	 * <td>Recipe Generator</td>
	 * </tr>
	 * <tr>
	 * <td>5</td>
	 * <td>ADVANCEMENTS</td>
	 * <td>Advancement Generator</td>
	 * </tr>
	 * <tr>
	 * <td>6</td>
	 * <td>MAP</td>
	 * <td>Map Management</td>
	 * </tr>
	 * </table> */
	public static final byte COMMANDS = 0, LOOT_TABLES = 1, DATA = 2, RECIPES = 4, ADVANCEMENTS = 5, MAP = 6;
	/** The current generator mode.
	 * 
	 * @see {@link CommandGenerator#COMMANDS} for possible values. */
	private static byte currentMode = COMMANDS;
	/** Temporary variable for /execute. When generating /execute, the execute part will be stored in this variable, while the user chooses the Command to execute. */
	private static String executeCommand = "";
	/** Temporary variable for /execute. If the user parsed a /execute command, the executed part is stored in this variable; then used when the user generates again to be parsed and setup the UI. */
	private static String executeInput = null;
	/** Is true while the data is reloading (false when loading for the first time during this instance). */
	private static boolean isReloading = false;
	/** The output log. */
	private static ArrayList<String> log = new ArrayList<String>();
	/** Names for the Generator mode. */
	private static final String[] MODE_NAMES =
	{ "Commands", "Loot tables", "Data", "Speedruns", "Recipes", "Advancements", "Map management" };
	private static Command selected;
	/** The State manager instance. */
	public static StateManager stateManager;
	/** The Window. */
	public static Window window;

	/** Cancels the /execute command. */
	public static void cancelExecute()
	{
		executeCommand = "";
		window.setExecuteCommand(false);
	}

	/** @return The Command History.
	 * @see {@link CommandGenerator#commandHistory} */
	public static String[] commandHistory()
	{
		return commandHistory.toArray(new String[commandHistory.size()]);
	}

	/** Parses the input generated command.
	 * 
	 * @param input - The command to parse.
	 * @throws CommandGenerationException - If the parsing fails. */
	public static void doLoad(String input) throws CommandGenerationException
	{
		Command command = Commands.identifyCommand(input);
		if (command == null) return;
		command.setupFrom(input);
		setSelected(command);
	}

	/** Exits the Generator. Saves data, settings and exports the log. */
	public static void exit()
	{
		log("Exiting Command Generator... Bye bye !");
		FileUtils.writeToFile("log.txt", log.toArray(new String[log.size()]));
		Settings.save();
		ObjectSaver.save();
		if (Settings.testMode)
		{
			Lang.untranslated.sort(Comparator.naturalOrder());
			saveVersion();
			FileUtils.writeToFile("untranslated.txt", Lang.untranslated.toArray(new String[Lang.untranslated.size()]));
		}
		log("Log, settings and custom objects saved successfully.");
	}

	/** Generates the Advancement. */
	public static void generateAdvancement()
	{
		log("Generating !");
		Advancement a = window.panelAdvancementSelection.selectedAdvancement();
		if (a != null)
		{
			String output = a.toCommand();
			window.showOutput(output);
			log("Successfully generated : " + output);
		}
	}

	/** Generates the Command. */
	public static void generateCommand()
	{
		log("Generating !");
		try
		{
			String command = selectedCommand().generate();
			{
				if (executeCommand.equals("")) executeCommand += command;
				else executeCommand += command;

				window.showOutput(executeCommand);

				if (command.startsWith("execute "))
				{
					Dialogs.showMessage(Lang.translate("general.success_execute"));
					if (executeInput != null)
					{
						String input = executeInput;
						executeInput = null; // To avoid setting back to null if this is another /execute
						try
						{
							doLoad(input);
						} catch (CommandGenerationException e)
						{}
					}
				} else
				{
					commandHistory.add(executeCommand);
					executeCommand = "";
				}
				window.setExecuteCommand(command.startsWith("execute "));
			}
			log("Successfully generated : " + command);
		} catch (CommandGenerationException e)
		{
			report(e);
		}
	}

	/** Generates the Recipe. */
	public static void generateRecipe()
	{
		log("Generating !");
		Recipe r = window.panelRecipeSelection.selectedRecipe();
		if (r != null && r.isValid())
		{
			String output = r.toCommand();
			window.showOutput(output);
			log("Successfully generated : " + output);
		}
	}

	/** Generates the Loot Table. */
	public static void generateTable()
	{
		log("Generating !");
		LootTable t = window.panelLootTableSelection.selectedLootTable();
		if (t != null)
		{
			String output = t.toCommand();
			window.showOutput(output);
			log("Successfully generated : " + output);
		}
	}

	/** @return {@link CommandGenerator#currentMode} */
	public static byte getCurrentMode()
	{
		return currentMode;
	}

	/** @return {@link CommandGenerator#isReloading} */
	public static boolean isReloading()
	{
		return isReloading;
	}

	/** Asks the user for a Command to parse and parses it. */
	public static void loadCommand()
	{
		Command command;
		String input = null;
		boolean done = false;
		do
		{
			input = Dialogs.showInputDialog(new Text("command.load.details").toString(), input == null ? "" : input);
			if (input == null) return;
			command = Commands.identifyCommand(input);
			if (command == null) Dialogs.showMessage(new Text("error.command.identify").toString());
			else

			try
			{
				doLoad(input);
				done = true;
			} catch (CommandGenerationException e)
			{
				report(e);
			}
		} while (!done);
	}

	/** Logs a line of text. Prints it in the console and adds it to the log.
	 * 
	 * @param text - The text to log. */
	public static void log(String text)
	{
		System.out.println(text);
		log.add(text);
	}

	public static void main(String[] args)
	{
		if (args.length == 1 && args[0].equals("true")) Settings.testMode = true;
		log("Welcome to the Command Generator v" + Settings.GENERATOR_VERSION + " by Cubi !");
		FileUtils.renameUpdater();
		if (!Settings.testMode) FileUtils.checkForUpdates();
		Settings.loadSettings();
		if (Settings.testMode) Lang.checkTranslations();
		log("---- Creating window ----");
		stateManager = new StateManager();
		window = new Window();
		ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);
		ToolTipManager.sharedInstance().setInitialDelay(200);
		window.updateTranslations();
		setSelected(Commands.getCommands()[0]);
		window.setVisible(true);
		if (!Settings.getSetting(Settings.LAST_VERSION).equals(Settings.GENERATOR_VERSION)) showChangelog();
	}

	/** Parses an Advancement.
	 * 
	 * @param text - The text to parse. */
	public static void parseAdvancement(String text)
	{
		TagCompound tag = (TagCompound) NBTParser.parse(text, true, true, true);
		Advancement advancement = new Advancement().fromNBT(tag);

		String name = Dialogs.showInputDialog(Lang.translate("objects.name"));
		if (name == null) return;
		advancement.setCustomName(name);
		window.panelAdvancementSelection.list.add(advancement);
	}

	/** Parses a Loot Table.
	 * 
	 * @param text - The text to parse. */
	public static void parseLootTable(String text)
	{
		TagCompound tag = (TagCompound) NBTParser.parse(text, true, true, true);
		LootTable table = new LootTable().fromNBT(tag);

		String name = Dialogs.showInputDialog(Lang.translate("objects.name"));
		if (name == null) return;
		table.setCustomName(name);
		window.panelLootTableSelection.list.add(table);
	}

	/** Parses a Recipe.
	 * 
	 * @param text - The text to parse. */
	public static void parseRecipe(String text)
	{
		TagCompound tag = (TagCompound) NBTParser.parse(text, true, true, true);
		Recipe recipe = new Recipe().fromNBT(tag);

		String name = Dialogs.showInputDialog(Lang.translate("objects.name"));
		if (name == null) return;
		recipe.setCustomName(name);
		window.panelRecipeSelection.list.add(recipe);
	}

	/** Reports an Exception. Prints it in the console and adds it to the log.
	 * 
	 * @param exception - The Exception to report. */
	public static void report(CommandGenerationException exception)
	{
		Dialogs.showMessage(exception.getMessage());
		log("Error : " + exception.getMessage());
	}

	/** Saves the data of the current version. */
	private static void saveVersion()
	{
		FileUtils.saveXMLFile(ObjectRegistry.allToXML(), "data/" + Settings.version().id);
	}

	/** @return The currently selected Command. */
	public static Command selectedCommand()
	{
		return selected;
	}

	/** Changes the current generator mode.
	 * 
	 * @param mode - The ID of the mode to set.
	 * @see {@link CommandGenerator#COMMANDS Generator modes}. */
	public static void setCurrentMode(byte mode)
	{
		currentMode = mode;
		log("Switching to " + MODE_NAMES[mode] + " mode.");
		window.updateMode();
		stateManager.onStateChange();
	}

	/** Sets the executeInput.
	 * 
	 * @see {@link CommandGenerator#executeInput} */
	public static void setExecuteInput(String input)
	{
		executeInput = input;
	}

	/** Sets the input Command as selected.
	 * 
	 * @param command - The selected Command. */
	public static void setSelected(Command command)
	{
		selected = command;
		window.setSelected(command);
		stateManager.clear();
		stateManager.setCommandState(selectedCommand().getUI(), null);
	}

	/** Displays the Change log to the user. */
	private static void showChangelog()
	{
		Dialogs.showMessage(FileUtils.readChangelog());
		Settings.setSetting(Settings.LAST_VERSION, Settings.GENERATOR_VERSION);
	}

	public static void updateData()
	{
		log("---- VERSION : " + Settings.version().name + " ----");
		log("---- Creating objects ----");

		LoadingFrame frame = new LoadingFrame(5);
		if (Settings.testMode && window != null) saveVersion();
		Settings.save();
		ObjectSaver.save();
		ObjectCreator.createObjects(frame);
		Commands.createCommands(frame);
		ObjectSaver.load();
		if (window != null)
		{
			window.dispose();
			SwingUtilities.invokeLater(new Runnable()
			{
				@Override
				public void run()
				{
					window = new Window();
					window.setVisible(true);
					setSelected(Commands.getCommands()[0]);
					isReloading = false;
				}
			});
		}
		frame.dispose();
	}

	public static void updateLanguage()
	{
		Lang.updateLang();
		if (window != null) window.updateTranslations();
	}

}
