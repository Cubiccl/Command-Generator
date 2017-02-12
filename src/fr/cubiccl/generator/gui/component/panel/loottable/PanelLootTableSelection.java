package fr.cubiccl.generator.gui.component.panel.loottable;

import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gameobject.loottable.LootTable;
import fr.cubiccl.generator.gameobject.registries.ObjectSaver;
import fr.cubiccl.generator.gui.component.button.CGButton;
import fr.cubiccl.generator.gui.component.interfaces.IObjectList;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.utils.PanelObjectList;
import fr.cubiccl.generator.utils.StateManager.State;
import fr.cubiccl.generator.utils.Text;

public class PanelLootTableSelection extends CGPanel implements IObjectList, ActionListener
{
	private static final long serialVersionUID = 4213073942144876574L;

	private CGButton buttonGenerate, buttonLoad;
	private PanelObjectList list;

	public PanelLootTableSelection()
	{
		super();

		this.setBorder(BorderFactory.createLoweredSoftBevelBorder());
		CGLabel l = new CGLabel("loottable.list");
		l.setHorizontalAlignment(SwingConstants.CENTER);
		l.setFont(l.getFont().deriveFont(Font.BOLD, 30));

		GridBagConstraints gbc = this.createGridBagLayout();
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		this.add(l, gbc);
		++gbc.gridy;
		this.add(this.list = new PanelObjectList(this), gbc);
		++gbc.gridy;
		gbc.gridwidth = 1;
		this.add(this.buttonGenerate = new CGButton("command.generate"), gbc);
		++gbc.gridx;
		this.add(this.buttonLoad = new CGButton("loottable.load"), gbc);

		this.buttonGenerate.setFont(this.buttonGenerate.getFont().deriveFont(Font.BOLD, 20));
		this.buttonGenerate.addActionListener(this);
		this.buttonLoad.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (this.list.selectedIndex() != -1) if (e.getSource() == this.buttonGenerate) CommandGenerator.generateTable();
		else CommandGenerator.loadTable();
	}

	@Override
	public boolean addObject(CGPanel panel, int editIndex)
	{
		return false;
	}

	@Override
	public CGPanel createAddPanel(int editIndex)
	{
		LootTable t;
		if (editIndex == -1)
		{
			t = new LootTable();
			t.setCustomName(JOptionPane.showInputDialog(new Text("objects.name")));
			if (t.customName() == null) return null;
			ObjectSaver.lootTables.addObject(t);
		} else t = this.selectedLootTable();
		CommandGenerator.stateManager.clear();
		return new PanelLootTable(t);
	}

	@Override
	public Component getDisplayComponent(int index)
	{
		return null;
	}

	@Override
	public String[] getValues()
	{
		LootTable[] t = ObjectSaver.lootTables.list();
		String[] names = new String[t.length];
		for (int i = 0; i < names.length; ++i)
			names[i] = t[i].customName();
		return names;
	}

	@Override
	public void removeObject(int index)
	{
		@SuppressWarnings("unchecked")
		State<PanelLootTable> s = CommandGenerator.stateManager.getState();
		if (s != null && s.panel.lootTable.customName().equals(this.selectedLootTable().customName())) CommandGenerator.stateManager.clear();
		ObjectSaver.lootTables.removeObject(index);
	}

	public LootTable selectedLootTable()
	{
		return ObjectSaver.lootTables.find(this.list.getSelectedValue());
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
