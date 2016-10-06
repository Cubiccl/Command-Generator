package fr.cubiccl.generator.gui;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gui.component.CScrollPane;
import fr.cubiccl.generator.gui.component.interfaces.ITranslated;
import fr.cubiccl.generator.gui.component.menubar.CMenuBar;
import fr.cubiccl.generator.gui.component.panel.CPanel;
import fr.cubiccl.generator.gui.component.panel.PanelCommand;
import fr.cubiccl.generator.gui.component.panel.PanelCommandSelection;

public class Window extends JFrame implements ComponentListener, ITranslated, WindowListener
{
	private static final long serialVersionUID = -3962531275009303736L;

	private CMenuBar menubar;
	private PanelCommand panelCommand;
	private PanelCommandSelection panelCommandSelection;
	private CPanel panelGui;
	private JScrollPane scrollpane;

	public Window()
	{
		super("Command Generator v2.0");
		this.setSize(800, 600);
		this.setMinimumSize(new Dimension(800, 400));
		this.setLocationRelativeTo(null);
		this.setExtendedState(MAXIMIZED_BOTH);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		this.createLayout();
		this.addComponentListener(this);
		this.addWindowListener(this);
	}

	@Override
	public void componentHidden(ComponentEvent arg0)
	{}

	@Override
	public void componentMoved(ComponentEvent arg0)
	{}

	@Override
	public void componentResized(ComponentEvent arg0)
	{
		this.onResized();
	}

	@Override
	public void componentShown(ComponentEvent arg0)
	{}

	private void createLayout()
	{
		Container contentPane = this.getContentPane();
		contentPane.setLayout(null);
		contentPane.add(this.panelCommand = new PanelCommand());
		contentPane.add(this.panelCommandSelection = new PanelCommandSelection());
		contentPane.add(this.scrollpane = new CScrollPane());
		this.scrollpane.setBorder(BorderFactory.createLoweredSoftBevelBorder());
		this.scrollpane.getVerticalScrollBar().setUnitIncrement(20);

		this.setJMenuBar(this.menubar = new CMenuBar());
	}

	public CPanel getCommandPanel()
	{
		return this.panelGui;
	}

	private void onResized()
	{
		Container contentPane = this.getContentPane();
		this.panelCommand.setBounds(0, 0, contentPane.getWidth(), PanelCommand.HEIGHT);
		this.panelCommandSelection.setBounds(0, this.panelCommand.getHeight(), contentPane.getWidth(), PanelCommandSelection.HEIGHT);
		this.scrollpane.setBounds(0, this.panelCommand.getHeight() + this.panelCommandSelection.getHeight(), contentPane.getWidth(), contentPane.getHeight()
				- this.panelCommand.getHeight() - this.panelCommandSelection.getHeight());
		this.validate();
	}

	public void setExecuteCommand(boolean executeCommand)
	{
		this.panelCommandSelection.buttonCancelExecute.setVisible(executeCommand);
	}

	public void setMainPanel(CPanel gui)
	{
		this.panelGui = gui;
		this.scrollpane.setViewportView(this.panelGui);
	}

	public void showCommand(String command)
	{
		this.panelCommand.textfieldCommand.setText(command);
	}

	@Override
	public void updateTranslations()
	{
		this.panelCommand.updateTranslations();
		this.panelCommandSelection.updateTranslations();
		this.menubar.updateTranslations();
	}

	@Override
	public void windowActivated(WindowEvent arg0)
	{}

	@Override
	public void windowClosed(WindowEvent arg0)
	{
		CommandGenerator.finishLog();
	}

	@Override
	public void windowClosing(WindowEvent arg0)
	{
		CommandGenerator.finishLog();
	}

	@Override
	public void windowDeactivated(WindowEvent arg0)
	{}

	@Override
	public void windowDeiconified(WindowEvent arg0)
	{}

	@Override
	public void windowIconified(WindowEvent arg0)
	{}

	@Override
	public void windowOpened(WindowEvent arg0)
	{}

}
