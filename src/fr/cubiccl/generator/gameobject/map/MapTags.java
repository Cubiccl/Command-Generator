package fr.cubiccl.generator.gameobject.map;

import static fr.cubiccl.generator.gameobject.tags.Tag.UNAVAILABLE;
import fr.cubiccl.generator.gameobject.templatetags.TemplateBoolean;
import fr.cubiccl.generator.gameobject.templatetags.TemplateNumber;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound.DefaultCompound;
import fr.cubiccl.generator.gameobject.templatetags.TemplateList.DefaultList;
import fr.cubiccl.generator.gameobject.templatetags.TemplateString;

/** Contains unique and constant NBT Tags for Map management. */
public final class MapTags
{
	public static final TemplateBoolean AllowFriendlyFire = new TemplateBoolean("AllowFriendlyFire", UNAVAILABLE);
	public static final TemplateString CollisionRule = new TemplateString("CollisionRule", UNAVAILABLE);
	public static final TemplateString CriteriaName = new TemplateString("CriteriaName", UNAVAILABLE);
	public static final DefaultCompound data = new DefaultCompound("data", UNAVAILABLE);
	public static final DefaultCompound Data = new DefaultCompound("Data", UNAVAILABLE);
	public static final TemplateString DeathMessageVisibility = new TemplateString("DeathMessageVisibility", UNAVAILABLE);
	public static final TemplateString DisplayName = new TemplateString("DisplayName", UNAVAILABLE);
	public static final DefaultCompound DisplaySlots = new DefaultCompound("DisplaySlots", UNAVAILABLE);
	public static final TemplateString LevelName = new TemplateString("LevelName", UNAVAILABLE);
	public static final TemplateBoolean Locked = new TemplateBoolean("Locked", UNAVAILABLE);
	public static final TemplateString Name = new TemplateString("Name", UNAVAILABLE);
	public static final TemplateString NameTagVisibility = new TemplateString("NameTagVisibility", UNAVAILABLE);
	public static final TemplateString Objective = new TemplateString("Objective", UNAVAILABLE);
	public static final DefaultCompound Objectives = new DefaultCompound("Objectives", UNAVAILABLE);
	public static final DefaultList Players = new DefaultList("Players", UNAVAILABLE);
	public static final DefaultCompound PlayerScores = new DefaultCompound("PlayerScores", UNAVAILABLE);
	public static final TemplateString Prefix = new TemplateString("Prefix", UNAVAILABLE);
	public static final TemplateString RenderType = new TemplateString("RenderType", UNAVAILABLE);
	public static final TemplateNumber Score = new TemplateNumber("Score", UNAVAILABLE);
	public static final TemplateBoolean SeeFriendlyInvisibles = new TemplateBoolean("SeeFriendlyInvisibles", UNAVAILABLE);
	public static final TemplateString Suffix = new TemplateString("Suffix", UNAVAILABLE);
	public static final TemplateString TeamColor = new TemplateString("TeamColor", UNAVAILABLE);
	public static final DefaultCompound Teams = new DefaultCompound("Teams", UNAVAILABLE);

	public static void create()
	{}

	private MapTags()
	{}

}
