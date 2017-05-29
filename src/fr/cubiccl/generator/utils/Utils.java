package fr.cubiccl.generator.utils;

import java.text.DecimalFormat;

/** Contains various constants and methods. */
public final class Utils
{
	/** Lists all Biomes. */
	public static final String[] BIOMES =
	{ "ocean", "deep_ocean", "frozen_ocean", "plains", "mutated_plains", "desert", "desert_hills", "mutated_desert", "extreme_hills", "mutated_extreme_hills",
			"smaller_extreme_hills", "extreme_hills_with_trees", "mutated_extreme_hills_with_trees", "forest", "forest_hills", "mutated_forest", "taiga",
			"mutated_taiga", "taiga_hills", "taiga_cold", "mutated_taiga_cold", "taiga_cold_hills", "redwood_taiga", "mutated_redwood_taiga",
			"redwood_taiga_hills", "mutated_redwood_taiga_hills", "swampland", "mutated_swampland", "river", "frozen_river", "ice_flats", "ice_mountains",
			"mutated_ice_flats", "mushroom_island", "mushroom_island_shore", "beaches", "stone_beach", "cold_beach", "jungle", "jungle_hills", "jungle_edge",
			"mutated_jungle", "mutated_jungle_edge", "birch_forest", "birch_forest_hills", "mutated_birch_forest", "mutated_birch_forest_hills",
			"roofed_forest", "mutated_roofed_forest", "savanna", "mutated_savanna", "savanna_rock", "mutated_savanna_rock", "mesa", "mutated_mesa",
			"mesa_rock", "mutated_mesa_rock", "mesa_clear_rock", "mutated_mesa_clear_rock", "hell", "sky", "void" };
	/** Lists all text colors. */
	public static final String[] COLORS =
	{ "white", "aqua", "black", "blue", "dark_aqua", "dark_blue", "dark_gray", "dark_green", "dark_purple", "dark_red", "gold", "gray", "green",
			"light_purple", "red", "yellow" };
	/** Lists all dimensions. */
	public static final String[] DIMENSIONS =
	{ "overworld", "the_end", "the_nether" };
	/** Lists all structures. */
	public static final String[] STRUCTURES =
	{ "EndCity", "Fortress", "Mansion", "Mineshaft", "Monument", "Stronghold", "Temple", "Village" };
	/** Lists all wool colors. */
	public static final String[] WOOL_COLORS =
	{ "white", "orange", "magenta", "light_blue", "yellow", "lime", "pink", "gray", "light_gray", "cyan", "purple", "blue", "brown", "green", "red", "black" };

	/** Checks if the input <code>value</code> can be parsed to a float.
	 * 
	 * @param name - The name of the value; will be used to tell the user if there is an error.
	 * @param value - The value to check.
	 * @throws CommandGenerationException if the input value can't be parsed. */
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

	/** Checks if the input <code>value</code> can be parsed to a float, and if so, if it's contained in the input values.
	 * 
	 * @param name - The name of the value; will be used to tell the user if there is an error.
	 * @param value - The value to check.
	 * @param minValue - The minimum value for the float.
	 * @param maxValue - The maximum value for the float.
	 * @throws CommandGenerationException if the input value can't be parsed. */
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

	/** Checks if the input <code>value</code> can be parsed to a float, and if so, if it's superior to the input value.
	 * 
	 * @param name - The name of the value; will be used to tell the user if there is an error.
	 * @param value - The value to check.
	 * @param minValue - The minimum value for the float.
	 * @throws CommandGenerationException if the input value can't be parsed. */
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

	/** Checks if the input <code>value</code> can be parsed to an integer.
	 * 
	 * @param name - The name of the value; will be used to tell the user if there is an error.
	 * @param value - The value to check.
	 * @throws CommandGenerationException if the input value can't be parsed. */
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

	/** Checks if the input <code>value</code> can be parsed to an integer, and if so, if it's contained in the input values.
	 * 
	 * @param name - The name of the value; will be used to tell the user if there is an error.
	 * @param value - The value to check.
	 * @param minValue - The minimum value for the integer.
	 * @param maxValue - The maximum value for the integer.
	 * @throws CommandGenerationException if the input value can't be parsed. */
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

	/** Checks if the input <code>value</code> can be parsed to an integer, and if so, if it's superior to the input value.
	 * 
	 * @param name - The name of the value; will be used to tell the user if there is an error.
	 * @param value - The value to check.
	 * @param minValue - The minimum value for the integer.
	 * @throws CommandGenerationException if the input value can't be parsed. */
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

	/** Checks if the input <code>value</code> can be parsed to a double.
	 * 
	 * @param name - The name of the value; will be used to tell the user if there is an error.
	 * @param value - The value to check.
	 * @throws CommandGenerationException if the input value can't be parsed. */
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

	/** Checks if the input <code>value</code> can be parsed to a double, and if so, if it's contained in the input values.
	 * 
	 * @param name - The name of the value; will be used to tell the user if there is an error.
	 * @param value - The value to check.
	 * @param minValue - The minimum value for the double.
	 * @param maxValue - The maximum value for the double.
	 * @throws CommandGenerationException if the input value can't be parsed. */
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

	/** Checks if the input <code>value</code> can be parsed to a double, and if so, if it's superior to the input value.
	 * 
	 * @param name - The name of the value; will be used to tell the user if there is an error.
	 * @param value - The value to check.
	 * @param minValue - The minimum value for the double.
	 * @throws CommandGenerationException if the input value can't be parsed. */
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

	/** Checks if the input <code>value</code> can be parsed to a positive integer.
	 * 
	 * @param name - The name of the value; will be used to tell the user if there is an error.
	 * @param value - The value to check.
	 * @throws CommandGenerationException if the input value can't be parsed. */
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

	/** Checks if the input <code>value</code> can be parsed to a positive double.
	 * 
	 * @param name - The name of the value; will be used to tell the user if there is an error.
	 * @param value - The value to check.
	 * @throws CommandGenerationException if the input value can't be parsed. */
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

	/** Checks if the input value matches the string ID format. i.e., if it doesn't contain any space and it's not empty.
	 * 
	 * @param name - The name of the value; will be used to tell the user if there is an error.
	 * @param value - The value to check.
	 * @throws CommandGenerationException if the input value doesn't match the format. */
	public static void checkStringId(Text name, String value) throws CommandGenerationException
	{
		if (value.contains(" ")) throw new WrongValueException(name, new Text("error.space"), value);
		if (value.equals("")) throw new MissingValueException(name);
	}

	/** Parses a double to a string.
	 * 
	 * @param d - The double to parse.
	 * @return The string value of the double. */
	public static String doubleToString(double d)
	{
		return (new DecimalFormat("#.######").format(d)).replaceAll(",", ".");
	}

	/** Generates an Integer array containing all numbers between 0 and <code>maxValue</code>.
	 * 
	 * @param maxValue - The maximum value.
	 * @return The generated array. */
	public static int[] generateArray(int maxValue)
	{
		if (maxValue < 0) return new int[0];
		return generateArray(0, maxValue);
	}

	/** Generates an Integer array containing all numbers between <code>minValue</code> and <code>maxValue</code>.
	 * 
	 * @param minValue - The minimum value.
	 * @param maxValue - The maximum value.
	 * @return The generated array. */
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

	/** @return True if the input array starts at 0 and the value at each index increments by 1. */
	public static boolean isArrayConsecutive(int... array)
	{
		for (int i = 0; i < array.length; ++i)
			if (array[i] != i) return false;
		return true;
	}

}
