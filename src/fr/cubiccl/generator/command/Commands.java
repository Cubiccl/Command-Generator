package fr.cubiccl.generator.command;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

import fr.cubiccl.generator.gui.LoadingFrame;
import fr.cubiccl.generator.utils.Settings;
import fr.cubiccl.generator.utils.Settings.Version;

/** Stores the available Commands. */
public class Commands
{

	/** Contains the Commands. */
	private static final HashMap<String, Command> commands = new HashMap<String, Command>();

	/** Loads all the Commands for the current Version.
	 * 
	 * @param frame - Loading Frame to inform on the progress. */
	public static void createCommands(LoadingFrame frame)
	{
		commands.clear();
		frame.setText("loading.commands");
		new CommandBlockdata();
		new CommandClear();
		new CommandClone();
		new CommandDefaultgamemode();
		new CommandDifficulty();
		new CommandEffect();
		new CommandEnchant();
		new CommandEntitydata("entitydata");
		new CommandExecute();
		new CommandFill();
		new CommandFunction();
		new CommandGamemode();
		new CommandGamerule();
		new CommandGive();
		new CommandKill();
		new CommandLocate();
		new CommandParticle();
		new CommandPlaysound();
		new CommandReplaceitem();
		new CommandSay();
		new CommandScoreboardObjectives();
		new CommandScoreboardPlayers();
		new CommandScoreboardTeams();
		new CommandSetblock();
		new CommandSetworldspawn();
		new CommandSpawnpoint();
		new CommandSpreadplayers();
		new CommandStats();
		new CommandStopsound();
		new CommandSummon();
		new CommandTeleport();
		new CommandTell();
		new CommandTellraw();
		new CommandEntitydata("testfor");
		new CommandTestforblock();
		new CommandTestforblocks();
		new CommandTime();
		new CommandTitle();
		new CommandTp();
		new CommandTrigger();
		new CommandWeather();
		new CommandWorldborder();
		new CommandXp();

		if (Settings.version().isBefore(Version.v1d11))
		{
			new CommandAchievement();
		}

		if (Settings.version().isAfter(Version.v1d12))
		{
			new CommandAdvancement();
			new CommandRecipe();
		}

	}

	/** @param id - ID of the Command.
	 * @return The Command with the input <code>ID</code>. */
	public static Command getCommandFromID(String id)
	{
		return commands.get(id);
	}

	/** @return An array containing all Commands, sorted by ID. */
	public static Command[] getCommands()
	{
		ArrayList<Command> c = new ArrayList<Command>();
		c.addAll(commands.values());
		c.sort(Comparator.naturalOrder());
		return c.toArray(new Command[c.size()]);
	}

	/** Finds the Command that matches the input generated command.
	 * 
	 * @param command - the command to parse.
	 * @return The Command matching the input text. <code>null</code> if the input matches no Command. */
	public static Command identifyCommand(String command)
	{
		if (command.startsWith("/")) command = command.substring(1);
		Command co = getCommandFromID(command.split(" ")[0]);
		if (co != null) return co;
		for (Command c : commands.values())
			if (command.startsWith(c.id)) return c;
		return null;
	}

	/** Registers a Command. */
	public static void registerCommand(Command command)
	{
		commands.put(command.id, command);
	}

	private Commands()
	{}
}