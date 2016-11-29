package fr.cubiccl.generator.gui.component.label;

import fr.cubi.cubigui.CLabel;
import fr.cubiccl.generator.gui.component.interfaces.ITranslated;
import fr.cubiccl.generator.utils.Text;

public class CGLabel extends CLabel implements ITranslated
{
	private static final long serialVersionUID = 5974963322978871539L;

	private boolean hasColumn;
	private Text text;

	public CGLabel(String textID)
	{
		this(textID.equals("") ? null : new Text(textID));
	}

	public CGLabel(Text text)
	{
		super(text == null ? "" : text.toString());
		this.text = text;
		this.hasColumn = false;
	}

	/** @return The text without the Column */
	public Text getAbsoluteText()
	{
		return text;
	}

	public boolean hasColumn()
	{
		return this.hasColumn;
	}

	public CGLabel setHasColumn(boolean hasColumn)
	{
		this.hasColumn = hasColumn;
		this.updateTranslations();
		return this;
	}

	@Override
	public void setText(String text)
	{
		this.setTextID(null);
		super.setText(text);
	}

	public void setTextID(Text text)
	{
		this.text = text;
		this.updateTranslations();
	}

	@Override
	public void updateTranslations()
	{
		if (this.text != null)
		{
			if (this.hasColumn) super.setText(this.text.toString() + " :");
			else super.setText(this.text.toString());
		}
	}

}
