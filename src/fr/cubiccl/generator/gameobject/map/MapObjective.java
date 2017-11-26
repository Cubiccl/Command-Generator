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
		this.criteria = nbt.getTag(MapTags.CriteriaName).value();
		this.id = nbt.getTag(MapTags.Name).value();
		this.name = nbt.getTag(MapTags.DisplayName).value();
		this.renderType = nbt.getTag(MapTags.RenderType).value();
		return this;
	}

	@Override
	public TagCompound toNBT(TemplateCompound container)
	{
		TagCompound tag = container.create();
		tag.addTag(MapTags.CriteriaName.create(this.criteria));
		tag.addTag(MapTags.Name.create(this.id));
		tag.addTag(MapTags.DisplayName.create(this.name));
		tag.addTag(MapTags.RenderType.create(this.renderType));
		return tag;
	}

}
