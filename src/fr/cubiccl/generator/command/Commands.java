package fr.cubiccl.generator.command;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

import fr.cubiccl.generator.gui.LoadingFrame;

public class Commands
{

	private static final HashMap<String, Command> commands = new HashMap<String, Command>();

	public static void createCommands(LoadingFrame frame)
	{
		frame.setText("loading.commands");
		new CommandAchievement();
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
	}

	public static Command getCommandFromID(String id)
	{
		return commands.get(id);
	}

	public static Command[] getCommands()
	{
		ArrayList<Command> c = new ArrayList<Command>();
		c.addAll(commands.values());
		c.sort(new Comparator<Command>()
		{

			@Override
			public int compare(Command o1, Command o2)
			{
				return o1.id.compareTo(o2.id);
			}
		});
		return c.toArray(new Command[c.size()]);
	}

	public static Command identifyCommand(String command)
	{
		if (command.startsWith("/")) command = command.substring(1);
		Command co = getCommandFromID(command.split(" ")[0]);
		if (co != null) return co;
		for (Command c : commands.values())
			if (command.startsWith(c.id)) return c;
		return null;
	}

	public static void registerCommand(Command command)
	{
		commands.put(command.id, command);
	}

	private Commands()
	{}
}