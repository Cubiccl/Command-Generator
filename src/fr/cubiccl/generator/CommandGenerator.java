package fr.cubiccl.generator;

import java.util.ArrayList;

import fr.cubi.cubigui.DisplayUtils;
import fr.cubiccl.generator.command.Command;
import fr.cubiccl.generator.command.Commands;
import fr.cubiccl.generator.gameobject.registries.ObjectCreator;
import fr.cubiccl.generator.gui.LoadingFrame;
import fr.cubiccl.generator.gui.Window;
import fr.cubiccl.generator.utils.*;

public class CommandGenerator
{
	private static ArrayList<String> commandHistory = new ArrayList<String>();
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

	public static String[] commandHistory()
	{
		return commandHistory.toArray(new String[commandHistory.size()]);
	}

	public static void exit()
	{
		log("Exiting Command Generator... Bye bye !");
		FileUtils.writeToFile("log.txt", log.toArray(new String[log.size()]));
		Settings.save();
		log("Log and settings save successful.");
	}

	public static void generate()
	{
		log("Generating !");
		try
		{
			String command = selectedCommand().generate();
			{
				if (executeCommand.equals("")) executeCommand += command;
				else executeCommand += command;

				window.showCommand(executeCommand);

				if (command.startsWith("execute ")) DisplayUtils.showMessage(window, Lang.translate("general.success"),
						Lang.translate("general.success_execute"));
				else
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

	public static void log(String text)
	{
		System.out.println(text);
		log.add(text);
	}

	public static void main(String[] args)
	{
		log("Welcome to the Command Generator v" + Settings.GENERATOR_VERSION + " by Cubi !");
		FileUtils.checkForUpdates();
		Settings.loadSettings();
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

		if (window != null) window.setEnabled(false);
		LoadingFrame frame = new LoadingFrame(5, window == null);
		ObjectCreator.loadObjects(frame);
		Commands.createCommands(frame);
		frame.dispose();

		if (window != null)
		{
			window.onVersionChange();
			window.setEnabled(true);
			window.requestFocus();
		}
	}

	public static void updateLanguage()
	{
		Lang.updateLang();
		if (window != null) window.updateTranslations();
	}

}
