package fr.cubiccl.generator.gameobject.templatetags.custom;

import fr.cubiccl.generator.gameobject.baseobjects.BaseObject;
import fr.cubiccl.generator.gameobject.baseobjects.RecipeType;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagList;
import fr.cubiccl.generator.gameobject.tags.TagString;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateList;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.utils.PanelObjectList;

public class TemplateRecipeList extends TemplateList
{
	public TemplateRecipeList(String id, byte applicationType, String[] applicable)
	{
		super(id, applicationType, applicable);
	}

	@Override
	protected CGPanel createPanel(BaseObject object, Tag previousValue)
	{
		PanelObjectList<RecipeType> p = new PanelObjectList<RecipeType>(null, (String) null, RecipeType.class);
		if (previousValue != null)
		{
			RecipeType[] recipes = new RecipeType[0];
			TagList t = ((TagList) previousValue);
			recipes = new RecipeType[t.size()];
			for (int i = 0; i < recipes.length; ++i)
				recipes[i] = ObjectRegistry.recipes.find((String) t.getTag(i).value());
			p.setValues(recipes);
		}
		p.setName(this.title());
		return p;
	}

	@Override
	public TagList generateTag(BaseObject object, CGPanel panel)
	{
		@SuppressWarnings("unchecked")
		RecipeType[] values = ((PanelObjectList<RecipeType>) panel).values();
		TagString[] tags = new TagString[values.length];
		for (int i = 0; i < tags.length; ++i)
			tags[i] = Tags.DEFAULT_STRING.create(values[i].id());
		return this.create(tags);
	}

}
