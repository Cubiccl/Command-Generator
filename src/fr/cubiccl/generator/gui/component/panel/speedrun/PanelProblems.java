package fr.cubiccl.generator.gui.component.panel.speedrun;

import fr.cubiccl.generator.gameobject.speedrun.Speedrun;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;

public class PanelProblems extends CGPanel
{
	private static final long serialVersionUID = 8496389805076984987L;

	private CGLabel labelFine;
	public final PanelSpeedrun parent;
	public final Speedrun speedrun;

	public PanelProblems(Speedrun speedrun, PanelSpeedrun parent)
	{
		super("speedrun.problems");
		this.speedrun = speedrun;
		this.parent = parent;
		// TODO PanelProblems.<init>(speedrun, parent)

		this.add(this.labelFine = new CGLabel("speedrun.problems.nothing"));
	}

	public void onSpeedrunUpdate()
	{
		// TODO PanelProblems.onSpeedrunUpdate()
		this.labelFine.setVisible(this.speedrun.isValid());
	}

}
