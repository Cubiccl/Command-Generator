package fr.cubiccl.generator.gameobject.utils;

import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;

/** Utility interface. Objects implementing this can be saved or loaded to/from XML. */
public interface NBTSaveable<T>
{

	/** Loads this Object from the input NBT Tag.
	 * 
	 * @param nbt - The NBT Tag describing this Objects.
	 * @return This loaded Object. */
	public T fromNBT(TagCompound nbt);

	/** @return This Object as an NBT Tag to be generated. */
	public TagCompound toNBT(TemplateCompound container);

}
