package fr.cubiccl.generator.gameobject.templatetags;

import fr.cubiccl.generator.gameobject.tags.Tag;

public interface ITagCreationListener
{

	/** Called when the NBT Tag is successfully created. Should store the created Tag.
	 * 
	 * @param template - The NBT Tag template.
	 * @param value - The created NBT Tag. */
	public void tagCreated(TemplateTag template, Tag value);

}
