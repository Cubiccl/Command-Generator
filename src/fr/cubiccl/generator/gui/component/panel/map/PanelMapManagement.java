package fr.cubiccl.generator.gui.component.panel.map;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import fr.cubi.cubigui.CTabbedPane;
import fr.cubiccl.generator.gameobject.map.Map;
import fr.cubiccl.generator.gui.component.button.CGButton;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;

public class PanelMapManagement extends CGPanel implements ComponentListener, ActionListener
{
	private static final long serialVersionUID = -8583477065140875486L;

	private CGButton buttonClose, buttonOpen;
	private CGLabel labelName;
	private Map map;
	private CTabbedPane tabs;

	public PanelMapManagement()
	{
		super(null);

		this.addComponentListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		// TODO actionPerformed()
	}

	@Override
	public void componentHidden(ComponentEvent e)
	{}

	@Override
	public void componentMoved(ComponentEvent e)
	{}

	@Override
	public void componentResized(ComponentEvent e)
	{
		// TODO componentResized()
	}

	@Override
	public void componentShown(ComponentEvent e)
	{}

}
