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
import fr.cubiccl.generator.gameobject.tags.NBTReader;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gui.Dialogs;
import fr.cubiccl.generator.gui.LoadingFrame;
import fr.cubiccl.generator.gui.Window;
import fr.cubiccl.generator.utils.*;

public class CommandGenerator
{
	private static ArrayList<String> commandHistory = new ArrayList<String>();
	public static final byte COMMANDS = 0, LOOT_TABLES = 1, DATA = 2, SPEEDRUN = 3, RECIPES = 4, ADVANCEMENTS = 5;
	private static byte currentMode = COMMANDS;
	private static String executeCommand = "", executeInput = null;
	private static boolean isReloading = false;
	private static ArrayList<String> log = new ArrayList<String>();
	private static final String[] MODE_NAMES =
	{ "Commands", "Loot tables", "Data", "Speedruns", "Recipes", "Advancements" };
	private static Command selected;
	public static StateManager stateManager;
	public static ArrayList<String> untranslated = new ArrayList<String>();
	public static Window window;

	public static void cancelExecute()
	{
		executeCommand = "";
		window.setExecuteCommand(false);
	}

	public static String[] commandHistory()
	{
		return commandHistory.toArray(new String[commandHistory.size()]);
	}

	public static void createAdvancement(String text)
	{
		TagCompound tag = (TagCompound) NBTReader.read(text, true, true, true);
		Advancement advancement = Advancement.createFrom(tag);

		String name = Dialogs.showInputDialog(Lang.translate("objects.name"));
		if (name == null) return;
		advancement.setCustomName(name);
		window.panelAdvancementSelection.list.add(advancement);
	}

	public static void createRecipe(String text)
	{
		TagCompound tag = (TagCompound) NBTReader.read(text, true, true, true);
		Recipe recipe = Recipe.createFrom(tag);

		String name = Dialogs.showInputDialog(Lang.translate("objects.name"));
		if (name == null) return;
		recipe.setCustomName(name);
		window.panelRecipeSelection.list.add(recipe);
	}

	public static void createTable(String text)
	{
		TagCompound tag = (TagCompound) NBTReader.read(text, true, true, true);
		LootTable table = LootTable.createFrom(tag);

		String name = Dialogs.showInputDialog(Lang.translate("objects.name"));
		if (name == null) return;
		table.setCustomName(name);
		window.panelLootTableSelection.list.add(table);
	}

	public static void doLoad(String input) throws CommandGenerationException
	{
		Command command = Commands.identifyCommand(input);
		if (command == null) return;
		command.setupFrom(input);
		setSelected(command);
	}

	public static void exit()
	{
		log("Exiting Command Generator... Bye bye !");
		FileUtils.writeToFile("log.txt", log.toArray(new String[log.size()]));
		Settings.save();
		ObjectSaver.save();
		if (Settings.testMode)
		{
			untranslated.sort(Comparator.naturalOrder());
			saveVersion();
			FileUtils.writeToFile("untranslated.txt", untranslated.toArray(new String[untranslated.size()]));
		}
		log("Log, settings and custom objects save successful.");
	}

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

	public static void generateTable()
	{
		log("Generating !");
		LootTable t = window.panelLootTableSelection.selectedLootTable();
		if (t != null)
		{
			String output = t.display();
			window.showOutput(output);
			log("Successfully generated : " + output);
		}
	}

	public static byte getCurrentMode()
	{
		return currentMode;
	}

	public static boolean isReloading()
	{
		return isReloading;
	}

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

	public static void report(CommandGenerationException e)
	{
		Dialogs.showMessage(e.getMessage());
		log("Error : " + e.getMessage());
	}

	private static void saveVersion()
	{
		FileUtils.saveXMLFile(ObjectRegistry.allToXML(), "data/" + Settings.version().codeName);
	}

	public static Command selectedCommand()
	{
		return selected;
	}

	public static void setCurrentMode(byte mode)
	{
		currentMode = mode;
		log("Switching to " + MODE_NAMES[mode] + " mode.");
		window.updateMode();
		stateManager.updateMode();
	}

	public static void setExecuteInput(String input)
	{
		executeInput = input;
	}

	public static void setSelected(Command command)
	{
		selected = command;
		window.setSelected(command);
		stateManager.clear();
		stateManager.setCommandState(selectedCommand().getGUI(), null);
	}

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
