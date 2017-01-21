package fr.cubiccl.generator.gameobject;

import fr.cubiccl.generator.gameobject.tags.TagBigNumber;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.tags.TagList;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gameobject.templatetags.TemplateList;

public class Coordinates extends GameObject
{

	public static Coordinates createFrom(TagList tag)
	{
		float x = 0, y = 0, z = 0;
		try
		{
			x = (float) (double) ((TagBigNumber) tag.getTag(0)).value();
			y = (float) (double) ((TagBigNumber) tag.getTag(1)).value();
			z = (float) (double) ((TagBigNumber) tag.getTag(2)).value();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return new Coordinates(x, y, z);
	}

	public final float x, y, z;
	public final boolean xRelative, yRelative, zRelative;

	public Coordinates(float x, float y, float z)
	{
		this(x, y, z, false, false, false);
	}

	public Coordinates(float x, float y, float z, boolean xRelative, boolean yRelative, boolean zRelative)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.xRelative = xRelative;
		this.yRelative = yRelative;
		this.zRelative = zRelative;
	}

	@Override
	public String toCommand()
	{
		String command = "";
		if (this.xRelative) command += "~";
		command += this.x + " ";

		if (this.yRelative) command += "~";
		command += this.y + " ";

		if (this.zRelative) command += "~";
		command += this.z;

		return command;
	}

	@Override
	public String toString()
	{
		String text = "X=";
		if (this.xRelative) text += "~";
		text += this.x + ", Y=";

		if (this.yRelative) text += "~";
		text += this.y + " ,Z=";

		if (this.zRelative) text += "~";
		text += this.z;

		return text;
	}

	@Override
	public TagCompound toTag(TemplateCompound container)
	{
		return new TagCompound(container, new TagBigNumber(Tags.COORD_X, this.x), new TagBigNumber(Tags.COORD_Y, this.y),
				new TagBigNumber(Tags.COORD_Z, this.z));
	}

	public TagList toTagList(TemplateList container)
	{
		return new TagList(container, new TagBigNumber(Tags.DEFAULT_FLOAT, this.x), new TagBigNumber(Tags.DEFAULT_FLOAT, this.y), new TagBigNumber(
				Tags.DEFAULT_FLOAT, this.z));
	}

}
