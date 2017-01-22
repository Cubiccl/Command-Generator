package fr.cubiccl.generator.gameobject.target;

import java.util.ArrayList;

public class Target
{

	public static enum TargetType
	{
		ALL_ENTITIES("@e"),
		ALL_PLAYERS("@a"),
		CLOSEST_PLAYER("@p"),
		PLAYER("player"),
		RANDOM_PLAYER("@r");

		public final String id;

		private TargetType(String id)
		{
			this.id = id;
		}
	}

	public static Target createFrom(String value)
	{
		if (!value.startsWith("@")) return new Target(value);
		TargetType type = TargetType.ALL_ENTITIES;
		if (value.charAt(1) == 'a') type = TargetType.ALL_PLAYERS;
		else if (value.charAt(1) == 'p') type = TargetType.CLOSEST_PLAYER;
		else if (value.charAt(1) == 'r') type = TargetType.RANDOM_PLAYER;
		if (value.length() == 2) return new Target(type);

		String[] args = value.substring(3, value.length() - 1).split(",");
		ArrayList<Argument> arguments = new ArrayList<Argument>();
		for (String arg : args)
			arguments.add(Argument.createFrom(arg.split("=")[0], arg.split("=")[1]));

		return new Target(type, arguments.toArray(new Argument[arguments.size()]));
	}

	public static TargetType typeFromID(String id)
	{
		TargetType[] types = TargetType.values();
		for (TargetType type : types)
			if (type.id.equals(id)) return type;
		return TargetType.ALL_PLAYERS;
	}

	public static String[] types()
	{
		TargetType[] types = TargetType.values();
		String[] names = new String[types.length];
		for (int i = 0; i < names.length; ++i)
			names[i] = types[i].id;
		return names;
	}

	public final Argument[] arguments;
	public final String playerName;
	public final TargetType type;

	public Target(String playerName)
	{
		this.type = TargetType.PLAYER;
		this.playerName = playerName;
		this.arguments = null;
	}

	public Target(TargetType type, Argument... arguments)
	{
		this.type = type;
		this.playerName = null;
		this.arguments = arguments;
	}

	public String toCommand()
	{
		if (this.type == TargetType.PLAYER) return this.playerName;
		String command = this.type.id;
		if (this.arguments.length == 0) return command;
		command += "[";
		for (int i = 0; i < this.arguments.length; ++i)
		{
			if (i != 0) command += ",";
			command += this.arguments[i].toCommand();
		}
		return command + "]";
	}

}
