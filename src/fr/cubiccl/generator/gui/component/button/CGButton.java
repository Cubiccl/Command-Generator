package fr.cubiccl.generator.gui.component.button;

import fr.cubi.cubigui.CButton;
import fr.cubiccl.generator.gui.component.interfaces.ITranslated;
import fr.cubiccl.generator.utils.Text;

public class CGButton extends CButton implements ITranslated
{
	private static final long serialVersionUID = -4425707045807350000L;

	private Text text;

	public CGButton(String textID)
	{
		this(textID.equals("") ? null : new Text(textID));
	}

	public CGButton(Text text)
	{
		super(text == null ? "" : text.toString());
		this.text = text;
	}

	@Override
	public void updateTranslations()
	{
		if (this.text == null) return;
		this.setText(this.text.toString());
	}

}
