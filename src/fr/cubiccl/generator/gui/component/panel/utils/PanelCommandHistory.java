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
import fr.cubiccl.generator.utils.Text;

public class PanelCommandHistory extends ConfirmPanel implements ListSelectionListener
{
	private static final long serialVersionUID = 6740618472685947499L;

	private CTextArea area;
	private CGButton buttonCopy;
	private String[] history;
	private CGList listCommand;

	public PanelCommandHistory()
	{
		super(new Text("command.history"), null, false);
		this.history = CommandGenerator.commandHistory();

		CScrollPane scrollpane = new CScrollPane(this.area = new CTextArea(""));
		CGPanel p = new CGPanel();
		GridBagConstraints gbc = p.createGridBagLayout();
		gbc.gridheight = 2;
		p.add((this.listCommand = new CGList(this.history)).scrollPane, gbc);
		++gbc.gridx;
		gbc.gridheight = 1;
		p.add(scrollpane, gbc);
		++gbc.gridy;
		p.add(this.buttonCopy = new CGButton("command.copy"), gbc);

		this.setMainComponent(p);

		this.area.setEditable(true);
		this.area.setLineWrap(true);
		this.area.setWrapStyleWord(true);
		this.buttonCopy.addActionListener(this);
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
				scrollpane.setPreferredSize(new Dimension(width, width / 2 - buttonCopy.getHeight() - 10));
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
		if (e.getSource() == this.buttonCancel) CommandGenerator.window.menubar.history.setEnabled(true);
	}

	@Override
	public void valueChanged(ListSelectionEvent e)
	{
		if (this.listCommand.getSelectedIndex() == -1) this.area.setText("");
		else this.area.setText(this.history[this.listCommand.getSelectedIndex()]);
	}

}
