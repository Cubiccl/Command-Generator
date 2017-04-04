package fr.cubiccl.generator.gui.component.panel.gameobject;

import java.awt.GridBagConstraints;

import fr.cubiccl.generator.gameobject.PlayerRecipe;
import fr.cubiccl.generator.gameobject.baseobjects.RecipeType;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gui.component.button.CGCheckBox;
import fr.cubiccl.generator.gui.component.combobox.ObjectCombobox;
import fr.cubiccl.generator.gui.component.panel.CGPanel;

public class RecipePanel extends CGPanel
{
	private static final long serialVersionUID = 3420268951142556238L;

	private CGCheckBox checkboxUnlocked, checkboxDisplayed;
	private ObjectCombobox<RecipeType> comboboxRecipe;

	public RecipePanel()
	{
		this(null);
	}

	public RecipePanel(PlayerRecipe recipe)
	{
		super();

		GridBagConstraints gbc = this.createGridBagLayout();
		this.add((this.comboboxRecipe = new ObjectCombobox<RecipeType>(ObjectRegistry.recipes.list())).container, gbc);
		++gbc.gridy;
		this.add(this.checkboxUnlocked = new CGCheckBox("recipe.unlocked"), gbc);
		++gbc.gridy;
		this.add(this.checkboxDisplayed = new CGCheckBox("recipe.displayed"), gbc);

		if (recipe != null) this.setupFrom(recipe);
	}

	public boolean isDisplayed()
	{
		return this.checkboxDisplayed.isSelected();
	}

	public boolean isUnlocked()
	{
		return this.checkboxUnlocked.isSelected();
	}

	public RecipeType selectedRecipe()
	{
		return this.comboboxRecipe.getSelectedObject();
	}

	private void setupFrom(PlayerRecipe recipe)
	{
		this.comboboxRecipe.setSelected(recipe.recipe);
		this.checkboxUnlocked.setSelected(recipe.unlocked);
		this.checkboxDisplayed.setSelected(recipe.displayed);
	}

}
