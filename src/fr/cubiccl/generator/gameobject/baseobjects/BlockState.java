package fr.cubiccl.generator.gameobject.baseobjects;

import java.util.ArrayList;

public class BlockState implements Comparable<BlockState>
{
	public static final byte STRING = 0, BOOLEAN = 1, INTEGER = 2;

	/** The damage value corresponding to this state. */
	public final int damageValue;
	/** This Block state's ID. */
	public final String id;
	/** Damage value to start at for (mostly) furniture states (ladders, furnaces). */
	public int startsAt = 0;
	/** The type of the value. */
	public final byte type;
	/** The state's values. */
	public final String[] values;

	public BlockState(String id, byte type, int damageValue, String... values)
	{
		super();
		this.damageValue = damageValue;
		this.id = id;
		this.type = type;
		this.values = values;
	}

	@Override
	public int compareTo(BlockState o)
	{
		return Integer.compare(this.damageValue, o.damageValue);
	}

	public ArrayList<Integer> damageValues()
	{
		ArrayList<Integer> d = new ArrayList<Integer>();
		for (int i = 0; i < this.values.length; ++i)
			d.add(this.startsAt + i * this.damageValue);
		return d;
	}

}
