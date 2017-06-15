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
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelPlayerRecipe;
import fr.cubiccl.generator.gui.component.panel.utils.ListProperties;
import fr.cubiccl.generator.utils.CommandGenerationException;

/** A Recipe for the Player Recipe Book. */
public class PlayerRecipe implements IObjectList<PlayerRecipe>
{

	/** Creates a Player Recipe from the input NBT Tag.
	 * 
	 * @param tag - The NBT Tag describing the Player Recipe.
	 * @return The created Player Recipe. */
	public static PlayerRecipe createFrom(TagCompound tag)
	{
		RecipeType r = ObjectRegistry.recipes.find(tag.id());
		boolean u = true, d = true;

		for (Tag t : tag.value())
		{
			if (t.id().equals(Tags.RECIPE_DISPLAYED.id())) d = ((TagBoolean) t).value();
			else if (t.id().equals(Tags.RECIPE_UNLOCKED.id())) u = ((TagBoolean) t).value();
		}

		return new PlayerRecipe(r, u, d);

	}

	/** <code>true</code> if this Recipe has already been displayed to the Player. */
	public boolean displayed;
	/** The {@link RecipeType Recipe} this represents. */
	public RecipeType recipe;
	/** <code>true</code> if this Recipe has been unlocked. */
	public boolean unlocked;

	public PlayerRecipe()
	{
		this(ObjectRegistry.recipes.first(), true, true);
	}

	public PlayerRecipe(RecipeType recipe, boolean unlocked, boolean displayed)
	{
		this.recipe = recipe;
		this.unlocked = unlocked;
		this.displayed = displayed;
	}

	@Override
	public CGPanel createPanel(ListProperties properties)
	{
		return new PanelPlayerRecipe(this);
	}

	@Override
	public Component getDisplayComponent()
	{
		return this.recipe.getDisplayComponent();
	}

	@Override
	public String getName(int index)
	{
		return this.recipe.item().getName(index);
	}

	/** Converts this Player Recipe to a NBT Tag.
	 * 
	 * @param container - The template for the container Tag.
	 * @return The Compound container tag. */
	public TagCompound toTag()
	{
		return new DefaultCompound(this.recipe.id(), Tag.UNKNOWN).create(Tags.RECIPE_UNLOCKED.create(this.unlocked),
				Tags.RECIPE_DISPLAYED.create(this.displayed));
	}

	@Override
	public PlayerRecipe update(CGPanel panel) throws CommandGenerationException
	{
		PanelPlayerRecipe p = (PanelPlayerRecipe) panel;
		this.recipe = p.selectedRecipe();
		this.unlocked = p.isUnlocked();
		this.displayed = p.isDisplayed();
		return this;
	}

}
