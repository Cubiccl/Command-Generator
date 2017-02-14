package fr.cubiccl.generator.gui.component.panel.utils;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gui.component.CGList;
import fr.cubiccl.generator.gui.component.button.CGButton;
import fr.cubiccl.generator.gui.component.interfaces.IObjectList;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.utils.IStateListener;
import fr.cubiccl.generator.utils.Replacement;
import fr.cubiccl.generator.utils.Text;

public class PanelObjectList extends CGPanel implements ActionListener, ListSelectionListener, IStateListener<CGPanel>
{
	private static final long serialVersionUID = 2923920419688577940L;

	protected CGButton buttonAdd, buttonEdit, buttonRemove;
	private Component componentDisplay;
	protected int editing = -1;
	protected CGList list;
	protected IObjectList objectList;

	public PanelObjectList(IObjectList objectList)
	{
		this(null, objectList);
	}

	public PanelObjectList(String titleID, IObjectList objectList)
	{
		super(titleID);
		this.objectList = objectList;

		GridBagConstraints gbc = this.createGridBagLayout();
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.gridheight = 3;
		this.add((this.list = new CGList()).scrollPane, gbc);

		++gbc.gridx;
		gbc.gridheight = 1;
		this.add(this.buttonAdd = new CGButton("general.add"), gbc);
		++gbc.gridy;
		this.add(this.buttonEdit = new CGButton("general.edit"), gbc);
		++gbc.gridy;
		this.add(this.buttonRemove = new CGButton("general.remove"), gbc);

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridheight = 3;

		this.buttonEdit.setEnabled(false);
		this.buttonRemove.setEnabled(false);
		this.buttonAdd.addActionListener(this);
		this.buttonEdit.addActionListener(this);
		this.buttonRemove.addActionListener(this);
		this.list.addListSelectionListener(this);
		this.list.scrollPane.setPreferredSize(new Dimension(200, 100));

		this.updateList();
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.buttonAdd)
		{
			this.editing = -1;
			CGPanel p = this.objectList.createAddPanel(this.editing);
			if (p == null) return;
			CommandGenerator.stateManager.setState(p, this);
		} else if (e.getSource() == this.buttonEdit)
		{
			this.editing = this.selectedIndex();
			CGPanel p = this.objectList.createAddPanel(this.editing);
			if (p == null) return;
			CommandGenerator.stateManager.setState(p, this);
		} else if (e.getSource() == this.buttonRemove) this.removeSelected();
		this.updateList();
	}

	public IObjectList getObjectList()
	{
		return this.objectList;
	}

	public String getSelectedValue()
	{
		return this.list.getSelectedValue();
	}

	protected void removeSelected()
	{
		int index = this.selectedIndex();
		if (index != -1)
		{
			if (JOptionPane.showConfirmDialog(CommandGenerator.window,
					new Text("general.delete.confirm", new Replacement("<object>", this.getSelectedValue())), null, JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
			this.objectList.removeObject(index);
			this.updateList();
		}
	}

	public int selectedIndex()
	{
		return this.list.getSelectedIndex();
	}

	@Override
	public void setEnabled(boolean enabled)
	{
		super.setEnabled(enabled);
		this.buttonAdd.setEnabled(enabled);
		if (enabled) this.updateDisplay();
		else
		{
			this.buttonEdit.setEnabled(false);
			this.buttonRemove.setEnabled(false);
		}
	}

	public void setList(IObjectList objectList)
	{
		this.objectList = objectList;
		this.updateList();
	}

	@Override
	public boolean shouldStateClose(CGPanel panel)
	{
		boolean shouldClose = this.objectList.addObject(panel, this.editing);
		if (shouldClose) this.updateList();
		return shouldClose;
	}

	private void updateDisplay()
	{
		int index = this.selectedIndex();
		this.buttonEdit.setEnabled(index != -1);
		this.buttonRemove.setEnabled(index != -1);
		if (this.componentDisplay != null) this.remove(this.componentDisplay);
		if (index == -1) this.componentDisplay = null;
		else
		{
			this.componentDisplay = this.objectList.getDisplayComponent(index);
			if (this.componentDisplay != null) this.add(this.componentDisplay, this.gbc);
		}
		this.revalidate();
		this.repaint();
	}

	public void updateList()
	{
		if (this.objectList == null) return;
		int index = this.selectedIndex();
		this.list.setValues(this.objectList.getValues());
		this.list.setSelectedIndex(index);

		this.updateDisplay();
	}

	@Override
	public void valueChanged(ListSelectionEvent e)
	{
		this.updateDisplay();
	}

}
