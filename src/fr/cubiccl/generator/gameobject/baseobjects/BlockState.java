package fr.cubiccl.generator.gameobject.baseobjects;

import java.util.ArrayList;
import java.util.HashMap;

import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

public class BlockState implements Comparable<BlockState>
{
	/** Identifiers for Block states value types.<br />
	 * <br />
	 * <table border="1">
	 * <tr>
	 * <td>ID</td>
	 * <td>Variable</td>
	 * <td>Mode</td>
	 * </tr>
	 * <tr>
	 * <td>0</td>
	 * <td>STRING</td>
	 * <td>String value</td>
	 * </tr>
	 * <tr>
	 * <td>1</td>
	 * <td>BOOLEAN</td>
	 * <td><code>"true"</code> or <code>"false"</code></td>
	 * </tr>
	 * <tr>
	 * <td>2</td>
	 * <td>INTEGER</td>
	 * <td>Integer value</td>
	 * </tr>
	 * </table> */
	public static final byte STRING = 0, BOOLEAN = 1, INTEGER = 2;

	/** Parses the input Block states.
	 * 
	 * @param state - The states to apply.
	 * @return A Hashmap containing a value for each used Block state.
	 * @throws CommandGenerationException - If State parsing fails. */
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

	/** @param states - A Hashmap containing a value for each used Block state.
	 * @return The command format for the input Block states. */
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
	/** The damage value corresponding to this state if {@link BlockState#customDamageValues} isn't <code>null</code>. */
	public final int damageValue;
	/** This Block state's ID. */
	public final String id;
	/** Damage value to start at for (mostly) furniture states (ladders, furnaces). */
	private int startsAt = 0;
	/** The type of the value.
	 * 
	 * @see BlockState#STRING */
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

	public BlockState(String id, byte type, String... values)
	{
		this(id, type, -1, values);
	}

	@Override
	public int compareTo(BlockState o)
	{
		return Integer.compare(this.damageValue, o.damageValue);
	}

	/** @param value - The value of this Block state.
	 * @return The damage value for the input state value. */
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

	/** @return The list of possible damage values created by this Block state. */
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

	/** Getter for {@link BlockState#startsAt}. */
	public int getStartsAt()
	{
		return this.startsAt;
	}

	/** @param value - A Block state value.
	 * @return <code>true</code> if the input value is possible for this State. */
	public boolean hasValue(String value)
	{
		for (String v : this.values)
			if (v.equals(value)) return true;
		return false;
	}

	/** @return <code>true</code> if the damage for this Block state isn't linear. */
	boolean isDamageCustom()
	{
		return this.customDamageValues != null;
	}

	/** Setter for {@link BlockState#startsAt}. */
	public BlockState setStartsAt(int startsAt)
	{
		this.startsAt = startsAt;
		return this;
	}

	/** @param damage - A damage value.
	 * @return The Block state value for the input damage value. */
	public String valueForDamage(int damage)
	{
		return this.values[this.damageValues().indexOf(damage)];
	}
}
