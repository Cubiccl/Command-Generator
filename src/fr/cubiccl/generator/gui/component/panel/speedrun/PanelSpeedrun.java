package fr.cubiccl.generator.gui.component.panel.speedrun;

import java.awt.Font;
import java.awt.GridBagConstraints;

import javax.swing.SwingConstants;

import fr.cubiccl.generator.gameobject.speedrun.Checkpoint;
import fr.cubiccl.generator.gameobject.speedrun.Speedrun;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.utils.Text;

public class PanelSpeedrun extends CGPanel
{
	private static final long serialVersionUID = 5199029123151332585L;

	private PanelCheckpoint panelCheckpoint;
	private PanelCheckpointSelection panelCheckpointSelection;
	public final Speedrun speedrun;

	public PanelSpeedrun(Speedrun speedrun)
	{
		this.speedrun = speedrun;
		CGLabel l = new CGLabel(new Text(speedrun.customName(), false));
		l.setFont(l.getFont().deriveFont(Font.BOLD, 30));
		l.setHorizontalAlignment(SwingConstants.CENTER);

		GridBagConstraints gbc = this.createGridBagLayout();
		this.add(l, gbc);
		++gbc.gridy;
		this.add(this.panelCheckpointSelection = new PanelCheckpointSelection(this.speedrun, this), gbc);
		++gbc.gridy;
		this.add(this.panelCheckpoint = new PanelCheckpoint(), gbc);
	}

	public void displayCheckpoint(Checkpoint checkpoint)
	{
		this.panelCheckpoint.setCheckpoint(checkpoint);
		this.panelCheckpoint.setVisible(checkpoint != null);
	}

}
