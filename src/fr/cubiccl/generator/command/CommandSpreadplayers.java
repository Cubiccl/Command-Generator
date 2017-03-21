package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;

import fr.cubiccl.generator.gameobject.target.Target;
import fr.cubiccl.generator.gui.component.button.CGCheckBox;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.utils.PanelObjectList;
import fr.cubiccl.generator.gui.component.textfield.CGEntry;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

public class CommandSpreadplayers extends Command
{
	private CGCheckBox checkboxTeams, checkboxX, checkboxZ;
	private CGEntry entryX, entryZ, entryDistance, entryRange;
	private PanelObjectList<Target> list;

	public CommandSpreadplayers()
	{
		super("spreadplayers", "spreadplayers <x> <z> <spreadDistance> <maxRange> <respectTeams> <entities ...>", -7);
	}

	@Override
	public CGPanel createGUI()
	{
		CGPanel panel = new CGPanel();
		GridBagConstraints gbc = panel.createGridBagLayout();

		++gbc.gridwidth;
		panel.add(this.labelDescription(), gbc);
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
		++gbc.gridy;
		panel.add(this.list = new PanelObjectList<Target>("spread.targets", (String) null, Target.class), gbc);

		this.entryX.addIntFilter();
		this.entryZ.addIntFilter();
		this.entryDistance.addIntFilter();
		this.entryRange.addIntFilter();

		return panel;
	}

	@Override
	protected void defaultGui()
	{
		this.list.clear();
		this.checkboxX.setSelected(false);
		this.checkboxZ.setSelected(false);
	}

	@Override
	protected Text description()
	{
		return this.defaultDescription();
	}

	@Override
	public String generate() throws CommandGenerationException
	{
		String x = this.entryX.getText(), z = this.entryZ.getText(), d = this.entryDistance.getText(), r = this.entryRange.getText();
		this.entryX.checkValue(CGEntry.FLOAT);
		this.entryZ.checkValue(CGEntry.FLOAT);
		this.entryDistance.checkValueSuperior(CGEntry.FLOAT, 0);
		this.entryRange.checkValueSuperior(CGEntry.FLOAT, Float.parseFloat(this.entryDistance.getText()) + 1);

		String command = this.id + " " + (this.checkboxX.isSelected() ? "~" : "") + x + " " + (this.checkboxZ.isSelected() ? "~" : "") + z + " " + d + " " + r
				+ " " + this.checkboxTeams.isSelected();

		if (this.list.length() == 0) throw new CommandGenerationException(new Text("error.spread.no_players"));
		for (Target target : this.list.values())
			command += " " + target.toCommand();

		return command;
	}

	@Override
	protected void readArgument(int index, String argument, String[] fullCommand) throws CommandGenerationException
	{
		// spreadplayers <x> <z> <spreadDistance> <maxRange> <respectTeams> <player>
		if (index >= 1 && index <= 4) try
		{
			if ((index == 1 || index == 2) && argument.startsWith("~"))
			{
				argument = argument.substring(1);
				if (index == 1) this.checkboxX.setSelected(true);
				if (index == 2) this.checkboxZ.setSelected(true);
			}
			Float.parseFloat(argument);
			if (index == 1) this.entryX.setText(argument);
			if (index == 2) this.entryZ.setText(argument);
			if (index == 3) this.entryDistance.setText(argument);
			if (index == 4) this.entryRange.setText(argument);
		} catch (Exception e)
		{}
		if (index == 5) this.checkboxTeams.setSelected(argument.equals("true"));
		if (index == 6)
		{
			for (String s : argument.split(" "))
				this.list.add(Target.createFrom(s));
		}
	}
}
