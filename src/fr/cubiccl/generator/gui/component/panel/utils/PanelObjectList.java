package fr.cubiccl.generator.gui.component.panel.utils;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gui.component.CGList;
import fr.cubiccl.generator.gui.component.button.CGButton;
import fr.cubiccl.generator.gui.component.interfaces.IObjectList;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.label.ImageLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.utils.IStateListener;

public class PanelObjectList extends CGPanel implements ActionListener, ListSelectionListener, IStateListener<CGPanel>
{
	private static final long serialVersionUID = 2923920419688577940L;

	private CGButton buttonAdd, buttonRemove;
	private ImageLabel labelImage;
	private CGLabel labelName;
	private CGList list;
	public final IObjectList objectList;

	public PanelObjectList(IObjectList objectList)
	{
		this(null, objectList);
	}

	public PanelObjectList(String titleID, IObjectList objectList)
	{
		super(titleID);
		this.objectList = objectList;

		GridBagConstraints gbc = this.createGridBagLayout();
		this.add(this.labelImage = new ImageLabel(), gbc);
		++gbc.gridy;
		this.add(this.labelName = new CGLabel(""), gbc);

		++gbc.gridx;
		--gbc.gridy;
		++gbc.gridheight;
		this.add((this.list = new CGList()).scrollPane, gbc);

		++gbc.gridx;
		--gbc.gridheight;
		this.add(this.buttonAdd = new CGButton("general.add"), gbc);
		++gbc.gridy;
		this.add(this.buttonRemove = new CGButton("general.remove"), gbc);

		this.buttonAdd.addActionListener(this);
		this.buttonRemove.addActionListener(this);
		this.list.addListSelectionListener(this);
		this.list.scrollPane.setPreferredSize(new Dimension(200, 100));

		this.updateList();
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.buttonAdd) CommandGenerator.stateManager.setState(this.objectList.createAddPanel(), this);
		else if (e.getSource() == this.buttonRemove) this.removeSelected();
	}

	private void removeSelected()
	{
		int index = this.selectedIndex();
		if (index != -1)
		{
			this.objectList.removeObject(index);
			this.updateList();
		}
	}

	private int selectedIndex()
	{
		return this.list.getSelectedIndex();
	}

	@Override
	public boolean shouldStateClose(CGPanel panel)
	{
		boolean shouldClose = this.objectList.addObject(panel);
		if (shouldClose) this.updateList();
		return shouldClose;
	}

	private void updateDisplay()
	{
		int index = this.selectedIndex();
		if (index == -1) return;
		this.labelImage.setImage(this.objectList.getTexture(index));
		this.labelName.setTextID(this.objectList.getName(index));
	}

	public void updateList()
	{
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
