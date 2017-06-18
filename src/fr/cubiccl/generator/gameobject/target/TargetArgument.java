package fr.cubiccl.generator.gameobject.target;

import org.jdom2.Element;

import fr.cubiccl.generator.utils.Lang;

/** Represents a single Argument used in a {@link Target}. */
public class TargetArgument
{

	/** Creates an Argument from the input String.
	 * 
	 * @param id - The ID of the Argument.
	 * @param value - The Argument value.
	 * @return The created Argument. */
	public static TargetArgument fromString(String id, String value)
	{
		boolean reversed = false;
		if (value.startsWith("!"))
		{
			reversed = true;
			value = value.substring(1);
		}
		ArgumentType a = null;
		for (ArgumentType arg : ArgumentType.arguments())
			if (id.equals(arg.id))
			{
				a = arg;
				break;
			}

		if (a != null) return new TargetArgument(a, value, reversed);

		if (id.contains("_min")) return new TargetArgument(ArgumentType.SCORE, id.substring("score_".length(), id.length() - "_min".length()), value, reversed);
		else if (id.contains("score_")) return new TargetArgument(ArgumentType.SCORE, id.substring("score_".length()), value, reversed);
		return null;
	}

	/** Creates an Argument from the input XML element.
	 * 
	 * @param xml - The XML element describing the Argument.
	 * @return The created Argument. */
	public static TargetArgument fromXML(Element xml)
	{
		if (xml.getChild("value2") == null) return new TargetArgument(ArgumentType.find(xml.getChildText("id")), xml.getChildText("value"),
				Boolean.parseBoolean(xml.getChildText("reversed")));
		return new TargetArgument(ArgumentType.find(xml.getChildText("id")), xml.getChildText("value"), xml.getChildText("value2"), Boolean.parseBoolean(xml
				.getChildText("reversed")));
	}

	/** The Argument type. */
	public final ArgumentType argument;
	/** <code>true</code> if the value of the Argument is reversed, thus should start with "<code>!</code>". */
	public final boolean reversed;
	/** This Argument's value. */
	public final String value;
	/** Sometimes needed as a second value. */
	public final String value2;

	public TargetArgument(ArgumentType argument, String value)
	{
		this(argument, value, false);
	}

	public TargetArgument(ArgumentType argument, String value, boolean reversed)
	{
		this(argument, value, null, reversed);
	}

	public TargetArgument(ArgumentType argument, String value, String value2, boolean reversed)
	{
		this.argument = argument;
		this.value = value;
		this.value2 = value2;
		this.reversed = reversed;
	}

	/** @return This Argument's value to display to the user. */
	private String displayValue()
	{
		return this.value.equals("") ? Lang.translate("general.none") : this.value;
	}

	public TargetArgument duplicate()
	{
		return new TargetArgument(this.argument, this.value, this.value2, this.reversed);
	}

	/** @return The separator between ID and value. */
	private String separator()
	{
		return this.reversed ? ": " + Lang.translate("general.not") + " " : ": ";
	}

	/** @return This Argument in command syntax to be generated. */
	public String toCommand()
	{
		if (this.argument == ArgumentType.SCORE) return "score_" + this.value + "=" + (this.reversed ? "!" : "") + this.value2;
		if (this.argument == ArgumentType.SCORE_MIN) return "score_" + this.value + "_min=" + (this.reversed ? "!" : "") + this.value2;
		return this.argument.id + "=" + (this.reversed ? "!" : "") + value;
	}

	@Override
	public String toString()
	{
		if (this.argument == ArgumentType.M) return this.argument.name() + this.separator()
				+ Lang.translate("gamemode." + (this.value.equals("-1") ? "all" : this.displayValue()));
		if (this.argument == ArgumentType.TYPE) return this.argument.name() + this.separator() + Lang.translate("entity." + this.displayValue());
		if (this.argument == ArgumentType.SCORE || this.argument == ArgumentType.SCORE_MIN) return this.argument.name() + this.separator()
				+ this.displayValue() + "=" + this.value2;

		return this.argument.name() + this.separator() + this.displayValue();
	}

	/** @return This Argument as an XML element to be stored. */
	public Element toXML()
	{
		Element root = new Element("argument");
		root.addContent(new Element("id").setText(this.argument.id));
		root.addContent(new Element("reversed").setText(Boolean.toString(this.reversed)));
		root.addContent(new Element("value").setText(this.value));
		if (this.argument == ArgumentType.SCORE || this.argument == ArgumentType.SCORE_MIN) root.addContent(new Element("value2").setText(this.value2));
		return root;
	}

}