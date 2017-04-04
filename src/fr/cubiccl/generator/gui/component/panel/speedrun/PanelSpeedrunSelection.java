package fr.cubiccl.generator.gui.component.panel.speedrun;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.SwingConstants;

import fr.cubiccl.generator.gameobject.registries.ObjectSaver;
import fr.cubiccl.generator.gameobject.speedrun.Speedrun;
import fr.cubiccl.generator.gui.Dialogs;
import fr.cubiccl.generator.gui.component.button.CGButton;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.utils.ListListener;
import fr.cubiccl.generator.gui.component.panel.utils.PanelObjectList;
import fr.cubiccl.generator.utils.Text;

public class PanelSpeedrunSelection extends CGPanel implements ListListener<Speedrun>
{
	public static final int HEIGHT = 190;
	private static final long serialVersionUID = 7092228745512002166L;

	private CGButton buttonHelp;
	public PanelObjectList<Speedrun> list;

	public PanelSpeedrunSelection()
	{
		super();

		this.setBorder(BorderFactory.createLoweredSoftBevelBorder());
		CGLabel l = new CGLabel("speedrun.list");
		l.setHorizontalAlignment(SwingConstants.CENTER);
		l.setFont(l.getFont().deriveFont(Font.BOLD, 30));

		GridBagConstraints gbc = this.createGridBagLayout();
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.gridwidth = 2;
		this.add(l, gbc);
		++gbc.gridy;
		gbc.gridwidth = 1;
		this.add(this.list = new PanelObjectList<Speedrun>(null, (String) null, Speedrun.class), gbc);
		++gbc.gridx;
		this.add(this.buttonHelp = new CGButton("speedrun.help.button"), gbc);

		this.list.setValues(ObjectSaver.speedruns.list());
		this.list.addListListener(this);
		this.buttonHelp.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Dialogs.showMessage(new Text("speedrun.help").toString());
			}
		});
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