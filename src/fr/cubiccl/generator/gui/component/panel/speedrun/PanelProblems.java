package fr.cubiccl.generator.gui.component.panel.speedrun;

import fr.cubiccl.generator.gameobject.speedrun.Speedrun;
import fr.cubiccl.generator.gui.component.panel.CGPanel;

public class PanelProblems extends CGPanel
{
	private static final long serialVersionUID = 8496389805076984987L;

	public final PanelSpeedrun parent;
	public final Speedrun speedrun;

	public PanelProblems(Speedrun speedrun, PanelSpeedrun parent)
	{
		this.speedrun = speedrun;
		this.parent = parent;
		// TODO Auto-generated constructor stub
	}

	public void onSpeedrunUpdate()
	{
		// TODO Auto-generated method stub
		
	}

}
