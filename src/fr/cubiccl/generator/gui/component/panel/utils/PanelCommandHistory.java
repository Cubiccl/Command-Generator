package fr.cubiccl.generator.gui.component.panel.utils;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import fr.cubi.cubigui.CTextArea;
import fr.cubiccl.generator.CommandGenerator;
import fr.cubiccl.generator.gui.component.CGList;
import fr.cubiccl.generator.gui.component.CScrollPane;
import fr.cubiccl.generator.gui.component.button.CGButton;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Settings;
import fr.cubiccl.generator.utils.Text;

public class PanelCommandHistory extends ConfirmPanel implements ListSelectionListener
{
	private static final long serialVersionUID = 6740618472685947499L;

	private CTextArea area;
	private CGButton buttonCopy, buttonLoad;
	private String[] history;
	private CGList listCommand;

	public PanelCommandHistory()
	{
		super(new Text("command.history"), null, false);
		this.history = CommandGenerator.commandHistory();
		if (Boolean.parseBoolean(Settings.getSetting(Settings.SLASH))) for (int i = 0; i < history.length; ++i)
			this.history[i] = "/" + this.history[i];

		CScrollPane scrollpane = new CScrollPane(this.area = new CTextArea(""));
		CGPanel p = new CGPanel();
		GridBagConstraints gbc = p.createGridBagLayout();
		gbc.gridheight = 3;
		p.add((this.listCommand = new CGList(this.history)).scrollPane, gbc);
		++gbc.gridx;
		gbc.gridheight = 1;
		p.add(scrollpane, gbc);
		++gbc.gridy;
		p.add(this.buttonCopy = new CGButton("command.copy"), gbc);
		++gbc.gridy;
		p.add(this.buttonLoad = new CGButton("command.load"), gbc);

		this.setMainComponent(p);

		this.area.setEditable(true);
		this.area.setLineWrap(true);
		this.area.setWrapStyleWord(true);
		this.buttonCopy.addActionListener(this);
		this.buttonLoad.addActionListener(this);
		this.buttonCopy.setEnabled(false);
		this.buttonLoad.setEnabled(false);
		this.buttonCancel.setText(new Text("general.back"));
		this.listCommand.addListSelectionListener(this);
		this.listCommand.setSelectedIndex(0);
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
				int width = getWidth() / 3;
				listCommand.scrollPane.setPreferredSize(new Dimension(width, width / 2));
				scrollpane.setPreferredSize(new Dimension(width, width / 2 - buttonCopy.getHeight() - buttonLoad.getHeight() - 20));
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
				CommandGenerator.doLoad(this.history[this.listCommand.getSelectedIndex()]);
			} catch (CommandGenerationException e1)
			{
				CommandGenerator.report(new CommandGenerationException(new Text(
						"The Generator failed to load a Command it generated! LOL This is weird. Can you report this bug please?")));
			}
		}
		if (e.getSource() == this.buttonCancel || e.getSource() == this.buttonLoad) CommandGenerator.window.menubar.toggleMenu(true);
	}

	@Override
	public void valueChanged(ListSelectionEvent e)
	{
		boolean enabled = this.listCommand.getSelectedIndex() != -1;
		this.buttonCopy.setEnabled(enabled);
		this.buttonLoad.setEnabled(enabled);
		if (!enabled) this.area.setText("");
		else this.area.setText(this.history[this.listCommand.getSelectedIndex()]);
	}

}
