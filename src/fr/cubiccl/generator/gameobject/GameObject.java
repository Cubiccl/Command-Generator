package fr.cubiccl.generator.gameobject;

import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;

public abstract class GameObject
{

	/** @return How this Object should display in a generated Command. */
	public abstract String toCommand();

	@Override
	public abstract String toString();

	/** @param container The container Tag.
	 * @return This Object, as an NBT Tag. */
	public abstract TagCompound toTag(TemplateCompound container);

}
