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
		return new TagString(Tags.OBJECT_NAME, this.customName());
	}

	public String save()
	{
		TagCompound t = this.toTag(Tags.DEFAULT_COMPOUND, true);
		Tag[] tags = new Tag[t.size() + 1];
		for (int i = 0; i < tags.length - 1; ++i)
			tags[1 + i] = t.getTag(i);
		tags[0] = new TagString(Tags.OBJECT_NAME, this.customName());
		return new TagCompound(Tags.DEFAULT_COMPOUND, tags).valueForCommand();
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
		return this.toTag(container, true);
	}

	/** @param container The container Tag.
	 * @return This Object, as an NBT Tag. */
	public abstract TagCompound toTag(TemplateCompound container, boolean includeName);

}
