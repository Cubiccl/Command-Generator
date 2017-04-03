package fr.cubiccl.generator.gameobject;

import org.jdom2.Element;

import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.tags.TagString;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.utils.Settings;
import fr.cubiccl.generator.utils.Settings.Version;

public abstract class GameObject
{
	private String customName = "";
	private Version versionMin = Settings.version();

	/** Creates this Object root XML element, with name and version as attributes. */
	protected Element createRoot(String id)
	{
		Element root = new Element(id);
		root.setAttribute("objectname", this.customName);
		root.setAttribute("version", this.versionMin.codeName);
		return root;
	}

	public String customName()
	{
		return this.customName;
	}

	public void findName(TagCompound tag)
	{
		for (Tag t : tag.value())
			if (t.id().equals(Tags.OBJECT_NAME.id()))
			{
				this.setCustomName(((TagString) t).value);
				return;
			}
	}

	protected void findProperties(Element element)
	{
		if (element.getAttribute("objectname") != null) this.customName = element.getAttributeValue("objectname");
		if (element.getAttribute("version") != null) this.versionMin = Version.get(element.getAttributeValue("version"));
	}

	public void onChange()
	{
		if (this.versionMin.isBefore(Settings.version())) this.versionMin = Settings.version();
	}

	public void setCustomName(String name)
	{
		this.customName = name;
	}

	/** @return How this Object should display in a generated Command. */
	public abstract String toCommand();

	@Override
	public abstract String toString();

	/** @param container The container Tag.
	 * @return This Object, as an NBT Tag. */
	public abstract TagCompound toTag(TemplateCompound container);

	/** @return This Object as an XML object to be saved. */
	public abstract Element toXML();
}
