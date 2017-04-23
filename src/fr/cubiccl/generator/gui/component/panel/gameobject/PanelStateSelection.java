package fr.cubiccl.generator.gui.component.panel.gameobject;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import fr.cubiccl.generator.gameobject.baseobjects.Block;
import fr.cubiccl.generator.gameobject.baseobjects.BlockState;
import fr.cubiccl.generator.gui.component.CGList;
import fr.cubiccl.generator.gui.component.button.CGButton;
import fr.cubiccl.generator.gui.component.combobox.CGComboBox;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.utils.Text;

public class PanelStateSelection extends CGPanel implements ActionListener, ListSelectionListener
{

	private static final long serialVersionUID = 3926182774046628691L;
	private ArrayList<BlockState> added;
	private Block block;
	private CGButton buttonAdd, buttonRemove;
	private CGComboBox comboboxValues;
	private CGList listAvailable, listAdded;
	private ArrayList<BlockState> states;
	public HashMap<String, String> values;

	public PanelStateSelection(Block block)
	{
		this(null, block);
	}

	public PanelStateSelection(String titleID, Block block)
	{
		super(titleID);
		this.states = new ArrayList<BlockState>();
		this.added = new ArrayList<BlockState>();
		this.values = new HashMap<String, String>();

		GridBagConstraints gbc = this.createGridBagLayout();
		this.add(new CGLabel("blockstate.available"), gbc);
		gbc.gridx = 2;
		this.add(new CGLabel("blockstate.added"), gbc);
		gbc.gridx = 0;
		++gbc.gridy;
		gbc.gridheight = 4;
		this.add((this.listAvailable = new CGList()).scrollPane, gbc);
		gbc.gridx = 2;
		this.add((this.listAdded = new CGList()).scrollPane, gbc);

		gbc.gridx = 1;
		gbc.gridheight = 1;
		this.add(new CGLabel("blockstate.value").setHasColumn(true), gbc);
		++gbc.gridy;
		this.add(this.comboboxValues = new CGComboBox(), gbc);
		++gbc.gridy;
		this.add(this.buttonAdd = new CGButton(new Text(">>>", false)), gbc);
		++gbc.gridy;
		this.add(this.buttonRemove = new CGButton(new Text("<<<", false)), gbc);

		this.setBlock(block);
		this.buttonAdd.addActionListener(this);
		this.buttonRemove.addActionListener(this);
		this.listAvailable.addListSelectionListener(this);
		this.listAdded.addListSelectionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.buttonAdd) this.setState(this.selectedState().id, this.comboboxValues.getValue());
		else if (e.getSource() == this.buttonRemove)
		{
			this.values.remove(this.selectedValue().id);
			this.added.remove(this.selectedValue());
			this.updateDisplay();
		}
	}

	public void clear()
	{
		this.added.clear();
		this.values.clear();
		this.updateDisplay();
	}

	public Block currentBlock()
	{
		return this.block;
	}

	private ArrayList<BlockState> remaining()
	{
		ArrayList<BlockState> remaining = new ArrayList<BlockState>();
		remaining.addAll(this.states);
		remaining.removeAll(this.added);
		remaining.sort(new Comparator<BlockState>()
		{
			@Override
			public int compare(BlockState o1, BlockState o2)
			{
				return o1.id.compareTo(o2.id);
			}
		});
		return remaining;
	}

	private BlockState selectedState()
	{
		if (this.listAvailable.getSelectedIndex() == -1) return null;
		return this.remaining().get(this.listAvailable.getSelectedIndex());
	}

	/** @return The STATE selected in the ADDED STATES. */
	private BlockState selectedValue()
	{
		return this.added.get(this.listAdded.getSelectedIndex());
	}

	public void setBlock(Block block)
	{
		this.block = block;
		this.states.clear();
		this.states.addAll(this.block.getStates());
		this.added.clear();
		ArrayList<String> ids = new ArrayList<String>();
		ids.addAll(this.values.keySet());
		for (String s : ids)
		{
			boolean found = false;
			for (BlockState state : this.states)
				if (state.id.equals(s) && state.hasValue(this.values.get(s)))
				{
					found = true;
					this.added.add(state);
					break;
				}
			if (!found) this.values.remove(s);
		}
		this.updateValues();
		this.updateDisplay();
	}

	public void setState(String stateID, String value)
	{
		for (BlockState state : this.states)
			if (state.id.equals(stateID) && state.hasValue(value))
			{
				this.values.put(stateID, value);
				this.added.add(state);
				break;
			}

		this.updateDisplay();
	}

	private void updateButtons()
	{
		this.comboboxValues.setEnabled(this.remaining().size() > 0 && this.listAvailable.getSelectedIndex() != -1);
		this.buttonAdd.setEnabled(this.comboboxValues.isEnabled());
		this.buttonRemove.setEnabled(this.added.size() > 0 && this.listAdded.getSelectedIndex() != -1);
	}

	private void updateDisplay()
	{
		this.added.sort(new Comparator<BlockState>()
		{
			@Override
			public int compare(BlockState o1, BlockState o2)
			{
				return o1.id.compareTo(o2.id);
			}
		});

		String[] names = new String[this.added.size()];
		for (int i = 0; i < names.length; ++i)
			names[i] = this.added.get(i).id + " = " + this.values.get(this.added.get(i).id);
		this.listAdded.setValues(names);

		ArrayList<BlockState> remaining = this.remaining();
		names = new String[remaining.size()];
		for (int i = 0; i < names.length; ++i)
			names[i] = remaining.get(i).id;
		this.listAvailable.setValues(names);

		this.updateButtons();
	}

	private void updateValues()
	{
		if (this.selectedState() == null) return;
		this.comboboxValues.setValues(this.selectedState().values);
	}

	@Override
	public void valueChanged(ListSelectionEvent e)
	{
		this.updateValues();
		this.updateButtons();
	}

}
