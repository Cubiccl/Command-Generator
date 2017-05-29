package fr.cubiccl.generator.gui.component.panel.advancement;

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
import fr.cubiccl.generator.gameobject.advancements.Advancement;
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

public class PanelAdvancementSelection extends CGPanel implements ActionListener, ListListener<Advancement>
{
	private static final long serialVersionUID = 458787504307263140L;

	private CGButton buttonGenerate, buttonLoad, buttonImport;
	public PanelObjectList<Advancement> list;

	public PanelAdvancementSelection()
	{
		super();

		this.setBorder(BorderFactory.createLoweredSoftBevelBorder());
		CGLabel l = new CGLabel("advancement.list");
		l.setHorizontalAlignment(SwingConstants.CENTER);
		l.setFont(l.getFont().deriveFont(Font.BOLD, 30));

		GridBagConstraints gbc = this.createGridBagLayout();
		gbc.gridwidth = 3;
		gbc.anchor = GridBagConstraints.CENTER;
		this.add(l, gbc);
		++gbc.gridy;
		this.add(this.list = new PanelObjectList<Advancement>(null, (String) null, Advancement.class), gbc);
		++gbc.gridy;
		gbc.gridwidth = 1;
		this.add(this.buttonGenerate = new CGButton("command.generate"), gbc);
		++gbc.gridx;
		this.add(this.buttonLoad = new CGButton("advancement.load"), gbc);
		++gbc.gridx;
		this.add(this.buttonImport = new CGButton("command.import"), gbc);

		this.buttonGenerate.setFont(this.buttonGenerate.getFont().deriveFont(Font.BOLD, 20));
		this.buttonGenerate.addActionListener(this);
		this.buttonLoad.addActionListener(this);
		this.buttonImport.addActionListener(this);
		this.list.setValues(ObjectSaver.advancements.list());
		this.list.addListListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (this.list.selectedIndex() != -1 && e.getSource() == this.buttonGenerate) CommandGenerator.generateAdvancement();
		else if (e.getSource() == this.buttonLoad)
		{
			CGPanel p = new CGPanel();
			p.setLayout(new BorderLayout());
			p.add(new CGLabel("advancement.load.description"), BorderLayout.NORTH);
			CTextArea area = new CTextArea("");
			area.setEditable(true);
			CScrollPane sc = new CScrollPane(area);
			sc.setPreferredSize(new Dimension(400, 200));
			p.add(sc, BorderLayout.CENTER);
			if (!Dialogs.showConfirmDialog(p)) return;
			CommandGenerator.parseAdvancement(area.getText());
		} else if (e.getSource() == this.buttonImport)
		{
			JFileChooser fileChooser = new JFileChooser(Settings.getSetting(Settings.LAST_FOLDER));
			int result = fileChooser.showOpenDialog(this);
			if (result == JFileChooser.APPROVE_OPTION)
			{
				File f = fileChooser.getSelectedFile();
				Settings.setSetting(Settings.LAST_FOLDER, f.getParentFile().getPath());
				CommandGenerator.parseAdvancement(FileUtils.readFile(f));
			}
		}
	}

	@Override
	public void onAddition(int index, Advancement object)
	{
		ObjectSaver.advancements.addObject(object);
	}

	@Override
	public void onChange(int index, Advancement object)
	{}

	@Override
	public void onDeletion(int index, Advancement object)
	{
		ObjectSaver.advancements.delete(object);
	}

	public Advancement selectedAdvancement()
	{
		return ObjectSaver.advancements.find(this.list.getSelectedValue());
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
