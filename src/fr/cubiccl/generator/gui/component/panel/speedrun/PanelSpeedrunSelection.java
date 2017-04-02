package fr.cubiccl.generator.gui.component.panel.speedrun;

import java.awt.Font;
import java.awt.GridBagConstraints;

import javax.swing.BorderFactory;
import javax.swing.SwingConstants;

import fr.cubiccl.generator.gameobject.registries.ObjectSaver;
import fr.cubiccl.generator.gameobject.speedrun.Speedrun;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.utils.ListListener;
import fr.cubiccl.generator.gui.component.panel.utils.PanelObjectList;

public class PanelSpeedrunSelection extends CGPanel implements ListListener<Speedrun>
{
	public static final int HEIGHT = 190;
	private static final long serialVersionUID = 7092228745512002166L;

	public PanelObjectList<Speedrun> list;

	public PanelSpeedrunSelection()
	{
		super();

		this.setBorder(BorderFactory.createLoweredSoftBevelBorder());
		CGLabel l = new CGLabel("speedrun.list");
		l.setHorizontalAlignment(SwingConstants.CENTER);
		l.setFont(l.getFont().deriveFont(Font.BOLD, 30));

		GridBagConstraints gbc = this.createGridBagLayout();
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.CENTER;
		this.add(l, gbc);
		++gbc.gridy;
		this.add(this.list = new PanelObjectList<Speedrun>(null, (String) null, Speedrun.class), gbc);

		this.list.setValues(ObjectSaver.speedruns.list());
		this.list.addListListener(this);
	}

	@Override
	public void onAddition(int index, Speedrun object)
	{
		ObjectSaver.speedruns.addObject(object);
	}

	@Override
	public void onChange(int index, Speedrun object)
	{}

	@Override
	public void onDeletion(int index, Speedrun object)
	{
		ObjectSaver.speedruns.delete(object);
	}

	@Override
	public void setEnabled(boolean enabled)
	{
		super.setEnabled(enabled);
		this.list.setEnabled(enabled);
	}

}
