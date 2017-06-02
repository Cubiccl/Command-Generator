package fr.cubiccl.generator.gameobject;

import org.jdom2.Element;

import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.tags.TagString;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.utils.Settings;
import fr.cubiccl.generator.utils.Settings.Version;

/** Represents an Object used in the game. */
public abstract class GameObject
{
	/** A Custom name for this Object, input by the user. */
	private String customName = "";
	/** The minimum Minecraft version to be using to load this Object. */
	private Version versionMin = Settings.version();

	/** Creates this Object's root XML element, with name and version as attributes.
	 * 
	 * @param id - The ID of the XML element.
	 * @return The XML root element. */
	protected Element createRoot(String id)
	{
		Element root = new Element(id);
		root.setAttribute("objectname", this.customName);
		root.setAttribute("version", this.versionMin.id);
		return root;
	}

	/** @return This Object's {@link GameObject#customName Custom name}. */
	public String customName()
	{
		return this.customName;
	}

	/** Finds the name of this Object in the input <code>tag</code>.
	 * 
	 * @param tag - The Tag describing this Object. */
	public void findName(TagCompound tag)
	{
		for (Tag t : tag.value())
			if (t.id().equals(Tags.OBJECT_NAME.id()))
			{
				this.setCustomName(((TagString) t).value);
				return;
			}
	}

	/** Finds the {@link GameObject#versionMin Version} and the {@link GameObject#customName Custom name} of this Object.
	 * 
	 * @param element - The XML element describing this Object. */
	protected void findProperties(Element element)
	{
		if (element.getAttribute("objectname") != null) this.customName = element.getAttributeValue("objectname");
		if (element.getAttribute("version") != null) this.versionMin = Version.get(element.getAttributeValue("version"));
	}

	/** Call this method whenever you change and attribute of this Object that depends on the current version.<br />
	 * If will set this Object's {@link GameObject#versionMin Version} to the currently selected version. */
	public void onChange()
	{
		if (this.versionMin.isBefore(Settings.version())) this.versionMin = Settings.version();
	}

	/** Setter for {@link GameObject#customName}.
	 * 
	 * @param name - The new Custom name. */
	public void setCustomName(String name)
	{
		this.customName = name;
	}

	/** @return How this Object should display in a generated Command. */
	public abstract String toCommand();

	@Override
	public abstract String toString();

	/** Converts this Object to a NBT Tag.
	 * 
	 * @param container - The template for the container Tag.
	 * @return The Compound container tag. */
	public abstract TagCompound toTag(TemplateCompound container);

	/** @return This Object as an XML element to be saved. */
	public abstract Element toXML();
}
