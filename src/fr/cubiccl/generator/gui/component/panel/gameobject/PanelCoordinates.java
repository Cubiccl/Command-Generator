package fr.cubiccl.generator.gui.component.panel.gameobject;

import java.awt.GridBagConstraints;

import fr.cubiccl.generator.gameobject.Coordinates;
import fr.cubiccl.generator.gui.component.button.CCheckBox;
import fr.cubiccl.generator.gui.component.panel.CPanel;
import fr.cubiccl.generator.gui.component.textfield.CEntry;
import fr.cubiccl.generator.utils.Lang;
import fr.cubiccl.generator.utils.WrongValueException;

public class PanelCoordinates extends CPanel
{
	private static final long serialVersionUID = -6721007750575550659L;

	private boolean canBeRelative;
	private CCheckBox checkboxX, checkboxY, checkboxZ;
	private CEntry entryX, entryY, entryZ;

	public PanelCoordinates(String titleID)
	{
		this(titleID, true);
	}

	public PanelCoordinates(String titleID, boolean canBeRelative)
	{
		super(titleID);
		this.canBeRelative = canBeRelative;

		GridBagConstraints gbc = this.createGridBagLayout();
		this.add((this.entryX = new CEntry("coordinate.x", "0")).container, gbc);
		++gbc.gridy;
		this.add((this.entryY = new CEntry("coordinate.y", "0")).container, gbc);
		++gbc.gridy;
		this.add((this.entryZ = new CEntry("coordinate.z", "0")).container, gbc);

		++gbc.gridx;
		gbc.gridy = 0;
		this.add(this.checkboxX = new CCheckBox("coordinate.relative"), gbc);
		++gbc.gridy;
		this.add(this.checkboxY = new CCheckBox("coordinate.relative"), gbc);
		++gbc.gridy;
		this.add(this.checkboxZ = new CCheckBox("coordinate.relative"), gbc);

		this.entryX.addNumberFilter();
		this.entryY.addNumberFilter();
		this.entryZ.addNumberFilter();
		this.checkboxX.setSelected(true);
		this.checkboxY.setSelected(true);
		this.checkboxZ.setSelected(true);

		if (!this.canBeRelative)
		{
			this.checkboxX.setVisible(false);
			this.checkboxY.setVisible(false);
			this.checkboxZ.setVisible(false);
		}
	}

	public Coordinates generateCoordinates() throws WrongValueException
	{
		float x = 0, y = 0, z = 0;

		try
		{
			x = Float.parseFloat(this.entryX.getText());
		} catch (NumberFormatException e)
		{
			throw new WrongValueException(this.entryX.label.getAbsoluteText(), Lang.translate("error.number"), this.entryX.getText());
		}
		try
		{
			y = Float.parseFloat(this.entryY.getText());
		} catch (NumberFormatException e)
		{
			throw new WrongValueException(this.entryY.label.getAbsoluteText(), Lang.translate("error.number"), this.entryY.getText());
		}
		try
		{
			z = Float.parseFloat(this.entryZ.getText());
		} catch (NumberFormatException e)
		{
			throw new WrongValueException(this.entryZ.label.getAbsoluteText(), Lang.translate("error.number"), this.entryZ.getText());
		}

		return new Coordinates(x, y, z, this.checkboxX.isSelected(), this.checkboxY.isSelected(), this.checkboxZ.isSelected());
	}

	/** Changes the text ID of the "relative" checkboxes. */
	public void setRelativeText(String textID)
	{
		this.checkboxX.setTextID(textID);
		this.checkboxY.setTextID(textID);
		this.checkboxZ.setTextID(textID);
	}

}
