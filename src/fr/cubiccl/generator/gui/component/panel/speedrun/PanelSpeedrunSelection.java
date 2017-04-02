package fr.cubiccl.generator.gui.component.panel.speedrun;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.SwingConstants;

import fr.cubiccl.generator.gameobject.registries.ObjectSaver;
import fr.cubiccl.generator.gameobject.speedrun.Speedrun;
import fr.cubiccl.generator.gui.component.button.CGButton;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.utils.ListListener;
import fr.cubiccl.generator.gui.component.panel.utils.PanelObjectList;

public class PanelSpeedrunSelection extends CGPanel implements ActionListener, ListListener<Speedrun>
{
	public static final int HEIGHT = 220;
	private static final long serialVersionUID = 7092228745512002166L;

	private CGButton buttonLoad;
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
		++gbc.gridy;
		this.add(this.buttonLoad = new CGButton("speedrun.load"), gbc);

		this.buttonLoad.addActionListener(this);
		this.list.setValues(ObjectSaver.speedruns.list());
		this.list.addListListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onAddition(int index, Speedrun object)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onChange(int index, Speedrun object)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onDeletion(int index, Speedrun object)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void setEnabled(boolean enabled)
	{
		super.setEnabled(enabled);
		this.buttonLoad.setEnabled(enabled);
		this.list.setEnabled(enabled);
	}

}
