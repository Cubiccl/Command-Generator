package fr.cubiccl.generator;

import java.util.ArrayList;

import fr.cubi.cubigui.DisplayUtils;
import fr.cubiccl.generator.command.Command;
import fr.cubiccl.generator.command.Commands;
import fr.cubiccl.generator.gameobject.ObjectRegistry;
import fr.cubiccl.generator.gui.Window;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.FileUtils;
import fr.cubiccl.generator.utils.Lang;
import fr.cubiccl.generator.utils.Settings;
import fr.cubiccl.generator.utils.StateManager;
import fr.cubiccl.generator.utils.Textures;

public class CommandGenerator
{
	private static String executeCommand = "";
	private static ArrayList<String> log = new ArrayList<String>();
	private static Command selected;
	public static StateManager stateManager;
	public static Window window;

	public static void cancelExecute()
	{
		executeCommand = "";
		window.setExecuteCommand(false);
	}

	public static void finishLog()
	{
		log("Exiting Command Generator... Bye bye !");
		FileUtils.writeToFile("log.txt", log.toArray(new String[log.size()]));
	}

	public static void generate()
	{
		log("Generating !");
		try
		{
			String command = selectedCommand().generate();
			{
				if (executeCommand.equals("")) executeCommand += command;
				else executeCommand += command.substring(1);
				window.showCommand(executeCommand);
				if (command.startsWith("/execute ")) DisplayUtils.showMessage(window, Lang.translate("general.success"), Lang.translate("general.success_execute"));
				else executeCommand = "";
				window.setExecuteCommand(command.startsWith("/execute "));
			}
			log("Successfully generated : " + command);
		} catch (CommandGenerationException e)
		{
			report(e);
		}
	}

	public static void log(String text)
	{
		System.out.println(text);
		log.add(text);
	}

	public static void main(String[] args)
	{
		log("Welcome to the Command Generator vIndev by Cubi !");
		log("Charging textures...");
		Textures.createTextures();
		Settings.createSettings();
		log("---- Creating window ----");
		stateManager = new StateManager();
		window = new Window();
		window.updateTranslations();
		setSelected(Commands.getCommandFromID("achievement"));
		window.setVisible(true);
	}

	public static void report(CommandGenerationException e)
	{
		DisplayUtils.showMessage(window, Lang.translate("error.title"), e.getMessage());
		log("Error : " + e.getMessage());
	}

	public static Command selectedCommand()
	{
		return selected;
	}

	public static void setSelected(Command command)
	{
		if (command == selected) return;
		selected = command;
		stateManager.clear();
		stateManager.setState(selectedCommand().createGUI(), null);
	}

	public static void updateData()
	{
		log("---- Creating objects ----");
		ObjectRegistry.createObjects();
		Commands.createCommands();
	}

	public static void updateLanguage()
	{
		Lang.updateLang();
		if (window != null) window.updateTranslations();
	}

}
