package fr.cubiccl.generator.gameobject;

import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.tags.TagNumber;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;

public class Coordinates
{

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

	public TagCompound toTag(TemplateCompound container)
	{
		return new TagCompound(container, new TagNumber(Tags.COORD_X, (int) this.x), new TagNumber(Tags.COORD_Y, (int) this.y), new TagNumber(Tags.COORD_Z,
				(int) this.z));
	}

}
