package fr.cubiccl.generator.gameobject;

import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;

public class GeneratedCommand extends GameObject
{

	public static GeneratedCommand createFrom(TagCompound tag)
	{
		String command = "GLITCHED COMMAND...";
		if (tag.hasTag(Tags.COMMAND)) command = (String) tag.getTag(Tags.COMMAND).value();
		GeneratedCommand c = new GeneratedCommand(command);
		c.findName(tag);
		return c;
	}

	public final String command;

	public GeneratedCommand(String command)
	{
		this.command = command;
	}

	@Override
	public String toCommand()
	{
		return this.command;
	}

	@Override
	public String toString()
	{
		return this.command;
	}

	@Override
	public TagCompound toTag(TemplateCompound container, boolean includeName)
	{
		if (includeName) return container.create(Tags.COMMAND.create(this.command), this.nameTag());
		return container.create(Tags.COMMAND.create(this.command));
	}

}
