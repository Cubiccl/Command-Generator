package fr.cubiccl.generator.utils;

public final class Utils
{
	public static final String[] COLORS =
	{ "white", "aqua", "black", "blue", "dark_aqua", "dark_blue", "dark_gray", "dark_green", "dark_purple", "dark_red", "gold", "gray", "green",
			"light_purple", "red", "yellow" };

	public static void checkFloatSuperior(String name, String value, float minValue) throws CommandGenerationException
	{
		try
		{
			float f = Float.parseFloat(value);
			if (f < minValue) throw new WrongValueException(name, Lang.translate("error.number.greater").replaceAll("<min>", Float.toString(minValue)), value);
		} catch (NumberFormatException e)
		{
			throw new WrongValueException(name, Lang.translate("error.number.greater").replaceAll("<min>", Float.toString(minValue)), value);
		}
	}

	public static void checkInteger(String name, String value) throws CommandGenerationException
	{
		try
		{
			Integer.parseInt(value);
		} catch (Exception e)
		{
			throw new WrongValueException(name, Lang.translate("error.integer"), value);
		}
	}

	public static void checkIntegerInBounds(String name, String value, int minValue, int maxValue) throws CommandGenerationException
	{
		try
		{
			int i = Integer.parseInt(value);
			if (i < minValue || i > maxValue) throw new WrongValueException(name, Lang.translate("error.integer.bounds")
					.replaceAll("<min>", Integer.toString(minValue)).replaceAll("<max>", Integer.toString(maxValue)), value);
		} catch (NumberFormatException e)
		{
			throw new WrongValueException(name, Lang.translate("error.integer.bounds").replaceAll("<min>", Integer.toString(minValue))
					.replaceAll("<max>", Integer.toString(maxValue)), value);
		}
	}

	public static void checkIntegerSuperior(String name, String value, int minValue) throws CommandGenerationException
	{
		try
		{
			int i = Integer.parseInt(value);
			if (i < minValue) throw new WrongValueException(name, Lang.translate("error.integer.greater").replaceAll("<min>", Integer.toString(minValue)),
					value);
		} catch (NumberFormatException e)
		{
			throw new WrongValueException(name, Lang.translate("error.integer.greater").replaceAll("<min>", Integer.toString(minValue)), value);
		}
	}

	public static void checkNumber(String name, String value) throws CommandGenerationException
	{
		try
		{
			Float.parseFloat(value);
		} catch (Exception e)
		{
			throw new WrongValueException(name, Lang.translate("error.number"), value);
		}
	}

	public static void checkNumberInBounds(String name, String value, float minValue, float maxValue) throws CommandGenerationException
	{
		try
		{
			float f = Float.parseFloat(value);
			if (f < minValue || f > maxValue) throw new WrongValueException(name, Lang.translate("error.number.bounds")
					.replaceAll("<min>", Float.toString(minValue)).replaceAll("<max>", Float.toString(maxValue)), value);
		} catch (NumberFormatException e)
		{
			throw new WrongValueException(name, Lang.translate("error.number.bounds").replaceAll("<min>", Float.toString(minValue))
					.replaceAll("<max>", Float.toString(maxValue)), value);
		}
	}

	public static void checkPositiveInteger(String name, String value) throws CommandGenerationException
	{
		try
		{
			int i = Integer.parseInt(value);
			if (i < 0) throw new WrongValueException(name, Lang.translate("error.integer.positive"), value);
		} catch (NumberFormatException e)
		{
			throw new WrongValueException(name, Lang.translate("error.integer.positive"), value);
		}
	}

	public static void checkPositiveNumber(String name, String value) throws CommandGenerationException
	{
		try
		{
			float f = Float.parseFloat(value);
			if (f < 0) throw new WrongValueException(name, Lang.translate("error.number.positive"), value);
		} catch (NumberFormatException e)
		{
			throw new WrongValueException(name, Lang.translate("error.number.positive"), value);
		}
	}

	public static void checkStringId(String name, String value) throws CommandGenerationException
	{
		if (value.contains(" ")) throw new WrongValueException(name, Lang.translate("error.space"), value);
		if (value.equals("")) throw new MissingValueException(name);
	}

	public static int[] generateArray(int maxValue)
	{
		return generateArray(0, maxValue);
	}

	public static int[] generateArray(int minValue, int maxValue)
	{
		int[] array = new int[maxValue - minValue + 1];
		int value = minValue;
		for (int i = 0; i < array.length; ++i)
		{
			array[i] = value;
			++value;
		}
		return array;
	}

}
