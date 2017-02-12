package fr.cubiccl.generator.gui.component.panel.loottable;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import fr.cubi.cubigui.CTextArea;
import fr.cubi.cubigui.RoundedCornerBorder;
import fr.cubiccl.generator.gui.component.button.CGButton;
import fr.cubiccl.generator.gui.component.button.CGCheckBox;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.panel.CGPanel;

public class PanelLootTableOutput extends CGPanel implements ActionListener
{
	public static final int HEIGHT = 250;
	private static final long serialVersionUID = -7446762724786768990L;

	public final CTextArea areaOutput;
	private CGButton buttonCopy;
	private CGCheckBox checkboxEdit;

	public PanelLootTableOutput()
	{
		GridBagConstraints gbc = this.createGridBagLayout();
		this.add(new CGLabel("loottable.output"), gbc);
		++gbc.gridy;
		this.add(this.checkboxEdit = new CGCheckBox("loottable.edit"), gbc);
		++gbc.gridy;
		this.add(this.buttonCopy = new CGButton("command.copy"), gbc);
		++gbc.gridx;
		gbc.gridy = 0;
		gbc.gridheight = 3;
		this.add(this.areaOutput = new CTextArea(""), gbc);

		this.areaOutput.setPreferredSize(new Dimension(180, 180));
		this.areaOutput.setBorder(new RoundedCornerBorder(true));
		this.checkboxEdit.addActionListener(this);
		this.buttonCopy.addActionListener(this);
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
				areaOutput.setPreferredSize(new Dimension(getWidth() - Math.max(buttonCopy.getWidth() + 20, checkboxEdit.getWidth() + 20) - 10, HEIGHT - 20));
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
		}
		if (e.getSource() == this.buttonCopy) Toolkit.getDefaultToolkit().getSystemClipboard()
				.setContents(new StringSelection(this.areaOutput.getText()), null);
	}

}
