package fr.cubiccl.generator.utils;

import java.io.IOException;
import java.io.StringWriter;
import java.text.DecimalFormat;

import com.eclipsesource.json.JsonValue;
import com.eclipsesource.json.PrettyPrint;

public final class Utils
{
	public static final String[] COLORS =
	{ "white", "aqua", "black", "blue", "dark_aqua", "dark_blue", "dark_gray", "dark_green", "dark_purple", "dark_red", "gold", "gray", "green",
			"light_purple", "red", "yellow" };
	public static final String[] WOOL_COLORS =
	{ "white", "orange", "magenta", "light_blue", "yellow", "lime", "pink", "gray", "light_gray", "cyan", "purple", "blue", "brown", "green", "red", "black" };

	public static void checkFloat(Text name, String value) throws CommandGenerationException
	{
		try
		{
			Float.parseFloat(value);
		} catch (Exception e)
		{
			throw new WrongValueException(name, new Text("error.number"), value);
		}
	}

	public static void checkFloatInBounds(Text name, String value, float minValue, float maxValue) throws CommandGenerationException
	{
		try
		{
			float i = Float.parseFloat(value);
			if (i < minValue || i > maxValue) throw new WrongValueException(name, new Text("error.number.bounds", new Replacement("<min>", new Text(
					Float.toString(minValue), false)), new Replacement("<max>", new Text(Float.toString(maxValue), false))), value);
		} catch (NumberFormatException e)
		{
			throw new WrongValueException(name, new Text("error.number.bounds", new Replacement("<min>", new Text(Float.toString(minValue), false)),
					new Replacement("<max>", new Text(Float.toString(maxValue), false))), value);
		}
	}

	public static void checkFloatSuperior(Text name, String value, float minValue) throws CommandGenerationException
	{
		try
		{
			float f = Float.parseFloat(value);
			if (f < minValue) throw new WrongValueException(name, new Text("error.number.greater", new Replacement("<min>", new Text(Float.toString(minValue),
					false))), value);
		} catch (NumberFormatException e)
		{
			throw new WrongValueException(name, new Text("error.number.greater", new Replacement("<min>", new Text(Float.toString(minValue), false))), value);
		}
	}

	public static void checkInteger(Text name, String value) throws CommandGenerationException
	{
		try
		{
			Integer.parseInt(value);
		} catch (Exception e)
		{
			throw new WrongValueException(name, new Text("error.integer"), value);
		}
	}

	public static void checkIntegerInBounds(Text name, String value, int minValue, int maxValue) throws CommandGenerationException
	{
		try
		{
			int i = Integer.parseInt(value);
			if (i < minValue || i > maxValue) throw new WrongValueException(name, new Text("error.integer.bounds", new Replacement("<min>", new Text(
					Integer.toString(minValue), false)), new Replacement("<max>", new Text(Integer.toString(maxValue), false))), value);
		} catch (NumberFormatException e)
		{
			throw new WrongValueException(name, new Text("error.integer.bounds", new Replacement("<min>", new Text(Integer.toString(minValue), false)),
					new Replacement("<max>", new Text(Integer.toString(maxValue), false))), value);
		}
	}

	public static void checkIntegerSuperior(Text name, String value, int minValue) throws CommandGenerationException
	{
		try
		{
			int i = Integer.parseInt(value);
			if (i < minValue) throw new WrongValueException(name, new Text("error.integer.greater", new Replacement("<min>", new Text(
					Integer.toString(minValue), false))), value);
		} catch (NumberFormatException e)
		{
			throw new WrongValueException(name, new Text("error.integer.greater", new Replacement("<min>", new Text(Integer.toString(minValue), false))), value);
		}
	}

	public static void checkNumber(Text name, String value) throws CommandGenerationException
	{
		try
		{
			Double.parseDouble(value);
		} catch (Exception e)
		{
			throw new WrongValueException(name, new Text("error.number"), value);
		}
	}

	public static void checkNumberInBounds(Text name, String value, float minValue, float maxValue) throws CommandGenerationException
	{
		try
		{
			float f = Float.parseFloat(value);
			if (f < minValue || f > maxValue) throw new WrongValueException(name, new Text("error.number.bounds", new Replacement("<min>", new Text(
					Float.toString(minValue), false)), new Replacement("<max>", new Text(Float.toString(maxValue), false))), value);
		} catch (NumberFormatException e)
		{
			throw new WrongValueException(name, new Text("error.number.bounds", new Replacement("<min>", new Text(Float.toString(minValue), false)),
					new Replacement("<max>", new Text(Float.toString(maxValue), false))), value);
		}
	}

	public static void checkNumberSuperior(Text name, String value, double minValue) throws CommandGenerationException
	{
		try
		{
			double f = Double.parseDouble(value);
			if (f < minValue) throw new WrongValueException(name, new Text("error.number.greater", new Replacement("<min>", new Text(Double.toString(minValue),
					false))), value);
		} catch (NumberFormatException e)
		{
			throw new WrongValueException(name, new Text("error.number.greater", new Replacement("<min>", new Text(Double.toString(minValue), false))), value);
		}
	}

	public static void checkPositiveInteger(Text name, String value) throws CommandGenerationException
	{
		try
		{
			int i = Integer.parseInt(value);
			if (i < 0) throw new WrongValueException(name, new Text("error.integer.positive"), value);
		} catch (NumberFormatException e)
		{
			throw new WrongValueException(name, new Text("error.integer.positive"), value);
		}
	}

	public static void checkPositiveNumber(Text name, String value) throws CommandGenerationException
	{
		try
		{
			float f = Float.parseFloat(value);
			if (f < 0) throw new WrongValueException(name, new Text("error.number.positive"), value);
		} catch (NumberFormatException e)
		{
			throw new WrongValueException(name, new Text("error.number.positive"), value);
		}
	}

	public static void checkStringId(Text name, String value) throws CommandGenerationException
	{
		if (value.contains(" ")) throw new WrongValueException(name, new Text("error.space"), value);
		if (value.equals("")) throw new MissingValueException(name);
	}

	public static String doubleToString(double d)
	{
		return (new DecimalFormat("#.######").format(d)).replaceAll(",", ".");
	}

	public static int[] generateArray(int maxValue)
	{
		if (maxValue < 0) return new int[0];
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

	public static String jsonToString(JsonValue json, boolean shouldIndent)
	{
		return jsonToString(json, shouldIndent, null);
	}

	public static String jsonToString(JsonValue json, boolean shouldIndent, String id)
	{
		boolean indentation = shouldIndent && Boolean.parseBoolean(Settings.getSetting(Settings.INDENTATION));
		StringWriter w = new StringWriter();
		try
		{
			json.writeTo(w, indentation ? PrettyPrint.PRETTY_PRINT : PrettyPrint.MINIMAL);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return id == null || id.equals("") ? w.toString() : id + ":" + (indentation ? " " : "") + w.toString();
	}
}
