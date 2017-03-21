package fr.cubiccl.generator.gui.component.panel.gameobject;

import java.awt.GridBagConstraints;

import fr.cubiccl.generator.gameobject.Coordinates;
import fr.cubiccl.generator.gameobject.registries.ObjectSaver;
import fr.cubiccl.generator.gui.component.button.CGCheckBox;
import fr.cubiccl.generator.gui.component.interfaces.ICustomObject;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.utils.PanelCustomObject;
import fr.cubiccl.generator.gui.component.textfield.CGEntry;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

public class PanelCoordinates extends CGPanel implements ICustomObject<Coordinates>
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
		this(titleID, canBeRelative, true);
	}

	public PanelCoordinates(String titleID, boolean canBeRelative, boolean customObjects)
	{
		super(titleID);
		this.canBeRelative = canBeRelative;

		GridBagConstraints gbc = this.createGridBagLayout();
		this.add((this.entryX = new CGEntry(new Text("coordinate.x"), "0", Text.NUMBER)).container, gbc);
		++gbc.gridy;
		this.add((this.entryY = new CGEntry(new Text("coordinate.y"), "0", Text.NUMBER)).container, gbc);
		++gbc.gridy;
		this.add((this.entryZ = new CGEntry(new Text("coordinate.z"), "0", Text.NUMBER)).container, gbc);

		++gbc.gridx;
		gbc.gridy = 0;
		this.add(this.checkboxX = new CGCheckBox("coordinate.relative"), gbc);
		++gbc.gridy;
		this.add(this.checkboxY = new CGCheckBox("coordinate.relative"), gbc);
		++gbc.gridy;
		this.add(this.checkboxZ = new CGCheckBox("coordinate.relative"), gbc);
		--gbc.gridx;
		++gbc.gridy;
		++gbc.gridwidth;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.CENTER;
		if (customObjects) this.add(new PanelCustomObject<Coordinates, Coordinates>(this, ObjectSaver.coordinates), gbc);

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

	public String displayCoordinates()
	{
		try
		{
			return this.generate().toString();
		} catch (CommandGenerationException e)
		{}

		String coords = "X=";

		try
		{
			float f = Float.parseFloat(this.entryX.getText());
			coords += (this.checkboxX.isSelected() ? "~" : "") + (f == 0 && this.checkboxX.isSelected() ? "" : f);
		} catch (NumberFormatException e)
		{
			coords += "???";
		}

		coords += ", Y=";
		try
		{
			float f = Float.parseFloat(this.entryY.getText());
			coords += (this.checkboxY.isSelected() ? "~" : "") + (f == 0 && this.checkboxY.isSelected() ? "" : f);
		} catch (NumberFormatException e)
		{
			coords += "???";
		}

		coords += ", Z=";
		try
		{
			float f = Float.parseFloat(this.entryZ.getText());
			coords += (this.checkboxZ.isSelected() ? "~" : "") + (f == 0 && this.checkboxZ.isSelected() ? "" : f);
		} catch (NumberFormatException e)
		{
			coords += "???";
		}

		return coords;
	}

	@Override
	public Coordinates generate() throws CommandGenerationException
	{
		this.entryX.checkValue(CGEntry.FLOAT);
		this.entryY.checkValue(CGEntry.FLOAT);
		this.entryZ.checkValue(CGEntry.FLOAT);
		float x = Float.parseFloat(this.entryX.getText()), y = Float.parseFloat(this.entryY.getText()), z = Float.parseFloat(this.entryZ.getText());
		return new Coordinates(x, y, z, this.checkboxX.isSelected(), this.checkboxY.isSelected(), this.checkboxZ.isSelected());
	}

	/** Changes the text of the coordinate entries. */
	public void setEntryText(Text textX, Text textY, Text textZ)
	{
		this.entryX.label.setTextID(textX);
		this.entryY.label.setTextID(textY);
		this.entryZ.label.setTextID(textZ);
	}

	/** Changes the text of the "relative" checkboxes. */
	public void setRelativeText(Text text)
	{
		this.checkboxX.setTextID(text);
		this.checkboxY.setTextID(text);
		this.checkboxZ.setTextID(text);
	}

	@Override
	public void setupFrom(Coordinates coordinates)
	{
		this.entryX.setText(Float.toString(coordinates.x));
		this.entryY.setText(Float.toString(coordinates.y));
		this.entryZ.setText(Float.toString(coordinates.z));

		if (this.canBeRelative)
		{
			this.checkboxX.setSelected(coordinates.xRelative);
			this.checkboxY.setSelected(coordinates.yRelative);
			this.checkboxZ.setSelected(coordinates.zRelative);
		}
	}

}
