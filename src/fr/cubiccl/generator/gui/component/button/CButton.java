package fr.cubiccl.generator.gui.component.button;

import java.awt.Dimension;

import javax.swing.JButton;

import fr.cubiccl.generator.gui.DisplayUtils;
import fr.cubiccl.generator.gui.RoundedCornerBorder;
import fr.cubiccl.generator.gui.component.interfaces.ITranslated;
import fr.cubiccl.generator.gui.component.ui.CButtonUI;
import fr.cubiccl.generator.utils.Lang;

public class CButton extends JButton implements ITranslated
{
	private static final long serialVersionUID = -4425707045807350000L;

	private String textID;

	public CButton(String textID)
	{
		super(textID);
		this.setUI(new CButtonUI());
		this.setBorder(new RoundedCornerBorder());
		this.textID = textID;
		this.setFont(DisplayUtils.FONT);
	}

	@Override
	public void updateTranslations()
	{
		if (this.textID == null) return;
		this.setText(Lang.translate(this.textID));
		int textWidth = DisplayUtils.textWidth(this.getText());
		this.setPreferredSize(new Dimension(Math.max(140, textWidth), 27));
	}

}
