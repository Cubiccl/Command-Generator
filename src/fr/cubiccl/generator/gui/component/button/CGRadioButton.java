package fr.cubiccl.generator.gui.component.button;

import fr.cubi.cubigui.CRadioButton;
import fr.cubiccl.generator.gui.component.interfaces.ITranslated;
import fr.cubiccl.generator.utils.Text;

public class CGRadioButton extends CRadioButton implements ITranslated
{
	private static final long serialVersionUID = -4425707045807350000L;

	private Text text;

	public CGRadioButton(String textID)
	{
		this(textID == null || textID.equals("") ? null : new Text(textID));
	}

	public CGRadioButton(Text text)
	{
		super(text == null ? "" : text.toString());
		this.text = text;
	}

	public void setText(Text text)
	{
		this.text = text;
		this.updateTranslations();
	}

	@Override
	public void updateTranslations()
	{
		if (this.text == null) return;
		this.setText(this.text.toString());
	}

}
