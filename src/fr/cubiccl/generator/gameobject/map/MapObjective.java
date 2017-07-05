package fr.cubiccl.generator.gameobject.map;

import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gameobject.utils.NBTSaveable;

/** Represents a Scoreboard objective in a Map. */
public class MapObjective implements NBTSaveable<MapObjective>
{

	/** The criteria. */
	public String criteria;
	/** The ID. */
	public String id;
	/** The display name. */
	public String name;
	/** The render type. */
	public String renderType = "integer";

	@Override
	public MapObjective fromNBT(TagCompound nbt)
	{
		return this;
	}

	@Override
	public TagCompound toNBT(TemplateCompound container)
	{
		// TODO Auto-generated method stub
		return null;
	}

}
