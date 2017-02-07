package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import fr.cubiccl.generator.gameobject.target.Target;
import fr.cubiccl.generator.gui.component.combobox.OptionCombobox;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelTarget;
import fr.cubiccl.generator.gui.component.textfield.CGEntry;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;
import fr.cubiccl.generator.utils.Utils;

public class CommandScoreboardTeams extends Command implements ActionListener
{

	private OptionCombobox comboboxMode, comboboxOptionName, comboboxOptionValue;
	private CGEntry entryTeam, entryDisplayName;
	private PanelTarget panelTarget;

	public CommandScoreboardTeams()
	{
		super("scoreboard teams", "scoreboard teams add <name> [display name]\n" + "scoreboard teams <join|leave> <player>\n"
				+ "scoreboard teams <empty|remove> <team>\n" + "scoreboard teams option <team> <option> <value>\n", 3, 4, 5);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.comboboxMode) this.onModeChange();
		else this.onOptionChange();
	}

	@Override
	public CGPanel createGUI()
	{
		CGPanel panel = new CGPanel();
		GridBagConstraints gbc = panel.createGridBagLayout();

		panel.add(this.labelDescription(), gbc);
		++gbc.gridy;
		panel.add(this.comboboxMode = new OptionCombobox("scoreboard.teams.mode", "add", "remove", "join", "leave", "empty", "option"), gbc);
		++gbc.gridy;
		panel.add((this.entryTeam = new CGEntry(new Text("score.team"), new Text("score.team"))).container, gbc);
		++gbc.gridy;
		panel.add((this.entryDisplayName = new CGEntry(new Text("score.add.display"), new Text("display.name"))).container, gbc);
		panel.add(this.panelTarget = new PanelTarget(PanelTarget.ALL_ENTITIES), gbc);
		panel.add(this.comboboxOptionName = new OptionCombobox("scoreboard.team.option", "color", "friendlyfire", "seeFriendlyInvisibles", "nametagVisibility",
				"deathMessageVisibility", "collisionRule"), gbc);
		++gbc.gridy;
		panel.add(this.comboboxOptionValue = new OptionCombobox("color", Utils.COLORS), gbc);

		this.panelTarget.setVisible(false);
		this.comboboxOptionName.setVisible(false);
		this.comboboxOptionValue.setVisible(false);

		this.comboboxMode.addActionListener(this);
		this.comboboxOptionName.addActionListener(this);

		return panel;
	}

	@Override
	protected void finishReading()
	{
		this.onModeChange();
	}

	@Override
	public String generate() throws CommandGenerationException
	{
		String command = this.id + " " + this.comboboxMode.getValue();
		String mode = this.comboboxMode.getValue();
		String team = this.entryTeam.getText();
		this.entryTeam.checkValue(CGEntry.STRING);
		command += " " + team;
		if (mode.equals("empty")) return command;
		if (mode.equals("add") && !this.entryDisplayName.getText().equals("")) command += " " + this.entryDisplayName.getText();
		if (mode.equals("join") || mode.equals("leave")) command += " " + this.panelTarget.generate().toCommand();
		if (mode.equals("option")) command += " " + this.comboboxOptionName.getValue() + " " + this.comboboxOptionValue.getValue();

		return command;
	}

	private void onModeChange()
	{
		String mode = this.comboboxMode.getValue();
		this.panelTarget.setVisible(mode.equals("join") || mode.equals("leave"));
		this.comboboxOptionName.setVisible(mode.equals("option"));
		this.comboboxOptionValue.setVisible(mode.equals("option"));
		this.entryDisplayName.container.setVisible(mode.equals("add"));
	}

	private void onOptionChange()
	{
		String option = this.comboboxOptionName.getValue();
		if (option.equals("color")) this.comboboxOptionValue.setOptions("color", Utils.COLORS);
		else if (option.equals("friendlyfire") || option.equals("seeFriendlyInvisibles")) this.comboboxOptionValue.setOptions("value", "true", "false");
		else if (option.equals("nametagVisibility") || option.equals("deathMessageVisibility")) this.comboboxOptionValue.setOptions("scoreboard.option",
				"never", "hideForOtherTeams", "hideForOwnTeam", "always");
		else this.comboboxOptionValue.setOptions("scoreboard.option", "never", "pushOtherTeams", "pushOwnTeam", "always");
	}

	@Override
	protected void readArgument(int index, String argument, String[] fullCommand) throws CommandGenerationException
	{
		// scoreboard teams add <name> [display name]
		// scoreboard teams <join|leave> <player>
		// scoreboard teams <empty|remove> <team>
		// scoreboard teams option <team> <option> <value>
		String mode = fullCommand[1];
		if (index == 1) this.comboboxMode.setValue(argument);
		if (index == 2) this.entryTeam.setText(argument);
		if (index == 3) if (mode.equals("add")) this.entryDisplayName.setText(argument);
		else if (mode.equals("join") || mode.equals("leave")) this.panelTarget.setupFrom(Target.createFrom(argument));
		else
		{
			this.comboboxOptionName.setValue(argument);
			onOptionChange();
		}
		if (index == 4) this.comboboxOptionValue.setValue(argument);
	}
}
