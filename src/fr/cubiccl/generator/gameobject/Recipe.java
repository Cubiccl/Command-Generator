package fr.cubiccl.generator.gameobject;

import java.awt.Component;

import fr.cubiccl.generator.gameobject.baseobjects.RecipeType;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gameobject.tags.Tag;
import fr.cubiccl.generator.gameobject.tags.TagBoolean;
import fr.cubiccl.generator.gameobject.tags.TagCompound;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound.DefaultCompound;
import fr.cubiccl.generator.gui.component.interfaces.IObjectList;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.RecipePanel;
import fr.cubiccl.generator.gui.component.panel.utils.ListProperties;
import fr.cubiccl.generator.utils.CommandGenerationException;

public class Recipe implements IObjectList<Recipe>
{

	public static Recipe fromNBT(TagCompound tag)
	{
		RecipeType r = ObjectRegistry.recipes.find(tag.id());
		boolean u = true, d = true;

		for (Tag t : tag.value())
		{
			if (t.id().equals(Tags.RECIPE_DISPLAYED.id())) d = ((TagBoolean) t).value();
			else if (t.id().equals(Tags.RECIPE_UNLOCKED.id())) u = ((TagBoolean) t).value();
		}

		return new Recipe(r, u, d);

	}

	public RecipeType recipe;
	public boolean unlocked, displayed;

	public Recipe()
	{
		this(ObjectRegistry.recipes.first(), true, true);
	}

	public Recipe(RecipeType recipe, boolean unlocked, boolean displayed)
	{
		this.recipe = recipe;
		this.unlocked = unlocked;
		this.displayed = displayed;
	}

	@Override
	public CGPanel createPanel(ListProperties properties)
	{
		return new RecipePanel(this);
	}

	@Override
	public Component getDisplayComponent()
	{
		return this.recipe.getDisplayComponent();
	}

	@Override
	public String getName(int index)
	{
		return this.recipe.item.getName(index);
	}

	public TagCompound toNBT()
	{
		return new DefaultCompound(this.recipe.id(), Tag.UNKNOWN).create(Tags.RECIPE_UNLOCKED.create(this.unlocked),
				Tags.RECIPE_DISPLAYED.create(this.displayed));
	}

	@Override
	public Recipe update(CGPanel panel) throws CommandGenerationException
	{
		RecipePanel p = (RecipePanel) panel;
		this.recipe = p.selectedRecipe();
		this.unlocked = p.isUnlocked();
		this.displayed = p.isDisplayed();
		return this;
	}

}
