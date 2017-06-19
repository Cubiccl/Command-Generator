package fr.cubiccl.generator.gameobject.map;

/** Represents a Player score in a Map. */
public class MapScore
{

	/** For trigger objectives, <code>true</code> if the objective is enabled for this player. */
	public boolean enabled;
	/** The name of the Player. */
	public String name;
	/** The Objective. */
	public MapObjective objective;
	/** The score. */
	public int score;

}
