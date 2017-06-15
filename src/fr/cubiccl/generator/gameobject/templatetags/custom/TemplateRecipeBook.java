package fr.cubiccl.generator.gameobject.templatetags.custom;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ButtonGroup;

import fr.cubiccl.generator.gameobject.PlayerRecipe;
import fr.cubiccl.generator.gameobject.baseobjects.BaseObject;
import fr.cubiccl.generator.gameobject.baseobjects.RecipeType;
import fr.cubiccl.generator.gameobject.registries.ObjectRegistry;
import fr.cubiccl.generator.gameobject.tags.*;
import fr.cubiccl.generator.gameobject.templatetags.Tags;
import fr.cubiccl.generator.gameobject.templatetags.TemplateCompound;
import fr.cubiccl.generator.gui.component.button.CGCheckBox;
import fr.cubiccl.generator.gui.component.button.CGRadioButton;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.utils.PanelObjectList;

public class TemplateRecipeBook extends TemplateCompound
{

	private static class PanelRecipeBook extends CGPanel
	{
		private static final long serialVersionUID = 1742114578826844700L;

		public CGRadioButton buttonFilterYes, buttonFilterNo, buttonFilterUnspecified;
		public CGRadioButton buttonGuiYes, buttonGuiNo, buttonGuiUnspecified;
		public CGCheckBox checkboxDisplay;
		public PanelObjectList<PlayerRecipe> panelRecipes;
		public PanelObjectList<RecipeType> panelRecipesToDisplay;

		public PanelRecipeBook()
		{
			CGPanel panelGui = new CGPanel("recipebook.gui");
			GridBagConstraints gbc = panelGui.createGridBagLayout();
			gbc.gridwidth = 3;
			panelGui.add(new CGLabel("recipebook.gui.description"), gbc);
			++gbc.gridy;
			gbc.gridwidth = 1;
			panelGui.add(this.buttonGuiYes = new CGRadioButton("general.yes"), gbc);
			++gbc.gridx;
			panelGui.add(this.buttonGuiNo = new CGRadioButton("general.no"), gbc);
			++gbc.gridx;
			panelGui.add(this.buttonGuiUnspecified = new CGRadioButton("general.unspecified"), gbc);

			CGPanel panelFilter = new CGPanel("recipebook.filter");
			gbc = panelFilter.createGridBagLayout();
			gbc.gridwidth = 3;
			panelFilter.add(new CGLabel("recipebook.filter.description"), gbc);
			++gbc.gridy;
			gbc.gridwidth = 1;
			panelFilter.add(this.buttonFilterYes = new CGRadioButton("general.yes"), gbc);
			++gbc.gridx;
			panelFilter.add(this.buttonFilterNo = new CGRadioButton("general.no"), gbc);
			++gbc.gridx;
			panelFilter.add(this.buttonFilterUnspecified = new CGRadioButton("general.unspecified"), gbc);

			gbc = this.createGridBagLayout();
			this.add(this.panelRecipes = new PanelObjectList<PlayerRecipe>(null, (String) null, PlayerRecipe.class), gbc);
			++gbc.gridy;
			this.add(panelGui, gbc);
			++gbc.gridy;
			this.add(panelFilter, gbc);
			++gbc.gridy;
			this.add(this.checkboxDisplay = new CGCheckBox("recipebook.display.check"), gbc);
			++gbc.gridy;
			this.add(this.panelRecipesToDisplay = new PanelObjectList<RecipeType>("recipebook.display", (String) null, RecipeType.class), gbc);

			this.buttonGuiUnspecified.setSelected(true);
			this.buttonFilterUnspecified.setSelected(true);
			this.checkboxDisplay.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					panelRecipesToDisplay.setVisible(checkboxDisplay.isSelected());
				}
			});
			this.checkboxDisplay.setSelected(false);

			ButtonGroup group = new ButtonGroup();
			group.add(this.buttonGuiYes);
			group.add(this.buttonGuiNo);
			group.add(this.buttonGuiUnspecified);
			group = new ButtonGroup();
			group.add(this.buttonFilterYes);
			group.add(this.buttonFilterNo);
			group.add(this.buttonFilterUnspecified);
		}

	}

	@Override
	protected CGPanel createPanel(BaseObject<?> object, Tag previousValue)
	{
		PanelRecipeBook panel = new PanelRecipeBook();
		if (previousValue != null)
		{
			Tag[] tags = ((TagCompound) previousValue).value();
			for (Tag tag : tags)
				if (tag instanceof TagCompound) panel.panelRecipes.add(PlayerRecipe.createFrom((TagCompound) tag));
				else if (tag.id().equals(Tags.RECIPEBOOK_ISGUIOPEN.id()))
				{
					if (((TagNumber) tag).value() == 0) panel.buttonGuiNo.setSelected(true);
					else if (((TagNumber) tag).value() == 1) panel.buttonGuiYes.setSelected(true);
				} else if (tag.id().equals(Tags.RECIPEBOOK_ISFILTERING.id()))
				{
					if (((TagNumber) tag).value() == 0) panel.buttonFilterNo.setSelected(true);
					else if (((TagNumber) tag).value() == 1) panel.buttonFilterYes.setSelected(true);
				} else if (tag.id().equals(Tags.RECIPEBOOK_TOBEDISPLAYED.id()))
				{
					panel.checkboxDisplay.setSelected(true);
					panel.panelRecipesToDisplay.setVisible(true);
					for (Tag t : ((TagList) tag).value())
						panel.panelRecipesToDisplay.add(ObjectRegistry.recipes.find(((TagString) t).value()));
				}
		}
		panel.setName(this.title());
		return panel;
	}

	@Override
	public TagCompound generateTag(BaseObject<?> object, CGPanel panel)
	{
		PanelRecipeBook p = (PanelRecipeBook) panel;
		PlayerRecipe[] recipes = p.panelRecipes.values();
		ArrayList<Tag> tags = new ArrayList<Tag>();
		for (int i = 0; i < recipes.length; ++i)
			tags.add(recipes[i].toTag());
		if (!p.buttonFilterUnspecified.isSelected()) tags.add(Tags.RECIPEBOOK_ISFILTERING.create(p.buttonFilterYes.isSelected() ? 1 : 0));
		if (!p.buttonGuiUnspecified.isSelected()) tags.add(Tags.RECIPEBOOK_ISGUIOPEN.create(p.buttonGuiYes.isSelected() ? 1 : 0));
		if (p.checkboxDisplay.isSelected())
		{
			RecipeType[] r = p.panelRecipesToDisplay.values();
			TagString[] values = new TagString[r.length];
			for (int i = 0; i < values.length; ++i)
				values[i] = Tags.DEFAULT_STRING.create(r[i].id());
			tags.add(Tags.RECIPEBOOK_TOBEDISPLAYED.create(values));
		}
		return this.create(tags.toArray(new Tag[tags.size()]));
	}
}
