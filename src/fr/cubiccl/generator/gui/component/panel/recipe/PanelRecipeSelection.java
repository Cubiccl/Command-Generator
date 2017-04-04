package fr.cubiccl.generator.gui.component.panel.recipe;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.SwingConstants;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gameobject.Recipe;
import fr.cubiccl.generator.gameobject.registries.ObjectSaver;
import fr.cubiccl.generator.gui.component.button.CGButton;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.utils.ListListener;
import fr.cubiccl.generator.gui.component.panel.utils.PanelObjectList;

public class PanelRecipeSelection extends CGPanel implements ActionListener, ListListener<Recipe>
{
	private static final long serialVersionUID = 4213073942144876574L;

	private CGButton buttonGenerate, buttonLoad;
	public PanelObjectList<Recipe> list;

	public PanelRecipeSelection()
	{
		super();

		this.setBorder(BorderFactory.createLoweredSoftBevelBorder());
		CGLabel l = new CGLabel("recipe.list");
		l.setHorizontalAlignment(SwingConstants.CENTER);
		l.setFont(l.getFont().deriveFont(Font.BOLD, 30));

		GridBagConstraints gbc = this.createGridBagLayout();
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		this.add(l, gbc);
		++gbc.gridy;
		this.add(this.list = new PanelObjectList<Recipe>(null, (String) null, Recipe.class), gbc);
		++gbc.gridy;
		gbc.gridwidth = 1;
		this.add(this.buttonGenerate = new CGButton("command.generate"), gbc);
		++gbc.gridx;
		this.add(this.buttonLoad = new CGButton("recipe.load"), gbc);

		this.buttonGenerate.setFont(this.buttonGenerate.getFont().deriveFont(Font.BOLD, 20));
		this.buttonGenerate.addActionListener(this);
		this.buttonLoad.addActionListener(this);
		this.list.setValues(ObjectSaver.recipes.list());
		this.list.addListListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (this.list.selectedIndex() != -1) if (e.getSource() == this.buttonGenerate) CommandGenerator.generateRecipe();
		if (e.getSource() == this.buttonLoad) CommandGenerator.loadRecipe();
	}

	@Override
	public void onAddition(int index, Recipe object)
	{
		ObjectSaver.recipes.addObject(object);
	}

	@Override
	public void onChange(int index, Recipe object)
	{}

	@Override
	public void onDeletion(int index, Recipe object)
	{
		ObjectSaver.recipes.delete(object);
	}

	public Recipe selectedRecipe()
	{
		return ObjectSaver.recipes.find(this.list.getSelectedValue());
	}

	@Override
	public void setEnabled(boolean enabled)
	{
		super.setEnabled(enabled);
		this.buttonGenerate.setEnabled(enabled);
		this.buttonLoad.setEnabled(enabled);
		this.list.setEnabled(enabled);
	}

}
