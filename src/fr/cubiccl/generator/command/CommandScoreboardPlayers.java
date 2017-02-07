package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import fr.cubiccl.generator.gameobject.target.Target;
import fr.cubiccl.generator.gui.component.button.CGCheckBox;
import fr.cubiccl.generator.gui.component.combobox.OptionCombobox;
import fr.cubiccl.generator.gui.component.panel.CGPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelTarget;
import fr.cubiccl.generator.gui.component.textfield.CGEntry;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Text;

public class CommandScoreboardPlayers extends Command implements ActionListener
{
	public static final String[] OPERATIONS =
	{ "+#", "-#", "*#", "/#", "%#", "#", "<", ">", "><" };

	private CGCheckBox checkbox;
	private OptionCombobox comboboxMode, comboboxMode2;
	private CGEntry entryObjective, entryObjective2, entryScore, entryScore2;
	private PanelTarget panelTarget, panelTarget2;

	public CommandScoreboardPlayers()
	{
		super("scoreboard players", "scoreboard players <add|remove|set> <player> <objective> <score>\n"
				+ "scoreboard players <enable|reset> <player> <objective>\n" + "scoreboard players test <player> <objective> <min> [max]\n"
				+ "scoreboard players operation <target player> <target objective> <operation> <player> <objective>\n"
				+ "scoreboard players tag <player> <add|remove> <tagName>", 4, 5, 6, 7);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.comboboxMode) this.onModeChange();
		else this.onCheckbox();
	}

	@Override
	public CGPanel createGUI()
	{
		CGPanel panel = new CGPanel();
		GridBagConstraints gbc = panel.createGridBagLayout();

		++gbc.gridwidth;
		panel.add(this.labelDescription(), gbc);
		++gbc.gridy;
		panel.add(this.comboboxMode = new OptionCombobox("scoreboard.players.mode", "set", "add", "remove", "reset", "enable", "test", "operation", "tag"), gbc);
		++gbc.gridy;
		panel.add(this.panelTarget = new PanelTarget(PanelTarget.ALL_ENTITIES), gbc);
		++gbc.gridy;
		panel.add((this.entryObjective = new CGEntry(Text.OBJECTIVE, Text.OBJECTIVE)).container, gbc);
		++gbc.gridy;
		panel.add((this.entryScore = new CGEntry(Text.VALUE, "0", Text.INTEGER)).container, gbc);
		++gbc.gridy;
		panel.add(this.comboboxMode2 = new OptionCombobox("scoreboard.operation", OPERATIONS), gbc);
		++gbc.gridy;
		panel.add(this.panelTarget2 = new PanelTarget("target.title.any2", PanelTarget.ALL_ENTITIES), gbc);
		++gbc.gridy;
		panel.add((this.entryObjective2 = new CGEntry(new Text("score.name2"), Text.OBJECTIVE)).container, gbc);
		++gbc.gridy;
		--gbc.gridwidth;
		panel.add(this.checkbox = new CGCheckBox("scoreboard.reset.all"), gbc);
		++gbc.gridx;
		panel.add((this.entryScore2 = new CGEntry(null, "0", Text.NUMBER)).container, gbc);

		this.comboboxMode.addActionListener(this);
		this.entryScore.addIntFilter();
		this.checkbox.setVisible(false);
		this.checkbox.addActionListener(this);
		this.panelTarget2.setVisible(false);
		this.entryObjective2.container.setVisible(false);
		this.entryScore2.container.setVisible(false);
		this.comboboxMode2.setVisible(false);

		return panel;
	}

	@Override
	protected void finishReading()
	{
		this.onCheckbox();
	}

	@Override
	public String generate() throws CommandGenerationException
	{
		String command = this.id + " " + this.comboboxMode.getValue() + " " + this.panelTarget.generate().toCommand() + " ";
		String mode = this.comboboxMode.getValue();
		String objective = this.entryObjective.getText();
		if (!mode.equals("reset")) this.entryObjective.checkValue(CGEntry.STRING);
		if (mode.equals("set") || mode.equals("add") || mode.equals("remove")) return command + objective + " " + this.entryScore.getText();

		if (mode.equals("reset"))
		{
			if (this.checkbox.isSelected()) return command;
			return command + objective;
		}
		if (mode.equals("enable")) return command + objective;

		if (mode.equals("test"))
		{
			if (this.checkbox.isSelected()) return command + objective + " " + this.entryScore.getText() + " " + this.entryScore2.getText();
			return command + this.entryScore.getText();
		}

		if (mode.equals("operation")) return command + objective + " " + this.comboboxMode2.getValue().replaceAll("#", "=") + " "
				+ this.panelTarget2.generate().toCommand() + " " + this.entryObjective2.getText();

		if (mode.equals("tag"))
		{
			this.entryScore.checkValue(CGEntry.STRING);
			return command + this.panelTarget.generate().toCommand() + " " + this.comboboxMode2.getValue() + " " + this.entryObjective.getText();
		}

		return command;
	}

	private void onCheckbox()
	{
		String mode = this.comboboxMode.getValue();
		this.entryObjective.container.setVisible(!mode.equals("reset") || !this.checkbox.isSelected());
		this.entryScore2.setEnabled(this.checkbox.isSelected() || !mode.equals("test"));
	}

	private void onModeChange()
	{
		String mode = this.comboboxMode.getValue();
		boolean operation = mode.equals("operation"), test = mode.equals("test"), tag = mode.equals("tag");
		this.entryScore.container.setVisible(!mode.equals("reset") && !mode.equals("enable") && !tag && !operation);
		this.checkbox.setVisible(mode.equals("reset") || test);
		this.panelTarget2.setVisible(operation);
		this.entryObjective2.container.setVisible(operation);
		this.entryScore2.container.setVisible(test);
		this.entryScore2.label.setVisible(!test);
		this.comboboxMode2.setVisible(operation || tag);
		if (test)
		{
			this.checkbox.setTextID(new Text("scoreboard.test.max"));
			this.entryObjective.label.setTextID(Text.OBJECTIVE);
			this.entryScore.label.setTextID(new Text("score.value.min"));
		} else if (operation)
		{
			this.entryObjective.label.setTextID(new Text("score.name1"));
			this.comboboxMode2.setOptions("scoreboard.operation", OPERATIONS);
		} else if (tag)
		{
			this.entryObjective.label.setTextID(new Text("scoreboard.tag.value"));
			this.comboboxMode2.setOptions("scoreboard.tag", "add", "remove");
		} else
		{
			this.checkbox.setTextID(new Text("scoreboard.reset.all"));
			this.entryObjective.label.setTextID(Text.OBJECTIVE);
			this.entryScore.label.setTextID(Text.VALUE);
		}

	}

	@Override
	protected void readArgument(int index, String argument, String[] fullCommand) throws CommandGenerationException
	{
		// scoreboard players <add|remove|set> <player> <objective> <score>
		// scoreboard players <enable|reset> <player> <objective>
		// scoreboard players test <player> <objective> <min> [max]
		// scoreboard players operation <target player> <target objective> <operation> <player> <objective>
		// scoreboard players tag <player> add <tagName>
		String mode = fullCommand[1];
		if (index == 1)
		{
			this.comboboxMode.setValue(argument);
			this.onModeChange();
		}
		if (index == 2) this.panelTarget.setupFrom(Target.createFrom(argument));
		if (index == 3) if (mode.equals("tag")) this.comboboxMode2.setValue(argument);
		else this.entryObjective.setText(argument);
		if (index == 4) if (mode.equals("operation")) this.comboboxMode2.setValue(argument.replace("=", "#"));
		else if (mode.equals("tag")) this.entryObjective.setText(argument);
		else try
		{
			Integer.parseInt(argument);
			this.entryScore.setText(argument);
		} catch (Exception e)
		{}
		if (index == 5) if (mode.equals("test")) try
		{
			Integer.parseInt(argument);
			this.checkbox.setSelected(true);
			this.entryScore2.setText(argument);
		} catch (Exception e)
		{}
		else this.panelTarget.setupFrom(Target.createFrom(argument));
		if (index == 6) this.entryObjective2.setText(argument);
	}
}
