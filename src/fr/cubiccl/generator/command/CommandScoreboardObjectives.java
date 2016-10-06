package fr.cubiccl.generator.command;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import fr.cubiccl.generator.gui.component.button.CCheckBox;
import fr.cubiccl.generator.gui.component.combobox.OptionCombobox;
import fr.cubiccl.generator.gui.component.label.CLabel;
import fr.cubiccl.generator.gui.component.panel.CPanel;
import fr.cubiccl.generator.gui.component.panel.gameobject.PanelCriteria;
import fr.cubiccl.generator.gui.component.textfield.CEntry;
import fr.cubiccl.generator.utils.CommandGenerationException;
import fr.cubiccl.generator.utils.Utils;

public class CommandScoreboardObjectives extends Command implements ActionListener
{
	public static final String[] SLOTS =
	{ "belowName", "list", "sidebar", "sidebar.team.aqua", "sidebar.team.black", "sidebar.team.blue", "sidebar.team.dark_aqua", "sidebar.team.dark_blue",
			"sidebar.team.dark_gray", "sidebar.team.dark_green", "sidebar.team.dark_purple", "sidebar.team.dark_red", "sidebar.team.gold", "sidebar.team.gray",
			"sidebar.team.green", "sidebar.team.light_purple", "sidebar.team.red", "sidebar.team.yellow", "sidebar.team.white" };

	private CCheckBox checkboxClearSlot;
	private OptionCombobox comboboxMode, comboboxDisplaySlot;
	private CEntry entryObjective, entryDisplayName;
	private CLabel labelSlot;
	private PanelCriteria panelCriteria;

	public CommandScoreboardObjectives()
	{
		super("scoreboard objectives");
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.comboboxMode)
		{
			boolean display = this.comboboxMode.getValue().equals("setdisplay"), add = this.comboboxMode.getValue().equals("add");
			this.comboboxDisplaySlot.setVisible(display);
			this.entryDisplayName.container.setVisible(add);
			this.checkboxClearSlot.setVisible(display);
			this.labelSlot.setVisible(display);
			this.panelCriteria.setVisible(add);
		}
		this.entryObjective.container.setVisible(!this.comboboxMode.getValue().equals("setdisplay") || !this.checkboxClearSlot.isSelected());
	}

	@Override
	public CPanel createGUI()
	{
		CPanel panel = new CPanel();
		GridBagConstraints gbc = panel.createGridBagLayout();

		++gbc.gridwidth;
		panel.add(this.labelDescription(), gbc);
		++gbc.gridy;
		--gbc.gridwidth;
		panel.add(new CLabel("general.mode").setHasColumn(true), gbc);
		++gbc.gridx;
		panel.add(this.comboboxMode = new OptionCombobox("scoreboard.objectives.mode", "add", "remove", "setdisplay"), gbc);
		--gbc.gridx;
		++gbc.gridy;
		++gbc.gridwidth;
		panel.add((this.entryObjective = new CEntry("score.name")).container, gbc);
		--gbc.gridwidth;
		++gbc.gridy;
		panel.add(this.labelSlot = new CLabel("score.add.slot").setHasColumn(true), gbc);
		++gbc.gridx;
		panel.add(this.comboboxDisplaySlot = new OptionCombobox("score.slot", SLOTS), gbc);
		--gbc.gridx;
		++gbc.gridy;
		++gbc.gridwidth;
		panel.add((this.entryDisplayName = new CEntry("score.add.display")).container, gbc);
		panel.add(this.checkboxClearSlot = new CCheckBox("score.slot.clear"), gbc);
		++gbc.gridy;
		panel.add(this.panelCriteria = new PanelCriteria(), gbc);

		this.comboboxMode.addActionListener(this);
		this.checkboxClearSlot.addActionListener(this);
		this.comboboxDisplaySlot.setVisible(false);
		this.checkboxClearSlot.setVisible(false);
		this.labelSlot.setVisible(false);

		return panel;
	}

	@Override
	public String generate() throws CommandGenerationException
	{
		String command = "/scoreboard objectives " + this.comboboxMode.getValue();
		String objective = this.entryObjective.getText();
		boolean display = this.comboboxMode.getValue().equals("setdisplay");
		boolean clear = display && this.checkboxClearSlot.isSelected();
		if (!(clear)) Utils.checkStringId(this.entryObjective.label.getText(), objective);

		switch (this.comboboxMode.getValue())
		{
			case "add":
				command += " " + objective + " " + this.panelCriteria.getCriteria();
				if (this.entryDisplayName.getText().equals("")) return command;
				return command + " " + this.entryDisplayName.getText();

			case "setdisplay":
				if (clear) return command + " " + this.comboboxDisplaySlot.getValue();
				return command + " " + this.comboboxDisplaySlot.getValue() + " " + objective;

			default:
				break;
		}

		return command + " " + objective;
	}
}
