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
import fr.cubiccl.generator.command.Command;
import fr.cubiccl.generator.gui.component.CScrollPane;
import fr.cubiccl.generator.gui.component.interfaces.ITranslated;
import fr.cubiccl.generator.gui.component.menubar.CMenuBar;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.PanelCommand;
import fr.cubiccl.generator.gui.component.panel.PanelCommandSelection;
import fr.cubiccl.generator.gui.component.panel.loottable.PanelLootTableOutput;
import fr.cubiccl.generator.gui.component.panel.loottable.PanelLootTableSelection;
import fr.cubiccl.generator.utils.Settings;
import fr.cubiccl.generator.utils.Textures;

public class Window extends JFrame implements ComponentListener, ITranslated, WindowListener
{
	private static final long serialVersionUID = -3962531275009303736L;

	public CMenuBar menubar;
	private PanelCommand panelCommand;
	private PanelCommandSelection panelCommandSelection;
	private CGPanel panelGui;
	private PanelLootTableOutput panelLootTableOutput;
	private PanelLootTableSelection panelLootTableSelection;
	private JScrollPane scrollpane;

	public Window()
	{
		super("Command Generator v" + Settings.GENERATOR_VERSION);
		this.setSize(800, 600);
		this.setMinimumSize(new Dimension(800, 400));
		this.setLocationRelativeTo(null);
		this.setExtendedState(MAXIMIZED_BOTH);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		this.createLayout();
		this.addComponentListener(this);
		this.addWindowListener(this);
		this.setIconImage(Textures.getTexture("gui.icon"));
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
		contentPane.add(this.panelLootTableOutput = new PanelLootTableOutput());
		contentPane.add(this.panelCommandSelection = new PanelCommandSelection());
		contentPane.add(this.panelLootTableSelection = new PanelLootTableSelection());
		contentPane.add(this.scrollpane = new CScrollPane(null));
		this.scrollpane.setBorder(BorderFactory.createLoweredSoftBevelBorder());
		this.scrollpane.getVerticalScrollBar().setUnitIncrement(20);
		this.scrollpane.getHorizontalScrollBar().setUnitIncrement(20);

		this.setJMenuBar(this.menubar = new CMenuBar());
	}

	public CGPanel getCommandPanel()
	{
		return this.panelGui;
	}

	private void onResized()
	{
		if (CommandGenerator.getCurrentMode() == CommandGenerator.COMMANDS)
		{
			Container contentPane = this.getContentPane();
			this.panelCommand.setBounds(0, 0, contentPane.getWidth(), PanelCommand.HEIGHT);
			this.panelCommandSelection.setBounds(0, this.panelCommand.getHeight(), contentPane.getWidth(), PanelCommandSelection.HEIGHT);
			this.scrollpane.setBounds(0, this.panelCommand.getHeight() + this.panelCommandSelection.getHeight(), contentPane.getWidth(),
					contentPane.getHeight() - this.panelCommand.getHeight() - this.panelCommandSelection.getHeight());
		} else
		{
			Container contentPane = this.getContentPane();
			this.panelLootTableOutput.setBounds(0, 0, contentPane.getWidth() / 2, PanelLootTableOutput.HEIGHT);
			this.panelLootTableSelection.setBounds(this.panelLootTableOutput.getWidth(), 0, this.getWidth() / 2, PanelLootTableOutput.HEIGHT);
			this.scrollpane.setBounds(0, PanelLootTableOutput.HEIGHT, contentPane.getWidth(), contentPane.getHeight() - this.panelLootTableOutput.getHeight());
		}
		this.validate();
	}

	public void setExecuteCommand(boolean executeCommand)
	{
		this.panelCommandSelection.buttonCancelExecute.setVisible(executeCommand);
	}

	public void setMainPanel(CGPanel gui)
	{
		this.panelGui = gui;
		this.scrollpane.setViewportView(this.panelGui);
		this.panelCommandSelection.setEnabled(CommandGenerator.stateManager.stateCount() <= 1);
	}

	public void setSelected(Command command)
	{
		this.panelCommandSelection.setSelected(command);
	}

	public void showOutput(String command)
	{
		if (CommandGenerator.getCurrentMode() == CommandGenerator.COMMANDS) this.panelCommand.textfieldCommand.setText((Boolean.parseBoolean(Settings
				.getSetting(Settings.SLASH)) ? "/" : "") + command);
		else this.panelLootTableOutput.areaOutput.setText(command);
	}

	public void updateMode()
	{
		boolean commands = CommandGenerator.getCurrentMode() == CommandGenerator.COMMANDS;
		boolean loot_tables = !commands;
		this.panelCommandSelection.setVisible(commands);
		this.panelCommand.setVisible(commands);
		this.panelLootTableOutput.setVisible(loot_tables);
		this.panelLootTableSelection.setVisible(loot_tables);
		this.onResized();
	}

	@Override
	public void updateTranslations()
	{
		this.panelCommand.updateTranslations();
		this.panelCommandSelection.updateTranslations();
		this.menubar.updateTranslations();
		if (this.panelGui != null) this.panelGui.updateTranslations();
	}

	@Override
	public void windowActivated(WindowEvent arg0)
	{}

	@Override
	public void windowClosed(WindowEvent arg0)
	{
		CommandGenerator.exit();
	}

	@Override
	public void windowClosing(WindowEvent arg0)
	{
		CommandGenerator.exit();
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
