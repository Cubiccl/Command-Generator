package fr.cubiccl.generator.gui.component.panel.speedrun;

import java.awt.Font;

import javax.swing.SwingConstants;

import fr.cubiccl.generator.gameobject.speedrun.Speedrun;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.utils.Text;

public class PanelSpeedrun extends CGPanel
{
	private static final long serialVersionUID = 5199029123151332585L;

	public final Speedrun speedrun;

	public PanelSpeedrun(Speedrun speedrun)
	{
		this.speedrun = speedrun;
		CGLabel l = new CGLabel(new Text(speedrun.customName(), false));
		l.setFont(l.getFont().deriveFont(Font.BOLD, 30));
		l.setHorizontalAlignment(SwingConstants.CENTER);
		this.add(l);
	}

}
