package fr.cubiccl.generator.gui.component.panel.recipe;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.SwingConstants;

import fr.cubiccl.generator.gameobject.ItemStack;
import fr.cubiccl.generator.gameobject.Recipe;
import fr.cubiccl.generator.gameobject.baseobjects.Container;
import fr.cubiccl.generator.gui.component.button.CGCheckBox;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.label.HelpLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.tag.PanelContainer;
import fr.cubiccl.generator.gui.component.panel.tag.PanelContainer.ItemChangeListener;
import fr.cubiccl.generator.gui.component.textfield.CGEntry;
import fr.cubiccl.generator.utils.Text;

public class PanelRecipe extends CGPanel implements ItemChangeListener
{
	private static final long serialVersionUID = 6226736127162381216L;

	private CGCheckBox buttonShapeless;
	private CGEntry entryGroup;
	private PanelContainer panelContainer;
	public final Recipe recipe;

	public PanelRecipe(Recipe recipe)
	{
		this.recipe = recipe;
		CGLabel l = new CGLabel(new Text(this.recipe.customName(), false));
		l.setFont(l.getFont().deriveFont(Font.BOLD, 30));
		l.setHorizontalAlignment(SwingConstants.CENTER);

		GridBagConstraints gbc = this.createGridBagLayout();
		++gbc.gridwidth;
		this.add(l, gbc);
		++gbc.gridy;
		--gbc.gridwidth;
		this.add((this.panelContainer = new PanelContainer(Container.CRAFTING)).container, gbc);
		++gbc.gridy;
		this.add(this.buttonShapeless = new CGCheckBox("recipe.shapeless"), gbc);
		++gbc.gridx;
		this.add(new HelpLabel("recipe.shapeless.help"), gbc);
		--gbc.gridx;
		++gbc.gridy;
		this.add((this.entryGroup = new CGEntry("recipe.group")).container, gbc);
		++gbc.gridx;
		this.add(new HelpLabel("recipe.group.help"), gbc);

		this.buttonShapeless.setSelected(this.recipe.type == Recipe.SHAPELESS);
		this.buttonShapeless.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				recipe.type = buttonShapeless.isSelected() ? Recipe.SHAPELESS : Recipe.SHAPED;
			}
		});

		this.entryGroup.addStringIdFilter();
		this.entryGroup.setText(this.recipe.group == null ? "" : this.recipe.group.replaceAll(" ", ""));
		this.entryGroup.addKeyListener(new KeyListener()
		{

			@Override
			public void keyPressed(KeyEvent e)
			{}

			@Override
			public void keyReleased(KeyEvent e)
			{
				if (entryGroup.getText().equals("")) recipe.group = null;
				else recipe.group = entryGroup.getText();
			}

			@Override
			public void keyTyped(KeyEvent e)
			{}

		});

		this.panelContainer.addItemChangeListener(this);
		this.panelContainer.setupFrom(this.recipe.getRecipe());
		this.panelContainer.hasNBT = false;
		this.panelContainer.hasDurability = false;
	}

	@Override
	public void onItemChange(int i, ItemStack item)
	{
		if (i != 9) item.amount = 1;
		try
		{
			this.recipe.setItemAt(i, item.clone());
		} catch (CloneNotSupportedException e)
		{
			e.printStackTrace();
		}
	}

}
