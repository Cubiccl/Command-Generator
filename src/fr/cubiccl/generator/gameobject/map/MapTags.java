package fr.cubiccl.generator.gameobject.map;

import static fr.cubiccl.generator.gameobject.tags.Tag.UNAVAILABLE;
import fr.cubiccl.generator.gameobject.templatetags.TemplateString;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound.DefaultCompound;

/** Contains unique and constant NBT Tags for Map management. */
public final class MapTags
{
	public static final DefaultCompound data = new DefaultCompound("data", UNAVAILABLE);
	public static final DefaultCompound Data = new DefaultCompound("Data", UNAVAILABLE);
	public static final DefaultCompound DisplaySlots = new DefaultCompound("DisplaySlots", UNAVAILABLE);
	public static final TemplateString LevelName = new TemplateString("LevelName", UNAVAILABLE);
	public static final DefaultCompound Objectives = new DefaultCompound("Objectives", UNAVAILABLE);
	public static final DefaultCompound PlayerScores = new DefaultCompound("PlayerScores", UNAVAILABLE);
	public static final DefaultCompound Teams = new DefaultCompound("Teams", UNAVAILABLE);

	public static void create()
	{}

	private MapTags()
	{}

}
