package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;

import fr.cubiccl.generator.gui.component.button.CGCheckBox;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelTarget;
import fr.cubiccl.generator.gui.component.textfield.CGEntry;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

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
		panel.add((this.entryX = new CGEntry(new Text("spread.x"), "0", Text.INTEGER)).container, gbc);
		++gbc.gridx;
		panel.add(this.checkboxX = new CGCheckBox("coordinate.relative"), gbc);
		--gbc.gridx;
		++gbc.gridy;
		panel.add((this.entryZ = new CGEntry(new Text("spread.z"), "0", Text.INTEGER)).container, gbc);
		++gbc.gridx;
		panel.add(this.checkboxZ = new CGCheckBox("coordinate.relative"), gbc);
		--gbc.gridx;
		++gbc.gridy;
		++gbc.gridwidth;
		panel.add((this.entryDistance = new CGEntry(new Text("spread.distance"), "0", Text.INTEGER)).container, gbc);
		++gbc.gridy;
		panel.add((this.entryRange = new CGEntry(new Text("spread.range"), "1", Text.INTEGER)).container, gbc);
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
		this.entryX.checkValue(CGEntry.FLOAT);
		this.entryZ.checkValue(CGEntry.FLOAT);
		this.entryDistance.checkValueSuperior(CGEntry.FLOAT, 0);
		this.entryRange.checkValueSuperior(CGEntry.FLOAT, Float.parseFloat(this.entryDistance.getText()) + 1);

		return "/spreadplayers " + (this.checkboxX.isSelected() ? "~" : "") + x + " " + (this.checkboxZ.isSelected() ? "~" : "") + z + " " + d + " " + r + " "
				+ this.checkboxTeams.isSelected() + " " + this.panelTarget.generateTarget().toCommand();
	}

}
