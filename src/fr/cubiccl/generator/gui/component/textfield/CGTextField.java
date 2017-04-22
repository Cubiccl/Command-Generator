package fr.cubiccl.generator.gui.component.textfield;

import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;

import fr.cubi.cubigui.CTextField;
import fr.cubiccl.generator.gui.component.interfaces.ITranslated;
import fr.cubiccl.generator.utils.Text;

public class CGTextField extends CTextField implements ITranslated
{

	private static final long serialVersionUID = -6329458551850787942L;

	private Text suggestedText;

	public CGTextField()
	{
		this((String) null);
	}

	public CGTextField(String suggestedTextID)
	{
		this(suggestedTextID == null ? null : new Text(suggestedTextID));
	}

	public CGTextField(Text suggestedText)
	{
		super();
		this.suggestedText = suggestedText;
		this.updateTranslations();
	}

	public void addFilter(DocumentFilter filter)
	{
		((PlainDocument) this.getDocument()).setDocumentFilter(filter);
	}

	public void addIntFilter()
	{
		this.addFilter(new AbstractFilter()
		{

			@Override
			protected boolean isStringValid(String string)
			{
				if (string.equals("")) return true;
				try
				{
					Integer.parseInt(string);
					return true;
				} catch (Exception e)
				{
					return false;
				}
			}
		});
	}

	public void addNumberFilter()
	{
		this.addFilter(new AbstractFilter()
		{

			@Override
			protected boolean isStringValid(String string)
			{
				if (string.equals("")) return true;
				try
				{
					Float.parseFloat(string);
					return true;
				} catch (Exception e)
				{
					return false;
				}
			}
		});
	}

	public void addStringIdFilter()
	{
		this.addFilter(new AbstractFilter()
		{

			@Override
			protected boolean isStringValid(String string)
			{
				return !string.contains(" ");
			}
		});
	}

	public void removeFilter()
	{
		this.addFilter(null);
	}

	@Override
	public void updateTranslations()
	{
		if (this.suggestedText != null) this.setSuggestedText(this.suggestedText.toString() + "...");
	}

}
