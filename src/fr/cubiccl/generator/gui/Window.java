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
import fr.cubiccl.generator.gui.component.panel.advancement.PanelAdvancementSelection;
import fr.cubiccl.generator.gui.component.panel.data.PanelObjectSelection;
import fr.cubiccl.generator.gui.component.panel.loottable.PanelJsonOutput;
import fr.cubiccl.generator.gui.component.panel.loottable.PanelLootTableSelection;
import fr.cubiccl.generator.gui.component.panel.mainwindow.PanelCommand;
import fr.cubiccl.generator.gui.component.panel.mainwindow.PanelCommandSelection;
import fr.cubiccl.generator.gui.component.panel.recipe.PanelRecipeSelection;
import fr.cubiccl.generator.gui.component.panel.speedrun.PanelSpeedrunSelection;
import fr.cubiccl.generator.utils.Replacement;
import fr.cubiccl.generator.utils.Settings;
import fr.cubiccl.generator.utils.Text;
import fr.cubiccl.generator.utils.Textures;

public class Window extends JFrame implements ComponentListener, ITranslated, WindowListener
{
	private static final long serialVersionUID = -3962531275009303736L;

	public CMenuBar menubar;
	public PanelAdvancementSelection panelAdvancementSelection;
	private PanelCommand panelCommand;
	private PanelCommandSelection panelCommandSelection;
	private CGPanel panelGui;
	private PanelJsonOutput panelJsonOutput;
	public PanelLootTableSelection panelLootTableSelection;
	public PanelObjectSelection panelObjectSelection;
	public PanelRecipeSelection panelRecipeSelection;
	public PanelSpeedrunSelection panelSpeedrunSelection;
	private JScrollPane scrollpane;

	public Window()
	{
		super();
		this.setSize(890, 600);
		this.setMinimumSize(new Dimension(890, 400));
		this.setLocationRelativeTo(null);
		this.setExtendedState(MAXIMIZED_BOTH);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		this.createLayout();
		this.addComponentListener(this);
		this.addWindowListener(this);
		this.setIconImage(Textures.getTexture("gui.icon"));
		this.updateTitle();
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
		contentPane.add(this.panelJsonOutput = new PanelJsonOutput());
		contentPane.add(this.panelCommandSelection = new PanelCommandSelection());
		contentPane.add(this.panelLootTableSelection = new PanelLootTableSelection());
		contentPane.add(this.panelRecipeSelection = new PanelRecipeSelection());
		contentPane.add(this.panelAdvancementSelection = new PanelAdvancementSelection());
		contentPane.add(this.panelSpeedrunSelection = new PanelSpeedrunSelection());
		contentPane.add(this.panelObjectSelection = new PanelObjectSelection());
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
		Container contentPane = this.getContentPane();
		if (CommandGenerator.getCurrentMode() == CommandGenerator.COMMANDS)
		{
			this.panelCommand.setBounds(0, 0, contentPane.getWidth(), PanelCommand.HEIGHT);
			this.panelCommandSelection.setBounds(0, this.panelCommand.getHeight(), contentPane.getWidth(), PanelCommandSelection.HEIGHT);
			this.scrollpane.setBounds(0, this.panelCommand.getHeight() + this.panelCommandSelection.getHeight(), contentPane.getWidth(),
					contentPane.getHeight() - this.panelCommand.getHeight() - this.panelCommandSelection.getHeight());
		} else if (CommandGenerator.getCurrentMode() == CommandGenerator.DATA)
		{
			this.panelObjectSelection.setBounds(0, 0, contentPane.getWidth(), PanelObjectSelection.HEIGHT);
			this.scrollpane.setBounds(0, this.panelObjectSelection.getHeight(), contentPane.getWidth(),
					contentPane.getHeight() - this.panelObjectSelection.getHeight());
		} else if (CommandGenerator.getCurrentMode() == CommandGenerator.LOOT_TABLES || CommandGenerator.getCurrentMode() == CommandGenerator.RECIPES
				|| CommandGenerator.getCurrentMode() == CommandGenerator.ADVANCEMENTS)
		{
			this.panelLootTableSelection.setBounds(0, 0, contentPane.getWidth() / 2, PanelJsonOutput.HEIGHT);
			this.panelRecipeSelection.setBounds(0, 0, contentPane.getWidth() / 2, PanelJsonOutput.HEIGHT);
			this.panelAdvancementSelection.setBounds(0, 0, contentPane.getWidth() / 2, PanelJsonOutput.HEIGHT);
			this.panelJsonOutput.setBounds(this.panelLootTableSelection.getWidth(), 0, this.getWidth() / 2, PanelJsonOutput.HEIGHT);
			this.scrollpane.setBounds(0, PanelJsonOutput.HEIGHT, contentPane.getWidth(), contentPane.getHeight() - this.panelJsonOutput.getHeight());
		} else if (CommandGenerator.getCurrentMode() == CommandGenerator.SPEEDRUN)
		{
			this.panelSpeedrunSelection.setBounds(0, 0, contentPane.getWidth(), PanelSpeedrunSelection.HEIGHT);
			this.scrollpane.setBounds(0, PanelSpeedrunSelection.HEIGHT, contentPane.getWidth(), contentPane.getHeight() - PanelSpeedrunSelection.HEIGHT);
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
		else this.panelJsonOutput.areaOutput.setText(command);
	}

	public void updateMode()
	{
		boolean commands = CommandGenerator.getCurrentMode() == CommandGenerator.COMMANDS;
		boolean loot_tables = CommandGenerator.getCurrentMode() == CommandGenerator.LOOT_TABLES;
		boolean data = CommandGenerator.getCurrentMode() == CommandGenerator.DATA;
		boolean recipes = CommandGenerator.getCurrentMode() == CommandGenerator.RECIPES;
		boolean advancements = CommandGenerator.getCurrentMode() == CommandGenerator.ADVANCEMENTS;
		this.panelCommandSelection.setVisible(commands);
		this.panelCommand.setVisible(commands);
		this.panelJsonOutput.setVisible(loot_tables || recipes || advancements);
		this.panelLootTableSelection.setVisible(loot_tables);
		this.panelRecipeSelection.setVisible(recipes);
		this.panelAdvancementSelection.setVisible(advancements);
		this.panelSpeedrunSelection.setVisible(CommandGenerator.getCurrentMode() == CommandGenerator.SPEEDRUN);
		this.panelObjectSelection.setVisible(data);
		this.onResized();
	}

	public void updateTitle()
	{
		this.setTitle(new Text("general.title", new Replacement("<gen>", Settings.GENERATOR_VERSION), new Replacement("<mc>", Settings.version().name))
				.toString());
	}

	@Override
	public void updateTranslations()
	{
		this.panelCommand.updateTranslations();
		this.panelCommandSelection.updateTranslations();
		this.panelJsonOutput.updateTranslations();
		this.panelLootTableSelection.updateTranslations();
		this.panelRecipeSelection.updateTranslations();
		this.panelAdvancementSelection.updateTranslations();
		this.panelSpeedrunSelection.updateTranslations();
		this.menubar.updateTranslations();
		if (this.panelGui != null) this.panelGui.updateTranslations();
		this.updateTitle();
	}

	@Override
	public void windowActivated(WindowEvent arg0)
	{}

	@Override
	public void windowClosed(WindowEvent arg0)
	{
		if (!CommandGenerator.isReloading()) CommandGenerator.exit();
	}

	@Override
	public void windowClosing(WindowEvent arg0)
	{
		if (!CommandGenerator.isReloading()) CommandGenerator.exit();
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
