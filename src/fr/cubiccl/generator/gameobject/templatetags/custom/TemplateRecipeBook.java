package fr.cubiccl.generator.gameobject.templatetags.custom;

import fr.cubiccl.generator.gameobject.Recipe;
import fr.cubiccl.generator.gameobject.baseobjects.BaseObject;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.utils.PanelObjectList;

public class TemplateRecipeBook extends TemplateCompound
{

	public TemplateRecipeBook(String id, byte applicationType, String[] applicable)
	{
		super(id, applicationType, applicable);
	}

	@Override
	protected CGPanel createPanel(BaseObject object, Tag previousValue)
	{
		PanelObjectList<Recipe> panel = new PanelObjectList<Recipe>(null, (String) null, Recipe.class);
		if (previousValue != null)
		{
			Tag[] tags = ((TagCompound) previousValue).value();
			for (Tag tag : tags)
				if (tag instanceof TagCompound) panel.add(Recipe.fromNBT((TagCompound) tag));
		}
		panel.setName(this.title());
		return panel;
	}

	@Override
	protected TagCompound generateTag(BaseObject object, CGPanel panel)
	{
		@SuppressWarnings("unchecked")
		Recipe[] recipes = ((PanelObjectList<Recipe>) panel).values();
		Tag[] tags = new Tag[recipes.length];
		for (int i = 0; i < tags.length; ++i)
			tags[i] = recipes[i].toNBT();
		return this.create(tags);
	}

}
