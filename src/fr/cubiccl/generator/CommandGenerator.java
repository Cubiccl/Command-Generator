package fr.cubiccl.generator;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Comparator;

import javax.swing.ToolTipManager;

import fr.cubi.cubigui.CTextArea;
import fr.cubiccl.generator.command.Command;
import fr.cubiccl.generator.command.Commands;
import fr.cubiccl.generator.gameobject.loottable.LootTable;
import fr.cubiccl.generator.gameobject.registries.ObjectCreator;
import fr.cubiccl.generator.gameobject.registries.ObjectSaver;
import fr.cubiccl.generator.gameobject.tags.NBTReader;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gui.Dialogs;
import fr.cubiccl.generator.gui.LoadingFrame;
import fr.cubiccl.generator.gui.Window;
import fr.cubiccl.generator.gui.component.CScrollPane;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.utils.*;

public class CommandGenerator
{
	private static ArrayList<String> commandHistory = new ArrayList<String>();
	public static final byte COMMANDS = 0, LOOT_TABLES = 1;
	private static byte currentMode = COMMANDS;
	private static String executeCommand = "", executeInput = null;
	private static ArrayList<String> log = new ArrayList<String>();
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
			FileUtils.writeToFile("untranslated.txt", untranslated.toArray(new String[untranslated.size()]));
		}
		log("Log, settings and custom objects save successful.");
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

	public static void loadTable()
	{
		CGPanel p = new CGPanel();
		p.setLayout(new BorderLayout());
		p.add(new CGLabel("loottable.load.description"), BorderLayout.NORTH);
		CTextArea area = new CTextArea("");
		area.setEditable(true);
		CScrollPane sc = new CScrollPane(area);
		sc.setPreferredSize(new Dimension(400, 200));
		p.add(sc, BorderLayout.CENTER);
		if (!Dialogs.showConfirmDialog(p)) return;

		TagCompound tag = (TagCompound) NBTReader.read(area.getText(), true, true, true);
		LootTable table = LootTable.createFrom(tag);

		String name = Dialogs.showInputDialog(Lang.translate("objects.name"));
		if (name == null) return;
		table.setCustomName(name);
		window.panelLootTableSelection.list.add(table);
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
		if (!Settings.testMode) FileUtils.checkForUpdates();
		Settings.loadSettings();
		if (Settings.testMode) Lang.checkTranslations();
		log("---- Creating window ----");
		stateManager = new StateManager();
		window = new Window();
		ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);
		window.updateTranslations();
		setSelected(Commands.getCommandFromID("achievement"));
		window.setVisible(true);
	}

	public static void report(CommandGenerationException e)
	{
		Dialogs.showMessage(e.getMessage());
		log("Error : " + e.getMessage());
	}

	public static Command selectedCommand()
	{
		return selected;
	}

	public static void setCurrentMode(byte mode)
	{
		currentMode = mode;
		log("Switching to " + (currentMode == COMMANDS ? "Commands" : "Loot Tables") + " mode.");
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
		stateManager.setState(selectedCommand().getGUI(), null);
	}

	public static void updateData()
	{
		log("---- Creating objects ----");

		LoadingFrame frame = new LoadingFrame(5);
		ObjectCreator.createObjects(frame);
		Commands.createCommands(frame);
		ObjectSaver.load();
		frame.dispose();
	}

	public static void updateLanguage()
	{
		Lang.updateLang();
		if (window != null) window.updateTranslations();
	}

}
