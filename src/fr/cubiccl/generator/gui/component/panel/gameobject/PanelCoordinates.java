package fr.cubiccl.generator.gui.component.panel.gameobject;

import java.awt.GridBagConstraints;

import fr.cubiccl.generator.gameobject.Coordinates;
import fr.cubiccl.generator.gui.component.button.CGCheckBox;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.textfield.CGEntry;
import fr.cubiccl.generator.utils.Text;
import fr.cubiccl.generator.utils.WrongValueException;

public class PanelCoordinates extends CGPanel
{
	private static final long serialVersionUID = -6721007750575550659L;

	private boolean canBeRelative;
	private CGCheckBox checkboxX, checkboxY, checkboxZ;
	private CGEntry entryX, entryY, entryZ;

	public PanelCoordinates(String titleID)
	{
		this(titleID, true);
	}

	public PanelCoordinates(String titleID, boolean canBeRelative)
	{
		super(titleID);
		this.canBeRelative = canBeRelative;

		GridBagConstraints gbc = this.createGridBagLayout();
		this.add((this.entryX = new CGEntry("coordinate.x", "0")).container, gbc);
		++gbc.gridy;
		this.add((this.entryY = new CGEntry("coordinate.y", "0")).container, gbc);
		++gbc.gridy;
		this.add((this.entryZ = new CGEntry("coordinate.z", "0")).container, gbc);

		++gbc.gridx;
		gbc.gridy = 0;
		this.add(this.checkboxX = new CGCheckBox("coordinate.relative"), gbc);
		++gbc.gridy;
		this.add(this.checkboxY = new CGCheckBox("coordinate.relative"), gbc);
		++gbc.gridy;
		this.add(this.checkboxZ = new CGCheckBox("coordinate.relative"), gbc);

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
			throw new WrongValueException(this.entryX.label.getAbsoluteText(), new Text("error.number"), this.entryX.getText());
		}
		try
		{
			y = Float.parseFloat(this.entryY.getText());
		} catch (NumberFormatException e)
		{
			throw new WrongValueException(this.entryY.label.getAbsoluteText(), new Text("error.number"), this.entryY.getText());
		}
		try
		{
			z = Float.parseFloat(this.entryZ.getText());
		} catch (NumberFormatException e)
		{
			throw new WrongValueException(this.entryZ.label.getAbsoluteText(), new Text("error.number"), this.entryZ.getText());
		}

		return new Coordinates(x, y, z, this.checkboxX.isSelected(), this.checkboxY.isSelected(), this.checkboxZ.isSelected());
	}

	/** Changes the text of the "relative" checkboxes. */
	public void setRelativeText(Text text)
	{
		this.checkboxX.setTextID(text);
		this.checkboxY.setTextID(text);
		this.checkboxZ.setTextID(text);
	}

	public void setupFrom(Coordinates coordinates)
	{
		this.entryX.setText(Float.toString(coordinates.x));
		this.entryY.setText(Float.toString(coordinates.y));
		this.entryZ.setText(Float.toString(coordinates.z));

		this.checkboxX.setSelected(coordinates.xRelative);
		this.checkboxY.setSelected(coordinates.yRelative);
		this.checkboxZ.setSelected(coordinates.zRelative);
	}

}
