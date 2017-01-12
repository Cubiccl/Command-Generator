package fr.cubiccl.generator.gui.component.panel.utils;

import java.awt.Font;
import java.awt.GridBagConstraints;

import fr.cubi.cubigui.CPanel;
import fr.cubi.cubigui.CTextArea;
import fr.cubiccl.generator.gui.component.textfield.CGTextField;
import fr.cubiccl.generator.utils.Text;

public class EntryPanel extends ConfirmPanel
{
	private static final long serialVersionUID = -3957744287487251022L;

	public final CGTextField entry;

	public EntryPanel(String descriptionTextID)
	{
		this(new Text(descriptionTextID));
	}

	public EntryPanel(Text descriptionText)
	{
		super();
		CTextArea area = new CTextArea(descriptionText.toString());
		area.setLineWrap(true);
		area.setWrapStyleWord(true);
		area.setFont(new Font(area.getFont().getFontName(), Font.BOLD, area.getFont().getSize()));

		CPanel p = new CPanel();
		GridBagConstraints gbc = p.createGridBagLayout();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		p.add(area, gbc);
		++gbc.gridy;
		p.add(this.entry = new CGTextField(), gbc);
		this.setMainComponent(p);
	}

}
