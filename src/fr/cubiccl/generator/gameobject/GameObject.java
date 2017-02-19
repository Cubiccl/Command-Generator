package fr.cubiccl.generator.gameobject;

import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.tags.TagString;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;

public abstract class GameObject
{
	private String customName = "";

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

	public TagString nameTag()
	{
		return Tags.OBJECT_NAME.create(this.customName());
	}

	public String save()
	{
		return this.toTag(Tags.DEFAULT_COMPOUND, true).valueForCommand();
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
	public TagCompound toTag(TemplateCompound container)
	{
		return this.toTag(container, false);
	}

	/** @param container The container Tag.
	 * @return This Object, as an NBT Tag. */
	public abstract TagCompound toTag(TemplateCompound container, boolean includeName);

}
