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
		new CommandClear();
		new CommandClone();
		new CommandDefaultgamemode();
		new CommandDifficulty();
		new CommandEffect();
		new CommandEnchant();
		new CommandExecute();
		new CommandFill();
		new CommandGamemode();
		new CommandGamerule();
		new CommandGive();
		new CommandKill();
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
		new CommandSummon();
		new CommandTeleport();
		new CommandTell();
		new CommandTellraw();
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

	public static void registerCommand(Command command)
	{
		commands.put(command.id, command);
	}

	private Commands()
	{}
}