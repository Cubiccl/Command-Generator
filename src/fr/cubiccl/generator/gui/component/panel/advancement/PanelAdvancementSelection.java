package fr.cubiccl.generator.gui.component.panel.advancement;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.SwingConstants;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gameobject.advancements.Advancement;
import fr.cubiccl.generator.gameobject.registries.ObjectSaver;
import fr.cubiccl.generator.gui.component.button.CGButton;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.utils.ListListener;
import fr.cubiccl.generator.gui.component.panel.utils.PanelObjectList;

public class PanelAdvancementSelection extends CGPanel implements ActionListener, ListListener<Advancement>
{
	private static final long serialVersionUID = 458787504307263140L;

	private CGButton buttonGenerate, buttonLoad;
	public PanelObjectList<Advancement> list;

	public PanelAdvancementSelection()
	{
		super();

		this.setBorder(BorderFactory.createLoweredSoftBevelBorder());
		CGLabel l = new CGLabel("advancement.list");
		l.setHorizontalAlignment(SwingConstants.CENTER);
		l.setFont(l.getFont().deriveFont(Font.BOLD, 30));

		GridBagConstraints gbc = this.createGridBagLayout();
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		this.add(l, gbc);
		++gbc.gridy;
		this.add(this.list = new PanelObjectList<Advancement>(null, (String) null, Advancement.class), gbc);
		++gbc.gridy;
		gbc.gridwidth = 1;
		this.add(this.buttonGenerate = new CGButton("command.generate"), gbc);
		++gbc.gridx;
		this.add(this.buttonLoad = new CGButton("advancement.load"), gbc);

		this.buttonGenerate.setFont(this.buttonGenerate.getFont().deriveFont(Font.BOLD, 20));
		this.buttonGenerate.addActionListener(this);
		this.buttonLoad.addActionListener(this);
		this.list.setValues(ObjectSaver.advancements.list());
		this.list.addListListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (this.list.selectedIndex() != -1) if (e.getSource() == this.buttonGenerate) CommandGenerator.generateAdvancement();
		if (e.getSource() == this.buttonLoad) CommandGenerator.loadAdvancement();
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
