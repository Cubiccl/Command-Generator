package fr.cubiccl.generator.gui.component.button;

import fr.cubi.cubigui.CCheckBox;
import fr.cubiccl.generator.gui.component.interfaces.ITranslated;
import fr.cubiccl.generator.utils.Text;

public class CGCheckBox extends CCheckBox implements ITranslated
{
	private static final long serialVersionUID = 5104176511435068723L;

	private Text text;

	public CGCheckBox(String textID)
	{
		this(textID.equals("") ? null : new Text(textID));
	}

	public CGCheckBox(Text text)
	{
		super(text == null ? "" : text.toString());
		this.text = text;
		this.updateTranslations();
	}

	public void setTextID(Text text)
	{
		this.text = text;
		this.updateTranslations();
	}

	@Override
	public void updateTranslations()
	{
		if (this.text != null) this.setText(this.text.toString());
	}

}
