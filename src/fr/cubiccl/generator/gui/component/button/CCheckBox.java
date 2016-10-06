package fr.cubiccl.generator.gui.component.button;

import javax.swing.JCheckBox;

import fr.cubiccl.generator.gui.DisplayUtils;
import fr.cubiccl.generator.gui.RoundedCornerBorder;
import fr.cubiccl.generator.gui.component.interfaces.ITranslated;
import fr.cubiccl.generator.gui.component.ui.CCheckBoxUI;
import fr.cubiccl.generator.utils.Lang;

public class CCheckBox extends JCheckBox implements ITranslated
{
	private static final long serialVersionUID = 5104176511435068723L;

	private String textID;

	public CCheckBox(String textID)
	{
		super(textID);
		this.setUI(new CCheckBoxUI());
		this.setBorder(new RoundedCornerBorder());
		this.setBorderPainted(true);
		this.textID = textID;
		this.setFont(DisplayUtils.FONT);
		this.updateTranslations();
	}

	public void setTextID(String textID)
	{
		this.textID = textID;
		this.updateTranslations();
	}

	@Override
	public void updateTranslations()
	{
		this.setText(Lang.translate(this.textID));
	}

}
