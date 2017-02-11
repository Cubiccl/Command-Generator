package fr.cubiccl.generator.gui.component.panel.utils;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.Box;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import fr.cubi.cubigui.CList;
import fr.cubi.cubigui.CTextArea;
import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gui.component.CGList;
import fr.cubiccl.generator.gui.component.CScrollPane;
import fr.cubiccl.generator.gui.component.button.CGButton;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.CGTabbedPane;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Settings;
import fr.cubiccl.generator.utils.Text;

public class PanelCommandHistory extends ConfirmPanel implements ListSelectionListener, ChangeListener
{
	private static final long serialVersionUID = 6740618472685947499L;

	private CTextArea area;
	private CGButton buttonCopy, buttonLoad, buttonSave;
	private String[] history, saved = new String[]
	{ "jkl", "/jiojio j" };
	private CGList listCommandHistory, listCommandSaved;
	private CGTabbedPane tabbedpane;

	public PanelCommandHistory()
	{
		super(new Text("command.history"), null, false);
		this.history = CommandGenerator.commandHistory();
		if (Boolean.parseBoolean(Settings.getSetting(Settings.SLASH))) for (int i = 0; i < history.length; ++i)
			this.history[i] = "/" + this.history[i];

		CScrollPane scrollpane = new CScrollPane(this.area = new CTextArea(""));
		CGPanel p = new CGPanel();
		GridBagConstraints gbc = p.createGridBagLayout();
		gbc.gridheight = 5;
		p.add(this.tabbedpane = new CGTabbedPane(), gbc);
		++gbc.gridx;
		gbc.gridheight = 1;
		p.add(Box.createRigidArea(new Dimension(5, 10)), gbc);
		++gbc.gridy;
		p.add(scrollpane, gbc);
		++gbc.gridy;
		p.add(this.buttonCopy = new CGButton("command.copy"), gbc);
		++gbc.gridy;
		p.add(this.buttonLoad = new CGButton("command.load"), gbc);
		++gbc.gridy;
		p.add(this.buttonSave = new CGButton("command.save"), gbc);

		this.tabbedpane.addTab("command.history", (this.listCommandHistory = new CGList(this.history)).scrollPane);
		this.tabbedpane.addTab("command.saved", (this.listCommandSaved = new CGList(this.saved)).scrollPane);
		this.tabbedpane.addChangeListener(this);

		this.setMainComponent(p);

		this.area.setEditable(true);
		this.area.setLineWrap(true);
		this.area.setWrapStyleWord(true);
		this.buttonCopy.addActionListener(this);
		this.buttonLoad.addActionListener(this);
		this.buttonSave.addActionListener(this);
		this.buttonCopy.setEnabled(false);
		this.buttonLoad.setEnabled(false);
		this.buttonSave.setEnabled(false);
		this.buttonCancel.setText(new Text("general.back"));
		this.listCommandHistory.addListSelectionListener(this);
		this.listCommandSaved.addListSelectionListener(this);
		this.addComponentListener(new ComponentListener()
		{

			@Override
			public void componentHidden(ComponentEvent e)
			{}

			@Override
			public void componentMoved(ComponentEvent e)
			{}

			@Override
			public void componentResized(ComponentEvent e)
			{
				int width = getWidth() / 3, height = getHeight();
				tabbedpane.setPreferredSize(new Dimension(width, height / 2));
				scrollpane.setPreferredSize(new Dimension(width, height / 2 - buttonCopy.getHeight() - buttonLoad.getHeight() - buttonSave.getHeight() - 50));
			}

			@Override
			public void componentShown(ComponentEvent e)
			{}
		});
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		super.actionPerformed(e);
		if (e.getSource() == this.buttonCopy) Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(this.area.getText()), null);
		if (e.getSource() == this.buttonLoad)
		{
			try
			{
				CommandGenerator.doLoad(this.history[this.listCommandHistory.getSelectedIndex()]);
			} catch (CommandGenerationException e1)
			{
				CommandGenerator.report(new CommandGenerationException(new Text(
						"The Generator failed to load a Command it generated! LOL This is weird. Can you report this bug please?")));
			}
		}
		if (e.getSource() == this.buttonSave)
		{
			if (this.tabbedpane.getSelectedIndex() == 1) this.forget(this.selectedList().getSelectedIndex());
			else this.save(this.selectedList().getSelectedIndex());
		}
		if (e.getSource() == this.buttonCancel || e.getSource() == this.buttonLoad) CommandGenerator.window.menubar.toggleMenu(true);
	}

	private void forget(int selectedIndex)
	{
		// TODO forget command
		this.updateListSaved();
	}

	private void save(int selectedIndex)
	{
		// TODO save command
		this.updateListSaved();
	}

	private CList selectedList()
	{
		return this.tabbedpane.getSelectedIndex() == 0 ? this.listCommandHistory : this.listCommandSaved;
	}

	@Override
	public void stateChanged(ChangeEvent e)
	{
		this.update();
	}

	private void update()
	{
		boolean enabled = this.selectedList().getSelectedIndex() != -1;
		boolean saved = this.tabbedpane.getSelectedIndex() == 1;
		String selected = enabled ? saved ? this.saved[this.selectedList().getSelectedIndex()] : this.history[this.selectedList().getSelectedIndex()] : null;
		this.buttonCopy.setEnabled(enabled);
		this.buttonLoad.setEnabled(enabled);
		if (!enabled) this.area.setText("");
		else this.area.setText(selected);

		this.buttonSave.setText(saved ? new Text("general.remove") : new Text("command.save"));
		if (!saved && enabled)
		{
			this.buttonSave.setEnabled(false);
			for (String command : this.saved)
				if (command.equals(selected)) return;
			this.buttonSave.setEnabled(true);
		} else this.buttonSave.setEnabled(enabled);
	}

	private void updateListSaved()
	{
		this.listCommandSaved.setValues(this.saved);
	}

	@Override
	public void valueChanged(ListSelectionEvent e)
	{
		this.update();
	}

}
