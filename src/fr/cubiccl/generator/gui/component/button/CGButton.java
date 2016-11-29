package fr.cubiccl.generator.gui.component.button;

import fr.cubi.cubigui.CButton;
import fr.cubiccl.generator.gui.component.interfaces.ITranslated;
import fr.cubiccl.generator.utils.Lang;

public class CGButton extends CButton implements ITranslated
{
	private static final long serialVersionUID = -4425707045807350000L;

	private String textID;

	public CGButton(String textID)
	{
		super(textID);
		this.textID = textID;
	}

	@Override
	public void updateTranslations()
	{
		if (this.textID == null) return;
		this.setText(Lang.translate(this.textID));
	}

}
