package fr.cubiccl.generator.gameobject.templatetags;

import fr.cubiccl.generator.gameobject.tags.Tag;

public interface ITagCreationListener
{

	public void createTag(TemplateTag template, Tag value);

}
