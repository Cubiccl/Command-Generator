package fr.cubiccl.generator.gameobject.map;

import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gameobject.utils.NBTSaveable;

/** Represents a Player score in a Map. */
public class MapScore implements NBTSaveable<MapScore>
{

	/** For trigger objectives, <code>true</code> if the objective is enabled for this player. */
	public boolean enabled;
	/** The name of the Player. */
	public String name;
	/** The Objective. */
	public MapObjective objective;
	/** The score. */
	public int score;

	@Override
	public MapScore fromNBT(TagCompound nbt)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TagCompound toNBT(TemplateCompound container)
	{
		// TODO Auto-generated method stub
		return null;
	}

}
