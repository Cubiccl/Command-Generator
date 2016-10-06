package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;

import fr.cubiccl.generator.gui.component.button.CCheckBox;
import fr.cubiccl.generator.gui.component.panel.CPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelTarget;
import fr.cubiccl.generator.gui.component.textfield.CEntry;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Lang;
import fr.cubiccl.generator.utils.WrongValueException;

public class CommandSpreadplayers extends Command
{
	private CCheckBox checkboxTeams, checkboxX, checkboxZ;
	private CEntry entryX, entryZ, entryDistance, entryRange;
	private PanelTarget panelTarget;

	public CommandSpreadplayers()
	{
		super("spreadplayers");
	}

	@Override
	public CPanel createGUI()
	{
		CPanel panel = new CPanel();
		GridBagConstraints gbc = panel.createGridBagLayout();

		++gbc.gridwidth;
		panel.add(this.labelDescription(), gbc);
		++gbc.gridy;
		panel.add(this.panelTarget = new PanelTarget(PanelTarget.ALL_ENTITIES), gbc);
		++gbc.gridy;
		panel.add((this.entryX = new CEntry("spread.x", "0")).container, gbc);
		++gbc.gridx;
		panel.add(this.checkboxX = new CCheckBox("coordinate.relative"), gbc);
		--gbc.gridx;
		++gbc.gridy;
		panel.add((this.entryZ = new CEntry("spread.z", "0")).container, gbc);
		++gbc.gridx;
		panel.add(this.checkboxZ = new CCheckBox("coordinate.relative"), gbc);
		--gbc.gridx;
		++gbc.gridy;
		--gbc.gridwidth;
		panel.add((this.entryDistance = new CEntry("spread.distance", "0")).container, gbc);
		++gbc.gridy;
		panel.add((this.entryRange = new CEntry("spread.range", "1")).container, gbc);
		++gbc.gridy;
		panel.add(this.checkboxTeams = new CCheckBox("spread.teams"), gbc);

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
			throw new WrongValueException(this.entryX.label.getAbsoluteText(), Lang.translate("error.number"), x);
		}

		try
		{
			Float.parseFloat(z);
		} catch (NumberFormatException e)
		{
			throw new WrongValueException(this.entryZ.label.getAbsoluteText(), Lang.translate("error.number"), z);
		}

		try
		{
			distance = Float.parseFloat(d);
			if (distance < 0) throw new WrongValueException(this.entryDistance.label.getAbsoluteText(), Lang.translate("error.number.positive"), d);
		} catch (NumberFormatException e)
		{
			throw new WrongValueException(this.entryDistance.label.getAbsoluteText(), Lang.translate("error.number.positive"), d);
		}

		try
		{
			float f = Float.parseFloat(r);
			if (f < distance + 1) throw new WrongValueException(this.entryRange.label.getAbsoluteText(), Lang.translate("error.spreadplayers"), r);
		} catch (NumberFormatException e)
		{
			throw new WrongValueException(this.entryRange.label.getAbsoluteText(), Lang.translate("error.number.positive"), r);
		}

		return "/spreadplayers " + (this.checkboxX.isSelected() ? "~" : "") + x + " " + (this.checkboxZ.isSelected() ? "~" : "") + z + " " + d + " " + r + " "
				+ this.checkboxTeams.isSelected() + " " + this.panelTarget.generateTarget().toCommand();
	}

}
