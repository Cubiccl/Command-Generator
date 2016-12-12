package fr.cubiccl.generator.gameobject.target;

import fr.cubiccl.generator.utils.Lang;

public class Argument
{
	public static Argument createFrom(String id, String value)
	{
		boolean reversed = false;
		if (value.startsWith("!"))
		{
			reversed = true;
			value = value.substring(1);
		}
		TargetArgument a = null;
		for (TargetArgument arg : TargetArgument.arguments())
			if (id.equals(arg.id))
			{
				a = arg;
				break;
			}

		if (a != null) return new Argument(a, value, reversed);

		if (id.contains("_min")) return new Argument(TargetArgument.SCORE, id.substring("score_".length(), id.length() - "_min".length()), value, reversed);
		else return new Argument(TargetArgument.SCORE, id.substring("score_".length()), value, reversed);
	}

	public final TargetArgument argument;
	public final boolean reversed;
	public final String value, value2;

	public Argument(TargetArgument argument, String value)
	{
		this(argument, value, false);
	}

	public Argument(TargetArgument argument, String value, boolean reversed)
	{
		this(argument, value, null, reversed);
	}

	public Argument(TargetArgument argument, String value, String value2, boolean reversed)
	{
		this.argument = argument;
		this.value = value;
		this.value2 = value2;
		this.reversed = reversed;
	}

	private String displayValue()
	{
		return this.value.equals("") ? Lang.translate("general.none") : this.value;
	}

	private String separator()
	{
		return this.reversed ? ": " + Lang.translate("general.not") + " " : ": ";
	}

	public String toCommand()
	{
		if (this.argument == TargetArgument.SCORE) return "score_" + this.value + "=" + (this.reversed ? "!" : "") + this.value2;
		if (this.argument == TargetArgument.SCORE_MIN) return "score_" + this.value + "_min=" + (this.reversed ? "!" : "") + this.value2;
		return this.argument.id + "=" + (this.reversed ? "!" : "") + value;
	}

	@Override
	public String toString()
	{
		if (this.argument == TargetArgument.M) return this.argument.name() + this.separator()
				+ Lang.translate("gamemode." + (this.value.equals("-1") ? "all" : this.displayValue()));
		if (this.argument == TargetArgument.TYPE) return this.argument.name() + this.separator() + Lang.translate("entity." + this.displayValue());
		if (this.argument == TargetArgument.SCORE || this.argument == TargetArgument.SCORE_MIN) return this.argument.name() + this.separator()
				+ this.displayValue() + "=" + this.value2;

		return this.argument.name() + this.separator() + this.displayValue();
	}
}