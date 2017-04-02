package fr.cubiccl.generator.gui.component.panel.speedrun;

import fr.cubiccl.generator.gameobject.speedrun.Checkpoint;
import fr.cubiccl.generator.gameobject.speedrun.Speedrun;
import fr.cubiccl.generator.gui.component.panel.CGPanel;

public class PanelCheckpointSelection extends CGPanel
{
	private static final long serialVersionUID = -1531239886923504426L;

	public final PanelSpeedrun parent;
	private int selected;
	public final Speedrun speedrun;

	public PanelCheckpointSelection(Speedrun speedrun, PanelSpeedrun parent)
	{
		this.speedrun = speedrun;
		this.parent = parent;
		this.selected = -1;
		// TODO Auto-generated constructor stub
	}

	public void onSpeedrunUpdate()
	{
		// TODO Auto-generated method stub
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
