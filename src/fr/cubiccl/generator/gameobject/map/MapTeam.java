package fr.cubiccl.generator.gameobject.map;

import java.util.ArrayList;

/** Represents a Team in a Map. */
public class MapTeam
{

	/** How to handle collisions. */
	public String collisionRule;
	/** The Team's color. */
	public String color;
	/** How to handle death messages. */
	public String deathMessageVisibility;
	/** <code>true</code> if friendly fire is enabled. */
	public boolean friendlyFire;
	/** <code>true</code> if friendly invisible players are visible. */
	public boolean friendlyInvisibles;
	/** This Team's ID. */
	public String id;
	/** This Team's display name. */
	public String name;
	/** How to handle name tags. */
	public String nameTagVisibility;
	/** The list of Players in this Team. */
	public ArrayList<String> players;
	/** The prefix to add to Players in this Team. */
	public String prefix = "";
	/** The suffix to add to Players in this Team. */
	public String suffix = "§r";

}
