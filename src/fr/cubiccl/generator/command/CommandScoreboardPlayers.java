package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import fr.cubiccl.generator.gui.component.button.CCheckBox;
import fr.cubiccl.generator.gui.component.combobox.OptionCombobox;
import fr.cubiccl.generator.gui.component.panel.CPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelTarget;
import fr.cubiccl.generator.gui.component.textfield.CEntry;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Utils;

public class CommandScoreboardPlayers extends Command implements ActionListener
{
	public static final String[] OPERATIONS =
	{ "+#", "-#", "*#", "/#", "%#", "#", "<", ">", "><" };

	private CCheckBox checkbox;
	private OptionCombobox comboboxMode, comboboxMode2;
	private CEntry entryObjective, entryObjective2, entryScore, entryScore2;
	private PanelTarget panelTarget, panelTarget2;

	public CommandScoreboardPlayers()
	{
		super("scoreboard players");
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		String mode = this.comboboxMode.getValue();
		boolean operation = mode.equals("operation"), test = mode.equals("test"), tag = mode.equals("tag");
		if (e.getSource() == this.comboboxMode)
		{
			this.entryScore.container.setVisible(!mode.equals("reset") && !mode.equals("enable") && !tag && !operation);
			this.checkbox.setVisible(mode.equals("reset") || test);
			this.panelTarget2.setVisible(operation);
			this.entryObjective2.container.setVisible(operation);
			this.entryScore2.container.setVisible(test);
			this.entryScore2.label.setVisible(!test);
			this.comboboxMode2.setVisible(operation || tag);
			if (test)
			{
				this.checkbox.setTextID("scoreboard.test.max");
				this.entryObjective.label.setTextID("score.name");
				this.entryScore.label.setTextID("score.value.min");
			} else if (operation)
			{
				this.entryObjective.label.setTextID("score.name1");
				this.comboboxMode2.setOptions("scoreboard.operation", OPERATIONS);
			} else if (tag)
			{
				this.entryObjective.label.setTextID("scoreboard.tag.value");
				this.comboboxMode2.setOptions("scoreboard.tag", "add", "remove");
			} else
			{
				this.checkbox.setTextID("scoreboard.reset.all");
				this.entryObjective.label.setTextID("score.name");
				this.entryScore.label.setTextID("score.value");
			}
		}

		this.entryObjective.container.setVisible(!mode.equals("reset") || !this.checkbox.isSelected());
		this.entryScore2.setEnabled(this.checkbox.isSelected() || !test);
	}

	@Override
	public CPanel createGUI()
	{
		CPanel panel = new CPanel();
		GridBagConstraints gbc = panel.createGridBagLayout();

		++gbc.gridwidth;
		panel.add(this.labelDescription(), gbc);
		++gbc.gridy;
		panel.add(this.comboboxMode = new OptionCombobox("scoreboard.players.mode", "set", "add", "remove", "reset", "enable", "test", "operation", "tag"), gbc);
		++gbc.gridy;
		panel.add(this.panelTarget = new PanelTarget(PanelTarget.ALL_ENTITIES), gbc);
		++gbc.gridy;
		panel.add((this.entryObjective = new CEntry("score.name")).container, gbc);
		++gbc.gridy;
		panel.add((this.entryScore = new CEntry("score.value", "0")).container, gbc);
		++gbc.gridy;
		panel.add(this.comboboxMode2 = new OptionCombobox("scoreboard.operation", OPERATIONS), gbc);
		++gbc.gridy;
		panel.add(this.panelTarget2 = new PanelTarget("target.title.any2", PanelTarget.ALL_ENTITIES), gbc);
		++gbc.gridy;
		panel.add((this.entryObjective2 = new CEntry("score.name2")).container, gbc);
		++gbc.gridy;
		--gbc.gridwidth;
		panel.add(this.checkbox = new CCheckBox("scoreboard.reset.all"), gbc);
		++gbc.gridx;
		panel.add((this.entryScore2 = new CEntry(null, "0")).container, gbc);

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
	public String generate() throws CommandGenerationException
	{
		String command = "/scoreboard players " + this.comboboxMode.getValue() + " " + this.panelTarget.generateTarget().toCommand() + " ";
		String mode = this.comboboxMode.getValue();
		String objective = this.entryObjective.getText();
		if (!mode.equals("reset")) Utils.checkStringId(this.entryObjective.label.getText(), objective);
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
				+ this.panelTarget2.generateTarget().toCommand() + " " + this.entryObjective2.getText();

		if (mode.equals("tag"))
		{
			Utils.checkStringId(this.entryScore.label.getText(), this.entryScore.getText());
			return command + this.panelTarget.generateTarget().toCommand() + " " + this.comboboxMode2.getValue() + " " + this.entryObjective.getText();
		}

		return command;
	}
}
