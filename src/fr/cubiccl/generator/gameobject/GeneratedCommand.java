package fr.cubiccl.generator.gameobject;

import org.jdom2.Element;

import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;

/** Represents a generated Command. */
public class GeneratedCommand extends GameObject<GeneratedCommand>
{

	/** The output. */
	public String command;

	public GeneratedCommand()
	{
		this("INVALID COMMAND");
	}

	public GeneratedCommand(String command)
	{
		this.command = command;
	}

	@Override
	public GeneratedCommand fromNBT(TagCompound nbt)
	{
		if (nbt.hasTag(Tags.COMMAND)) this.command = nbt.getTag(Tags.COMMAND).value();
		this.findName(nbt);
		return this;
	}

	@Override
	public GeneratedCommand fromXML(Element xml)
	{
		this.command = xml.getChildText("value");
		this.findProperties(xml);
		return this;
	}

	@Override
	public String toCommand()
	{
		return this.command;
	}

	@Override
	public TagCompound toNBT(TemplateCompound container)
	{
		return container.create(Tags.COMMAND.create(this.command));
	}

	@Override
	public String toString()
	{
		return this.command;
	}

	@Override
	public Element toXML()
	{
		return this.createRoot("command").addContent(new Element("value").setText(this.command));
	}

}
