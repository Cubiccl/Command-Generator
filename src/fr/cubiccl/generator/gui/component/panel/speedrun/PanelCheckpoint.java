package fr.cubiccl.generator.gui.component.panel.speedrun;

import fr.cubiccl.generator.gameobject.speedrun.Checkpoint;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.utils.Text;

public class PanelCheckpoint extends CGPanel
{
	private static final long serialVersionUID = -9059269199436502504L;

	private Checkpoint checkpoint = null;
	private CGLabel labelName;

	public PanelCheckpoint()
	{
		super("speedrun.checkpoint");
		// TODO Auto-generated constructor stub
		this.add(this.labelName = new CGLabel(new Text("", false)));
	}

	public void setCheckpoint(Checkpoint checkpoint)
	{
		this.checkpoint = checkpoint;
		// TODO Auto-generated method stub
		if (this.checkpoint == null) this.labelName.setTextID(new Text("", false));
		else this.labelName.setTextID(new Text(this.checkpoint.getName(), false));
	}

}
