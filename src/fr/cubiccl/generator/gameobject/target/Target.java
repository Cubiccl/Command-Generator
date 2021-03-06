package fr.cubiccl.generator.gameobject.target;

import java.awt.Component;
import java.util.ArrayList;

import org.jdom2.Element;

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

/** Represents a Target in a command. */
public class Target extends GameObject<Target> implements IObjectList<Target>
{

	/** Types for Target. */
	public static enum TargetType
	{
		ALL_ENTITIES("@e"),
		ALL_PLAYERS("@a"),
		CLOSEST_PLAYER("@p"),
		PLAYER("player"),
		RANDOM_PLAYER("@r"),
		SELF("@s");

		/** @param id - The ID of the type.
		 * @return The matching Target Type. */
		public static TargetType find(String id)
		{
			for (TargetType type : values())
				if (type.id.equals(id)) return type;
			return ALL_ENTITIES;
		}

		/** This type's ID. */
		public final String id;

		private TargetType(String id)
		{
			this.id = id;
		}
	}

	/** @return The Target types names. */
	public static String[] types()
	{
		TargetType[] types = TargetType.values();
		String[] names = new String[types.length];
		for (int i = 0; i < names.length; ++i)
			names[i] = types[i].id;
		return names;
	}

	/** The {@link TargetArgument Arguments} applied to this Target. */
	public TargetArgument[] arguments;
	/** If {@link Target#type type} is {@link TargetType#PLAYER}, the name of the targeted Player. */
	public String playerName;
	/** This Target's type. */
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

	public Target(TargetType type, TargetArgument... arguments)
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
	public Target duplicate(Target object)
	{
		this.playerName = object.playerName;
		this.type = object.type;
		this.arguments = new TargetArgument[object.arguments.length];
		for (int i = 0; i < this.arguments.length; ++i)
			this.arguments[i] = object.arguments[i].duplicate();
		return this;
	}

	@Override
	public Target fromNBT(TagCompound nbt)
	{
		if (!nbt.hasTag(Tags.TARGET.id())) return null;
		return this.fromString((String) nbt.getTagFromId(Tags.TARGET.id()).value());
	}

	/** Creates a Target from the input String.
	 * 
	 * @param value - The value to parse.
	 * @return The created Target. */
	public Target fromString(String value)
	{
		if (!value.startsWith("@")) return new Target(value);
		this.type = TargetType.ALL_ENTITIES;
		if (value.charAt(1) == 'a') this.type = TargetType.ALL_PLAYERS;
		else if (value.charAt(1) == 'p') this.type = TargetType.CLOSEST_PLAYER;
		else if (value.charAt(1) == 'r') this.type = TargetType.RANDOM_PLAYER;
		else if (value.charAt(1) == 's') this.type = TargetType.SELF;
		if (value.length() == 2) return this;

		String[] args = value.substring(3, value.length() - 1).split(",");
		ArrayList<TargetArgument> arguments = new ArrayList<TargetArgument>();
		for (String arg : args)
		{
			TargetArgument a = TargetArgument.fromString(arg.split("=")[0], arg.split("=")[1]);
			if (a != null) arguments.add(a);
		}

		this.arguments = arguments.toArray(new TargetArgument[arguments.size()]);
		return this;
	}

	@Override
	public Target fromXML(Element xml)
	{
		this.type = TargetType.find(xml.getChildText("type"));
		if (this.type == TargetType.PLAYER) this.playerName = xml.getChildText("playername");
		else
		{
			ArrayList<TargetArgument> a = new ArrayList<TargetArgument>();
			for (Element argument : xml.getChildren("argument"))
				a.add(TargetArgument.fromXML(argument));
			this.arguments = a.toArray(new TargetArgument[a.size()]);
		}
		this.findProperties(xml);
		return this;
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

	@Override
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
	public TagCompound toNBT(TemplateCompound container)
	{
		return container.create(Tags.TARGET.create(this.toCommand()));
	}

	@Override
	public String toString()
	{
		if (this.type == TargetType.PLAYER) return this.playerName;
		String s = new Text("target.type.tostring." + this.type.id).toString();
		if (this.arguments.length != 0)
		{
			s += " (";
			for (int i = 0; i < this.arguments.length; ++i)
			{
				if (i != 0) s += ",";
				s += this.arguments[i].toCommand();
			}
			s += ")";
		}
		return s;
	}

	@Override
	public Element toXML()
	{
		Element root = this.createRoot("target");
		root.addContent(new Element("type").setText(this.type.id));
		if (this.type == TargetType.PLAYER) root.addContent(new Element("playername").setText(this.playerName));
		else
		{
			for (TargetArgument argument : this.arguments)
				root.addContent(argument.toXML());
		}
		return root;
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
