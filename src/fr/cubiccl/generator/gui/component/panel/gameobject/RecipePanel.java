package fr.cubiccl.generator.gui.component.panel.gameobject;

import java.awt.GridBagConstraints;

import fr.cubiccl.generator.gameobject.Recipe;
import fr.cubiccl.generator.gameobject.baseobjects.Item;
import fr.cubiccl.generator.gui.component.button.CGCheckBox;
import fr.cubiccl.generator.gui.component.panel.CGPanel;

public class RecipePanel extends CGPanel
{
	private static final long serialVersionUID = 3420268951142556238L;

	private CGCheckBox checkboxUnlocked, checkboxDisplayed;
	private PanelItem panelItem;

	public RecipePanel()
	{
		this(null);
	}

	public RecipePanel(Recipe recipe)
	{
		super();
		
		GridBagConstraints gbc = this.createGridBagLayout();
		this.add(this.panelItem = new PanelItem("recipe.item"), gbc);
		++gbc.gridy;
		this.add(this.checkboxUnlocked = new CGCheckBox("recipe.unlocked"), gbc);
		++gbc.gridy;
		this.add(this.checkboxDisplayed = new CGCheckBox("recipe.displayed"), gbc);

		this.panelItem.setHasAmount(false);
		this.panelItem.setHasData(false);
		this.panelItem.setHasNBT(false);
		
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

	public Item selectedItem()
	{
		return this.panelItem.selectedItem();
	}

	private void setupFrom(Recipe recipe)
	{
		this.panelItem.setItem(recipe.item);
		this.checkboxUnlocked.setSelected(recipe.unlocked);
		this.checkboxDisplayed.setSelected(recipe.displayed);
	}

}
