package fr.cubiccl.generator.gui.component.panel.speedrun;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import fr.cubiccl.generator.gameobject.speedrun.Checkpoint;
import fr.cubiccl.generator.gameobject.speedrun.Speedrun;
import fr.cubiccl.generator.gui.Dialogs;
import fr.cubiccl.generator.gui.component.button.CGButton;
import fr.cubiccl.generator.gui.component.combobox.SearchCombobox;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.utils.Lang;
import fr.cubiccl.generator.utils.Replacement;
import fr.cubiccl.generator.utils.Text;

public class PanelCheckpointSelection extends CGPanel implements ActionListener
{
	private static final long serialVersionUID = -1531239886923504426L;

	public final PanelSpeedrun parent;
	private int selected;
	public final Speedrun speedrun;

	private CGButton ui_buttonAdd, ui_buttonRemove;
	private SearchCombobox ui_comboboxSearch;

	public PanelCheckpointSelection(Speedrun speedrun, PanelSpeedrun parent)
	{
		super("speedrun.checkpoints");
		this.speedrun = speedrun;
		this.parent = parent;
		this.selected = -1;

		GridBagConstraints gbc = this.createGridBagLayout();
		gbc.gridheight = 2;
		this.add((this.ui_comboboxSearch = new SearchCombobox()).container, gbc);
		gbc.gridheight = 1;
		++gbc.gridx;
		this.add(this.ui_buttonAdd = new CGButton("general.add"), gbc);
		++gbc.gridy;
		this.add(this.ui_buttonRemove = new CGButton("general.remove"), gbc);

		this.ui_buttonAdd.addActionListener(this);
		this.ui_buttonRemove.addActionListener(this);
		this.ui_comboboxSearch.addActionListener(this);

		this.onSpeedrunUpdate();
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.ui_comboboxSearch) this.setSelected(this.speedrun.getCheckpoint(this.ui_comboboxSearch.getValue()));
		else if (e.getSource() == this.ui_buttonAdd)
		{
			String name = Dialogs.showInputDialog(new Text("objects.name").toString());
			if (name == null) return;
			this.speedrun.addCheckpoint(new Checkpoint(this.speedrun, name));
		} else if (e.getSource() == this.ui_buttonRemove
				&& Dialogs.showConfirmMessage(new Text("general.delete.confirm", new Replacement("<object>", this.selectedCheckpoint().getName())).toString(),
						Lang.translate("general.yes"), Lang.translate("general.no"))) this.speedrun.removeCheckpoint(this.selectedCheckpoint());
	}

	public void onSpeedrunUpdate()
	{
		Checkpoint[] checkpoints = this.speedrun.getCheckpoints();
		String[] names = new String[checkpoints.length];
		for (int i = 0; i < names.length; ++i)
			names[i] = checkpoints[i].getName();
		this.ui_comboboxSearch.setValues(names);
		if (this.selected >= names.length) this.selected = names.length - 1;
		if (this.selected != -1) this.ui_comboboxSearch.setSelectedIndex(this.selected);

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
		else
		{
			this.selected = checkpoint.position;
			this.ui_comboboxSearch.setSelectedIndex(this.selected);
		}
		this.updateParent();
	}

	private void updateParent()
	{
		this.parent.displayCheckpoint(this.selectedCheckpoint());
	}

}
