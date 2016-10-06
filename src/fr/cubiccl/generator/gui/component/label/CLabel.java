package fr.cubiccl.generator.gui.component.label;

import javax.swing.JLabel;

import fr.cubiccl.generator.gui.DisplayUtils;
import fr.cubiccl.generator.gui.component.interfaces.ITranslated;
import fr.cubiccl.generator.utils.Lang;

public class CLabel extends JLabel implements ITranslated
{
	private static final long serialVersionUID = 5974963322978871539L;

	private boolean hasColumn;
	private String textID;

	public CLabel(String textID)
	{
		this.textID = textID;
		this.hasColumn = false;
		this.setFont(DisplayUtils.FONT);
	}

	/** @return The text without the Column */
	public String getAbsoluteText()
	{
		if (this.textID == null) return "";
		return Lang.translate(this.textID);
	}

	public boolean hasColumn()
	{
		return this.hasColumn;
	}

	public CLabel setHasColumn(boolean hasColumn)
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

	public void setTextID(String textID)
	{
		this.textID = textID;
		this.updateTranslations();
	}

	@Override
	public void updateTranslations()
	{
		if (this.textID != null)
		{
			if (this.hasColumn) super.setText(Lang.translate(this.textID) + " :");
			else super.setText(Lang.translate(this.textID));
		}
	}

}
