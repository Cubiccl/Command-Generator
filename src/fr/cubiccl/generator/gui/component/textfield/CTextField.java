package fr.cubiccl.generator.gui.component.textfield;

import javax.swing.JTextField;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;

import fr.cubiccl.generator.gui.RoundedCornerBorder;

public class CTextField extends JTextField
{

	private static final long serialVersionUID = -6329458551850787942L;

	public CTextField()
	{
		super(20);
		this.setBorder(new RoundedCornerBorder());
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

	public void removeFilter()
	{
		this.addFilter(null);
	}

}
