package fr.cubiccl.generator.gui.component.panel.recipe;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.SwingConstants;

import fr.cubi.cubigui.CTextArea;
import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gameobject.Recipe;
import fr.cubiccl.generator.gameobject.registries.ObjectSaver;
import fr.cubiccl.generator.gui.Dialogs;
import fr.cubiccl.generator.gui.component.CScrollPane;
import fr.cubiccl.generator.gui.component.button.CGButton;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.utils.ListListener;
import fr.cubiccl.generator.gui.component.panel.utils.PanelObjectList;
import fr.cubiccl.generator.utils.FileUtils;
import fr.cubiccl.generator.utils.Settings;

public class PanelRecipeSelection extends CGPanel implements ActionListener, ListListener<Recipe>
{
	private static final long serialVersionUID = 4213073942144876574L;

	private CGButton buttonGenerate, buttonLoad, buttonImport;
	public PanelObjectList<Recipe> list;

	public PanelRecipeSelection()
	{
		super();

		this.setBorder(BorderFactory.createLoweredSoftBevelBorder());
		CGLabel l = new CGLabel("recipe.list");
		l.setHorizontalAlignment(SwingConstants.CENTER);
		l.setFont(l.getFont().deriveFont(Font.BOLD, 30));

		GridBagConstraints gbc = this.createGridBagLayout();
		gbc.gridwidth = 3;
		gbc.anchor = GridBagConstraints.CENTER;
		this.add(l, gbc);
		++gbc.gridy;
		this.add(this.list = new PanelObjectList<Recipe>(null, (String) null, Recipe.class), gbc);
		++gbc.gridy;
		gbc.gridwidth = 1;
		this.add(this.buttonGenerate = new CGButton("command.generate"), gbc);
		++gbc.gridx;
		this.add(this.buttonLoad = new CGButton("recipe.load"), gbc);
		++gbc.gridx;
		this.add(this.buttonImport = new CGButton("command.import"), gbc);

		this.buttonGenerate.setFont(this.buttonGenerate.getFont().deriveFont(Font.BOLD, 20));
		this.buttonGenerate.addActionListener(this);
		this.buttonLoad.addActionListener(this);
		this.buttonImport.addActionListener(this);
		this.list.setValues(ObjectSaver.recipes.list());
		this.list.addListListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (this.list.selectedIndex() != -1 && e.getSource() == this.buttonGenerate) CommandGenerator.generateRecipe();
		else if (e.getSource() == this.buttonLoad)
		{
			CGPanel p = new CGPanel();
			p.setLayout(new BorderLayout());
			p.add(new CGLabel("recipe.load.description"), BorderLayout.NORTH);
			CTextArea area = new CTextArea("");
			area.setEditable(true);
			CScrollPane sc = new CScrollPane(area);
			sc.setPreferredSize(new Dimension(400, 200));
			p.add(sc, BorderLayout.CENTER);
			if (!Dialogs.showConfirmDialog(p)) return;
			CommandGenerator.createRecipe(area.getText());
		} else if (e.getSource() == this.buttonImport)
		{
			JFileChooser fileChooser = new JFileChooser(Settings.getSetting(Settings.LAST_FOLDER));
			int result = fileChooser.showOpenDialog(this);
			if (result == JFileChooser.APPROVE_OPTION)
			{
				File f = fileChooser.getSelectedFile();
				Settings.setSetting(Settings.LAST_FOLDER, f.getParentFile().getPath());
				CommandGenerator.createRecipe(FileUtils.readFile(f));
			}
		}
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
