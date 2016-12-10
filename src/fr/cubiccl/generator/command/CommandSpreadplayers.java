package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;

import fr.cubiccl.generator.gui.component.button.CGCheckBox;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelTarget;
import fr.cubiccl.generator.gui.component.textfield.CGEntry;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;
import fr.cubiccl.generator.utils.WrongValueException;

public class CommandSpreadplayers extends Command
{
	private CGCheckBox checkboxTeams, checkboxX, checkboxZ;
	private CGEntry entryX, entryZ, entryDistance, entryRange;
	private PanelTarget panelTarget;

	public CommandSpreadplayers()
	{
		super("spreadplayers");
	}

	@Override
	public CGPanel createGUI()
	{
		CGPanel panel = new CGPanel();
		GridBagConstraints gbc = panel.createGridBagLayout();

		++gbc.gridwidth;
		panel.add(this.labelDescription(), gbc);
		++gbc.gridy;
		panel.add(this.panelTarget = new PanelTarget(PanelTarget.ALL_ENTITIES), gbc);
		++gbc.gridy;
		--gbc.gridwidth;
		panel.add((this.entryX = new CGEntry("spread.x", "0")).container, gbc);
		++gbc.gridx;
		panel.add(this.checkboxX = new CGCheckBox("coordinate.relative"), gbc);
		--gbc.gridx;
		++gbc.gridy;
		panel.add((this.entryZ = new CGEntry("spread.z", "0")).container, gbc);
		++gbc.gridx;
		panel.add(this.checkboxZ = new CGCheckBox("coordinate.relative"), gbc);
		--gbc.gridx;
		++gbc.gridy;
		++gbc.gridwidth;
		panel.add((this.entryDistance = new CGEntry("spread.distance", "0")).container, gbc);
		++gbc.gridy;
		panel.add((this.entryRange = new CGEntry("spread.range", "1")).container, gbc);
		++gbc.gridy;
		panel.add(this.checkboxTeams = new CGCheckBox("spread.teams"), gbc);

		this.entryX.addIntFilter();
		this.entryZ.addIntFilter();
		this.entryDistance.addIntFilter();
		this.entryRange.addIntFilter();

		return panel;
	}

	@Override
	public String generate() throws CommandGenerationException
	{
		String x = this.entryX.getText(), z = this.entryZ.getText(), d = this.entryDistance.getText(), r = this.entryRange.getText();
		float distance = 0;

		try
		{
			Float.parseFloat(x);
		} catch (NumberFormatException e)
		{
			throw new WrongValueException(this.entryX.label.getAbsoluteText(), new Text("error.number"), x);
		}

		try
		{
			Float.parseFloat(z);
		} catch (NumberFormatException e)
		{
			throw new WrongValueException(this.entryZ.label.getAbsoluteText(), new Text("error.number"), z);
		}

		try
		{
			distance = Float.parseFloat(d);
			if (distance < 0) throw new WrongValueException(this.entryDistance.label.getAbsoluteText(), new Text("error.number.positive"), d);
		} catch (NumberFormatException e)
		{
			throw new WrongValueException(this.entryDistance.label.getAbsoluteText(), new Text("error.number.positive"), d);
		}

		try
		{
			float f = Float.parseFloat(r);
			if (f < distance + 1) throw new WrongValueException(this.entryRange.label.getAbsoluteText(), new Text("error.spreadplayers"), r);
		} catch (NumberFormatException e)
		{
			throw new WrongValueException(this.entryRange.label.getAbsoluteText(), new Text("error.number.positive"), r);
		}

		return "/spreadplayers " + (this.checkboxX.isSelected() ? "~" : "") + x + " " + (this.checkboxZ.isSelected() ? "~" : "") + z + " " + d + " " + r + " "
				+ this.checkboxTeams.isSelected() + " " + this.panelTarget.generateTarget().toCommand();
	}

}
