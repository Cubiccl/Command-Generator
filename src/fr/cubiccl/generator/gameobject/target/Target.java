package fr.cubiccl.generator.gameobject.target;

import java.awt.Component;
import java.util.ArrayList;

import fr.cubiccl.generator.gameobject.GameObject;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gui.component.interfaces.IObjectList;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelTarget;
import fr.cubiccl.generator.gui.component.panel.utils.ListProperties;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

public class Target extends GameObject implements IObjectList<Target>
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
		{
			Argument a = Argument.createFrom(arg.split("=")[0], arg.split("=")[1]);
			if (a != null) arguments.add(a);
		}

		return new Target(type, arguments.toArray(new Argument[arguments.size()]));
	}

	public static Target createFrom(TagCompound tag)
	{
		if (!tag.hasTag(Tags.TARGET.id())) return null;
		return createFrom((String) tag.getTagFromId(Tags.TARGET.id()).value());
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

	public Argument[] arguments;
	public String playerName;
	public TargetType type;

	public Target()
	{
		this(TargetType.ALL_PLAYERS);
	}

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

	@Override
	public CGPanel createPanel(ListProperties properties)
	{
		PanelTarget p = new PanelTarget(null, PanelTarget.ALL_ENTITIES);
		p.setupFrom(this);
		p.setName(new Text("target.title.any"));
		return p;
	}

	@Override
	public Component getDisplayComponent()
	{
		return new CGLabel(new Text(this.toString(), false));
	}

	@Override
	public String getName(int index)
	{
		return this.customName() == null || this.customName().equals("") ? this.toString() : this.customName();
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

	@Override
	public String toString()
	{
		return this.toCommand();
	}

	@Override
	public TagCompound toTag(TemplateCompound container, boolean includeName)
	{
		if (includeName) return container.create(Tags.TARGET.create(this.toCommand()), this.nameTag());
		return container.create(Tags.TARGET.create(this.toCommand()));
	}

	@Override
	public Target update(CGPanel panel) throws CommandGenerationException
	{
		Target t = ((PanelTarget) panel).generate();
		this.arguments = t.arguments;
		this.playerName = t.playerName;
		this.type = t.type;
		return this;
	}

}
