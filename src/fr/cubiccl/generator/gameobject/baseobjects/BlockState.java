package fr.cubiccl.generator.gameobject.baseobjects;

import java.util.ArrayList;
import java.util.HashMap;

import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

public class BlockState implements Comparable<BlockState>
{
	public static final byte STRING = 0, BOOLEAN = 1, INTEGER = 2;

	public static HashMap<String, String> parseState(String state) throws CommandGenerationException
	{
		HashMap<String, String> parsed = new HashMap<String, String>();
		try
		{
			String[] parts = state.split(",");
			for (String part : parts)
				parsed.put(part.substring(0, part.indexOf('=')), part.substring(part.indexOf('=') + 1));
		} catch (Exception e)
		{
			throw new CommandGenerationException(new Text("error.state.parsing"));
		}
		return parsed;
	}

	public static String toCommand(HashMap<String, String> states)
	{
		String command = "";
		for (String id : states.keySet())
		{
			command += id + "=" + states.get(id) + ",";
		}
		return command.substring(0, command.length() - 1);
	}

	/** The damage values corresponding to this state. */
	public final int[] customDamageValues;
	/** The damage value corresponding to this state. */
	public final int damageValue;
	/** This Block state's ID. */
	public final String id;
	/** Damage value to start at for (mostly) furniture states (ladders, furnaces). */
	private int startsAt = 0;

	/** The type of the value. */
	public final byte type;

	/** The state's values. */
	public final String[] values;

	public BlockState(String id, byte type, int damageValue, int[] customDamage, String... values)
	{
		super();
		this.damageValue = damageValue;
		this.customDamageValues = customDamage;
		this.id = id;
		this.type = type;
		this.values = values;
	}

	public BlockState(String id, byte type, int damageValue, String... values)
	{
		this(id, type, damageValue, null, values);
	}

	public BlockState(String id, byte type, int[] customDamage, String... values)
	{
		this(id, type, -1, customDamage, values);
	}

	@Override
	public int compareTo(BlockState o)
	{
		return Integer.compare(this.damageValue, o.damageValue);
	}

	public int damageForValue(String value)
	{
		if (this.damageValue == -1) return 0;
		int index = 0;
		for (int i = 0; i < this.values.length; ++i)
			if (value.equals(this.values[i]))
			{
				index = i;
				break;
			}
		return this.damageValues().get(index);
	}

	public ArrayList<Integer> damageValues()
	{
		ArrayList<Integer> d = new ArrayList<Integer>();
		if (this.isDamageCustom())
		{
			for (int i : this.customDamageValues)
				d.add(i);
			return d;
		}

		if (this.damageValue != -1) for (int i = 0; i < this.values.length; ++i)
			d.add(this.startsAt + i * this.damageValue);
		else d.add(0);
		return d;
	}

	public int getStartsAt()
	{
		return this.startsAt;
	}

	public boolean hasValue(String value)
	{
		for (String v : this.values)
			if (v.equals(value)) return true;
		return false;
	}

	boolean isDamageCustom()
	{
		return this.customDamageValues != null;
	}

	public BlockState setStartsAt(int startsAt)
	{
		this.startsAt = startsAt;
		return this;
	}
}
