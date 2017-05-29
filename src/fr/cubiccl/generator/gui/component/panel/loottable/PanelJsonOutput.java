package fr.cubiccl.generator.gui.component.panel.loottable;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;

import javax.swing.JFileChooser;

import fr.cubi.cubigui.CTextArea;
import fr.cubi.cubigui.RoundedCornerBorder;
import fr.cubiccl.generator.gui.Dialogs;
import fr.cubiccl.generator.gui.component.CScrollPane;
import fr.cubiccl.generator.gui.component.button.CGButton;
import fr.cubiccl.generator.gui.component.button.CGCheckBox;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.utils.FileUtils;
import fr.cubiccl.generator.utils.Settings;
import fr.cubiccl.generator.utils.Text;

public class PanelJsonOutput extends CGPanel implements ActionListener
{
	public static final int HEIGHT = 250;
	private static final long serialVersionUID = -7446762724786768990L;

	public final CTextArea areaOutput;
	private CGButton buttonCopy, buttonExport;
	private CGCheckBox checkboxEdit;
	private CScrollPane scrollpane;

	public PanelJsonOutput()
	{
		GridBagConstraints gbc = this.createGridBagLayout();
		this.add(new CGLabel("loottable.output"), gbc);
		++gbc.gridy;
		this.add(this.checkboxEdit = new CGCheckBox("loottable.edit"), gbc);
		++gbc.gridy;
		this.add(this.buttonCopy = new CGButton("command.copy"), gbc);
		++gbc.gridy;
		this.add(this.buttonExport = new CGButton("command.export"), gbc);
		++gbc.gridx;
		gbc.gridy = 0;
		gbc.gridheight = 4;
		this.add(this.scrollpane = new CScrollPane(this.areaOutput = new CTextArea("")), gbc);

		this.areaOutput.setLineWrap(true);
		this.areaOutput.setBorder(new RoundedCornerBorder(true));
		this.scrollpane.setPreferredSize(new Dimension(180, 180));
		this.checkboxEdit.addActionListener(this);
		this.buttonCopy.addActionListener(this);
		this.buttonExport.addActionListener(this);
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
				scrollpane.setPreferredSize(new Dimension(getWidth() - Math.max(buttonCopy.getWidth() + 20, checkboxEdit.getWidth() + 20) - 10, HEIGHT - 20));
				revalidate();
			}

			@Override
			public void componentShown(ComponentEvent e)
			{}
		});
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.checkboxEdit)
		{
			this.areaOutput.setEditable(this.checkboxEdit.isSelected());
			this.areaOutput.setBorder(new RoundedCornerBorder(true));
		} else if (e.getSource() == this.buttonCopy && !this.areaOutput.getText().equals("")) Toolkit.getDefaultToolkit().getSystemClipboard()
				.setContents(new StringSelection(this.areaOutput.getText()), null);
		else if (e.getSource() == this.buttonExport && !this.areaOutput.getText().equals(""))
		{
			JFileChooser fileChooser = new JFileChooser(Settings.getSetting(Settings.LAST_FOLDER));
			int result = fileChooser.showSaveDialog(this);
			if (result == JFileChooser.APPROVE_OPTION)
			{
				File f = fileChooser.getSelectedFile();
				Settings.setSetting(Settings.LAST_FOLDER, f.getParentFile().getPath());
				FileUtils.writeToFile(this.areaOutput.getText(), f);
				Dialogs.showMessage(new Text("command.export.success").addReplacement("<file>", fileChooser.getSelectedFile().getName()).toString());
			}
		}
	}

}
