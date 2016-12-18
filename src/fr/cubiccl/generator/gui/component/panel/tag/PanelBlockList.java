package fr.cubiccl.generator.gui.component.panel.tag;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gameobject.baseobjects.Block;
import fr.cubiccl.generator.gui.component.CGList;
import fr.cubiccl.generator.gui.component.button.CGButton;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.label.ImageLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelBlockSelection;
import fr.cubiccl.generator.utils.IStateListener;
import fr.cubiccl.generator.utils.Utils.BlockComparator;

public class PanelBlockList extends CGPanel implements ActionListener, IStateListener<PanelBlockSelection>, ListSelectionListener
{
	private static final long serialVersionUID = 2923920419688577940L;

	private ArrayList<Block> blocks;
	private CGButton buttonAdd, buttonRemove;
	private ImageLabel label;
	private CGLabel labelName;
	private CGList listBlocks;

	public PanelBlockList()
	{
		this.blocks = new ArrayList<Block>();

		GridBagConstraints gbc = this.createGridBagLayout();
		this.add(this.label = new ImageLabel(), gbc);
		++gbc.gridy;
		this.add(this.labelName = new CGLabel(""), gbc);

		++gbc.gridx;
		--gbc.gridy;
		++gbc.gridheight;
		this.add((this.listBlocks = new CGList()).scrollPane, gbc);

		++gbc.gridx;
		--gbc.gridheight;
		this.add(this.buttonAdd = new CGButton("block.add"), gbc);
		++gbc.gridy;
		this.add(this.buttonRemove = new CGButton("block.remove"), gbc);

		this.buttonAdd.addActionListener(this);
		this.buttonRemove.addActionListener(this);
		this.listBlocks.addListSelectionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.buttonAdd) CommandGenerator.stateManager.setState(new PanelBlockSelection(false), this);
		else if (e.getSource() == this.buttonRemove) this.removeSelected();
	}

	private void removeSelected()
	{
		int index = this.selectedIndex();
		if (index != -1)
		{
			this.blocks.remove(index);
			this.updateList();
		}
	}

	public Block[] selectedBlocks()
	{
		return this.blocks.toArray(new Block[this.blocks.size()]);
	}

	private int selectedIndex()
	{
		return this.listBlocks.getSelectedIndex();
	}

	public void setBlocks(Block[] b)
	{
		this.blocks.clear();
		for (Block block : b)
			this.blocks.add(block);
		this.updateList();
	}

	@Override
	public boolean shouldStateClose(PanelBlockSelection panel)
	{
		if (this.blocks.contains(panel.selectedBlock())) return true;
		this.blocks.add(panel.selectedBlock());
		this.updateList();
		return true;
	}

	private void updateDisplay()
	{
		int index = this.selectedIndex();
		if (index == -1) return;
		this.label.setImage(this.blocks.get(index).texture(0));
		this.labelName.setTextID(this.blocks.get(index).mainName());
	}

	private void updateList()
	{
		this.blocks.sort(new BlockComparator());
		
		int index = this.selectedIndex();
		String[] values = new String[this.blocks.size()];
		for (int i = 0; i < values.length; ++i)
			values[i] = this.blocks.get(i).mainName().toString();
		this.listBlocks.setValues(values);
		if (values.length > 0) this.listBlocks.setSelectedIndex(index);

		this.updateDisplay();
	}

	@Override
	public void valueChanged(ListSelectionEvent e)
	{
		this.updateDisplay();
	}

}
