package fr.cubiccl.generator.gameobject.map;

import static fr.cubiccl.generator.gameobject.map.MapTags.*;

import java.util.ArrayList;

import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.tags.TagList;
import fr.cubiccl.generator.gameobject.tags.TagString;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gameobject.utils.NBTSaveable;

/** Represents a Team in a Map. */
public class MapTeam implements NBTSaveable<MapTeam>
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

	@Override
	public MapTeam fromNBT(TagCompound nbt)
	{
		this.collisionRule = nbt.getTag(CollisionRule).value();
		this.color = nbt.getTag(TeamColor).value();
		this.deathMessageVisibility = nbt.getTag(DeathMessageVisibility).value();
		this.friendlyFire = nbt.getTag(AllowFriendlyFire).value();
		this.friendlyInvisibles = nbt.getTag(SeeFriendlyInvisibles).value();
		this.id = nbt.getTag(Name).value();
		this.name = nbt.getTag(DisplayName).value();
		this.nameTagVisibility = nbt.getTag(NameTagVisibility).value();
		this.prefix = nbt.getTag(Prefix).value();
		this.suffix = nbt.getTag(Suffix).value();

		this.players.clear();
		for (Tag player : nbt.getTag(Players).value())
			this.players.add(((TagString) player).value());

		return this;
	}

	@Override
	public TagCompound toNBT(TemplateCompound container)
	{
		TagCompound tag = container.create();
		tag.addTag(CollisionRule.create(this.collisionRule));
		tag.addTag(TeamColor.create(this.color));
		tag.addTag(DeathMessageVisibility.create(this.deathMessageVisibility));
		tag.addTag(AllowFriendlyFire.create(this.friendlyFire));
		tag.addTag(SeeFriendlyInvisibles.create(this.friendlyInvisibles));
		tag.addTag(Name.create(this.id));
		tag.addTag(DisplayName.create(this.name));
		tag.addTag(NameTagVisibility.create(this.nameTagVisibility));
		tag.addTag(Prefix.create(this.prefix));
		tag.addTag(Suffix.create(this.suffix));

		TagList list = Players.create();
		for (String player : this.players)
			list.addTag(Tags.DEFAULT_STRING.create(player));
		tag.addTag(list);

		return tag;
	}

}
