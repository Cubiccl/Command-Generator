package fr.cubiccl.generator.gui.component.textfield;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import fr.cubi.cubigui.CPanel;
import fr.cubi.cubigui.RoundedCornerBorder;
import fr.cubiccl.generator.gui.component.label.CGLabel;
import fr.cubiccl.generator.gui.component.label.HelpLabel;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;
import fr.cubiccl.generator.utils.Utils;

public class CGEntry extends CGTextField
{
	private static final long serialVersionUID = 1L;

	public static final byte STRING = 0, INTEGER = 1, FLOAT = 2, NUMBER = 3;

	public final CPanel container;
	public final CGLabel label;
	private boolean warning = false;

	public CGEntry(String textID)
	{
		this(textID == null ? null : new Text(textID), null);
	}

	public CGEntry(Text text, String defaultValue, Text suggestedText)
	{
		super(suggestedText);
		this.setText(defaultValue);
		this.label = new CGLabel(text).setHasColumn(true);

		this.container = new CPanel();
		GridBagConstraints gbc = this.container.createGridBagLayout();
		this.container.add(this.label, gbc);
		++gbc.gridx;
		this.container.add(this, gbc);
		++gbc.gridx;

		this.addFocusListener(new FocusListener()
		{

			@Override
			public void focusGained(FocusEvent e)
			{}

			@Override
			public void focusLost(FocusEvent e)
			{
				setWarning(false);
			}
		});
	}

	public CGEntry(Text text, Text suggestedText)
	{
		this(text, "", suggestedText);
	}

	public void addHelpLabel(HelpLabel label)
	{
		this.container.add(label, this.container.getGBC());
	}

	/** @param checkMode - What format to check. See {@link CGEntry#STRING_ID}
	 * @throws CommandGenerationException If the value is invalid. */
	public void checkValue(byte checkMode) throws CommandGenerationException
	{
		try
		{
			if (checkMode == STRING) Utils.checkStringId(this.label.getAbsoluteText(), this.getText());
			else if (checkMode == INTEGER) Utils.checkInteger(this.label.getAbsoluteText(), this.getText());
			else if (checkMode == FLOAT) Utils.checkFloat(this.label.getAbsoluteText(), this.getText());
			else if (checkMode == NUMBER) Utils.checkNumber(this.label.getAbsoluteText(), this.getText());
		} catch (CommandGenerationException e)
		{
			this.setWarning(true);
			throw e;
		}
	}

	/** @param checkMode - What format to check. See {@link CGEntry#STRING_ID}
	 * @throws CommandGenerationException If the value is invalid. */
	public void checkValueInBounds(byte checkMode, double min, double max) throws CommandGenerationException
	{
		try
		{
			if (checkMode == INTEGER) Utils.checkIntegerInBounds(this.label.getAbsoluteText(), this.getText(), (int) min, (int) max);
			else if (checkMode == FLOAT) Utils.checkFloatInBounds(this.label.getAbsoluteText(), this.getText(), (int) min, (int) max);
			else if (checkMode == NUMBER) Utils.checkNumberInBounds(this.label.getAbsoluteText(), this.getText(), (int) min, (int) max);
		} catch (CommandGenerationException e)
		{
			this.setWarning(true);
			throw e;
		}
	}

	/** @param checkMode - What format to check. See {@link CGEntry#STRING_ID}
	 * @throws CommandGenerationException If the value is invalid. */
	public void checkValueSuperior(byte checkMode, double min) throws CommandGenerationException
	{
		try
		{
			if (checkMode == INTEGER) Utils.checkIntegerSuperior(this.label.getAbsoluteText(), this.getText(), (int) min);
			else if (checkMode == FLOAT) Utils.checkFloatSuperior(this.label.getAbsoluteText(), this.getText(), (int) min);
			else if (checkMode == NUMBER) Utils.checkNumberSuperior(this.label.getAbsoluteText(), this.getText(), (int) min);
		} catch (CommandGenerationException e)
		{
			this.setWarning(true);
			throw e;
		}
	}

	public void setWarning(boolean warning)
	{
		this.warning = warning;
		if (this.warning) ((RoundedCornerBorder) this.getBorder()).setColor(Color.RED);
		else ((RoundedCornerBorder) this.getBorder()).setColor(null);
		this.repaint();
	}

	@Override
	public void updateTranslations()
	{
		super.updateTranslations();
		if (this.label != null) this.label.updateTranslations();
	}

}
