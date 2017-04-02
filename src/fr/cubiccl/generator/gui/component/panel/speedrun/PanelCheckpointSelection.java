package fr.cubiccl.generator.gui.component.panel.speedrun;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import fr.cubiccl.generator.gameobject.speedrun.Checkpoint;
import fr.cubiccl.generator.gameobject.speedrun.Speedrun;
import fr.cubiccl.generator.gui.component.combobox.SearchCombobox;
import fr.cubiccl.generator.gui.component.panel.CGPanel;

public class PanelCheckpointSelection extends CGPanel implements ActionListener
{
	private static final long serialVersionUID = -1531239886923504426L;

	public final PanelSpeedrun parent;
	private int selected;
	public final Speedrun speedrun;

	private SearchCombobox ui_comboboxSearch;

	public PanelCheckpointSelection(Speedrun speedrun, PanelSpeedrun parent)
	{
		super("speedrun.checkpoints");
		this.speedrun = speedrun;
		this.parent = parent;
		this.selected = -1;

		GridBagConstraints gbc = this.createGridBagLayout();
		this.add((this.ui_comboboxSearch = new SearchCombobox()).container, gbc);

		this.ui_comboboxSearch.addActionListener(this);

		this.onSpeedrunUpdate();
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		this.setSelected(this.speedrun.getCheckpoint(this.ui_comboboxSearch.getValue()));
	}

	public void onSpeedrunUpdate()
	{
		Checkpoint[] checkpoints = this.speedrun.getCheckpoints();
		String[] names = new String[checkpoints.length];
		for (int i = 0; i < names.length; ++i)
			names[i] = checkpoints[i].getName();
		this.ui_comboboxSearch.setValues(names);
		if (this.selected != -1 && this.selected < names.length) this.ui_comboboxSearch.setSelectedIndex(this.selected);

		this.ui_comboboxSearch.container.setVisible(names.length > 0);
		this.updateParent();
	}

	public Checkpoint selectedCheckpoint()
	{
		if (this.selected == -1) return null;
		return this.speedrun.getCheckpoint(this.selected);
	}

	public void setSelected(Checkpoint checkpoint)
	{
		if (checkpoint == null) this.selected = -1;
		else this.selected = checkpoint.position;
		this.updateParent();
	}

	private void updateParent()
	{
		this.parent.displayCheckpoint(this.selectedCheckpoint());
	}

}
