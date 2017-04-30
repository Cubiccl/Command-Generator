package fr.cubiccl.generator.gameobject.templatetags;

import static fr.cubiccl.generator.gameobject.tags.Tag.UNAVAILABLE;
import fr.cubiccl.generator.gameobject.tags.Tag;

public class TagsMain
{

	public static final TemplateNumber VALUE_MAX = new TemplateNumber("max", UNAVAILABLE, Tag.INT);
	public static final TemplateNumber VALUE_MAX_DOUBLE = new TemplateNumber("max", UNAVAILABLE, Tag.DOUBLE);
	public static final TemplateNumber VALUE_MAX_FLOAT = new TemplateNumber("max", UNAVAILABLE, Tag.FLOAT);
	public static final TemplateNumber VALUE_MIN = new TemplateNumber("min", UNAVAILABLE, Tag.INT);
	public static final TemplateNumber VALUE_MIN_DOUBLE = new TemplateNumber("min", UNAVAILABLE, Tag.DOUBLE);
	public static final TemplateNumber VALUE_MIN_FLOAT = new TemplateNumber("min", UNAVAILABLE, Tag.FLOAT);

	public static void create()
	{}

}
