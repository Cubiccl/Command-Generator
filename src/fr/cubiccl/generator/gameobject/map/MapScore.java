package fr.cubiccl.generator.gameobject.map;

import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gameobject.utils.NBTSaveable;

/** Represents a Player score in a Map. */
public class MapScore implements NBTSaveable<MapScore>
{

	/** For trigger objectives, <code>true</code> if the objective is enabled for this player. */
	public boolean locked;
	/** The name of the Player. */
	public String name;
	/** The Objective ID. */
	public String objective;
	/** The score. */
	public int score;

	@Override
	public MapScore fromNBT(TagCompound nbt)
	{
		this.locked = nbt.getTag(MapTags.Locked).value();
		this.name = nbt.getTag(MapTags.Name).value();
		this.objective = nbt.getTag(MapTags.Objective).value();
		this.score = nbt.getTag(MapTags.Score).valueInt();
		return this;
	}

	@Override
	public TagCompound toNBT(TemplateCompound container)
	{
		TagCompound tag = container.create();
		tag.addTag(MapTags.Locked.create(this.locked));
		tag.addTag(MapTags.Name.create(this.name));
		tag.addTag(MapTags.Objective.create(this.objective));
		tag.addTag(MapTags.Score.create(this.score));
		return tag;
	}

}
